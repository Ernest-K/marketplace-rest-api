package com.example.marketplace.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRequest {

    @Size(max = 30, message = "Category name is too long (max 30 characters)")
    @NotBlank(message = "Category name is required")
    private String name;
}
