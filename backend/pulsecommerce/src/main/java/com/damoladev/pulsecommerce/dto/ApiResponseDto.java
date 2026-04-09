package com.damoladev.pulsecommerce.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponseDto {
    private boolean status;
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object data;


    ApiResponseDto(boolean status,String message){
        this.status = status;
        this.message = message;
    }
}
