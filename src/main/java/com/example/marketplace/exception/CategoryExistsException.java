package com.example.marketplace.exception;

public class CategoryExistsException extends RuntimeException{
    public CategoryExistsException(String message){
        super(message);
    }
}
