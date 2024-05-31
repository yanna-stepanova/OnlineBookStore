package com.yanna.stepanova.mapper;

import com.yanna.stepanova.config.MapperConfig;
import com.yanna.stepanova.dto.orderitem.OrderItemDto;
import com.yanna.stepanova.model.CartItem;
import com.yanna.stepanova.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class,
        uses = {CartItemMapper.class, BookMapper.class})
public interface OrderItemMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "book", target = "book")
    @Mapping(source = "book.price", target = "price")
    OrderItem mapCartItemToOrderItem(CartItem cartItem);

    @Mapping(source = "book.id", target = "bookId")
    @Mapping(source = "book.title", target = "bookTitle")
    OrderItemDto toDto(OrderItem orderItem);
}
