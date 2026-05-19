package com.damoladev.pulsecommerce.order;

import com.damoladev.pulsecommerce.dto.ApiResponseDto;
import com.damoladev.pulsecommerce.dto.PlaceOrderDto;
import com.damoladev.pulsecommerce.enums.OrderStatus;
import com.damoladev.pulsecommerce.exception.InsufficientStockException;
import com.damoladev.pulsecommerce.exception.ResourceNotFoundException;
import com.damoladev.pulsecommerce.model.ProductVariant;
import com.damoladev.pulsecommerce.model.User;
import com.damoladev.pulsecommerce.repository.OrderItemRepository;
import com.damoladev.pulsecommerce.repository.OrderRepository;
import com.damoladev.pulsecommerce.repository.ProductVariantRepository;
import com.damoladev.pulsecommerce.repository.UserRepository;
import com.damoladev.pulsecommerce.service.order.OrderService;
import com.damoladev.pulsecommerce.service.order.OrderServiceImpl;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private ProductVariantRepository productVariantRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    OrderServiceImpl orderService;

    @Test
    @DisplayName("Returns success immediately idempotency key exists")
    void placeOrder_idempotentKeyExists_returnsSuccessWithoutProcessing() throws BadRequestException {
        PlaceOrderDto orderRequest = OrderTestFixtures.getMockOrderRequest();
        when(orderRepository.existsByIdempotentKey(orderRequest.key())).thenReturn(true);

        ApiResponseDto<Void> result = orderService.placeOrder(orderRequest);

        assertTrue(result.isStatus());
        assertThat(result.getMessage()).isEqualTo("Order placed successfully");
        verifyNoInteractions(userRepository, productVariantRepository, orderItemRepository);
        Mockito.verify(orderRepository, never()).save(any());
    }

    @Test
    @DisplayName("Returns resource not found exception when user cannot be found")
    void placeOrder_userNotFound() throws BadRequestException {
        PlaceOrderDto orderDto = OrderTestFixtures.getMockOrderRequest();
        when(orderRepository.existsByIdempotentKey(orderDto.key())).thenReturn(false);
        when(userRepository.findById("userId")).thenReturn(Optional.empty());

        assertThatThrownBy(()->orderService.placeOrder(orderDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("user id");
    }

    @Test
    @DisplayName("Returns resource not found exception when variant does not exist")
    void placeOrder_variantDoesNotExist() throws BadRequestException{
        PlaceOrderDto orderDto = OrderTestFixtures.getMockOrderRequest();
        User user = OrderTestFixtures.getMockUser();

        when(orderRepository.existsByIdempotentKey(orderDto.key())).thenReturn(false);
        when(productVariantRepository.findByIdForUpdate(orderDto.orderItems().getFirst().productId())).thenReturn(Optional.empty());
        when(userRepository.findById(orderDto.userId())).thenReturn(Optional.of(user));

        assertThatThrownBy(()->orderService.placeOrder(orderDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Product variant");
    }


    @Test
    @DisplayName("Throws InsufficientStock exception when request quantity exceeds available")
    void placeOrderDto_requestedQuantityExceedsAvailable() throws BadRequestException{
        PlaceOrderDto orderDto = OrderTestFixtures.getMockOrderRequest();
        User user = OrderTestFixtures.getMockUser();

        ProductVariant productVariant1 = OrderTestFixtures.mockVariant(1,"NWHT20","60000");
        when(orderRepository.existsByIdempotentKey(orderDto.key())).thenReturn(false);
        when(userRepository.findById("userId")).thenReturn(Optional.of(user));
        when(productVariantRepository.findByIdForUpdate(orderDto.orderItems().getFirst().productId()))
                .thenReturn(Optional.of(productVariant1));
        when(orderItemRepository.sumReservedQuantityByVariant(eq(productVariant1.getId()),any(LocalDateTime.class)))
                .thenReturn(Optional.of(1));

        assertThatThrownBy(()-> orderService.placeOrder(orderDto))
                .isInstanceOf(InsufficientStockException.class)
                .hasMessageContaining("Requested: 2")
                .hasMessageContaining("Availability: 0");
    }

    @Test
    @DisplayName("Saves order with correct total and items on valid request")
    void placeOrder_successfully() throws BadRequestException{
        PlaceOrderDto orderDto = OrderTestFixtures.getMockOrderRequest();
        User user = OrderTestFixtures.getMockUser();

        ProductVariant productVariant1 = OrderTestFixtures.mockVariant(2,"NWHT20","60000");
        when(orderRepository.existsByIdempotentKey(orderDto.key())).thenReturn(false);
        when(userRepository.findById("userId")).thenReturn(Optional.of(user));
        when(productVariantRepository.findByIdForUpdate(orderDto.orderItems().getFirst().productId()))
                .thenReturn(Optional.of(productVariant1));
        when(orderItemRepository.sumReservedQuantityByVariant(eq(productVariant1.getId()),any(LocalDateTime.class)))
                .thenReturn(Optional.empty());

        assertTrue(orderService.placeOrder(orderDto).isStatus());
        verify(orderRepository).save(argThat(order->
                order.getIdempotentKey().equals(orderDto.key())
                        && order.getUser().equals(user)
                        && order.getStatus().equals(OrderStatus.PENDING)
                        && order.getTotalAmount().compareTo(new BigDecimal("120000"))==0
                        && order.getOrderItems().size()==1

        ));
    }

}
