package com.damoladev.pulsecommerce.exception;

public class InsufficientStockException extends RuntimeException{
    public InsufficientStockException(String sku, int available, int requested){
        super(String.format("Insufficient stock for: %s, Availability: %d, Requested: %d",sku,available,requested));
    }
}
