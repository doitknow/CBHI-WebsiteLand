package com.medco.cbhi.dto;

import jakarta.validation.constraints.NotBlank;

public class ForgotPasswordRequest {

    @NotBlank(message = "Email or username is required")
    private String emailOrUsername;

    private String originUrl;

    public ForgotPasswordRequest() {}

    public ForgotPasswordRequest(String emailOrUsername) {
        this.emailOrUsername = emailOrUsername;
    }

    public String getEmailOrUsername() { return emailOrUsername; }
    public void setEmailOrUsername(String emailOrUsername) { this.emailOrUsername = emailOrUsername; }

    public String getOriginUrl() { return originUrl; }
    public void setOriginUrl(String originUrl) { this.originUrl = originUrl; }
}
