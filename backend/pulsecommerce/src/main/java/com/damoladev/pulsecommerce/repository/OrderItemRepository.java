package com.damoladev.pulsecommerce.repository;

import com.damoladev.pulsecommerce.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface OrderItemRepository extends JpaRepository<OrderItem, String> {

    @Query(
            """
                SELECT COALESCE(SUM(oi.quantity),0) FROM OrderItem oi
                WHERE oi.product.id = :variantId
                AND oi.order.status = 'PENDING'
                AND oi.order.reservationExpiresAt > :now
            """
    )
    Optional<Integer> sumReservedQuantityByVariant(@Param("variantId") Long id, @Param("now")LocalDateTime now);
}
