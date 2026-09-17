package com.medco.cbhi.controller;

import com.medco.cbhi.dto.*;
import com.medco.cbhi.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        try {
            AuthResponse response = authService.login(request);
            return ResponseEntity.ok(ApiResponse.ok("Login successful", response));
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(401).body(ApiResponse.error("Invalid username or password: " + ex.getMessage()));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<AuthResponse>> getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body(ApiResponse.error("Unauthenticated"));
        }
        AuthResponse response = authService.getCurrentUser(authentication.getName());
        return ResponseEntity.ok(ApiResponse.ok("Authenticated user", response));
    }

    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            Authentication authentication,
            @Valid @RequestBody ChangePasswordRequest request) {
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getName())) {
            return ResponseEntity.status(401).body(ApiResponse.error("Authentication required to change password"));
        }

        try {
            authService.changePassword(authentication.getName(), request.getCurrentPassword(), request.getNewPassword());
            return ResponseEntity.ok(ApiResponse.ok("Password changed successfully", null));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body(ApiResponse.error("Failed to update password: " + ex.getMessage()));
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<Map<String, Object>>> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request) {
        try {
            Map<String, Object> result = authService.forgotPassword(request.getEmailOrUsername(), request.getOriginUrl());
            return ResponseEntity.ok(ApiResponse.ok("If that account exists, a password reset link has been dispatched to your email address.", result));
        } catch (IllegalArgumentException ex) {
            // For security, can still return a friendly response or report non-existent account
            return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body(ApiResponse.error("Could not process forgot password request: " + ex.getMessage()));
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse<Map<String, Object>>> verifyOtp(
            @RequestBody Map<String, String> payload) {
        try {
            String otp = payload.get("otp");
            if (otp == null || otp.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(ApiResponse.error("OTP code is required"));
            }
            Map<String, Object> result = authService.verifyOtp(otp);
            return ResponseEntity.ok(ApiResponse.ok("OTP is valid", result));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body(ApiResponse.error("Failed to verify OTP: " + ex.getMessage()));
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<Void>> resetPassword(
            @Valid @RequestBody ResetPasswordRequest request) {
        try {
            authService.resetPassword(request.getToken(), request.getNewPassword());
            return ResponseEntity.ok(ApiResponse.ok("Password has been reset successfully. You can now log in.", null));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body(ApiResponse.error("Failed to reset password: " + ex.getMessage()));
        }
    }

    @PostMapping("/update-profile")
    public ResponseEntity<ApiResponse<AuthResponse>> updateProfile(
            Authentication authentication,
            @RequestBody Map<String, String> payload) {
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getName())) {
            return ResponseEntity.status(401).body(ApiResponse.error("Authentication required to update profile"));
        }

        try {
            String email = payload.get("email");
            if (email == null || email.trim().isEmpty() || !email.contains("@")) {
                return ResponseEntity.badRequest().body(ApiResponse.error("A valid email address is required"));
            }

            AuthResponse response = authService.updateProfile(authentication.getName(), email.trim());
            return ResponseEntity.ok(ApiResponse.ok("Admin profile email updated successfully", response));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body(ApiResponse.error("Failed to update admin profile: " + ex.getMessage()));
        }
    }
}
