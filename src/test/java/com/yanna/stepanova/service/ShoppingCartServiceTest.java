package com.yanna.stepanova.service;

import com.yanna.stepanova.dto.cartitem.CartItemDto;
import com.yanna.stepanova.dto.shoppingcart.ShoppingCartDto;
import com.yanna.stepanova.mapper.ShoppingCartMapper;
import com.yanna.stepanova.model.ShoppingCart;
import com.yanna.stepanova.repository.shoppingcart.ShoppingCartRepository;
import com.yanna.stepanova.service.impl.ShoppingCartServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ShoppingCartServiceTest {
    @Mock
    private ShoppingCartRepository shoppingCartRepo;
    @Mock
    private ShoppingCartMapper shoppingCartMapper;
    @InjectMocks
    private ShoppingCartServiceImpl shoppingCartService;

    @Test
    @DisplayName("""
            Get shopping cart by user id""")
    public void getShopCart_WithValidId_ReturnShoppingCart() {
        //given
        Long userId = 5L;
        ShoppingCart expected = new ShoppingCart();
        expected.setId(userId);

        Mockito.when(shoppingCartRepo.findById(userId)).thenReturn(Optional.of(expected));
        //when
        ShoppingCart actual = shoppingCartService.getShopCart(userId);
        //then
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("""
            Get exception for non-existing user id""")
    public void getShopCart_NonExistingId_ReturnException() {
        // given
        Long userId = 2L;
        String expected = "Can't find shopping cart by id:" + userId;
        Mockito.when(shoppingCartRepo.findById(userId)).thenReturn(Optional.empty());
        // when
        try {
            ShoppingCart result = shoppingCartService.getShopCart(userId);
            Assertions.assertNull(result);
        } catch (EntityNotFoundException exception) {
            // then
            Assertions.assertEquals(expected, exception.getMessage());
        }
    }

    @Test
    @DisplayName("""
            Get correct ShoppingCartDto by user id""")
    void getShopCartDto_WithValidId_ReturnShoppingCartDto() {
        //given
        Long userId = 3L;
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(userId);

        ShoppingCartDto expected = new ShoppingCartDto(userId,
                Set.of(new CartItemDto(1L, 1L, "New book",15),
                       new CartItemDto(2L, 20L, "Antiquarian book", 1)));
        Mockito.when(shoppingCartRepo.findById(userId)).thenReturn(Optional.of(shoppingCart));
        Mockito.when(shoppingCartMapper.toDto(shoppingCart)).thenReturn(expected);
        //when
        ShoppingCartDto actual = shoppingCartService.getShopCartDto(userId);
        //then
        Assertions.assertEquals(expected,actual);
    }
}
