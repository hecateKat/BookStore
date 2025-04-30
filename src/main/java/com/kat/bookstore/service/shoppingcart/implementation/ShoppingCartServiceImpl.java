package com.kat.bookstore.service.shoppingcart.implementation;

import com.kat.bookstore.dto.shoppingcart.ShoppingCartDto;
import com.kat.bookstore.entity.shoppingcart.ShoppingCart;
import com.kat.bookstore.exception.EntityNotFoundException;
import com.kat.bookstore.mapper.shoppingcart.ShoppingCartMapper;
import com.kat.bookstore.repository.shoppingcart.ShoppingCartRepository;
import com.kat.bookstore.service.shoppingcart.ShoppingCartService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;

    @Override
    public ShoppingCart getShoppingCart(Long userId) {
        return shoppingCartRepository.findById(userId).orElseThrow(() ->
                new EntityNotFoundException("Can't find shopping cart by id:" + userId));
    }

    @Transactional
    @Override
    public ShoppingCartDto getShoppingCartDto(Long userId) {
        return shoppingCartMapper.toDto(getShoppingCart(userId));
    }
}
