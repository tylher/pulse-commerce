package com.damoladev.pulsecommerce.utils;

import com.damoladev.pulsecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SlugHelper {
    private final ProductRepository productRepository;

    public String generateUniqueSlug(String productName){
        int counter = 1;
        String baseSlug = createSlug(productName);
        String uniqueSlug = baseSlug;
        while(productRepository.existsBySlug(uniqueSlug)){
            uniqueSlug = baseSlug+"-"+counter;
            counter++;
        }

        return uniqueSlug;
    }

    private String createSlug(String input){
        return input.toLowerCase()
                .trim()
                .replaceAll("[^\\p{ASCII}]","")
                .replaceAll("[^0-9a-z\\s]","")
                .replaceAll("\\s+","-")
                .replaceAll("^-|-$","");

    }
}
