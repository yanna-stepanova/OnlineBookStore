package com.yanna.stepanova.service.impl;

import com.yanna.stepanova.dto.cartitem.CartItemDto;
import com.yanna.stepanova.dto.shoppingcart.ShoppingCartDto;
import com.yanna.stepanova.mapper.CartItemMapper;
import com.yanna.stepanova.mapper.ShoppingCartMapper;
import com.yanna.stepanova.model.ShoppingCart;
import com.yanna.stepanova.model.User;
import com.yanna.stepanova.repository.shoppingcart.CartItemRepository;
import com.yanna.stepanova.repository.shoppingcart.ShoppingCartRepository;
import com.yanna.stepanova.service.ShoppingCartService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shopCartRepo;
    private final ShoppingCartMapper shopCartMapper;
    private final CartItemRepository cartItemRepo;
    private final CartItemMapper cartItemMapper;

    @Override
    public ShoppingCart getShopCart(User user) {
        return shopCartRepo.findById(user.getId()).orElseThrow(() ->
                new EntityNotFoundException("Can't find shopping cart by id:" + user.getId()));
    }

    @Override
    @Transactional
    public ShoppingCartDto getShopCartDto(User user) {
        ShoppingCartDto shopCartDto = shopCartMapper.toDto(getShopCart(user));
        Set<CartItemDto> collect = cartItemRepo.findAllByShopCartId(shopCartDto.getId()).stream()
                .map(cartItemMapper::toDto)
                .collect(Collectors.toSet());
        shopCartDto.setCartItems(collect);
        return shopCartDto;
    }
}
