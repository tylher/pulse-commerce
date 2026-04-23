package com.damoladev.pulsecommerce.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(exclude = "product")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String imageId;

    private String url;

    private String publicId;

    private boolean isPrimary;

    @ManyToOne
    @JoinColumn(name = "productId",referencedColumnName = "productId")
    private Product product;
}
