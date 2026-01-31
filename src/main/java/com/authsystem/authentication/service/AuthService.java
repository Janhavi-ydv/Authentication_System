package com.authsystem.authentication.service;

import com.authsystem.authentication.dto.LoginRequest;
import com.authsystem.authentication.dto.RegisterRequest;
import com.authsystem.authentication.dto.AuthResponse;

public interface AuthService {

    void register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
}
