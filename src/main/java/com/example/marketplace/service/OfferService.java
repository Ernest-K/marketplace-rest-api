package com.example.marketplace.service;

import com.example.marketplace.dto.OfferResponse;

import java.util.List;

public interface OfferService {
    OfferResponse getOffer(Long id);
    List<OfferResponse> getOffers();

    List<OfferResponse> getOffersByCategoryId(Long id);
}
