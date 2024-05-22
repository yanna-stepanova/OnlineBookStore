package com.yanna.stepanova.service;

import com.yanna.stepanova.dto.cartitem.CartItemDto;
import com.yanna.stepanova.dto.cartitem.CartItemQuantityRequestDto;
import com.yanna.stepanova.dto.cartitem.CreateCartItemRequestDto;
import com.yanna.stepanova.model.ShoppingCart;
import com.yanna.stepanova.model.User;

public interface CartItemService {
    CartItemDto save(CreateCartItemRequestDto requestDto, ShoppingCart shopCart);

    CartItemDto updateQuantity(User authenticatedUser, Long cartItemId,
                               CartItemQuantityRequestDto requestDto);

    boolean deleteById(Long id, User user);
}
