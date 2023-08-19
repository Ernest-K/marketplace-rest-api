package com.example.marketplace.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OfferCount {
    private Long categoryId;
    private String categoryName;
    private Long total;
}
