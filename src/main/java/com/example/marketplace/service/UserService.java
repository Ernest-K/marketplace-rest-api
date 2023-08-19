package com.example.marketplace.service;

import com.example.marketplace.dto.UpdateUserRequest;
import com.example.marketplace.dto.UserResponse;
import org.springframework.web.server.MethodNotAllowedException;

import java.util.List;

public interface UserService {
    List<UserResponse> getUsers();
    UserResponse getUserById(Long id);
    UserResponse updateUser(Long id, UpdateUserRequest updateUserRequest) throws MethodNotAllowedException;
    void deleteUser(Long id);
}
