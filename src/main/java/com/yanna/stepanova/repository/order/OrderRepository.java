package com.yanna.stepanova.repository.order;

import com.yanna.stepanova.model.Order;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query(value = "SELECT * FROM orders o WHERE o.user_id = :userId", nativeQuery = true)
    List<Order> findAllByUserId(@Param("userId") Long userId, Pageable pageable);
}
