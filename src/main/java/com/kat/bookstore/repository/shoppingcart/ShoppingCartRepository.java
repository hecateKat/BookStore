package com.kat.bookstore.repository.shoppingcart;

import com.kat.bookstore.entity.shoppingcart.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
}
