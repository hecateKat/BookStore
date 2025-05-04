package com.kat.bookstore.dto.shoppingcart;

import com.kat.bookstore.dto.cartitem.CartItemDto;
import java.util.Set;

public record ShoppingCartDto(Long userId,
                              Set<CartItemDto> cartItems) {}
