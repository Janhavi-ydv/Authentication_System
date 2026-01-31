package com.authsystem.authentication.dto;

public record LoginRequest(
        String username,
        String password
) {}
