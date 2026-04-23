package com.damoladev.pulsecommerce.model;

import com.damoladev.pulsecommerce.enums.ProductStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.AnyDiscriminatorImplicitValues;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;



@Entity
@Data
@EqualsAndHashCode(exclude = {"productVariants","productImages","productSpecifications"}, callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class Product extends AuditableEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String productId;

    private String name;

    private String description;

    private String slug;

    private String brand;

    private boolean active = true;

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    @OneToOne
    @JoinColumn(name = "categoryId")
    private Category category;

    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<ProductVariant> productVariants = new HashSet<>();

    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL)
    private List<ProductImage> productImages = new ArrayList<>();

    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<Specification> productSpecifications = new HashSet<>();

}
