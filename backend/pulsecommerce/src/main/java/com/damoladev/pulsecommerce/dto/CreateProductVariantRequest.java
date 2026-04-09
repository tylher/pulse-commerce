package com.damoladev.pulsecommerce.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.List;

public record CreateProductVariantRequest(
        @NotNull(message = "Price is required")
        @DecimalMin(value = "0.01", message = "Price must be greater than 0")
        @Digits(integer = 10, fraction = 2, message = "Price max 2 decimal places")
        BigDecimal price,

        @NotNull(message = "Quantity is required")
        @Min(value = 0, message = "Quantity cannot be negative")
        @Max(value = 10000, message = "Quantity unrealistically high")
        Integer quantity,

        @NotEmpty(message = "Variant must have at least one option")
        @Size(max = 10, message = "Too many options")
        List<CreateVariantOptionRequest> variantOptions

) {
}
