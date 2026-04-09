package com.damoladev.pulsecommerce.dto;

import jakarta.validation.constraints.*;

import java.util.List;

public record CreateProductRequestDto(
        @NotBlank(message = "name cannot be blank")
        String name,
        @NotBlank(message = "Product description cannot be blank")
        @Size(max = 2000, message = "Description too long")
        String description,
        @Pattern(regexp = "^[a-z0-9-]+$", message = "Slug must be lowercase letters, numbers and hyphens only")
        String slug,
        @NotBlank(message = "Category id cannot be blank")
        String categoryId,
        @Size(max = 100,message ="Brand name too long")
        String brand,
        List<CreateProductSpecificationRequest> specifications,
        @NotEmpty(message = "Product should have at least on variant")
        @Size(max = 10, message = "Cannot exceed 10 variants")
        List<CreateProductVariantRequest> productVariants,
        @NotEmpty
        Integer primaryImageIndex
) {
}
