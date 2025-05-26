package com.kat.bookstore.service.cartitem;

import com.kat.bookstore.dto.cartitem.CartItemDto;
import com.kat.bookstore.dto.cartitem.CartItemQuantityRequestDto;
import com.kat.bookstore.dto.cartitem.CreateCartItemRequestDto;
import com.kat.bookstore.entity.book.Book;
import com.kat.bookstore.entity.cartitem.CartItem;
import com.kat.bookstore.entity.shoppingcart.ShoppingCart;
import com.kat.bookstore.mapper.cartitem.CartItemMapper;
import com.kat.bookstore.repository.book.BookRepository;
import com.kat.bookstore.repository.cartitem.CartItemRepository;
import com.kat.bookstore.service.cartitem.implementation.CartItemServiceImpl;
import com.kat.bookstore.service.shoppingcart.ShoppingCartService;
import com.kat.bookstore.exception.EntityNotFoundException;
import java.util.Optional;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CartItemServiceTest {
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private CartItemMapper cartItemMapper;
    @Mock
    private ShoppingCartService shoppingCartService;
    @InjectMocks
    private CartItemServiceImpl cartItemService;

    @Test
    public void should_return_true_when_save_with_valid_requestDto() {
        //given
        Long bookId = 1L;
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(1L);
        Book book = new Book();
        book.setTitle("My book");
        book.setAuthor("My author");
        book.setIsbn("000-00-00000007");

        CreateCartItemRequestDto requestDto = new CreateCartItemRequestDto(bookId, 5);

        CartItem cartItem = new CartItem();
        cartItem.setBook(book);
        cartItem.setShoppingCart(shoppingCart);
        cartItem.setQuantity(requestDto.quantity());

        CartItem savedCartItem = new CartItem();
        savedCartItem.setId(7L);
        savedCartItem.setBook(cartItem.getBook());
        savedCartItem.setShoppingCart(cartItem.getShoppingCart());
        savedCartItem.setQuantity(cartItem.getQuantity());

        CartItemDto expected = new CartItemDto(
                savedCartItem.getId(),
                savedCartItem.getBook().getId(),
                savedCartItem.getBook().getTitle(),
                savedCartItem.getQuantity());

        Mockito.when(cartItemMapper.toEntity(requestDto)).thenReturn(cartItem);
        Mockito.when(bookRepository.findById(requestDto.bookId())).thenReturn(Optional.of(book));
        Mockito.when(cartItemRepository.save(cartItem)).thenReturn(savedCartItem);
        Mockito.when(cartItemMapper.toDto(savedCartItem)).thenReturn(expected);

        //when
        CartItemDto actual = cartItemService.save(requestDto, shoppingCart);

        //then
        Assertions.assertTrue(EqualsBuilder.reflectionEquals(expected, actual));
    }

    @Test
    public void test_should_throw_exception_when_save_with_non_existing_book() {
        //given
        Long bookId = 100L;
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(1L);
        CreateCartItemRequestDto requestDto = new CreateCartItemRequestDto(bookId, 100);
        Mockito.when(bookRepository.findById(requestDto.bookId())).thenReturn(Optional.empty());
        String expected = "Can't find book by id=" + requestDto.bookId();

        //when & then
        EntityNotFoundException exception = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> cartItemService.save(requestDto, shoppingCart)
        );

        Assertions.assertEquals(expected, exception.getMessage());
    }

    @Test
    public void test_should_return_true_when_update_with_valid_Id_and_requestDto() {
        //given
        Long userId = 8L;
        Long cartItemId = 3L;

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(userId);

        CartItem cartItem = new CartItem();
        cartItem.setId(cartItemId);
        cartItem.setBook(new Book());
        cartItem.setShoppingCart(shoppingCart);
        cartItem.setQuantity(100);

        CartItemQuantityRequestDto requestDto = new CartItemQuantityRequestDto(10);

        CartItem updatedCartItem = new CartItem();
        updatedCartItem.setId(cartItem.getId());
        updatedCartItem.setQuantity(requestDto.quantity());
        updatedCartItem.setShoppingCart(cartItem.getShoppingCart());
        updatedCartItem.setBook(cartItem.getBook());

        CartItemDto expected = new CartItemDto(
                updatedCartItem.getId(),
                updatedCartItem.getBook().getId(),
                updatedCartItem.getBook().getTitle(),
                updatedCartItem.getQuantity());

        Mockito.when(shoppingCartService.getShoppingCart(userId)).thenReturn(shoppingCart);
        Mockito.when(cartItemRepository.findByIdAndShoppingCart_Id(cartItemId, userId))
                .thenReturn(Optional.of(cartItem));
        Mockito.when(cartItemRepository.save(cartItem)).thenReturn(updatedCartItem);
        Mockito.when(cartItemMapper.toDto(updatedCartItem)).thenReturn(expected);

        //when
        CartItemDto actual = cartItemService.updateQuantity(userId, cartItemId, requestDto);

        //then
        Assertions.assertTrue(EqualsBuilder.reflectionEquals(expected, actual));
    }

    @Test
    public void test_should_throw_exception_when_update_with_non_existing_cartItem() {
        //given
        Long userId = 8L;
        Long cartItemId = 18L;
        CartItemQuantityRequestDto requestDto = new CartItemQuantityRequestDto(10);

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(userId);

        Mockito.when(shoppingCartService.getShoppingCart(userId)).thenReturn(shoppingCart);
        Mockito.when(cartItemRepository.findByIdAndShoppingCart_Id(cartItemId, userId))
                .thenReturn(Optional.empty());

        //when & then
        EntityNotFoundException exception = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> cartItemService.updateQuantity(userId, cartItemId, requestDto)
        );

        Assertions.assertEquals("Can't find cartItem by id = 18 for this user", exception.getMessage());
    }

    @Test
    public void test_should_return_true_when_delete_with_valid_Id_and_shoppingCartId() {
        //given
        Long userId = 8L;
        Long cartItemId = 3L;

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(userId);

        CartItem cartItem = new CartItem();
        cartItem.setId(cartItemId);
        cartItem.setBook(new Book());
        cartItem.setShoppingCart(shoppingCart);
        cartItem.setQuantity(100);

        Mockito.when(shoppingCartService.getShoppingCart(userId)).thenReturn(shoppingCart);
        Mockito.when(cartItemRepository.findByIdAndShoppingCart_Id(cartItemId, userId))
                .thenReturn(Optional.of(cartItem));

        //when
        cartItemService.deleteById(cartItemId, userId);

        //then
        Mockito.verify(cartItemRepository).delete(cartItem);
    }
}
