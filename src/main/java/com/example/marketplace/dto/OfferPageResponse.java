package com.example.marketplace.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OfferPageResponse {
    private List<OfferResponse> offerResponses;
    private Integer pageNo;
    private Integer pageSize;
    private long totalPages;
    private long totalElements;
    private boolean last;
}
