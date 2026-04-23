package com.damoladev.pulsecommerce.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(exclude = {"variantOptions", "product"})
@NoArgsConstructor
public class ProductVariant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sku;

    private BigDecimal price;

    private BigDecimal compareAtPrice;

    private Integer stockQuantity;

    private boolean inStock;

    @OneToMany(mappedBy = "variant",cascade = CascadeType.ALL)
    private Set<VariantOption> variantOptions = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;


    public void addOption(VariantOption option){
        option.setVariant(this);
        variantOptions.add(option);
    }
}
