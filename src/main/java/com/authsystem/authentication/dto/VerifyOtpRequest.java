package com.authsystem.authentication.dto;

public record VerifyOtpRequest(
        String email,
        String otp
) {}
