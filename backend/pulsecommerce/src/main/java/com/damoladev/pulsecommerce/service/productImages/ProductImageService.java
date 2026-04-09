package com.damoladev.pulsecommerce.service.productImages;

import com.damoladev.pulsecommerce.model.ProductImage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface ProductImageService {
    public List<ProductImage> uploadImages(List<MultipartFile> images, int primaryIndex) throws IOException;
}
