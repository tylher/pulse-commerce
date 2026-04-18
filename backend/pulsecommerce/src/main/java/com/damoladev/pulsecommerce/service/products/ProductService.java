package com.damoladev.pulsecommerce.service.products;

import com.damoladev.pulsecommerce.dto.ApiResponseDto;
import com.damoladev.pulsecommerce.dto.CreateProductRequestDto;
import com.damoladev.pulsecommerce.dto.ProductFilter;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    public ApiResponseDto createProducts(List<CreateProductRequestDto> dtos, List<List<MultipartFile>> imageGroups);

    ApiResponseDto getProducts(ProductFilter filter, Pageable pageable);
}
