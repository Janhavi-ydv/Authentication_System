package com.authsystem.authentication.dto;

import java.util.Set;

public record UserProfileResponse(
        String username,
        Set<String> roles
) {}
