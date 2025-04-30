package com.kat.bookstore.mapper.cartitem;

import com.kat.bookstore.config.MapperConfig;
import com.kat.bookstore.dto.cartitem.CartItemDto;
import com.kat.bookstore.dto.cartitem.CreateCartItemRequestDto;
import com.kat.bookstore.entity.cartitem.CartItem;
import com.kat.bookstore.mapper.book.BookMapper;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class, uses = BookMapper.class)
public interface CartItemMapper {

    CartItem toEntity(CreateCartItemRequestDto createCartItemRequestDto);

    CartItemDto toDto(CartItem cartItem);

}
