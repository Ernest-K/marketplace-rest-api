package com.example.marketplace.service.impl;

import com.example.marketplace.dto.OfferResponse;
import com.example.marketplace.exception.OfferNotFoundException;
import com.example.marketplace.mapper.OfferMapper;
import com.example.marketplace.repository.OfferRepository;
import com.example.marketplace.service.OfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class OfferServiceImpl implements OfferService {

    private final OfferRepository offerRepository;
    private final OfferMapper offerMapper;

    @Override
    @Transactional
    public OfferResponse getOffer(Long id) {
        return offerRepository.findById(id)
                .map(offerMapper)
                .orElseThrow(() -> new OfferNotFoundException(id.toString()));
    }

    @Override
    public List<OfferResponse> getOffers() {
        return offerRepository.findAll()
                .stream().map(offerMapper)
                .collect(Collectors.toList());
    }
}
