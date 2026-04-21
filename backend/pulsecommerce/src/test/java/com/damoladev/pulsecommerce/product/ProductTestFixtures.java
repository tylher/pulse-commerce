package com.damoladev.pulsecommerce.product;

import com.damoladev.pulsecommerce.dto.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.mock;

public class ProductTestFixtures {
    public static CreateProductRequestDto iphoneRequestDto() {
        CreateProductSpecificationRequest spec = new CreateProductSpecificationRequest(
                "Material", "Titanium"
        );

        CreateProductVariantRequest variant = new CreateProductVariantRequest(
                new BigDecimal("799.99"),
                50,
                List.of(
                        new CreateVariantOptionRequest("Color", "Black"),
                        new CreateVariantOptionRequest("Storage", "256GB")
                )
        );

        return new CreateProductRequestDto(
                "iPhone 15 Pro",
                "Apple's flagship smartphone with titanium design and A17 Pro chip.",
                "iphone-15-pro",
                "cat-electronics-001",
                "Apple",
                List.of(spec),
                List.of(variant),
                0
        );
    }

    public static CreateProductRequestDto samsungRequestDto() {
        CreateProductSpecificationRequest spec = new CreateProductSpecificationRequest(
                "Display", "6.8-inch Dynamic AMOLED"
        );

        CreateProductVariantRequest variantBlack = new CreateProductVariantRequest(
//                "Phantom Black / 128GB",
                new BigDecimal("649.99"),
                80,
                List.of(new CreateVariantOptionRequest("Color","Phantom Black"),
                        new CreateVariantOptionRequest("Size","128GB"))
        );

        CreateProductVariantRequest variantGreen = new CreateProductVariantRequest(
//                "Botanic Green / 256GB",
                new BigDecimal("749.99"),
                40,
                List.of(new CreateVariantOptionRequest("Color","Botanic Green"),
                        new CreateVariantOptionRequest("Size","256GB"))
        );

        return new CreateProductRequestDto(
                "Samsung Galaxy S24 Ultra",
                "Samsung's most powerful Galaxy smartphone with built-in S Pen and 200MP camera.",
                "samsung-galaxy-s24-ultra",
                "cat-electronics-001",
                "Samsung",
                List.of(spec),
                List.of(variantBlack, variantGreen),
                0
        );
    }

    public static CreateProductRequestDto sonyHeadphonesRequestDto() {
        CreateProductSpecificationRequest spec = new CreateProductSpecificationRequest(
                "Battery Life", "30 hours"
        );

        CreateProductVariantRequest variantBlack = new CreateProductVariantRequest(
//                "Midnight Black",
                new BigDecimal("299.99"),
                120,
                List.of(new CreateVariantOptionRequest("Color","Midnight Black"))
        );

        CreateProductVariantRequest variantSilver = new CreateProductVariantRequest(
//                "Platinum Silver",
                new BigDecimal("299.99"),
                60,
                List.of(new CreateVariantOptionRequest("Color","Platinum Silver"))
        );

        return new CreateProductRequestDto(
                "Sony WH-1000XM5",
                "Industry-leading noise cancelling headphones with 30-hour battery life.",
                "sony-wh-1000xm5",
                "cat-audio-002",
                "Sony",
                List.of(spec),
                List.of(variantBlack, variantSilver),
                0
        );
    }

    // ─── Response DTOs ───────────────────────────────────────────────────────

    public static ProductResponseDto iphoneResponseDto() {
        ImageResponseDto image = new ImageResponseDto("https://cdn.example.com/products/iphone-15-pro.jpg");
//        image.setUrl("https://cdn.example.com/products/iphone-15-pro.jpg");
//        image.setIsPrimary(true);

        ProductResponseDto response = new ProductResponseDto();
        response.setId("prod-uuid-001");
        response.setName("iPhone 15 Pro");
        response.setImages(List.of(image));
        response.setSuccess(true);

        return response;
    }

    public static ProductResponseDto samsungResponseDto() {
        ImageResponseDto primaryImage = new ImageResponseDto("https://cdn.example.com/products/samsung-s24-ultra.jpg");
//        primaryImage.setUrl("https://cdn.example.com/products/samsung-s24-ultra.jpg");
//        primaryImage.setIsPrimary(true);

        ImageResponseDto secondaryImage = new ImageResponseDto("https://cdn.example.com/products/samsung-s24-ultra-2.jpg");
//        secondaryImage.setUrl("https://cdn.example.com/products/samsung-s24-ultra-2.jpg");
//        secondaryImage.setIsPrimary(false);

        ProductResponseDto response = new ProductResponseDto();
        response.setId("prod-uuid-002");
        response.setName("Samsung Galaxy S24 Ultra");
        response.setImages(List.of(primaryImage, secondaryImage));
        response.setSuccess(true);

        return response;
    }

    public static ProductResponseDto sonyHeadphonesResponseDto() {
        ImageResponseDto primaryImage = new ImageResponseDto("https://cdn.example.com/products/sony-wh1000xm5.jpg");
//        primaryImage.setUrl("https://cdn.example.com/products/sony-wh1000xm5.jpg");
//        primaryImage.setIsPrimary(true);

        ImageResponseDto secondaryImage = new ImageResponseDto("https://cdn.example.com/products/sony-wh1000xm5-2.jpg");
//        secondaryImage.setUrl("https://cdn.example.com/products/sony-wh1000xm5-2.jpg");
//        secondaryImage.setIsPrimary(false);

        ProductResponseDto response = new ProductResponseDto();
        response.setId("prod-uuid-003");
        response.setName("Sony WH-1000XM5");
        response.setImages(List.of(primaryImage, secondaryImage));
        response.setSuccess(true);

        return response;
    }

    public static List<CreateProductRequestDto> allRequestDtos() {
        return List.of(iphoneRequestDto(), samsungRequestDto(), sonyHeadphonesRequestDto());
    }

    public static List<ProductResponseDto> allResponseDtos() {
        return List.of(iphoneResponseDto(), samsungResponseDto(), sonyHeadphonesResponseDto());
    }

    public static List<List<MultipartFile>> mockImageGroups() {
        MultipartFile mockFile = mock(MultipartFile.class);
        return List.of(
                List.of(mockFile),
                List.of(mockFile, mockFile),
                List.of(mockFile, mockFile)
        );
    }

}
