package com.example.marketplace.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateOfferRequest {
    @Size(max = 30, message = "Offer name is too long (max 30 characters)")
    private String name;

    @Size(max = 250, message = "Offer description is too long (max 250 characters)")
    private String description;

    @Min(0)
    private Double price;

    private Long categoryId;
}
