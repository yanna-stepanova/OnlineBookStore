package com.yanna.stepanova.mapper;

import com.yanna.stepanova.config.MapperConfig;
import com.yanna.stepanova.dto.cartitem.CartItemDto;
import com.yanna.stepanova.dto.cartitem.CartItemQuantityRequestDto;
import com.yanna.stepanova.dto.cartitem.CreateCartItemRequestDto;
import com.yanna.stepanova.model.CartItem;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class, uses = BookMapper.class)
public interface CartItemMapper {
    @Mapping(target = "book", source = "bookId", qualifiedByName = "bookFromId")
    CartItem toModel(CreateCartItemRequestDto requestDto);

    CartItemDto toDto(CartItem cartItem);

    @AfterMapping
    default void setBook(@MappingTarget CartItemDto cartItemDto, CartItem cartItem) {
        cartItemDto.setBookId(cartItem.getBook().getId());
        cartItemDto.setBookTitle(cartItem.getBook().getTitle());
    }

    CartItem updateQuantityFromDto(@MappingTarget CartItem cartItem,
                                   CartItemQuantityRequestDto requestDto);
}
