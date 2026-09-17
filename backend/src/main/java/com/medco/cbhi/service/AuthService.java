package com.medco.cbhi.service;

import com.medco.cbhi.dto.AuthResponse;
import com.medco.cbhi.dto.LoginRequest;
import com.medco.cbhi.entity.PasswordResetToken;
import com.medco.cbhi.entity.User;
import com.medco.cbhi.repository.PasswordResetTokenRepository;
import com.medco.cbhi.repository.UserRepository;
import com.medco.cbhi.security.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetTokenRepository resetTokenRepository;
    private final EmailService emailService;

    public AuthService(AuthenticationManager authenticationManager,
                       JwtTokenProvider tokenProvider,
                       UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       PasswordResetTokenRepository resetTokenRepository,
                       EmailService emailService) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.resetTokenRepository = resetTokenRepository;
        this.emailService = emailService;
    }

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenProvider.generateToken(authentication);

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new AuthResponse(token, user.getUsername(), user.getEmail(), user.getRole());
    }

    public AuthResponse getCurrentUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new AuthResponse(null, user.getUsername(), user.getEmail(), user.getRole());
    }

    public void changePassword(String username, String currentPassword, String newPassword) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new IllegalArgumentException("Current password does not match");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Transactional
    public Map<String, Object> forgotPassword(String emailOrUsername, String originUrl) {
        String identifier = (emailOrUsername != null) ? emailOrUsername.trim() : "";
        User user = userRepository.findByEmailIgnoreCase(identifier)
                .or(() -> userRepository.findByUsernameIgnoreCase(identifier))
                .or(() -> userRepository.findByEmail(identifier))
                .or(() -> userRepository.findByUsername(identifier))
                .orElseThrow(() -> new IllegalArgumentException("No account found with provided email or username: " + identifier));

        // Invalidate old tokens for this user
        resetTokenRepository.deleteByUser(user);

        // Generate cryptographically secure 6-digit numeric OTP code
        int randomCode = 100000 + new java.security.SecureRandom().nextInt(900000);
        String otp = String.valueOf(randomCode);
        LocalDateTime expiryDate = LocalDateTime.now().plusMinutes(15);

        PasswordResetToken resetToken = new PasswordResetToken(otp, user, expiryDate);
        resetTokenRepository.save(resetToken);

        return emailService.sendPasswordResetOtp(user.getEmail(), user.getUsername(), otp, originUrl);
    }

    @Transactional(readOnly = true)
    public Map<String, Object> verifyOtp(String otp) {
        String cleanOtp = (otp != null) ? otp.trim() : "";
        PasswordResetToken resetToken = resetTokenRepository.findByToken(cleanOtp)
                .orElseThrow(() -> new IllegalArgumentException("Invalid 6-digit OTP code"));

        if (resetToken.isUsed()) {
            throw new IllegalArgumentException("This OTP code has already been used. Please request a new one.");
        }

        if (resetToken.isExpired()) {
            throw new IllegalArgumentException("This OTP code has expired. Please request a new one.");
        }

        Map<String, Object> result = new java.util.HashMap<>();
        result.put("valid", true);
        result.put("otp", cleanOtp);
        result.put("email", resetToken.getUser().getEmail());
        result.put("username", resetToken.getUser().getUsername());
        return result;
    }

    @Transactional
    public AuthResponse updateProfile(String currentUsername, String newEmail) {
        User user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new RuntimeException("User not found: " + currentUsername));

        if (newEmail != null && !newEmail.isBlank()) {
            user.setEmail(newEmail.trim());
            userRepository.save(user);
        }

        return new AuthResponse(null, user.getUsername(), user.getEmail(), user.getRole());
    }

    @Transactional
    public void resetPassword(String tokenOrOtp, String newPassword) {
        String cleanCode = (tokenOrOtp != null) ? tokenOrOtp.trim() : "";
        PasswordResetToken resetToken = resetTokenRepository.findByToken(cleanCode)
                .orElseThrow(() -> new IllegalArgumentException("Invalid OTP code or password reset token"));

        if (resetToken.isUsed()) {
            throw new IllegalArgumentException("This OTP code has already been used");
        }

        if (resetToken.isExpired()) {
            throw new IllegalArgumentException("This OTP code has expired. Please request a new one");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        resetToken.setUsed(true);
        resetTokenRepository.save(resetToken);
    }
}
