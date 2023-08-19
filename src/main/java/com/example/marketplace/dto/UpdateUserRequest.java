package com.example.marketplace.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateUserRequest {

    @Size(max = 30, message = "Username is too long (max 30 characters)")
    private String username;

    private String password;

    @Email
    @Size(max = 30, message = "Email is too long (max 30 characters)")
    private String email;
}
