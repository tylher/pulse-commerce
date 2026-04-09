package com.damoladev.pulsecommerce.mappers;

import com.damoladev.pulsecommerce.dto.ImageResponseDto;
import com.damoladev.pulsecommerce.dto.ProductResponseDto;
import com.damoladev.pulsecommerce.model.Product;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductMapper {
    public static ProductResponseDto toProductResponseDto(Product product){
        ProductResponseDto responseDto = new ProductResponseDto();
        responseDto.setId(product.getProductId());
        responseDto.setName(product.getName());
        responseDto.setSuccess(true);

        List<ImageResponseDto> imgs = product.getProductImages().stream().map((img)->{
            return new ImageResponseDto(img.getPublicId());
        }).toList();
        responseDto.setImages(imgs);

        return responseDto;
    }
}
