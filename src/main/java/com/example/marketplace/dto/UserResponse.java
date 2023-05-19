package com.example.marketplace.dto;

import java.util.List;

public record UserResponse(Long id, String username, String email, List<OfferResponse> offers) {
}
