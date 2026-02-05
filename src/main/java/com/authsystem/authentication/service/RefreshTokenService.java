package com.authsystem.authentication.service;

import com.authsystem.authentication.entity.RefreshToken;
import com.authsystem.authentication.entity.User;

public interface RefreshTokenService {

    RefreshToken createRefreshToken(User user);

    RefreshToken validateRefreshToken(String token);
}
