package com.kat.bookstore.repository.cartitem;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.sql.DataSource;

import com.kat.bookstore.entity.book.Book;
import com.kat.bookstore.entity.cartitem.CartItem;
import com.kat.bookstore.entity.shoppingcart.ShoppingCart;
import lombok.SneakyThrows;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CartItemRepositoryTest {
    @Autowired
    private CartItemRepository cartItemRepo;

    @BeforeAll
    public static void beforeAll(@Autowired DataSource dataSource) throws SQLException {
        teardown(dataSource);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection, new ClassPathResource(
                    "cartItem/repository/add-required-data.sql"));
        }
    }

    @AfterAll
    public static void afterAll(@Autowired DataSource dataSource) {
        teardown(dataSource);
    }

    @SneakyThrows
    public static void teardown(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection, new ClassPathResource(
                    "cartItem/repository/delete-all.sql"));
        }
    }

    @Test
    public void test_should_return_true_when_findAll() {
        Set<CartItem> actual = cartItemRepo.findAllByShoppingCart_Id(2L);
        Assertions.assertEquals(4, actual.size());
    }

    @Test
    public void test_should_return_true_when_findAll_returns_all_cartItems_by_shopping_cart() {
        Set<CartItem> actual = cartItemRepo.findAllByShoppingCart_Id(1L);
        Assertions.assertEquals(2, actual.size());
    }

    @Test
    public void test_should_return_true_when_findAll_finds_0_eleemnts_on_non_existing_shoppingCart_Id() {
        Set<CartItem> actual = cartItemRepo.findAllByShoppingCart_Id(500L);
        Assertions.assertEquals(0, actual.size());
    }

    @Test
    public void test_should_return_true_when_findByShoppingCart_Id_using_existing_shoppingCart_and_CartItem_Id() {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(1L);

        CartItem expected = new CartItem();
        expected.setId(1L);
        expected.setShoppingCart(shoppingCart);
        expected.setBook(new Book());
        expected.setQuantity(1);

        Optional<CartItem> actual = cartItemRepo.findByIdAndShoppingCart_Id(1L, 1L);
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertTrue(EqualsBuilder.reflectionEquals(expected, actual.get(),
                List.of("shopcart", "book")));
        Assertions.assertEquals(expected.getShoppingCart().getId(), actual.get().getShoppingCart().getId());
    }

    @Test
    public void test_should_return_false_when_findByIdAndShoppingCart_Id_receives_non_existing_Id() {
        Optional<CartItem> actual = cartItemRepo.findByIdAndShoppingCart_Id(100L, 100L);
        Assertions.assertFalse(actual.isPresent());
    }
}
