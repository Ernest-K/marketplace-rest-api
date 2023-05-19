package com.example.marketplace.controller;

import com.example.marketplace.dto.OfferResponse;
import com.example.marketplace.service.OfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/offers")
public class OfferController {

    private final OfferService offerService;

    @GetMapping("/{id}")
    public ResponseEntity<OfferResponse> getOffer(@PathVariable Long id){
        OfferResponse offerResponse = offerService.getOffer(id);
        if (offerResponse == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(offerResponse, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<OfferResponse>> getAllOffers(){
        return new ResponseEntity<>(offerService.getOffers(), HttpStatus.OK);
    }
}
