package com.damoladev.pulsecommerce.repository;

import com.damoladev.pulsecommerce.enums.ProductStatus;
import com.damoladev.pulsecommerce.model.Product;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,String>, JpaSpecificationExecutor<Product> {
    boolean existsByNameIgnoreCaseAndStatus(@NotBlank(message = "name cannot be blank") String name, ProductStatus productStatus);

    Optional<Product> findByByNameIgnoreCaseAndStatus(@NotBlank(message = "name cannot be blank") String name, ProductStatus productStatus);

    boolean existsBySlug(String uniqueSlug);

}
