package com.damoladev.pulsecommerce.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String itemId;

    private Integer quantity;

    private BigDecimal priceAtPurchase;

    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "productVariantId")
    private ProductVariant product;


    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "orderId", referencedColumnName = "id")
    private Order order;
}
