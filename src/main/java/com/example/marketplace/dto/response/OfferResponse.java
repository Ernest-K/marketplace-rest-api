package com.example.marketplace.dto.response;

import com.example.marketplace.model.Category;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OfferResponse{
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Long userId;
    private Category category;
}
