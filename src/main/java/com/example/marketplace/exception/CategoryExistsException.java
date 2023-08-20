package com.example.marketplace.exception;

import jakarta.persistence.EntityExistsException;

public class CategoryExistsException extends EntityExistsException {
    public CategoryExistsException(String message){
        super(message);
    }
}
