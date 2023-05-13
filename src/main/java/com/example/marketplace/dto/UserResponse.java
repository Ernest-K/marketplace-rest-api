package com.example.marketplace.dto;

import java.util.List;

public record UserResponse(String username, String email, List<OfferResponse> offers) {
}
