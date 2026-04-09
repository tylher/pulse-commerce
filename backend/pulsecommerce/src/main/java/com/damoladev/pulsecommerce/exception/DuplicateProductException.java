package com.damoladev.pulsecommerce.exception;

public class DuplicateProductException extends RuntimeException{
    public DuplicateProductException(String message){
        super(message);
    }
}
