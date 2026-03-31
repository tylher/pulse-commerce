package com.damoladev.pulsecommerce.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
public class ProductVariant {
    private Long id;

    private String sku;

    private BigDecimal price;

    private BigDecimal compareAtPrice;

    private Integer stockQuantity;

    private boolean inStock;

    @OneToMany(mappedBy = "variant",cascade = CascadeType.ALL)
    private Set<VariantOption> variantOption = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
