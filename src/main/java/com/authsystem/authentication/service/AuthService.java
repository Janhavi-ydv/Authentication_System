package com.authsystem.authentication.service;

import com.authsystem.authentication.dto.AuthResponse;
import com.authsystem.authentication.dto.LoginRequest;
import com.authsystem.authentication.dto.RefreshTokenResponse;
import com.authsystem.authentication.dto.RegisterRequest;

public interface AuthService {

    void register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    void verifyOtp(String email, String otp);

    RefreshTokenResponse refreshAccessToken(String refreshToken);

}
