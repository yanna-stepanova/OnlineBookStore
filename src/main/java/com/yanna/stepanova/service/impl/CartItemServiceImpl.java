package com.yanna.stepanova.service.impl;

import com.yanna.stepanova.dto.cartitem.CartItemDto;
import com.yanna.stepanova.dto.cartitem.CartItemQuantityRequestDto;
import com.yanna.stepanova.dto.cartitem.CreateCartItemRequestDto;
import com.yanna.stepanova.mapper.CartItemMapper;
import com.yanna.stepanova.model.Book;
import com.yanna.stepanova.model.CartItem;
import com.yanna.stepanova.model.ShoppingCart;
import com.yanna.stepanova.repository.book.BookRepository;
import com.yanna.stepanova.repository.shoppingcart.CartItemRepository;
import com.yanna.stepanova.service.CartItemService;
import com.yanna.stepanova.service.ShoppingCartService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Transactional
@RequiredArgsConstructor
@Service
public class CartItemServiceImpl implements CartItemService {
    private final CartItemRepository cartItemRepo;
    private final CartItemMapper cartItemMapper;
    private final BookRepository bookRepo;
    private final ShoppingCartService shoppingCartService;

    @Override
    public CartItemDto save(CreateCartItemRequestDto requestDto, ShoppingCart shopCart) {
        CartItem cartItem = cartItemMapper.toModel(requestDto);
        Book bookFromDB = bookRepo.findById(cartItem.getBook().getId()).orElseThrow(() ->
                new EntityNotFoundException("Can't find book by id=" + requestDto.bookId()));
        cartItem.setBook(bookFromDB);
        cartItem.setShopcart(shopCart);
        CartItem savedCartItem = cartItemRepo.save(cartItem);
        shopCart.setCartItems(cartItemRepo.findAllByShopCartId(shopCart.getId()));
        return cartItemMapper.toDto(savedCartItem);
    }

    @Override
    public CartItemDto updateQuantity(Long userId, Long cartItemId,
                                      CartItemQuantityRequestDto requestDto) {
        CartItem cartItemFromDB = getCartItemByIdAndUserId(cartItemId, userId);
        cartItemFromDB.setQuantity(requestDto.quantity());
        return cartItemMapper.toDto(cartItemRepo.save(cartItemFromDB));
    }

    @Override
    public void deleteById(Long cartItemId, Long userId) {
        cartItemRepo.delete(getCartItemByIdAndUserId(cartItemId, userId));
    }

    private CartItem getCartItemByIdAndUserId(Long cartItemId, Long userId) {
        return cartItemRepo.findByIdAndShoppingCartId(
                cartItemId, shoppingCartService.getShopCart(userId).getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Can't find cartItem by id = %s for this user", cartItemId)));
    }
}
