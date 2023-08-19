package com.example.marketplace.controller;

import com.example.marketplace.dto.response.OfferCount;
import com.example.marketplace.dto.response.OfferPageResponse;
import com.example.marketplace.dto.request.OfferRequest;
import com.example.marketplace.dto.response.OfferResponse;
import com.example.marketplace.service.OfferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class OfferController {

    private final OfferService offerService;

    @GetMapping("/offers")
    public ResponseEntity<OfferPageResponse> getAllOffers(@RequestParam(defaultValue = "0", required = false) Integer pageNo, @RequestParam(defaultValue = "10", required = false) Integer pageSize){
        return new ResponseEntity<>(offerService.getOffers(pageNo, pageSize), HttpStatus.OK);
    }

    @GetMapping("/offers/{id}")
    public ResponseEntity<OfferResponse> getOfferById(@PathVariable Long id){
        return new ResponseEntity<>(offerService.getOfferById(id), HttpStatus.OK);
    }

    @GetMapping("/users/{userId}/offers")
    public ResponseEntity<OfferPageResponse> getOffersByUserId(@PathVariable Long userId, @RequestParam(defaultValue = "0", required = false) Integer pageNo, @RequestParam(defaultValue = "10", required = false) Integer pageSize){
        return new ResponseEntity<>(offerService.getOffersByUserId(userId, pageNo, pageSize), HttpStatus.OK);
    }

    @GetMapping(value = "/offers", params = "categoryName")
    public ResponseEntity<OfferPageResponse> getOffersByCategoryName(@RequestParam String categoryName, @RequestParam(defaultValue = "0", required = false) Integer pageNo, @RequestParam(defaultValue = "10", required = false) Integer pageSize){
        return new ResponseEntity<>(offerService.getOffersByCategoryName(categoryName, pageNo, pageSize), HttpStatus.OK);
    }

    @GetMapping("/offers/count")
    public ResponseEntity<List<OfferCount>> getCountOffersByCategory(){
        return new ResponseEntity<>(offerService.getCountOffersByCategory(), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('USER')")
    @PostMapping("/users/{userId}/offers")
    public ResponseEntity<OfferResponse> createOffer(@PathVariable Long userId, @RequestBody @Valid OfferRequest offerRequest){
        return new ResponseEntity<>(offerService.createOffer(userId, offerRequest), HttpStatus.CREATED);
    }
}
