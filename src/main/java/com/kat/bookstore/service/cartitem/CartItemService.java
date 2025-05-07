package com.kat.bookstore.service.cartitem;

import com.kat.bookstore.dto.cartitem.CartItemDto;
import com.kat.bookstore.dto.cartitem.CartItemQuantityRequestDto;
import com.kat.bookstore.dto.cartitem.CreateCartItemRequestDto;
import com.kat.bookstore.entity.shoppingcart.ShoppingCart;

public interface CartItemService {
    CartItemDto save(CreateCartItemRequestDto createCartItemRequestDto, ShoppingCart shopCart);

    CartItemDto updateQuantity(Long userId, Long cartItemId,
                               CartItemQuantityRequestDto cartItemQuantityRequestDto);

    void deleteById(Long id, Long userId);
}
