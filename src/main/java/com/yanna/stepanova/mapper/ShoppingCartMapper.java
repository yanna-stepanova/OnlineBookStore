package com.yanna.stepanova.mapper;

import com.yanna.stepanova.config.MapperConfig;
import com.yanna.stepanova.dto.shoppingcart.ShoppingCartDto;
import com.yanna.stepanova.model.ShoppingCart;
import com.yanna.stepanova.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = CartItemMapper.class)
public interface ShoppingCartMapper {
    @Mapping(source = "user", target = "user")
    ShoppingCart mapUserToShopCart(User user);

    @Mapping(source = "user.id", target = "userId")
    ShoppingCartDto toDto(ShoppingCart shopCart);
}
