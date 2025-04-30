package com.kat.bookstore.repository.cartitem;

import com.kat.bookstore.entity.cartitem.CartItem;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Set<CartItem> findAllByShoppingCart_Id(Long id);

    Optional<CartItem> findByIdAndShoppingCart_Id(Long id, Long shoppingCartId);
}
