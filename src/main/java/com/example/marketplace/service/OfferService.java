package com.example.marketplace.service;

import com.example.marketplace.dto.OfferCount;
import com.example.marketplace.dto.OfferResponse;

import java.util.List;

public interface OfferService {
    OfferResponse getOfferById(Long id);
    List<OfferResponse> getOffers();

    List<OfferResponse> getOffersByCategoryName(String categoryName);

    List<OfferResponse> getOffersByUserId(Long id);

    List<OfferCount> getCountOffersByCategory();
}
