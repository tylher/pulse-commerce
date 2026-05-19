package com.damoladev.pulsecommerce.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;

import java.util.List;

public record PlaceOrderDto(
         @NotEmpty(message = "User id is required")
         String userId,
         @NotEmpty(message = "Idempotent key is required")
         String key,

         @NotEmpty(message = "Order must contain at least one item")
         @Valid
         List<OrderItemDto> orderItems
) {
}
