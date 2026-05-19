package com.damoladev.pulsecommerce.repository;

import com.damoladev.pulsecommerce.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order,String> {
    boolean existsByIdempotentKey(String key);

    @Query(
            """
                SELECT o FROM Order o
                WHERE o.reservationExpiresAt < :now 
                AND o.status = 'PENDING'
            """
    )
    List<Order> findExpiredOrders(@Param("now") LocalDateTime now);

}
