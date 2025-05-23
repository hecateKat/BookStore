package com.kat.bookstore.service.cartitem.implementation;

import com.kat.bookstore.dto.cartitem.CartItemDto;
import com.kat.bookstore.dto.cartitem.CartItemQuantityRequestDto;
import com.kat.bookstore.dto.cartitem.CreateCartItemRequestDto;
import com.kat.bookstore.entity.book.Book;
import com.kat.bookstore.entity.cartitem.CartItem;
import com.kat.bookstore.entity.shoppingcart.ShoppingCart;
import com.kat.bookstore.exception.EntityNotFoundException;
import com.kat.bookstore.mapper.cartitem.CartItemMapper;
import com.kat.bookstore.repository.book.BookRepository;
import com.kat.bookstore.repository.cartitem.CartItemRepository;
import com.kat.bookstore.service.cartitem.CartItemService;
import com.kat.bookstore.service.shoppingcart.ShoppingCartService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Transactional
public class CartItemServiceImpl implements CartItemService {
    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartItemMapper;
    private final BookRepository bookRepository;
    private final ShoppingCartService shoppingCartService;

    @Override
    public CartItemDto save(
            CreateCartItemRequestDto createCartItemRequestDto,
            ShoppingCart shoppingCart) {
        Book bookFromDB = bookRepository.findById(createCartItemRequestDto.bookId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find book by id=" + createCartItemRequestDto.bookId()));
        CartItem cartItem = cartItemMapper.toEntity(createCartItemRequestDto);
        cartItem.setBook(bookFromDB);
        cartItem.setShoppingCart(shoppingCart);
        CartItem savedCartItem = cartItemRepository.save(cartItem);
        shoppingCart.setCartItems(
                cartItemRepository.findAllByShoppingCart_Id(
                        shoppingCart.getId()));
        return cartItemMapper.toDto(savedCartItem);
    }

    @Override
    public CartItemDto updateQuantity(Long userId, Long cartItemId,
                                      CartItemQuantityRequestDto cartItemQuantityRequestDto) {
        CartItem cartItemFromDB = getCartItemByIdAndUserId(cartItemId, userId);
        cartItemFromDB.setQuantity(cartItemQuantityRequestDto.quantity());
        return cartItemMapper.toDto(cartItemRepository.save(cartItemFromDB));
    }

    @Override
    public void deleteById(Long cartItemId, Long userId) {
        cartItemRepository.delete(getCartItemByIdAndUserId(cartItemId, userId));
    }

    private CartItem getCartItemByIdAndUserId(Long cartItemId, Long userId) {
        return cartItemRepository.findByIdAndShoppingCart_Id(
                        cartItemId, shoppingCartService.getShoppingCart(userId).getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(
                                "Can't find cartItem by id = %s for this user", cartItemId)));
    }
}
