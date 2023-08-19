package com.example.marketplace.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OfferRequest {
    @Size(max = 30, message = "Offer name is too long (max 30 characters)")
    @NotBlank(message = "Offer name is required")
    private String name;

    @Size(max = 250, message = "Offer description is too long (max 250 characters)")
    @NotBlank(message = "Offer name is required")
    private String description;

    @Min(0)
    @NotNull(message = "Offer price is required")
    private Double price;

    @NotNull(message = "Category id is required")
    private Long categoryId;
}
