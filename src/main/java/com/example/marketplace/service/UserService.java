package com.example.marketplace.service;

import com.example.marketplace.dto.UserResponse;

import java.util.List;

public interface UserService {
    List<UserResponse> getUsers();
    UserResponse getUserById(Long id);
}
