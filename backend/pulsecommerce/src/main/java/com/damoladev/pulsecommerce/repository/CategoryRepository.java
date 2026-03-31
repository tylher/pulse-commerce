package com.damoladev.pulsecommerce.repository;

import com.damoladev.pulsecommerce.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,String> {
}
