package com.yanna.stepanova.service;

import com.yanna.stepanova.dto.orderitem.OrderItemDto;
import com.yanna.stepanova.model.CartItem;
import com.yanna.stepanova.model.Order;
import com.yanna.stepanova.model.OrderItem;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface OrderItemService {
    OrderItem save(CartItem cartItem, Order order);

    List<OrderItemDto> getAllByOrderId(Long orderId, Pageable pageable, Long userId);

    OrderItemDto getByIdAndOrderId(Long orderItemId, Long orderId, Long id);
}
