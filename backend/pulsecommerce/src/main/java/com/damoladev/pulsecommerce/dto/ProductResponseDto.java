package com.damoladev.pulsecommerce.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ProductResponseDto{
        private String id;
        private String name;
        private List<ImageResponseDto> images;
        private boolean success;
}
