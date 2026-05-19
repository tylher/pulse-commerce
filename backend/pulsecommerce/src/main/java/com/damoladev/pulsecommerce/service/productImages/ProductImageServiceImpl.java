package com.damoladev.pulsecommerce.service.productImages;

import com.damoladev.pulsecommerce.exception.ResourceNotFoundException;
import com.damoladev.pulsecommerce.model.Product;
import com.damoladev.pulsecommerce.model.ProductImage;
import com.damoladev.pulsecommerce.repository.ProductImageRepository;
import com.damoladev.pulsecommerce.repository.ProductRepository;
import com.damoladev.pulsecommerce.service.Images.FileStorageService;
import com.damoladev.pulsecommerce.utils.FilenameSanitizer;
import com.damoladev.pulsecommerce.utils.ImageUploadHelper;
import com.damoladev.pulsecommerce.validator.ImageValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.stream.IntStream;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductImageServiceImpl implements ProductImageService{
    private final ProductImageRepository productImageRepository;
    private final ProductRepository productRepository;
    private final ImageUploadHelper imageUploadHelper;

    @Async("imageUploadExecutor")
    @Override
    public CompletableFuture<Void> uploadAndAttachImages(String productId,List<MultipartFile> images, int primaryIndex){

        long start = System.currentTimeMillis();
        log.info("uploadAndAttachImages started on thread: {} for product: {}",
                Thread.currentThread().getName(), productId);
        try{
            long uploadStart = System.currentTimeMillis();
            Product product = productRepository.findById(productId).orElseThrow(
                    ()-> new ResourceNotFoundException("Product","product id",productId)
            );
            List<ProductImage> savedImages = imageUploadHelper.uploadImages(images,primaryIndex);
            log.info("uploadImages took {}ms", System.currentTimeMillis() - uploadStart);

            long dbSaveStart = System.currentTimeMillis();
            productImageRepository.saveAll(savedImages.stream().peek(
                    img->{
                        img.setProduct(product);
                    }

            ).toList());


            log.info("Image DB save took {}ms", System.currentTimeMillis() - dbSaveStart);

            log.info("uploadAndAttachImages total time: {}ms", System.currentTimeMillis() - start);

        }catch (Exception e){
            log.error("Image upload failed for product {}: {}", productId, e.getMessage());
        }
        return CompletableFuture.completedFuture(null);
    }


}
