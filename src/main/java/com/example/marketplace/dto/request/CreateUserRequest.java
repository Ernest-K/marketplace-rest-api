package com.example.marketplace.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateUserRequest {
    @NotBlank(message = "Username is required")
    @Size(max = 30, message = "Username is too long (max 30 characters)")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 30, message = "Password not valid (min 6 characters, max 30 characters)")
    private String password;

    @Email
    @NotBlank(message = "Email is required")
    @Size(max = 30, message = "Email is too long (max 30 characters)")
    private String email;

    @Size(max = 30, message = "First name is too long (max 30 characters)")
    private String firstName;

    @Size(max = 30, message = "Last name is too long (max 30 characters)")
    private String lastName;

    @Pattern(regexp="(^$|[0-9]{10})", message = "Phone format not valid")
    private String phone;
}
