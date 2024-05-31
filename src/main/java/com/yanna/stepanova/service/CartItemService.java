package com.yanna.stepanova.service;

import com.yanna.stepanova.dto.cartitem.CartItemDto;
import com.yanna.stepanova.dto.cartitem.CartItemQuantityRequestDto;
import com.yanna.stepanova.dto.cartitem.CreateCartItemRequestDto;
import com.yanna.stepanova.model.ShoppingCart;

public interface CartItemService {
    CartItemDto save(CreateCartItemRequestDto requestDto, ShoppingCart shopCart);

    CartItemDto updateQuantity(Long userId, Long cartItemId,
                               CartItemQuantityRequestDto requestDto);

    void deleteById(Long id, Long userId);
}
