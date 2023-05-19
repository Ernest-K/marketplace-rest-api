package com.example.marketplace.service;

import com.example.marketplace.dto.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse getUser(Long id);
    List<UserResponse> getUsers();
}
