package com.example.marketplace.service;

import com.example.marketplace.dto.request.CreateUserRequest;
import com.example.marketplace.dto.request.UpdateUserRequest;
import com.example.marketplace.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    List<UserResponse> getUsers();

    UserResponse getUserById(Long userId);

    UserResponse createUser(CreateUserRequest createUserRequest);

    UserResponse updateUser(Long userId, UpdateUserRequest updateUserRequest);

    void deleteUser(Long userId);
}
