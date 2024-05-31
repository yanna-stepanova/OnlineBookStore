package com.yanna.stepanova.controller;

import com.yanna.stepanova.dto.order.CreateOrderRequestDto;
import com.yanna.stepanova.dto.order.OrderDto;
import com.yanna.stepanova.dto.order.OrderDtoWithoutOrderItems;
import com.yanna.stepanova.dto.order.OrderStatusRequestDto;
import com.yanna.stepanova.dto.orderitem.OrderItemDto;
import com.yanna.stepanova.model.User;
import com.yanna.stepanova.service.OrderItemService;
import com.yanna.stepanova.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order manager", description = "Endpoints to manage user's orders")
@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
@Validated
public class OrderController {
    private final OrderService orderService;
    private final OrderItemService orderItemService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping
    @Operation(summary = "Make an order",
            description = "Place an order to buy the books in the cart")
    public OrderDto createOrder(@RequestBody @Valid CreateOrderRequestDto requestDto,
                                Authentication authentication) {
        return orderService.save(requestDto, getAuthenticatedUser(authentication));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping
    @Operation(summary = "View user's order history",
            description = "Get order history to track past purchases")
    public List<OrderDtoWithoutOrderItems> getAllOrders(
            @ParameterObject @PageableDefault Pageable pageable,
            Authentication authentication) {
        return orderService.getAllByUserId(getAuthenticatedUser(authentication).getId(), pageable);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/{id}/items")
    @Operation(summary = "View items in a certain order",
            description = "Get all items for a certain order")
    public List<OrderItemDto> getOrderItemsByOrderId(
            @PathVariable @Positive Long id,
            @ParameterObject @PageableDefault Pageable pageable,
            Authentication authentication) {
        return orderItemService.getAllByOrderId(id, pageable,
                getAuthenticatedUser(authentication).getId());
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/{orderId}/items/{orderItemId}")
    @Operation(summary = "View a specific item in a certain order",
            description = "Get item by id for a certain order")
    public OrderItemDto getOrderItemByIdAndOrderId(
            @PathVariable @Positive Long orderId,
            @PathVariable @Positive Long orderItemId,
            Authentication authentication) {
        return orderItemService.getByIdAndOrderId(orderItemId, orderId,
                getAuthenticatedUser(authentication).getId());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/{id}")
    @Operation(summary = "Update a certain order status",
            description = "Managing the order processing workflow")
    public OrderDtoWithoutOrderItems updateOrderStatus(
            @PathVariable @Positive Long id,
            @RequestBody @Valid OrderStatusRequestDto requestDto) {
        return orderService.updateStatusById(id, requestDto);
    }

    private User getAuthenticatedUser(Authentication authentication) {
        return (User) authentication.getPrincipal();
    }
}
