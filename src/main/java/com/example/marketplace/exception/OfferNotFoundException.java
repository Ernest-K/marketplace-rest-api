package com.example.marketplace.exception;

import jakarta.persistence.EntityNotFoundException;

public class OfferNotFoundException extends EntityNotFoundException {
    public OfferNotFoundException(String message) {
        super(message);
    }
}
