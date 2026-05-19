package com.damoladev.pulsecommerce.order;

import com.damoladev.pulsecommerce.dto.OrderItemDto;
import com.damoladev.pulsecommerce.dto.PlaceOrderDto;
import com.damoladev.pulsecommerce.model.ProductVariant;
import com.damoladev.pulsecommerce.model.User;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class OrderTestFixtures {
    private static final Long variantId = 10L;
    @Getter
    private static final String idempotentKey = UUID.randomUUID().toString();
    private static final String userId = "userId";


    public static User getMockUser(){
        User user = new User();
        user.setUserId(userId);

        return user;
    }

    public static ProductVariant mockVariant(int stock, String sku, String price) {
        ProductVariant v = new ProductVariant();
        v.setId(variantId);
        v.setStockQuantity(stock);
        v.setSku(sku);
        v.setPrice(new BigDecimal(price));
        return v;
    }

    public static PlaceOrderDto getMockOrderRequest(){
        PlaceOrderDto dto = new PlaceOrderDto( userId,idempotentKey,
                List.of(new OrderItemDto(null,variantId, 2)));
        return dto;
    }

}
