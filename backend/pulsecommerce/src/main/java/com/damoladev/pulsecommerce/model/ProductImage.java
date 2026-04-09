package com.damoladev.pulsecommerce.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
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
    private Product product;
}
