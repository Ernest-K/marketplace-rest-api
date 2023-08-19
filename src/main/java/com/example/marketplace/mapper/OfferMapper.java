package com.example.marketplace.mapper;

import com.example.marketplace.dto.response.OfferResponse;
import com.example.marketplace.model.Offer;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class OfferMapper implements Function<Offer, OfferResponse> {

    @Override
    public OfferResponse apply(Offer offer) {
        return new OfferResponse(offer.getId(),
                offer.getName(),
                offer.getDescription(),
                offer.getPrice(),
                offer.getUser().getId(),
                offer.getCategory());
    }
}
