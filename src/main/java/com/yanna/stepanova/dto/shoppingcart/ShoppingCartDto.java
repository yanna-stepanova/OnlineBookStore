package com.yanna.stepanova.dto.shoppingcart;

import com.yanna.stepanova.dto.cartitem.CartItemDto;
import java.util.Set;

public record ShoppingCartDto(Long userId,
                              Set<CartItemDto> cartItems) {}
