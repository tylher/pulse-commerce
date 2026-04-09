package com.damoladev.pulsecommerce.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateProductSpecificationRequest(
        @NotBlank(message = "specification key cannot be blank")
        String key,
        @NotBlank(message = "specification value cannot be blank")
        String value
) {
}
