package com.damoladev.pulsecommerce.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String imageId;

    private String url;

    private boolean isPrimary;

    @ManyToOne
    private Product product;
}
