package com.example.marketplace.mapper;

import com.example.marketplace.dto.response.OfferResponse;
import com.example.marketplace.model.Offer;
import org.springframework.stereotype.Component;

@Component
public class OfferMapper {

    public OfferResponse mapToDto(Offer offer){
        return new OfferResponse(offer.getId(),
                offer.getName(),
                offer.getDescription(),
                offer.getPrice(),
                offer.getUser().getId(),
                offer.getCategory());
    }
}
