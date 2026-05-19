package com.damoladev.pulsecommerce.utils;

import com.damoladev.pulsecommerce.model.ProductImage;
import com.damoladev.pulsecommerce.service.Images.FileStorageService;
import com.damoladev.pulsecommerce.validator.ImageValidator;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.stream.IntStream;

@Slf4j
@RequiredArgsConstructor
@Service
public class ImageUploadHelper {

    private final ImageValidator imageValidator;
    private final FileStorageService storageService;

    public List<ProductImage> uploadImages(List<MultipartFile> images, int primaryIndex) throws IOException {
        long start = System.currentTimeMillis();
        log.info("uploadImages called with {} images", images.size());

        if (images == null || images.isEmpty()) {
            throw new ValidationException("At least one image is required");
        }
        if (primaryIndex >= images.size()) {
            throw new ValidationException("primary index is out of bounds");
        }

        List<CompletableFuture<ProductImage>> futures = IntStream.range(0, images.size())
                .mapToObj(i -> CompletableFuture.supplyAsync(() -> {
                    MultipartFile file = images.get(i);
                    long imageStart = System.currentTimeMillis();
                    try {
                        imageValidator.validate(file);

                        String filename = FilenameSanitizer.sanitize(file.getOriginalFilename());
                        Map result = storageService.uploadFile(file, "pulse-commerce/products", filename);

                        ProductImage img = new ProductImage();
                        img.setUrl(result.get("secure_url").toString());
                        img.setPublicId(result.get("public_id").toString());
                        img.setPrimary(i == primaryIndex);

                        log.info("Image[{}] ({}) uploaded in {}ms", i, filename, System.currentTimeMillis() - imageStart);
                        return img;

                    } catch (Exception e) {
                        throw new CompletionException(e);
                    }
                }))
                .toList();

        List<ProductImage> uploadedImages = futures.stream()
                .map(CompletableFuture::join)
                .toList();

        log.info("uploadImages total time: {}ms", System.currentTimeMillis() - start);
        return uploadedImages;
    }

}
