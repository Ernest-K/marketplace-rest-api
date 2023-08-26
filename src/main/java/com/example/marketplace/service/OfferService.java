package com.example.marketplace.service;

import com.example.marketplace.dto.response.OfferCount;
import com.example.marketplace.dto.response.OfferPageResponse;
import com.example.marketplace.dto.request.OfferRequest;
import com.example.marketplace.dto.response.OfferResponse;

import java.util.List;

public interface OfferService {
    OfferResponse getOfferById(Long offerId);

    OfferPageResponse getOffers(Integer pageNo, Integer pageSize, String sortBy, String direction);

    OfferPageResponse getOffersByCategoryName(String categoryName, Integer pageNo, Integer pageSize, String sortBy, String direction);

    OfferPageResponse getOffersByUserId(Long userId, Integer pageNo, Integer pageSize, String sortBy, String direction);

    List<OfferCount> getCountOffersByCategory();

    OfferResponse createOffer(Long userId, OfferRequest offerRequest);

    OfferResponse updateOffer(Long userId, Long offerId, OfferRequest offerRequest);

    void deleteOffer(Long userId, Long offerId);
}
