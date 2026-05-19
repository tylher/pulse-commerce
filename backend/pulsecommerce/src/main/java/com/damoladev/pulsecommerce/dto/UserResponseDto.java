package com.damoladev.pulsecommerce.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponseDto {
    private String firstName;
    private String lastName;
    private String email;
}
