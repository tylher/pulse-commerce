package com.damoladev.pulsecommerce.specifications;

import com.damoladev.pulsecommerce.dto.ProductFilter;
import com.damoladev.pulsecommerce.model.Product;
import com.damoladev.pulsecommerce.model.ProductVariant;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class ProductSpecification {
    public static Specification<Product> withFilter(ProductFilter filter){
        return (root,query,cb)->{
            query.distinct(true);

            return Specification.where(hasCategory(filter.getCategoryId(), filter.getCategoryName()))
                    .and(hasPriceAboveAndPriceBelow(filter.getMinPrice(),filter.getMaxPrice()))
                    .and(isInStock(filter.getInStock()))
                    .and(matchesQuery(filter.getQuery()))
                    .and(isActive())
                    .toPredicate(root,query,cb);

        };

    }

    private static Specification<Product> hasCategory(String categoryId, String categoryName){
        return (root,query,cb)->
                categoryId==null&&categoryName==null?null:
                        cb.or(cb.like(cb.lower(root.get("category").get("categoryId")),categoryId),
                                cb.like(cb.lower(root.get("category").get("name")),categoryName));
    }

    private static Specification<Product> hasPriceAboveAndPriceBelow(BigDecimal minPrince, BigDecimal maxPrice){
        return (root,query,cb)->{
            Join<Product, ProductVariant> variants = root.join("productVariants");

            Predicate minPredicate = minPrince!=null? cb.greaterThanOrEqualTo(variants.get("price")
            ,minPrince):null;

            Predicate maxPedicate  = maxPrice!=null ? cb.lessThanOrEqualTo(variants.get("price"),maxPrice):null;

            return cb.and(maxPedicate,minPredicate);
        };
    }

    private static Specification<Product> isInStock(Boolean inStock){
        return (root,query,cb)->{
            Join<Product, ProductVariant> variants = root.join("productVariants");
            if (inStock == null || !inStock) return null;
            return cb.greaterThan(variants.get("stockQuantity"),0);
        };
    }

    private static Specification<Product> matchesQuery(String q){
        return (root,query,cb)->{
            if(q==null||q.isBlank()) return null;

            String pattern = "%"+q.toLowerCase()+"%";
            return cb.or(cb.like(cb.lower(root.get("name")),pattern),
                    cb.like(cb.lower(root.get("description")), pattern));
        };
    }

    private  static Specification<Product> isActive(){
        return (root,query,cb)->
                cb.isTrue(root.get("active"));
    }
}
