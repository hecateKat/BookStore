package com.kat.bookstore.service.shoppingcart;

import com.kat.bookstore.dto.shoppingcart.ShoppingCartDto;
import com.kat.bookstore.entity.shoppingcart.ShoppingCart;

public interface ShoppingCartService {
    ShoppingCart getShoppingCart(Long userId);

    ShoppingCartDto getShoppingCartDto(Long userId);
}
