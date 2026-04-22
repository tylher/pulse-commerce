package com.damoladev.pulsecommerce.specifications;

import com.damoladev.pulsecommerce.dto.ProductFilter;
import com.damoladev.pulsecommerce.model.Product;
import com.damoladev.pulsecommerce.model.ProductVariant;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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


        return (root, query, cb) -> {
            // Both null — no filter, return null so Specification.where() ignores it
            if (categoryId == null && categoryName == null) return cb.conjunction();

            List<Predicate> predicates = new ArrayList<>();

            // Check each independently — only add the condition if the value is present
            if (categoryId != null) {
                predicates.add(cb.equal(root.get("category").get("categoryId"), categoryId));
            }
            if (categoryName != null) {
                predicates.add(
                        cb.like(cb.lower(root.get("category").get("name")),
                                "%" + categoryName.toLowerCase() + "%")
                );
            }

            return cb.or(predicates.toArray(new Predicate[0]));
        };
    }

    private static Specification<Product> hasPriceAboveAndPriceBelow(BigDecimal minPrice, BigDecimal maxPrice){

        return (root,query,cb)->{
            if (minPrice == null && maxPrice == null) return cb.conjunction();
            Join<Product, ProductVariant> variants = root.join("productVariants",JoinType.LEFT);

            Predicate minPredicate = minPrice!=null? cb.greaterThanOrEqualTo(variants.get("price")
            ,minPrice):cb.conjunction();

            Predicate maxPedicate  = maxPrice!=null ? cb.lessThanOrEqualTo(variants.get("price"),maxPrice): cb.conjunction();

            return cb.and(maxPedicate,minPredicate);
        };
    }

    private static Specification<Product> isInStock(Boolean inStock){


        return (root, query, cb) -> {
            // Skip the join entirely when the filter is not requested
            if (inStock == null || !inStock) return cb.conjunction();
            Join<Product, ProductVariant> variants = root.join("productVariants", JoinType.LEFT);
            return cb.greaterThan(variants.get("stockQuantity"), 0);
        };
    }

    private static Specification<Product> matchesQuery(String q){
        return (root,query,cb)->{
            if(q==null||q.isBlank()) return cb.conjunction();

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
