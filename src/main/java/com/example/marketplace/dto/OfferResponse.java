package com.example.marketplace.dto;

import com.example.marketplace.model.Category;

public record OfferResponse(String name, String description, Double price, Long userId, Category category) {
}
