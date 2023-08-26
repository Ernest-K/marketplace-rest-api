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
    public ResponseEntity<OfferPageResponse> getAllOffers(@RequestParam(defaultValue = "0", required = false) Integer pageNo,
                                                          @RequestParam(defaultValue = "10", required = false) Integer pageSize,
                                                          @RequestParam(defaultValue = "id", required = false) String sortBy,
                                                          @RequestParam(defaultValue = "asc", required = false) String direction){
        return new ResponseEntity<>(offerService.getOffers(pageNo, pageSize, sortBy, direction), HttpStatus.OK);
    }

    @GetMapping("/offers/{id}")
    public ResponseEntity<OfferResponse> getOfferById(@PathVariable Long id){
        return new ResponseEntity<>(offerService.getOfferById(id), HttpStatus.OK);
    }

    @GetMapping("/users/{userId}/offers")
    public ResponseEntity<OfferPageResponse> getOffersByUserId(@PathVariable Long userId,
                                                               @RequestParam(defaultValue = "0", required = false) Integer pageNo,
                                                               @RequestParam(defaultValue = "10", required = false) Integer pageSize,
                                                               @RequestParam(defaultValue = "id", required = false) String sortBy,
                                                               @RequestParam(defaultValue = "asc", required = false) String direction){
        return new ResponseEntity<>(offerService.getOffersByUserId(userId, pageNo, pageSize, sortBy, direction), HttpStatus.OK);
    }

    @GetMapping(value = "/offers", params = "categoryName")
    public ResponseEntity<OfferPageResponse> getOffersByCategoryName(@RequestParam String categoryName,
                                                                     @RequestParam(defaultValue = "0", required = false) Integer pageNo,
                                                                     @RequestParam(defaultValue = "10", required = false) Integer pageSize,
                                                                     @RequestParam(defaultValue = "id", required = false) String sortBy,
                                                                     @RequestParam(defaultValue = "asc", required = false) String direction){
        return new ResponseEntity<>(offerService.getOffersByCategoryName(categoryName, pageNo, pageSize, sortBy, direction), HttpStatus.OK);
    }

    @GetMapping("/offers/count")
    public ResponseEntity<List<OfferCount>> getCountOffersByCategory(){
        return new ResponseEntity<>(offerService.getCountOffersByCategory(), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('USER')")
    @PostMapping("/users/{userId}/offers")
    public ResponseEntity<OfferResponse> createOffer(@PathVariable Long userId,
                                                     @RequestBody @Valid OfferRequest offerRequest){
        return new ResponseEntity<>(offerService.createOffer(userId, offerRequest), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('USER')")
    @PutMapping("/users/{userId}/offers/{offerId}")
    public ResponseEntity<OfferResponse> updateOffer(@PathVariable Long userId,
                                                     @PathVariable Long offerId,
                                                     @RequestBody @Valid OfferRequest offerRequest){
        return new ResponseEntity<>(offerService.updateOffer(userId, offerId, offerRequest), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('USER')")
    @DeleteMapping("/users/{userId}/offers/{offerId}")
    public ResponseEntity<String> deleteOffer(@PathVariable Long userId, @PathVariable Long offerId){
        offerService.deleteOffer(userId, offerId);
        return new ResponseEntity<>("Offer deleted successfully", HttpStatus.OK);
    }
}
