package com.damoladev.pulsecommerce.repository;

import com.damoladev.pulsecommerce.enums.ProductStatus;
import com.damoladev.pulsecommerce.model.Product;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,String> {
    boolean existsByNameIgnoreCaseAndStatus(@NotBlank(message = "name cannot be blank") String name, ProductStatus productStatus);

    Optional<Product> findByByNameIgnoreCaseAndStatus(@NotBlank(message = "name cannot be blank") String name, ProductStatus productStatus);

    boolean existsBySlug(String uniqueSlug);
}
