package com.damoladev.pulsecommerce.exception;

import lombok.Getter;

@Getter
public class InactiveProductException extends RuntimeException{
    private final String productId;

    public InactiveProductException(String message,String productId){
        super(message);
        this.productId = productId;
    }

}
