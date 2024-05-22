package com.yanna.stepanova.service;

import com.yanna.stepanova.dto.shoppingcart.ShoppingCartDto;
import com.yanna.stepanova.model.ShoppingCart;
import com.yanna.stepanova.model.User;

public interface ShoppingCartService {
    ShoppingCart getShopCart(User user);

    ShoppingCartDto getShopCartDto(User user);
}
