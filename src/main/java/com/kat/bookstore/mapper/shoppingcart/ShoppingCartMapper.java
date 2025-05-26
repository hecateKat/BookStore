package com.kat.bookstore.mapper.shoppingcart;

import com.kat.bookstore.config.MapperConfig;
import com.kat.bookstore.dto.shoppingcart.ShoppingCartDto;
import com.kat.bookstore.entity.shoppingcart.ShoppingCart;
import com.kat.bookstore.entity.user.User;
import com.kat.bookstore.mapper.cartitem.CartItemMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = CartItemMapper.class)
public interface ShoppingCartMapper {

    @Mapping(source = "user", target = "user")
    ShoppingCart mapUserToShopCart(User user);

    @Mapping(source = "user.id", target = "userId")
    ShoppingCartDto toDto(ShoppingCart shopCart);
}
