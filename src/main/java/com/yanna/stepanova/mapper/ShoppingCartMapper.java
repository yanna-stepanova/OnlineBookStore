package com.yanna.stepanova.mapper;

import com.yanna.stepanova.config.MapperConfig;
import com.yanna.stepanova.dto.shoppingcart.ShoppingCartDto;
import com.yanna.stepanova.model.ShoppingCart;
import com.yanna.stepanova.model.User;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface ShoppingCartMapper {
    ShoppingCart mapUserToShopCart(User user);

    @AfterMapping
    default void setUser(@MappingTarget ShoppingCart shopCart,
                               User user) {
        shopCart.setUser(user);
    }

    @Mapping(target = "cartItems", ignore = true)
    @Mapping(source = "user.id", target = "userId")
    ShoppingCartDto toDto(ShoppingCart shopCart);

   /* @AfterMapping
    default void setUserId(@MappingTarget ShoppingCartDto shopCartDto, ShoppingCart shopCart) {
        shopCartDto.setUserId(shopCart.getUser().getId());
    }*/
}
