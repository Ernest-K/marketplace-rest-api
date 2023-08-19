package com.example.marketplace.service;

import com.example.marketplace.dto.request.RegisterRequest;

public interface AuthService {
    void register(RegisterRequest registerRequest);
}
