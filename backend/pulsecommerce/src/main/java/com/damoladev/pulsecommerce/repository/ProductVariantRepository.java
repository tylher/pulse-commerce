package com.damoladev.pulsecommerce.repository;

import com.damoladev.pulsecommerce.model.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductVariantRepository extends JpaRepository<ProductVariant,Long> {
    @Query("""
            SELECT v FROM ProductVariant v WHERE v.id = :id
            """)
    public Optional<ProductVariant> findByIdForUpdate(@Param("id") Long id);

}
