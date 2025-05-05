package com.kat.bookstore.mapper.order;

import com.kat.bookstore.config.MapperConfig;
import com.kat.bookstore.dto.order.OrderItemDto;
import com.kat.bookstore.entity.cartitem.CartItem;
import com.kat.bookstore.entity.order.OrderItem;
import com.kat.bookstore.mapper.book.BookMapper;
import com.kat.bookstore.mapper.cartitem.CartItemMapper;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class,
        uses = {CartItemMapper.class, BookMapper.class})
public interface OrderItemMapper {

    OrderItem mapCartItemToOrderItem(CartItem cartItem);

    OrderItemDto toDto(OrderItem orderItem);
}
