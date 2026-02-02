package com.authsystem.authentication.repository;

import com.authsystem.authentication.entity.VerificationOtp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationOtpRepository
        extends JpaRepository<VerificationOtp, Long> {

    Optional<VerificationOtp> findByEmailAndOtp(String email, String otp);
}
