package com.damoladev.pulsecommerce.controller;

import com.damoladev.pulsecommerce.dto.ApiResponseDto;
import com.damoladev.pulsecommerce.dto.CreateProductRequestDto;
import com.damoladev.pulsecommerce.service.products.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ObjectMapper objectMapper;
    private final Validator validator;
    private final ProductService productService;

    @PostMapping(value = "/batch", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponseDto> createProducts(HttpServletRequest request){
        StandardMultipartHttpServletRequest multipart = (StandardMultipartHttpServletRequest) request;
        List<CreateProductRequestDto> productRequestDTOs = new ArrayList<>();
        List<List<MultipartFile>> productImages= new ArrayList<>();

        int index= 0;
        while (multipart.getParameterMap().containsKey("product["+index+"].data")){
            String json = multipart.getParameter("product["+index+"].data");
            CreateProductRequestDto dto = objectMapper.readValue(json, CreateProductRequestDto.class);

            Set<ConstraintViolation<CreateProductRequestDto>> violations = validator.validate(dto);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException("Product " + index + " invalid", violations);
            }

            List<MultipartFile> imagesDTO = multipart.getFiles("product["+index+"].images");

            productRequestDTOs.add(dto);
            productImages.add(imagesDTO);
            index++;
        }

        if (productRequestDTOs.isEmpty()){
            throw new ValidationException("No products provided");
        }

        return new ResponseEntity<>(productService.createProducts(productRequestDTOs,productImages), HttpStatus.CREATED);
    }
}
