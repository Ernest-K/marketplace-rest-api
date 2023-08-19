package com.example.marketplace.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @Email
    @NotEmpty(message = "Email is required")
    @Size(max = 30, message = "Email is too long (max 30 characters)")
    private String email;

    @NotEmpty(message = "Username is required")
    @Size(max = 30, message = "Username is too long (max 30 characters)")
    private String username;

    @NotEmpty(message = "Password is required")
    private String password;

}

