package com.damoladev.pulsecommerce.service.productImages;

import com.damoladev.pulsecommerce.model.ProductImage;
import com.damoladev.pulsecommerce.service.Images.FileStorageService;
import com.damoladev.pulsecommerce.utils.FilenameSanitizer;
import com.damoladev.pulsecommerce.validator.ImageValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductImageServiceImpl implements ProductImageService{
    private final ImageValidator imageValidator;
    private final FileStorageService storageService;

    @Override
    public List<ProductImage> uploadImages(List<MultipartFile> images, int primaryIndex) throws IOException {
        if(images==null||images.isEmpty()){
            throw new ValidationException("At least one image is required");
        }

        if(primaryIndex >= images.size()){
            throw new ValidationException("primary index is out of bounds");
        }

        List<ProductImage> imageList = new ArrayList<>();

        for(int i=0;i<images.size();i++){
            MultipartFile file = images.get(i);
            imageValidator.validate(file);
            String filename = FilenameSanitizer.sanitize(file.getOriginalFilename());
            log.info(filename);
            Map result = storageService.uploadFile(file,"pulse-commerce/products",filename);
            String url = result.get("secure_url").toString();
            String publicId = result.get("public_id").toString();


            ProductImage productImage = new ProductImage();
            productImage.setUrl(url);
            productImage.setPublicId(publicId);
            productImage.setPrimary(i==primaryIndex);
            imageList.add(productImage);
        }

        return imageList;
    }


}
