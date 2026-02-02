package com.authsystem.authentication.controller;

import com.authsystem.authentication.dto.UserProfileResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
public class UserController {

    // ✅ STEP 1: Verify JWT + SecurityContext (NO role check)
    @GetMapping("/me")
    public UserProfileResponse me(Authentication authentication) {

        Set<String> roles = authentication.getAuthorities()
                .stream()
                .map(a -> a.getAuthority())
                .collect(Collectors.toSet());

        return new UserProfileResponse(
                authentication.getName(),
                roles
        );
    }



    // ✅ STEP 2: Role-based access (USER or ADMIN)
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/profile")
    public String userProfile() {
        return "👤 USER profile";
    }
}
