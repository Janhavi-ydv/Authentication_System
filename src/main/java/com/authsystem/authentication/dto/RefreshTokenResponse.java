package com.authsystem.authentication.dto;

public record RefreshTokenResponse(
        String accessToken,
        String refreshToken
) {}
