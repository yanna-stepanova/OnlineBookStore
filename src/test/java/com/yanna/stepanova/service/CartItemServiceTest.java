package com.yanna.stepanova.service;

import com.yanna.stepanova.dto.cartitem.CartItemDto;
import com.yanna.stepanova.dto.cartitem.CartItemQuantityRequestDto;
import com.yanna.stepanova.dto.cartitem.CreateCartItemRequestDto;
import com.yanna.stepanova.mapper.CartItemMapper;
import com.yanna.stepanova.model.Book;
import com.yanna.stepanova.model.CartItem;
import com.yanna.stepanova.model.ShoppingCart;
import com.yanna.stepanova.repository.book.BookRepository;
import com.yanna.stepanova.repository.shoppingcart.CartItemRepository;
import com.yanna.stepanova.service.impl.CartItemServiceImpl;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CartItemServiceTest {
    @Mock
    private CartItemRepository cartItemRepo;
    @Mock
    private BookRepository bookRepo;
    @Mock
    private CartItemMapper cartItemMapper;
    @Mock
    private ShoppingCartService shoppingCartService;
    @InjectMocks
    private CartItemServiceImpl cartItemService;

    @Test
    @DisplayName("""
            Get correct CartItemDto for valid requestDto""")
    void save_WithValidRequestDto_ReturnCartItemDto() {
        //given
        Long bookId = 1L;
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(1L);
        Book book = new Book(bookId);
        book.setTitle("My book");
        book.setAuthor("My author");
        book.setIsbn("000-00-00000007");

        CreateCartItemRequestDto requestDto = new CreateCartItemRequestDto(bookId, 5);

        CartItem cartItem = new CartItem();
        cartItem.setBook(book);
        cartItem.setShopcart(shoppingCart);
        cartItem.setQuantity(requestDto.quantity());

        CartItem savedCartItem = new CartItem();
        savedCartItem.setId(7L);
        savedCartItem.setBook(cartItem.getBook());
        savedCartItem.setShopcart(cartItem.getShopcart());
        savedCartItem.setQuantity(cartItem.getQuantity());

        CartItemDto expected = new CartItemDto(
                savedCartItem.getId(),
                savedCartItem.getBook().getId(),
                savedCartItem.getBook().getTitle(),
                savedCartItem.getQuantity());

        Mockito.when(cartItemMapper.toModel(requestDto)).thenReturn(cartItem);
        Mockito.when(bookRepo.findById(requestDto.bookId())).thenReturn(Optional.of(book));
        Mockito.when(cartItemRepo.save(cartItem)).thenReturn(savedCartItem);
        Mockito.when(cartItemRepo.findAllByShopCartId(shoppingCart.getId()))
                .thenReturn(Set.of(savedCartItem));
        Mockito.when(cartItemMapper.toDto(savedCartItem)).thenReturn(expected);
        //when
        CartItemDto actual = cartItemService.save(requestDto, shoppingCart);
        //then
        Assertions.assertTrue(EqualsBuilder.reflectionEquals(expected, actual));
    }

    @Test
    @DisplayName("""
            Get updated CartItemDto by valid id and requestDto""")
    void updateQuantity_WithValidIdAndRequestDto_ReturnCartItemDto() {
        //given
        Long userId = 8L;
        Long cartItemId = 3L;

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(userId);

        CartItem cartItem = new CartItem();
        cartItem.setId(cartItemId);
        cartItem.setBook(new Book(3L));
        cartItem.setShopcart(shoppingCart);
        cartItem.setQuantity(100);

        CartItemQuantityRequestDto requestDto = new CartItemQuantityRequestDto(10);

        CartItem updatedCartItem = new CartItem();
        updatedCartItem.setId(cartItem.getId());
        updatedCartItem.setQuantity(requestDto.quantity());
        updatedCartItem.setShopcart(cartItem.getShopcart());
        updatedCartItem.setBook(cartItem.getBook());

        CartItemDto expected = new CartItemDto(
                updatedCartItem.getId(),
                updatedCartItem.getBook().getId(),
                updatedCartItem.getBook().getTitle(),
                updatedCartItem.getQuantity());

        Mockito.when(shoppingCartService.getShopCart(userId)).thenReturn(shoppingCart);
        Mockito.when(cartItemRepo.findByIdAndShoppingCartId(cartItemId, userId))
                .thenReturn(Optional.of(cartItem));
        Mockito.when(cartItemRepo.save(cartItem)).thenReturn(updatedCartItem);
        Mockito.when(cartItemMapper.toDto(updatedCartItem)).thenReturn(expected);
        //when
        CartItemDto actual = cartItemService.updateQuantity(userId, cartItemId, requestDto);
        //then
        Assertions.assertTrue(EqualsBuilder.reflectionEquals(expected, actual));
    }
}
