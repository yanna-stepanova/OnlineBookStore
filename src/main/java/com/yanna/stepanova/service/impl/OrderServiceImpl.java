package com.yanna.stepanova.service.impl;

import com.yanna.stepanova.dto.order.CreateOrderRequestDto;
import com.yanna.stepanova.dto.order.OrderDto;
import com.yanna.stepanova.dto.order.OrderDtoWithoutOrderItems;
import com.yanna.stepanova.dto.order.OrderStatusRequestDto;
import com.yanna.stepanova.mapper.OrderMapper;
import com.yanna.stepanova.model.CartItem;
import com.yanna.stepanova.model.Order;
import com.yanna.stepanova.model.OrderItem;
import com.yanna.stepanova.model.Status;
import com.yanna.stepanova.model.User;
import com.yanna.stepanova.repository.order.OrderRepository;
import com.yanna.stepanova.service.CartItemService;
import com.yanna.stepanova.service.OrderItemService;
import com.yanna.stepanova.service.OrderService;
import com.yanna.stepanova.service.ShoppingCartService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Transactional
@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepo;
    private final OrderMapper orderMapper;
    private final CartItemService cartItemService;
    private final OrderItemService orderItemService;
    private final ShoppingCartService shopCartService;

    @Override
    public OrderDto save(CreateOrderRequestDto requestDto, User user) {
        Order order = orderMapper.toModel(requestDto);
        order.setUser(user);
        order.setStatus(Status.PENDING);
        order.setOrderDate(LocalDateTime.now());
        order.setTotal(getTotalAmount(user.getId()));
        Order savedOrder = orderRepo.save(order);
        Set<CartItem> cartItemsFromDB = shopCartService.getShopCart(user.getId()).getCartItems();
        savedOrder.setOrderItems(createOrderItems(cartItemsFromDB, savedOrder));
        Order updatedOrder = orderRepo.save(order);
        clearAllFromShoppingCart(cartItemsFromDB, user.getId());
        return orderMapper.toDto(updatedOrder);
    }

    @Override
    public List<OrderDtoWithoutOrderItems> getAllByUserId(Long userId, Pageable pageable) {
        return orderRepo.findAllByUserId(userId, pageable).stream()
                .map(orderMapper::toDtoWithoutOrderItems)
                .toList();
    }

    @Override
    public OrderDtoWithoutOrderItems updateStatusById(Long id, OrderStatusRequestDto requestDto) {
        Order oldOrder = orderRepo.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Can't get order by id = " + id));
        return orderMapper.toDtoWithoutOrderItems(
                orderMapper.updateStatusFromDto(oldOrder, requestDto));
    }

    private Set<OrderItem> createOrderItems(Set<CartItem> cartItemSet, Order order) {
        Set<OrderItem> orderItemSet = new HashSet<>();
        for (CartItem cartItem: cartItemSet) {
            orderItemSet.add(orderItemService.save(cartItem, order));
        }
        return orderItemSet;
    }

    private void clearAllFromShoppingCart(Set<CartItem> cartItemSet, Long userId) {
        for (CartItem cartItem: cartItemSet) {
            cartItemService.deleteById(cartItem.getId(), userId);
        }
    }

    private BigDecimal getTotalAmount(Long userId) {
        return shopCartService.getShopCart(userId).getCartItems().stream()
                .map(item -> item.getBook().getPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal::add).get();
    }
}
