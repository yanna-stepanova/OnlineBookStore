package com.yanna.stepanova.service;

import com.yanna.stepanova.dto.shoppingcart.ShoppingCartDto;
import com.yanna.stepanova.model.ShoppingCart;
import com.yanna.stepanova.model.User;

public interface ShoppingCartService {
    ShoppingCart getShopCart(Long userId);

    ShoppingCartDto getShopCartDto(Long userId);

    void createShoppingCart(User user);
}
