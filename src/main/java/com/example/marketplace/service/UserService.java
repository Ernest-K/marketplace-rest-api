package com.example.marketplace.service;

import com.example.marketplace.dto.UpdateUserRequest;
import com.example.marketplace.dto.UserResponse;

import java.util.List;

public interface UserService {
    List<UserResponse> getUsers();
    UserResponse getUserById(Long id);
    UserResponse updateUser(Long id, UpdateUserRequest updateUserRequest);
    void deleteUser(Long id);
}
