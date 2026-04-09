package com.damoladev.pulsecommerce.model;

import com.damoladev.pulsecommerce.enums.ProductStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String categoryId;

    private String name;

    @Column(nullable = false)
    private String slug;

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    private boolean isSystem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parentId")
    private Category parentCategory;
}
