package com.authsystem.authentication.service.impl;

import com.authsystem.authentication.dto.AuthResponse;
import com.authsystem.authentication.dto.LoginRequest;
import com.authsystem.authentication.dto.RegisterRequest;
import com.authsystem.authentication.entity.Role;
import com.authsystem.authentication.entity.User;
import com.authsystem.authentication.entity.VerificationOtp;
import com.authsystem.authentication.repository.RoleRepository;
import com.authsystem.authentication.repository.UserRepository;
import com.authsystem.authentication.repository.VerificationOtpRepository;
import com.authsystem.authentication.security.JwtService;
import com.authsystem.authentication.service.AuthService;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.Set;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final VerificationOtpRepository verificationOtpRepository;

    public AuthServiceImpl(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            VerificationOtpRepository verificationOtpRepository
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.verificationOtpRepository = verificationOtpRepository;
    }

    // ================= REGISTER =================
    @Override
    public void register(RegisterRequest request) {

        if (userRepository.existsByUsername(request.username())) {
            throw new RuntimeException("Username already exists");
        }

        if (userRepository.existsByEmail(request.email())) {
            throw new RuntimeException("Email already exists");
        }

        Role roleUser = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("ROLE_USER not found"));

        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setEnabled(false); // 🔒 enabled only after OTP verification
        user.setRoles(Set.of(roleUser));

        userRepository.save(user);

        // 🔐 Generate OTP
        String otp = String.valueOf(100000 + new Random().nextInt(900000));

        VerificationOtp verificationOtp = new VerificationOtp();
        verificationOtp.setEmail(request.email());
        verificationOtp.setOtp(otp);
        verificationOtp.setVerified(false);
        verificationOtp.setExpiryTime(LocalDateTime.now().plusMinutes(5));

        verificationOtpRepository.save(verificationOtp);

        // TEMP: console output (later replace with email)
        System.out.println("OTP for " + request.email() + " = " + otp);
    }

    // ================= VERIFY OTP =================
    @Override
    public void verifyOtp(String email, String otp) {

        VerificationOtp verificationOtp =
                verificationOtpRepository.findByEmailAndOtp(email, otp)
                        .orElseThrow(() -> new RuntimeException("Invalid OTP"));

        if (verificationOtp.isVerified()) {
            throw new RuntimeException("OTP already used");
        }

        if (verificationOtp.getExpiryTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP expired");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setEnabled(true);
        verificationOtp.setVerified(true);

        userRepository.save(user);
        verificationOtpRepository.save(verificationOtp);
    }

    // ================= LOGIN =================
    @Override
    public AuthResponse login(LoginRequest request) {

        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!user.isEnabled()) {
            throw new RuntimeException("Account not verified. Please verify OTP.");
        }


        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtService.generateToken(user.getUsername());
        return new AuthResponse(token);
    }
}
