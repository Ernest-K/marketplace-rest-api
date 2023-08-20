package com.example.marketplace.exception;

import jakarta.persistence.EntityExistsException;

public class UserExistsException extends EntityExistsException {
    public UserExistsException(String message){
        super(message);
    }
}
