package com.yanna.stepanova.service.impl;

import com.yanna.stepanova.dto.shoppingcart.ShoppingCartDto;
import com.yanna.stepanova.mapper.ShoppingCartMapper;
import com.yanna.stepanova.model.ShoppingCart;
import com.yanna.stepanova.model.User;
import com.yanna.stepanova.repository.shoppingcart.ShoppingCartRepository;
import com.yanna.stepanova.service.ShoppingCartService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shopCartRepo;
    private final ShoppingCartMapper shopCartMapper;

    @Override
    public ShoppingCart getShopCart(User user) {
        return shopCartRepo.findById(user.getId()).orElseThrow(() ->
                new EntityNotFoundException("Can't find shopping cart by id:" + user.getId()));
    }

    @Override
    @Transactional
    public ShoppingCartDto getShopCartDto(User user) {
        return shopCartMapper.toDto(getShopCart(user));
    }

    @Override
    public void createShoppingCart(User user) {
        ShoppingCart shopCart = shopCartMapper.mapUserToShopCart(user);
        shopCartRepo.save(shopCart);
    }
}
