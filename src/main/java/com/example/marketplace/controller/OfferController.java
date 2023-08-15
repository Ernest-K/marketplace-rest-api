package com.example.marketplace.controller;

import com.example.marketplace.dto.OfferCount;
import com.example.marketplace.dto.OfferResponse;
import com.example.marketplace.service.OfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class OfferController {

    private final OfferService offerService;

    @GetMapping("/offers")
    public ResponseEntity<List<OfferResponse>> getAllOffers(){
        return new ResponseEntity<>(offerService.getOffers(), HttpStatus.OK);
    }
    @GetMapping("/offers/{id}")
    public ResponseEntity<OfferResponse> getOfferById(@PathVariable Long id){
        OfferResponse offerResponse = offerService.getOfferById(id);
        if (offerResponse == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(offerResponse, HttpStatus.OK);
    }

    @GetMapping("/users/{userId}/offers")
    public ResponseEntity<List<OfferResponse>> getOffersByUserId(@PathVariable Long userId){
        return new ResponseEntity<>(offerService.getOffersByUserId(userId), HttpStatus.OK);
    }

    @GetMapping(value = "/offers", params = "categoryName")
    public ResponseEntity<List<OfferResponse>> getOffersByCategoryName(@RequestParam String categoryName){
        return new ResponseEntity<>(offerService.getOffersByCategoryName(categoryName), HttpStatus.OK);
    }

    @GetMapping("/offers/count")
    public ResponseEntity<List<OfferCount>> getCountOffersByCategory(){
        return new ResponseEntity<>(offerService.getCountOffersByCategory(), HttpStatus.OK);
    }
}
