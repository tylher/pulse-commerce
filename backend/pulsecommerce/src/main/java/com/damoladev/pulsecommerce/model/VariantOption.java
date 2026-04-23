package com.damoladev.pulsecommerce.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DialectOverride;

@Entity
@Data
@EqualsAndHashCode(exclude = "variant")
@NoArgsConstructor
public class VariantOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String value;

    @ManyToOne
    @JoinColumn(name="variantId",referencedColumnName = "id")
    private ProductVariant variant;
}
