package com.damoladev.pulsecommerce.controller;

import com.damoladev.pulsecommerce.dto.ApiResponseDto;
import com.damoladev.pulsecommerce.dto.PlaceOrderDto;
import com.damoladev.pulsecommerce.service.order.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    @PostMapping()
    public ResponseEntity<ApiResponseDto<Void>> createOrder(@Valid @RequestBody PlaceOrderDto dto) throws BadRequestException {
        ApiResponseDto<Void> response  = orderService.placeOrder(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}
