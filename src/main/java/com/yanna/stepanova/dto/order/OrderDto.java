package com.yanna.stepanova.dto.order;

import com.yanna.stepanova.dto.orderitem.OrderItemDto;
import com.yanna.stepanova.model.Status;
import java.math.BigDecimal;
import java.util.Set;

public record OrderDto(Long id,
                       Long userId,
                       Set<OrderItemDto> orderItems,
                       String orderDate,
                       BigDecimal total,
                       Status status) {
}
