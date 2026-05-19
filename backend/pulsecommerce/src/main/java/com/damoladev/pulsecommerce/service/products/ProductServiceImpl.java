package com.damoladev.pulsecommerce.service.products;

import com.damoladev.pulsecommerce.dto.ApiResponseDto;
import com.damoladev.pulsecommerce.dto.CreateProductRequestDto;
import com.damoladev.pulsecommerce.dto.ProductFilter;
import com.damoladev.pulsecommerce.dto.ProductResponseDto;
import com.damoladev.pulsecommerce.exception.BatchProductException;
import com.damoladev.pulsecommerce.mappers.ProductMapper;
import com.damoladev.pulsecommerce.model.Product;
import com.damoladev.pulsecommerce.repository.ProductRepository;
import com.damoladev.pulsecommerce.specifications.ProductSpecification;
import com.damoladev.pulsecommerce.utils.ProductCreationHelper;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{

    private final ProductCreationHelper productCreationHelper;
    private final ProductRepository productRepository;

    @Override
    public ApiResponseDto<List<ProductResponseDto>> createProducts(List<CreateProductRequestDto> dtos, List<List<MultipartFile>> imageGroups) {
        long start = System.currentTimeMillis();
        log.info("createProducts called with {} products", dtos.size());

        if (dtos.size() != imageGroups.size()) {
            throw new ValidationException("Product count and image group count must match");
        }

        List<ProductResponseDto> responses = new ArrayList<>();
        List<String> errors = new ArrayList<>();

        for (int i = 0; i < dtos.size(); i++) {
            long productStart = System.currentTimeMillis();
            log.info("Creating product[{}]: {}", i, dtos.get(i).name());
            try {
                responses.add(productCreationHelper.createProduct(dtos.get(i), imageGroups.get(i)));
                log.info("Product[{}] ({}) created in {}ms", i, dtos.get(i).name(), System.currentTimeMillis() - productStart);
            } catch (Exception ex) {
                log.error("Product[{}] ({}) failed after {}ms: {}", i, dtos.get(i).name(), System.currentTimeMillis() - productStart, ex.getMessage());
                errors.add("Product " + i + " (" + dtos.get(i).name() + "): " + ex.getMessage());
            }
        }

        log.info("createProducts total time: {}ms", System.currentTimeMillis() - start);

        if (!errors.isEmpty()) {
            throw new BatchProductException("Some products failed", responses, errors);
        }

        return new ApiResponseDto<>(true, "Batch products created successfully", responses);
    }

    @Override
    public ApiResponseDto<List<ProductResponseDto>> getProducts(ProductFilter filter, Pageable pageable) {
        Specification<Product> spec = ProductSpecification.withFilter(filter);

        Page<ProductResponseDto> products = productRepository.findAll(spec,pageable).map(
                ProductMapper::toProductResponseDto
        );
        return new ApiResponseDto<>(true, "Products fetched successfully",products.getContent());
    }




}
