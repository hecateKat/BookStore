package com.kat.bookstore.controller.shoppingcart;

import com.kat.bookstore.dto.cartitem.CartItemDto;
import com.kat.bookstore.dto.cartitem.CartItemQuantityRequestDto;
import com.kat.bookstore.dto.cartitem.CreateCartItemRequestDto;
import com.kat.bookstore.dto.shoppingcart.ShoppingCartDto;
import com.kat.bookstore.entity.user.User;
import com.kat.bookstore.service.cartitem.CartItemService;
import com.kat.bookstore.service.shoppingcart.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Shopping Cart management", description = "Endpoints for managing user's shopping cart")
@RequiredArgsConstructor
@RestController
@RequestMapping("/cart")
@Validated
public class ShoppingCartController {
    private final CartItemService cartItemService;
    private final ShoppingCartService shopCartService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping
    @Operation(summary = "Add a some book to the shopping cart",
            description = "Create a new cartItem entity in the database")
    public CartItemDto createCartItem(@RequestBody @Valid CreateCartItemRequestDto requestDto,
                                      Authentication authentication) {
        return cartItemService.save(requestDto,
                shopCartService.getShoppingCart(getAuthenticatedUser(authentication).getId()));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping
    @Operation(summary = "Retrieve authenticated user's shopping cart",
            description = "View owns shopping cart before placing an order")
    public ShoppingCartDto getShopCart(Authentication authentication) {
        return shopCartService.getShoppingCartDto(getAuthenticatedUser(authentication).getId());
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("/cart-items/{id}")
    @Operation(summary = "Update the books quantity",
            description = "Update the books quantity in the shopping cart")
    public CartItemDto updateBookQuantity(
            @PathVariable @Positive Long id,
            @RequestBody @Valid CartItemQuantityRequestDto requestDto,
            Authentication authentication) {
        return cartItemService
                .updateQuantity(getAuthenticatedUser(
                        authentication).getId(),
                        id,
                        requestDto);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/cart-items/{id}")
    @Operation(summary = "Remove a book from the shopping cart",
            description = "Remove purchases by id from the shopping cart "
                    + "(physically - not mark it as deleted)")
    public void delete(@PathVariable @Positive Long id, Authentication authentication) {
        cartItemService.deleteById(id, getAuthenticatedUser(authentication).getId());
    }

    private User getAuthenticatedUser(Authentication authentication) {
        return (User) authentication.getPrincipal();
    }
}
