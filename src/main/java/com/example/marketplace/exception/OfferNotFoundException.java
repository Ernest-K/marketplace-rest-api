package com.example.marketplace.exception;

public class OfferNotFoundException extends RuntimeException{
    public OfferNotFoundException(String message) {
        super(message);
    }
}
