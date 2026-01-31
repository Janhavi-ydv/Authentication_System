package com.authsystem.authentication.dto;

public record RegisterRequest(
        String username,
        String email,
        String password
) {}
