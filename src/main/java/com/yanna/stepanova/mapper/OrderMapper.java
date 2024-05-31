package com.yanna.stepanova.mapper;

import com.yanna.stepanova.config.MapperConfig;
import com.yanna.stepanova.dto.order.CreateOrderRequestDto;
import com.yanna.stepanova.dto.order.OrderDto;
import com.yanna.stepanova.dto.order.OrderDtoWithoutOrderItems;
import com.yanna.stepanova.dto.order.OrderStatusRequestDto;
import com.yanna.stepanova.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class, uses = OrderItemMapper.class)
public interface OrderMapper {
    Order toModel(CreateOrderRequestDto requestDto);

    @Mapping(source = "user.id", target = "userId")
    OrderDtoWithoutOrderItems toDtoWithoutOrderItems(Order order);

    @Mapping(source = "user.id", target = "userId")
    OrderDto toDto(Order order);

    Order updateStatusFromDto(@MappingTarget Order order, OrderStatusRequestDto requestDto);
}
