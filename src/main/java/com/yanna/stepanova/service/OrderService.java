package com.yanna.stepanova.service;

import com.yanna.stepanova.dto.order.CreateOrderRequestDto;
import com.yanna.stepanova.dto.order.OrderDto;
import com.yanna.stepanova.dto.order.OrderDtoWithoutOrderItems;
import com.yanna.stepanova.dto.order.OrderStatusRequestDto;
import com.yanna.stepanova.model.User;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderDto save(CreateOrderRequestDto requestDto, User userId);

    List<OrderDtoWithoutOrderItems> getAllByUserId(Long userId, Pageable pageable);

    OrderDtoWithoutOrderItems updateStatusById(Long id, OrderStatusRequestDto requestDto);
}
