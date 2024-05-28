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
    public ShoppingCart getShopCart(Long userId) {
        return shopCartRepo.findById(userId).orElseThrow(() ->
                new EntityNotFoundException("Can't find shopping cart by id:" + userId));
    }

    @Override
    @Transactional
    public ShoppingCartDto getShopCartDto(Long userId) {
        return shopCartMapper.toDto(getShopCart(userId));
    }

    @Override
    public void createShoppingCart(User user) {
        shopCartRepo.save(shopCartMapper.mapUserToShopCart(user));
    }
}
