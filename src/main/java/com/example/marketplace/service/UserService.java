package com.example.marketplace.service;

import com.example.marketplace.dto.UserResponse;
import com.example.marketplace.model.User;

public interface UserService {
    void save(User user);
    UserResponse get(Long id);
}
