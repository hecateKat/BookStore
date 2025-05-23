package com.kat.bookstore.service.shoppingcart;

import com.kat.bookstore.dto.shoppingcart.ShoppingCartDto;
import com.kat.bookstore.entity.shoppingcart.ShoppingCart;
import com.kat.bookstore.mapper.shoppingcart.ShoppingCartMapper;
import com.kat.bookstore.repository.shoppingcart.ShoppingCartRepository;
import java.util.Optional;
import com.kat.bookstore.service.shoppingcart.implementation.ShoppingCartServiceImpl;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class ShoppingCartServiceTest {

    @Mock
    private ShoppingCartRepository shoppingCartRepository;
    @Mock
    private ShoppingCartMapper shoppingCartMapper;
    @InjectMocks
    private ShoppingCartServiceImpl shoppingCartService;

    @Test
    void test_should_return_true_when_findById_ShoppingCartDto_with_valid_Id() {
        //given
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(1L);

        ShoppingCartDto expected = new ShoppingCartDto(shoppingCart.getId(), null);
        Mockito.when(shoppingCartRepository.findById(1L)).thenReturn(Optional.of(shoppingCart));
        Mockito.when(shoppingCartMapper.toDto(shoppingCart)).thenReturn(expected);

        //when
        ShoppingCartDto actual = shoppingCartService.getShoppingCartDto(1L);

        //then
        Assertions.assertTrue(
                EqualsBuilder.reflectionEquals(expected, actual));

    }

    @Test
    public void test_should_throw_exception_when_findById_gets_invalid_Id() {
        //given
        Long shoppingCartId = 1L;
        Mockito.when(shoppingCartRepository.findById(shoppingCartId)).thenReturn(Optional.empty());

        //then
        Assertions.assertThrows(
                RuntimeException.class,
                () -> shoppingCartService.getShoppingCart(shoppingCartId),
                "ShoppingCart with that id not found" + shoppingCartId);
    }

    @Test
    public void test_should_return_ShoppingCartDto_with_valid_Id_in_transactional_context() {
        // given
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(1L);

        ShoppingCartDto expected = new ShoppingCartDto(shoppingCart.getId(), null);
        Mockito.when(shoppingCartRepository.findById(1L)).thenReturn(Optional.of(shoppingCart));
        Mockito.when(shoppingCartMapper.toDto(shoppingCart)).thenReturn(expected);

        // when
        ShoppingCartDto actual = shoppingCartService.getShoppingCartDto(1L);

        // then
        Assertions.assertNotNull(actual);
        Assertions.assertTrue(
                EqualsBuilder.reflectionEquals(expected, actual));
        Mockito.verify(shoppingCartRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(shoppingCartMapper, Mockito.times(1)).toDto(shoppingCart);
    }

    @Test
    public void test_should_throw_exception_with_valid_Id_in_transactional_context() {
        // given
        Long shoppingCartId = 1L;
        Mockito.when(shoppingCartRepository.findById(shoppingCartId)).thenReturn(Optional.empty());

        // then
        Assertions.assertThrows(
                RuntimeException.class,
                () -> shoppingCartService.getShoppingCartDto(shoppingCartId),
                "Expected exception when shopping cart with id " + shoppingCartId + " is not found"
        );

        // verify
        Mockito.verify(shoppingCartRepository, Mockito.times(1)).findById(shoppingCartId);
        Mockito.verifyNoInteractions(shoppingCartMapper);
    }
}
