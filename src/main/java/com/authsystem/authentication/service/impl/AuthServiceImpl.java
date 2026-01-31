package com.authsystem.authentication.service.impl;

import com.authsystem.authentication.dto.LoginRequest;
import com.authsystem.authentication.dto.RegisterRequest;
import com.authsystem.authentication.dto.AuthResponse;
import com.authsystem.authentication.entity.Role;
import com.authsystem.authentication.entity.User;
import com.authsystem.authentication.repository.RoleRepository;
import com.authsystem.authentication.repository.UserRepository;
import com.authsystem.authentication.security.JwtService;
import com.authsystem.authentication.service.AuthService;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthServiceImpl(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    // ===================== REGISTER =====================
    @Override
    public void register(RegisterRequest request) {

        // 🔒 Validate uniqueness
        if (userRepository.existsByUsername(request.username())) {
            throw new RuntimeException("Username already exists");
        }

        if (userRepository.existsByEmail(request.email())) {
            throw new RuntimeException("Email already exists");
        }

        // 🔐 Fetch ROLE_USER
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() ->
                        new RuntimeException("ROLE_USER not found in database")
                );

        // 👤 Create User entity
        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setEnabled(true);
        user.setRoles(Set.of(userRole));

        // 💾 SAVE (this is where DB insert happens)
        userRepository.save(user);
    }

    // ===================== LOGIN =====================
    @Override
    public AuthResponse login(LoginRequest request) {

        // 🔍 Find user
        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() ->
                        new RuntimeException("Invalid username or password")
                );

        // 🔑 Validate password
        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        // 🎟 Generate JWT
        String token = jwtService.generateToken(user.getUsername());

        return new AuthResponse(token);
    }
}
