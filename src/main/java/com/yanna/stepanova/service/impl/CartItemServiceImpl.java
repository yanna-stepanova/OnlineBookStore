package com.yanna.stepanova.service.impl;

import com.yanna.stepanova.dto.cartitem.CartItemDto;
import com.yanna.stepanova.dto.cartitem.CartItemQuantityRequestDto;
import com.yanna.stepanova.dto.cartitem.CreateCartItemRequestDto;
import com.yanna.stepanova.mapper.CartItemMapper;
import com.yanna.stepanova.model.Book;
import com.yanna.stepanova.model.CartItem;
import com.yanna.stepanova.model.ShoppingCart;
import com.yanna.stepanova.model.User;
import com.yanna.stepanova.repository.book.BookRepository;
import com.yanna.stepanova.repository.shoppingcart.CartItemRepository;
import com.yanna.stepanova.service.CartItemService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CartItemServiceImpl implements CartItemService {
    private final CartItemRepository cartItemRepo;
    private final CartItemMapper cartItemMapper;
    private final BookRepository bookRepo;

    @Override
    @Transactional
    public CartItemDto save(CreateCartItemRequestDto requestDto, ShoppingCart shopCart) {
        CartItem cartItem = cartItemMapper.toModel(requestDto);
        Book bookFromDB = bookRepo.findById(cartItem.getBook().getId()).orElseThrow(() ->
                new EntityNotFoundException("Can't find book by id=" + requestDto.bookId()));
        cartItem.setBook(bookFromDB);
        cartItem.setShopcart(shopCart);
        return cartItemMapper.toDto(cartItemRepo.save(cartItem));
    }

    @Override
    @Transactional
    public CartItemDto updateQuantity(User user, Long cartItemId,
                                      CartItemQuantityRequestDto requestDto) {
        CartItem cartItem = cartItemRepo.findById(cartItemId)
                .filter(item -> item.getShopcart().getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new EntityNotFoundException(
                        "This authenticated user isn't owner of cartItem by id = " + cartItemId));
        CartItem updatedCartItem = cartItemMapper.updateQuantityFromDto(cartItem, requestDto);
        return cartItemMapper.toDto(updatedCartItem);
    }

    @Override
    @Transactional
    public boolean deleteById(Long cartItemId, User user) {
        boolean isOwner = cartItemRepo.findById(cartItemId)
                .filter(item -> item.getShopcart().getUser().getId().equals(user.getId()))
                .isPresent();
        if (isOwner) {
            cartItemRepo.deleteById(cartItemId);
        }
        return isOwner;
    }
}