package com.damoladev.pulsecommerce.product;

import com.damoladev.pulsecommerce.dto.ApiResponseDto;
import com.damoladev.pulsecommerce.dto.CreateProductRequestDto;
import com.damoladev.pulsecommerce.dto.ProductResponseDto;
import com.damoladev.pulsecommerce.service.products.ProductService;
import com.damoladev.pulsecommerce.service.products.ProductServiceImpl;
import com.damoladev.pulsecommerce.utils.ProductCreationHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    ProductCreationHelper productCreationHelper;

    @InjectMocks
    ProductServiceImpl productService;

    @Test
    public void ShouldCreateAllProductsSuccessfully(){
        List<CreateProductRequestDto> mockProductsRequest  = ProductTestFixtures.allRequestDtos();
        List<List<MultipartFile>> mockImageGroup = ProductTestFixtures.mockImageGroups();
        List<ProductResponseDto> mockCreateProductResponseDto = ProductTestFixtures.allResponseDtos();

        for (int i=0; i < mockProductsRequest.size();i++){
            when(productCreationHelper.createProduct(mockProductsRequest.get(i),mockImageGroup.get(i))).thenReturn(
                    mockCreateProductResponseDto.get(i)
            );
        }
        ApiResponseDto<List<ProductResponseDto>> responseDto = productService.createProducts(mockProductsRequest,mockImageGroup);

        assertTrue(responseDto.isStatus());
        List<ProductResponseDto> data =  responseDto.getData();
        assertEquals(3,data.size());
        assertEquals("prod-uuid-002", data.get(1).getId());

    }
}
