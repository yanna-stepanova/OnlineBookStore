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
import com.yanna.stepanova.repository.shoppingcart.ShoppingCartRepository;
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
    private final ShoppingCartRepository shopCartRepo;

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
        CartItem cartItemFromDB = cartItemRepo.findByIdAndShoppingCartId(cartItemId, user.getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Can't find cartItem by id = %s for this user", cartItemId)));
        cartItemFromDB.setQuantity(requestDto.quantity());
        return cartItemMapper.toDto(cartItemRepo.save(cartItemFromDB));
    }

    @Override
    @Transactional
    public void deleteById(Long cartItemId, User user) {
        ShoppingCart shopCart = shopCartRepo.findById(user.getId()).orElseThrow(() ->
                new EntityNotFoundException("Can't find shopping cart by id: " + user.getId()));
        CartItem cartItem = cartItemRepo.findByIdAndShoppingCartId(cartItemId, shopCart.getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Can't find cartItem by id = %s for this user", cartItemId)));
        cartItemRepo.deleteById(cartItemId);
    }
}
