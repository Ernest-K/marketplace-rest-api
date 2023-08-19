package com.example.marketplace.dto.response;

import com.example.marketplace.model.Category;

public record OfferResponse(Long id,
                            String name,
                            String description,
                            Double price,
                            Long userId,
                            Category category) {
}
