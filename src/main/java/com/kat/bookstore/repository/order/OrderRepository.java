package com.kat.bookstore.repository.order;

import com.kat.bookstore.entity.order.Order;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByUserId(Long userId, Pageable pageable);
}
