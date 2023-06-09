package com.example.marketplace.controller;

import com.example.marketplace.dto.OfferResponse;
import com.example.marketplace.service.OfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping(params = "categoryId")
    public ResponseEntity<List<OfferResponse>> getOffersByCategoryId(@RequestParam Long categoryId){
        return new ResponseEntity<>(offerService.getOffersByCategoryId(categoryId), HttpStatus.OK);
    }

    @GetMapping(params = "userId")
    public ResponseEntity<List<OfferResponse>> getOffersByUserId(@RequestParam Long userId){
        return new ResponseEntity<>(offerService.getOffersByUserId(userId), HttpStatus.OK);
    }
}
