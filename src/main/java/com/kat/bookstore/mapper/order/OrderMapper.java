package com.kat.bookstore.mapper.order;

import com.kat.bookstore.config.MapperConfig;
import com.kat.bookstore.dto.order.CreateOrderRequestDto;
import com.kat.bookstore.dto.order.OrderDto;
import com.kat.bookstore.dto.order.OrderDtoWithoutOrderItems;
import com.kat.bookstore.dto.order.OrderStatusRequestDto;
import com.kat.bookstore.entity.order.Order;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class, uses = OrderItemMapper.class)
public interface OrderMapper {
    Order toEntity(CreateOrderRequestDto requestDto);

    OrderDtoWithoutOrderItems toDtoWithoutOrderItems(Order order);

    OrderDto toDto(Order order);

    Order updateStatusFromDto(@MappingTarget Order order, OrderStatusRequestDto requestDto);
}
