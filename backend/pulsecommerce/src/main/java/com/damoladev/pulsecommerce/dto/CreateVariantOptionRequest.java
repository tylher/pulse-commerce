package com.damoladev.pulsecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateVariantOptionRequest(
        @NotBlank(message = "option name cannot be blank")
        @Size(max = 100,message = "variant option too long")
        String option,

        @NotBlank(message = "value cannot be blank")
        @Size(max = 100, message = "value to long, should not exceed 100 characters")
        String value
) {
}
