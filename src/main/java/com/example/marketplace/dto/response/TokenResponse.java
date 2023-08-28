package com.example.marketplace.dto.response;

import lombok.Data;

@Data
public class TokenResponse {
    private String token;
    private String type = "Bearer";
}
