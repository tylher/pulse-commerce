package com.damoladev.pulsecommerce.service.order;

import com.damoladev.pulsecommerce.dto.ApiResponseDto;
import com.damoladev.pulsecommerce.dto.PlaceOrderDto;
import org.apache.coyote.BadRequestException;

public interface OrderService {
    public ApiResponseDto<Void> placeOrder(PlaceOrderDto dto) throws BadRequestException;
}
