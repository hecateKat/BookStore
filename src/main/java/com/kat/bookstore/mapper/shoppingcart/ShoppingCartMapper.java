package com.kat.bookstore.mapper.shoppingcart;

import com.kat.bookstore.config.MapperConfig;
import com.kat.bookstore.dto.shoppingcart.ShoppingCartDto;
import com.kat.bookstore.entity.shoppingcart.ShoppingCart;
import com.kat.bookstore.entity.user.User;
import com.kat.bookstore.mapper.cartitem.CartItemMapper;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class, uses = CartItemMapper.class)
public interface ShoppingCartMapper {

    ShoppingCart mapUserToShopCart(User user);

    ShoppingCartDto toDto(ShoppingCart shopCart);
}
