package com.medco.cbhi.service;

import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Value("${app.frontend.url:http://localhost:5174}")
    private String defaultFrontendUrl;

    @Value("${spring.mail.username:bereketmussie281@gmail.com}")
    private String senderEmail;

    @Value("${spring.mail.password:}")
    private String senderPassword;

    private final JavaMailSender mailSender;

    public EmailService(@Autowired(required = false) JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Send password reset 6-digit OTP email
     */
    public Map<String, Object> sendPasswordResetOtp(String recipientEmail, String username, String otp, String originUrl) {
        String baseUrl = (originUrl != null && !originUrl.isBlank()) ? originUrl : defaultFrontendUrl;
        if (baseUrl.endsWith("/")) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }

        String resetUrl = baseUrl + "/admin/reset-password?otp=" + otp;

        logger.info("=================================================================");
        logger.info(" [CBHI EMAIL DISPATCHER] Password Reset Request with 6-Digit OTP");
        logger.info(" To: {} ({})", recipientEmail, username);
        logger.info(" OTP Code: {}", otp);
        logger.info(" Direct Reset Link: {}", resetUrl);
        logger.info(" Valid for: 15 minutes");
        logger.info("=================================================================");

        boolean sentViaSmtp = false;
        String smtpError = null;

        // Attempt real SMTP dispatch if mailSender is available and a password is configured
        if (mailSender != null && senderPassword != null && !senderPassword.isBlank()) {
            try {
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
                helper.setFrom(senderEmail, "CBHI Ethiopia Admin Security");
                helper.setTo(recipientEmail);
                helper.setSubject("[CBHI Ethiopia] Your Password Reset OTP Code: " + otp);

                String html = buildOtpHtmlEmail(username, otp, resetUrl);
                helper.setText(html, true);

                mailSender.send(message);
                sentViaSmtp = true;
                logger.info(">> [CBHI EMAIL DISPATCHER] Email sent successfully via SMTP to {}", recipientEmail);
            } catch (Exception e) {
                smtpError = e.getMessage();
                logger.warn(">> [CBHI EMAIL DISPATCHER] SMTP delivery failed: {}. Falling back to simulated dispatch.", e.getMessage());
            }
        } else {
            logger.info(">> [CBHI EMAIL DISPATCHER] SMTP password not set. Logged OTP code to console and response for immediate use.");
        }

        Map<String, Object> result = new HashMap<>();
        result.put("sent", true);
        result.put("recipient", recipientEmail);
        result.put("expiresInMinutes", 15);
        result.put("sentViaSmtp", sentViaSmtp);
        if (smtpError != null) {
            result.put("smtpError", smtpError);
        }
        result.put("mode", sentViaSmtp ? "LIVE_SMTP_DISPATCH" : "SECURE_LOCAL_DISPATCH");

        return result;
    }

    /**
     * Backward compatibility wrapper
     */
    public Map<String, Object> sendPasswordResetEmail(String recipientEmail, String username, String token, String originUrl) {
        return sendPasswordResetOtp(recipientEmail, username, token, originUrl);
    }

    private String buildOtpHtmlEmail(String username, String otp, String resetUrl) {
        return "<!DOCTYPE html>"
                + "<html>"
                + "<head><meta charset='UTF-8'></head>"
                + "<body style='margin:0; padding:0; background-color:#f1f5f9; font-family:Arial, sans-serif;'>"
                + "<div style='max-width:600px; margin:30px auto; background-color:#ffffff; border-radius:16px; overflow:hidden; box-shadow:0 4px 20px rgba(0,0,0,0.08);'>"
                + "<div style='background:linear-gradient(135deg, #022742, #0873b9); padding:32px 24px; text-align:center; color:#ffffff;'>"
                + "<h1 style='margin:0; font-size:22px; font-weight:800; letter-spacing:1px;'>CBHI ETHIOPIA</h1>"
                + "<p style='margin:6px 0 0; font-size:13px; opacity:0.85;'>Community Based Health Insurance Management System</p>"
                + "</div>"
                + "<div style='padding:32px 28px; color:#1e293b;'>"
                + "<h2 style='font-size:18px; margin-top:0;'>Password Reset OTP Verification</h2>"
                + "<p style='font-size:14px; line-height:1.6; color:#475569;'>Hello <strong>" + username + "</strong>,</p>"
                + "<p style='font-size:14px; line-height:1.6; color:#475569;'>A request was received to reset the password for your CBHI administrator account (<strong>bereketmussie281@gmail.com</strong>).</p>"
                + "<div style='background-color:#02182b; border-radius:12px; padding:24px; text-align:center; margin:28px 0;'>"
                + "<p style='color:#94a3b8; font-size:12px; text-transform:uppercase; letter-spacing:2px; margin:0 0 8px 0;'>Your 6-Digit OTP Code</p>"
                + "<div style='font-size:42px; font-weight:900; letter-spacing:12px; color:#38bdf8; font-family:monospace; margin:12px 0;'>" + otp + "</div>"
                + "<p style='color:#64748b; font-size:12px; margin:8px 0 0 0;'>This OTP code expires in 15 minutes. Never share this code with anyone.</p>"
                + "</div>"
                + "<p style='font-size:13px; line-height:1.6; color:#64748b;'>You can also click the button below to open the password reset page directly with your OTP pre-filled:</p>"
                + "<div style='text-align:center; margin:24px 0;'>"
                + "<a href='" + resetUrl + "' style='display:inline-block; background-color:#0873b9; color:#ffffff; font-weight:bold; font-size:14px; padding:12px 28px; border-radius:8px; text-decoration:none;'>Reset Password Directly</a>"
                + "</div>"
                + "<p style='font-size:12px; color:#94a3b8; border-top:1px solid #e2e8f0; padding-top:16px; margin-top:24px;'>If you did not request this password reset, please ignore this email or notify your system administrator immediately.</p>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";
    }
}
