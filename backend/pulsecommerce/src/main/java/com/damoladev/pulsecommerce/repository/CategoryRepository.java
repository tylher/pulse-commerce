package com.damoladev.pulsecommerce.repository;

import com.damoladev.pulsecommerce.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category,String> {
    boolean existsByIsSystemTrue();


    Optional<Category> findByIsSystemTrue();
}
