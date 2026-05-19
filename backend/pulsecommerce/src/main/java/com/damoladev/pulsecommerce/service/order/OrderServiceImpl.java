package com.damoladev.pulsecommerce.service.order;

import com.damoladev.pulsecommerce.dto.ApiResponseDto;
import com.damoladev.pulsecommerce.dto.OrderItemDto;
import com.damoladev.pulsecommerce.dto.PlaceOrderDto;
import com.damoladev.pulsecommerce.enums.OrderStatus;
import com.damoladev.pulsecommerce.exception.InsufficientStockException;
import com.damoladev.pulsecommerce.exception.ResourceNotFoundException;
import com.damoladev.pulsecommerce.model.Order;
import com.damoladev.pulsecommerce.model.OrderItem;
import com.damoladev.pulsecommerce.model.ProductVariant;
import com.damoladev.pulsecommerce.model.User;
import com.damoladev.pulsecommerce.repository.OrderItemRepository;
import com.damoladev.pulsecommerce.repository.OrderRepository;
import com.damoladev.pulsecommerce.repository.ProductVariantRepository;
import com.damoladev.pulsecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{
    private final OrderRepository orderRepository;

    private final OrderItemRepository orderItemRepository;

    private final ProductVariantRepository productVariantRepository;

    private final UserRepository userRepository;

    @Override
    @Transactional
    public ApiResponseDto<Void> placeOrder(PlaceOrderDto dto) throws BadRequestException {
        if(dto.key()==null){
            throw new BadRequestException("An idempotency key is required to place an order");
        }

        if(orderRepository.existsByIdempotentKey(dto.key())){
            return new ApiResponseDto<>(true, "Order placed successfully");
        }

        User savedUser = userRepository.findById(dto.userId()).
                orElseThrow(()-> new ResourceNotFoundException("User","user id",dto.userId()));

        Map<Long,ProductVariant> variantMap = validateAvailability(dto.orderItems());

        Order order = new Order();
        order.setIdempotentKey(dto.key());
        order.setUser(savedUser);
        order.setStatus(OrderStatus.PENDING);

        List<OrderItem> items = dto.orderItems().stream().map(
                item->{
                    ProductVariant variant = variantMap.get(item.productId());
                    OrderItem orderItem = new OrderItem();
                    orderItem.setProduct(variant);
                    orderItem.setQuantity(item.quantity());
                    orderItem.setPriceAtPurchase(variant.getPrice());
                    orderItem.setOrder(order);

                    return orderItem;
                }
        ).toList();


        BigDecimal totalPrice = items.stream().map(
                i-> i.getPriceAtPurchase().multiply(BigDecimal.valueOf(i.getQuantity()))
        ).reduce(BigDecimal.ZERO,BigDecimal::add);

        order.setOrderItems(items);
        order.setTotalAmount(totalPrice);
        orderRepository.save(order);

        return new ApiResponseDto<>(true, "Order placed successfully");
    }

    private Map<Long, ProductVariant> validateAvailability(List<OrderItemDto> itemDtoList){
        Map<Long, ProductVariant> availableVariant = new HashMap<>();
        Map<Long,Integer> requestedQuantities = new HashMap<>();
        for(OrderItemDto item:itemDtoList){
            requestedQuantities.merge(item.productId(),item.quantity(),Integer::sum);
        }

        for(Map.Entry<Long,Integer> entry:requestedQuantities.entrySet()){
            Long productId = entry.getKey();
            Integer quantity = entry.getValue();
            ProductVariant variant = productVariantRepository.findByIdForUpdate(productId).orElseThrow(
                    ()-> new ResourceNotFoundException("Product variant","id",productId.toString())
            );

            int totalReserved = orderItemRepository.sumReservedQuantityByVariant(productId
                    , LocalDateTime.now()).orElse(0);
            int available = variant.getStockQuantity() - totalReserved;

            if(quantity>available){
                throw new InsufficientStockException(variant.getSku(),available,quantity);
            }

            availableVariant.put(productId,variant);
        }

        return availableVariant;
    }

    @Transactional
    public void expireStaleOrders(){
        List<Order> expiredOrders = orderRepository.findExpiredOrders(LocalDateTime.now());

        List<Order>  updatedOrders = expiredOrders.stream().peek(
                order-> order.setStatus(OrderStatus.CANCELLED)
        ).toList();

        orderRepository.saveAll(updatedOrders);
    }


    @Transactional
    public void  confirmOrder(String orderId){
        Order savedOrder = orderRepository.findById(orderId).orElseThrow(
                ()-> new ResourceNotFoundException("Order","order id",orderId)
        );

        List<OrderItem> items = savedOrder.getOrderItems();

        for(OrderItem item:items){
            ProductVariant variant = item.getProduct();
            variant.setStockQuantity(variant.getStockQuantity() - item.getQuantity());
            productVariantRepository.save(variant);
        }

        savedOrder.setStatus(OrderStatus.CONFIRMED);
        orderRepository.save(savedOrder);

    }


}


