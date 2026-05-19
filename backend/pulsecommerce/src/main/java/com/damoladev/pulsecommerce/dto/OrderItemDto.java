package com.damoladev.pulsecommerce.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record OrderItemDto(
        String productName,
        @NotNull(message = "Product Id cannot be blank")
        Long productId,
        @Min(value = 1,message = "Quantity must be at least 1")
        @NotNull(message = "Quantity cannot be null")
        Integer quantity
) {
}
