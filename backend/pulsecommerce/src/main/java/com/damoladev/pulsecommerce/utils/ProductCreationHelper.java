package com.damoladev.pulsecommerce.utils;

import com.damoladev.pulsecommerce.dto.*;
import com.damoladev.pulsecommerce.enums.ProductStatus;
import com.damoladev.pulsecommerce.exception.DuplicateProductException;
import com.damoladev.pulsecommerce.exception.ImageValidationException;
import com.damoladev.pulsecommerce.exception.InactiveProductException;
import com.damoladev.pulsecommerce.mappers.ProductMapper;
import com.damoladev.pulsecommerce.model.*;
import com.damoladev.pulsecommerce.repository.CategoryRepository;
import com.damoladev.pulsecommerce.repository.ProductRepository;
import com.damoladev.pulsecommerce.service.productImages.ProductImageService;
import com.damoladev.pulsecommerce.specifications.ProductSpecification;
import com.damoladev.pulsecommerce.validator.ImageValidator;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductCreationHelper {
    private final ImageValidator imageValidator;
    private final CategoryRepository categoryRepository;
    private final SlugHelper slugHelper;
    private final ProductRepository productRepository;
    private final ProductImageService productImageService;


    @Transactional
    public ProductResponseDto createProduct(CreateProductRequestDto dto, List<MultipartFile> files){
        try{
            //validate user as admin
            //check if the category exist, if not add it to uncategorized

            Category category  = categoryRepository.findById(dto.categoryId()).orElseThrow(
                    ()-> new ValidationException("Category with id, %s, not found".formatted(dto.categoryId()))
            );

            //check if the name already exist
            validateProductName(dto.name());

            // populate the product object
            Product newProduct = new Product();
            newProduct.setName(dto.name());
            newProduct.setBrand(dto.brand());
            newProduct.setDescription(dto.description());
            newProduct.setStatus(ProductStatus.DRAFT);
            newProduct.setCategory(category);
            newProduct.setActive(false);
            String productSlug = slugHelper.generateUniqueSlug(dto.name());
            newProduct.setSlug(productSlug);

            Set<ProductVariant> variants = createAndValidateProductVariants(dto.productVariants(),dto.name(),category.getName());

            for (ProductVariant variant : variants){
                variant.setProduct(newProduct);
                newProduct.getProductVariants().add(variant);
            }

            for(CreateProductSpecificationRequest spec:dto.specifications()){
                Specification specification = new Specification();
                specification.setKey(spec.key());
                specification.setValue(spec.value());
                specification.setProduct(newProduct);
                newProduct.getProductSpecifications().add(specification);
            }


            List<ProductImage> images = productImageService.uploadImages(files,dto.primaryImageIndex());


            for (ProductImage image : images){
                image.setProduct(newProduct);
            }

            newProduct.setProductImages(images);
            Product savedProduct = productRepository.save(newProduct);


            return ProductMapper.toProductResponseDto(savedProduct);


        }catch (ValidationException | DuplicateProductException
                | ImageValidationException | InactiveProductException ex) {
            throw ex;
        }  catch (IOException ex) {
            throw new ServiceException("Image upload failed", ex);
        } catch (DataAccessException ex) {
            throw new ServiceException("Database error during product creation", ex);
        } catch (Exception ex) {
            log.error("Unexpected error creating product: {}", dto.name(), ex);
            throw new ServiceException("Unexpected error", ex);
        }

    }

    private void validateVariants(List<CreateProductVariantRequest> variants){
        if (variants == null || variants.isEmpty()) {
            throw new ValidationException("Product must have at least one variant");
        }

        Set<String> seenCombinations = new HashSet<>();

        for (int i=0;i<variants.size();i++){
            CreateProductVariantRequest variant = variants.get(i);

            validateOptionNames(variant.variantOptions(),i);

            String combinationKey = variant.variantOptions().stream()
                    .sorted(Comparator.comparing(o-> o.option().toLowerCase()))
                    .map(opt->String.join(opt.option().toLowerCase(),
                            "=", opt.value().toLowerCase()))
                    .collect(Collectors.joining("|"));

            if(!seenCombinations.add(combinationKey)){
                throw new ValidationException(
                        "Duplicate variant combination at index " + i
                                + ": " + combinationKey
                );
            }
        }
    }

    private void validateOptionNames(List<CreateVariantOptionRequest> options,int index){
        Set<String> seenNames = new HashSet<>();

        for(CreateVariantOptionRequest option:options){
            String normalised = option.option().toLowerCase().trim();

            if(!seenNames.add(normalised)){
                throw new ValidationException(
                        "Variant at index " + index
                                + " has duplicate option name: '" + option.option() + "'"
                );
            }
        }
    }

    private Set<ProductVariant> createAndValidateProductVariants(List<CreateProductVariantRequest> variants
            ,String productName,String category){
        validateVariants(variants);
        Set<ProductVariant> variantSet = new HashSet<>();
        for(CreateProductVariantRequest variantDto: variants){
            ProductVariant variant = new ProductVariant();
            variant.setPrice(variantDto.price());
            variant.setStockQuantity(variantDto.quantity());
            variant.setInStock(variantDto.quantity()>0);
            variant.setCompareAtPrice(variantDto.price());
            variant.setSku(generateSKU(productName,category,variantDto.variantOptions()));

            variantDto.variantOptions().forEach(opt->{
                VariantOption option = new VariantOption();
                option.setName(opt.option());
                option.setValue(opt.value());

                variant.addOption(option);
            });

            variantSet.add(variant);
        }

        return variantSet;
    }

    private void validateProductName(String name){
        boolean activeExists = productRepository
                .existsByNameIgnoreCaseAndStatus(name, ProductStatus.ACTIVE);

        if (activeExists) {
            throw new DuplicateProductException(
                    "An active product named '" + name + "' already exists. " +
                            "Add a variant to it instead of creating a new product."
            );
        }

        Optional<Product> inActiveProduct = productRepository
                .findByByNameIgnoreCaseAndStatus(name, ProductStatus.ARCHIVED);

        if (inActiveProduct.isPresent()) {
            throw new InactiveProductException(
                    "An inactive product named '" + name+ "' already exists. " +
                            "Reactivate and update it, or choose a different name.",

                    inActiveProduct.get().getProductId()
            );
        }
    }

    private String generateSKU(String productName,String category,List<CreateVariantOptionRequest> options){
        StringBuilder sku = new StringBuilder();
        String prefix = category.substring(0,Math.min(3,category.length()));
        sku.append(prefix.toUpperCase()).append('-');

        String core = productName.replaceAll("[^0-9a-zA-Z]","").substring(0,Math.min(3,productName.length()));
        sku.append(core.toUpperCase());

        List<CreateVariantOptionRequest> sortedOptions = options.stream().sorted(Comparator
                .comparing(opt->opt.option().toLowerCase())).toList();

        for(CreateVariantOptionRequest opt: sortedOptions){
            String shortName = opt.value().replaceAll("[^0-9a-zA-Z]","").substring(0,Math.min(2,opt.value().length()));
            sku.append('-').append(shortName);
        }

        return sku.toString();
    }
}

