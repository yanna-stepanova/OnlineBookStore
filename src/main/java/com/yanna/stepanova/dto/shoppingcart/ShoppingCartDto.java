package com.yanna.stepanova.dto.shoppingcart;

import com.yanna.stepanova.dto.cartitem.CartItemDto;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShoppingCartDto {
    private Long userId;
    private Set<CartItemDto> cartItems;
}
