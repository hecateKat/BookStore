package com.kat.bookstore.service.cartitem.implementation;

import com.kat.bookstore.dto.cartitem.CartItemDto;
import com.kat.bookstore.dto.cartitem.CartItemQuantityRequestDto;
import com.kat.bookstore.dto.cartitem.CreateCartItemRequestDto;
import com.kat.bookstore.entity.book.Book;
import com.kat.bookstore.entity.cartitem.CartItem;
import com.kat.bookstore.entity.shoppingcart.ShoppingCart;
import com.kat.bookstore.entity.user.User;
import com.kat.bookstore.exception.EntityNotFoundException;
import com.kat.bookstore.mapper.cartitem.CartItemMapper;
import com.kat.bookstore.repository.book.BookRepository;
import com.kat.bookstore.repository.cartitem.CartItemRepository;
import com.kat.bookstore.repository.shoppingcart.ShoppingCartRepository;
import com.kat.bookstore.service.cartitem.CartItemService;
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
    private final ShoppingCartRepository shoppingCartRepository;

    @Override
    public CartItemDto save(
            CreateCartItemRequestDto createCartItemRequestDto,
            ShoppingCart shoppingCart) {
        CartItem cartItem = cartItemMapper.toEntity(createCartItemRequestDto);
        Book bookFromDB = bookRepository.findById(cartItem.getBook().getId()).orElseThrow(() ->
                new EntityNotFoundException(
                        "Can't find book by id=" + createCartItemRequestDto.bookId()));
        cartItem.setBook(bookFromDB);
        cartItem.setShoppingCart(shoppingCart);
        CartItem savedCartItem = cartItemRepository.save(cartItem);
        shoppingCart.setCartItems(
                cartItemRepository.findAllByShoppingCart_Id(shoppingCart.getId()));
        return cartItemMapper.toDto(savedCartItem);
    }

    @Override
    public CartItemDto updateQuantity(User user, Long cartItemId,
                                      CartItemQuantityRequestDto cartItemQuantityRequestDto) {
        CartItem cartItemFromDB = getCartItemByIdAndUser(cartItemId, user);
        cartItemFromDB.setQuantity(cartItemQuantityRequestDto.quantity());
        return cartItemMapper.toDto(cartItemRepository.save(cartItemFromDB));
    }

    @Override
    public void deleteById(Long cartItemId, User user) {
        cartItemRepository.delete(getCartItemByIdAndUser(cartItemId, user));
    }

    private ShoppingCart getShopCartByUser(User user) {
        return shoppingCartRepository
                .findById(user.getId()).orElseThrow(() ->
                new EntityNotFoundException(
                        "Can't find shopping cart by id: " + user.getId()));
    }

    private CartItem getCartItemByIdAndUser(Long cartItemId, User user) {
        return cartItemRepository
                .findByIdAndShoppingCart_Id(cartItemId, getShopCartByUser(user).getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(
                                "Can't find cartItem by id = %s for this user", cartItemId)));
    }
}
