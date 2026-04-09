package com.damoladev.pulsecommerce.utils;

import com.damoladev.pulsecommerce.enums.ProductStatus;
import com.damoladev.pulsecommerce.model.Category;
import com.damoladev.pulsecommerce.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SystemDataSeeder implements ApplicationRunner {
    private  final CategoryRepository categoryRepository;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        boolean exists= categoryRepository.existsByIsSystemTrue();

        if(!exists){
            Category category = new Category();
            category.setSystem(true);
            category.setName("Uncategorized");
            category.setStatus(ProductStatus.ACTIVE);
            categoryRepository.save(category);
        }
    }
}
