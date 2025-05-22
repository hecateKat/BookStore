package com.kat.bookstore.controller.shoppingcart;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Set;
import javax.sql.DataSource;

import com.kat.bookstore.dto.cartitem.CartItemDto;
import com.kat.bookstore.dto.cartitem.CartItemQuantityRequestDto;
import com.kat.bookstore.dto.cartitem.CreateCartItemRequestDto;
import com.kat.bookstore.dto.shoppingcart.ShoppingCartDto;
import com.kat.bookstore.entity.book.Book;
import com.kat.bookstore.exception.EntityNotFoundException;
import lombok.SneakyThrows;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ShoppingCartControllerTest {
    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    public static void beforeAll(@Autowired DataSource dataSource,
                                 @Autowired WebApplicationContext appContext) throws SQLException {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(appContext)
                .apply(springSecurity())
                .build();
        teardown(dataSource);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource("shoppingCart/controller/initial-data.sql"));
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
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource("shoppingCart/controller/delete-all.sql"));
        }
    }

    @Test
    @Sql(scripts = "classpath:shoppingCart/controller/remove-added-cart-item.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithUserDetails("example1@example.com")
    public void test_should_return_true_when_createCartItem_with_existing_BookId_and_valid_RequestDto()
            throws Exception {
        //given
        Long bookId = 1L;
        CreateCartItemRequestDto requestDto = new CreateCartItemRequestDto(bookId, 1);

        Book book = new Book();
        book.setId(bookId);
        book.setTitle("Title of first test book");

        CartItemDto expected = new CartItemDto(1L, book.getId(), book.getTitle(),
                requestDto.quantity());

        //when
        MvcResult result = mockMvc.perform(post("/cart")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        //then
        CartItemDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CartItemDto.class);
        Assertions.assertNotNull(actual);
        assertTrue(EqualsBuilder.reflectionEquals(expected, actual, "id"));
    }

    @Test
    @Sql(scripts = "classpath:shoppingCart/controller/add-cart-items.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:shoppingCart/controller/remove-added-cart-item.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithUserDetails("example1@example.com")
    public void test_should_return_true_when_getShopCart_with_existing_User() throws Exception {
        //given
        Long userId = 1L;
        ShoppingCartDto expected = new ShoppingCartDto(userId,
                Set.of(new CartItemDto(1L, 1L, "Title of first test book", 17),
                        new CartItemDto(2L, 2L, "Title of second test book", 14)));

        //when
        MvcResult result = mockMvc.perform(get("/cart")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        //then
        ShoppingCartDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), ShoppingCartDto.class);
        Assertions.assertNotNull(actual);
        assertEquals(expected.userId(), actual.userId());
        assertEquals(expected.cartItems().size(), actual.cartItems().size());
        Set<CartItemDto> cartItemDtoSet = expected.cartItems();
        for (CartItemDto fromExpected: cartItemDtoSet) {
            Optional<CartItemDto> fromActual = actual.cartItems().stream()
                    .filter(item -> item.id().equals(fromExpected.id()))
                    .findFirst();
            assertTrue(fromActual.isPresent());
            assertTrue(EqualsBuilder.reflectionEquals(fromExpected, fromActual.get()));
        }
    }

    @Test
    @Sql(scripts = "classpath:shoppingCart/controller/add-cart-items.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:shoppingCart/controller/remove-added-cart-item.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithUserDetails("example1@example.com")
    public void test_should_return_true_when_updateBookQuantity_with_existing_CartItemId_and_valid_RequestDto()
            throws Exception {
        //given
        Long cartItemId = 2L;
        CartItemQuantityRequestDto requestDto = new CartItemQuantityRequestDto(100);
        CartItemDto expected = new CartItemDto(cartItemId, 2L,
                "Title of second test book", requestDto.quantity());

        //when
        MvcResult result = mockMvc.perform(put("/cart/cart-items/{id}", cartItemId)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        //then
        CartItemDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CartItemDto.class);
        Assertions.assertNotNull(actual);
        assertTrue(EqualsBuilder.reflectionEquals(expected, actual));
    }

    @Test
    @WithUserDetails("example1@example.com")
    public void test_should_throw_Exception_when_updateBookQuantity_with_not_existing_CartItemId() throws Exception {
        //given
        Long cartItemId = 100L;
        CartItemQuantityRequestDto requestDto = new CartItemQuantityRequestDto(100);

        //when & then
        mockMvc.perform(put("/cart/cart-items/{id}", cartItemId)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertInstanceOf(EntityNotFoundException.class, result.getResolvedException()));
    }

    @Test
    @Sql(scripts = "classpath:shoppingCart/controller/add-cart-item-to-delete.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @WithUserDetails("example1@example.com")
    public void test_should_return_true_when_delete_with_existing_CartItemId() throws Exception {
        //given
        Long cartItemId = 4L;

        //when & then
        mockMvc.perform(delete("/cart/cart-items/{id}", cartItemId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
    }
}
