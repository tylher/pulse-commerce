package com.damoladev.pulsecommerce.service.productImages;

import com.damoladev.pulsecommerce.model.Product;
import com.damoladev.pulsecommerce.model.ProductImage;
import jakarta.servlet.http.HttpServletRequest;
import org.hibernate.validator.constraints.LuhnCheck;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public interface ProductImageService {
    public CompletableFuture<Void> uploadAndAttachImages(String productId, List<MultipartFile> images, int primaryIndex);

}
