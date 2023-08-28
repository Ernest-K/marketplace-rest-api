package com.example.marketplace.service;

import com.example.marketplace.dto.request.LoginRequest;
import com.example.marketplace.dto.request.RegisterRequest;
import com.example.marketplace.dto.response.TokenResponse;

public interface AuthService {
    TokenResponse login(LoginRequest loginRequest);

    void register(RegisterRequest registerRequest);
}
