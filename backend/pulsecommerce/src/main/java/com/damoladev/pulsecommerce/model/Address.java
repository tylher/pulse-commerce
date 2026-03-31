package com.damoladev.pulsecommerce.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;

@Embeddable
@AllArgsConstructor
@Data
public class Address {
    private String line1;
    private String line2;
    private String postalCode;
    private String city;
    private String state;
    private String country;

    private Double latitude;
    private Double longitude;
}
