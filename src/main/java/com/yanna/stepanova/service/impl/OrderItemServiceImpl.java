package com.yanna.stepanova.service.impl;

import com.yanna.stepanova.dto.orderitem.OrderItemDto;
import com.yanna.stepanova.mapper.OrderItemMapper;
import com.yanna.stepanova.model.CartItem;
import com.yanna.stepanova.model.Order;
import com.yanna.stepanova.model.OrderItem;
import com.yanna.stepanova.repository.order.OrderItemRepository;
import com.yanna.stepanova.service.OrderItemService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Transactional
@RequiredArgsConstructor
@Service
public class OrderItemServiceImpl implements OrderItemService {
    private final OrderItemRepository orderItemRepo;
    private final OrderItemMapper orderItemMapper;

    @Override
    public OrderItem save(CartItem cartItem, Order order) {
        OrderItem orderItem = orderItemMapper.mapCartItemToOrderItem(cartItem);
        orderItem.setOrder(order);
        OrderItem savedOrderItem = orderItemRepo.save(orderItem);
        return orderItemRepo.save(savedOrderItem);
    }

    @Override
    public List<OrderItemDto> getAllByOrderId(Long orderId, Pageable pageable, Long userId) {
        return orderItemRepo.findAllByOrderIdAndUserId(userId, orderId, pageable).stream()
                .map(orderItemMapper::toDto)
                .toList();
    }

    @Override
    public OrderItemDto getByIdAndOrderId(Long orderItemId, Long orderId, Long userId) {
        return orderItemRepo.findByIdAndOrderIdAndUserId(orderItemId, orderId, userId)
                .map(orderItemMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Order item with id=%s and order_id=%s wasn't found "
                                        + "for this user", orderItemId, orderId)));
    }
}
