package com.example.marketplace.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "Username is required")
    @Size(max = 30, message = "Username is too long (max 30 characters)")
    private String username;

    @Email
    @NotBlank(message = "Email is required")
    @Size(max = 30, message = "Email is too long (max 30 characters)")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 30, message = "Password not valid (min 6 characters, max 30 characters)")
    private String password;
}

