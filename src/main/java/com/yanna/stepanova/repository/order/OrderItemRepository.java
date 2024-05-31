package com.yanna.stepanova.repository.order;

import com.yanna.stepanova.model.OrderItem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    @Query(value = "SELECT oi.id, oi.order_id, oi.book_id, oi.quantity, oi.price, oi.is_deleted "
            + "FROM order_items oi INNER JOIN orders o ON oi.order_id = o.id "
            + "WHERE oi.order_id = :orderId AND o.user_id = :userId",
            nativeQuery = true)
    List<OrderItem> findAllByOrderIdAndUserId(@Param("userId") Long userId,
                                              @Param("orderId") Long orderId, Pageable pageable);

    @Query(value = "SELECT oi.id, oi.order_id, oi.book_id, oi.quantity, oi.price, oi.is_deleted "
            + "FROM order_items oi INNER JOIN orders o ON oi.order_id = o.id "
            + "WHERE oi.id = :orderItemId AND oi.order_id = :orderId AND o.user_id = :userId",
            nativeQuery = true)
    Optional<OrderItem> findByIdAndOrderIdAndUserId(@Param("orderItemId") Long orderItemId,
                                                    @Param("orderId") Long orderId,
                                                    @Param("userId")Long userId);
}
