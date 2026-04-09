package com.damoladev.pulsecommerce.exception;

import com.damoladev.pulsecommerce.dto.ProductResponseDto;
import lombok.Getter;

import java.util.List;

@Getter
public class BatchProductException extends RuntimeException{
    private final List<ProductResponseDto> successes;
    private final List<String> errors;

    public BatchProductException(String message,List<ProductResponseDto> successes,List<String> errors){
        super(message);
        this.successes = successes;
        this.errors = errors;
    }
}
