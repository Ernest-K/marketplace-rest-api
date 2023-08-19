package com.example.marketplace.service;

import com.example.marketplace.dto.OfferCount;
import com.example.marketplace.dto.OfferPageResponse;
import com.example.marketplace.dto.OfferRequest;
import com.example.marketplace.dto.OfferResponse;

import java.util.List;

public interface OfferService {
    OfferResponse getOfferById(Long id);

    OfferPageResponse getOffers(Integer pageNo, Integer pageSize);

    OfferPageResponse getOffersByCategoryName(String categoryName, Integer pageNo, Integer pageSize);

    OfferPageResponse getOffersByUserId(Long userId, Integer pageNo, Integer pageSize);

    List<OfferCount> getCountOffersByCategory();

    OfferResponse createOffer(Long userId, OfferRequest offerRequest);
}
