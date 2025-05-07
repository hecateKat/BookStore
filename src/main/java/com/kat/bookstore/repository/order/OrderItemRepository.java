package com.kat.bookstore.repository.order;

import com.kat.bookstore.entity.order.OrderItem;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    Page<OrderItem> findAllByOrder_User_IdAndOrder_Id(Long userId,
                                                      Long orderId,
                                                      Pageable pageable);

    Optional<OrderItem> findByIdAndOrder_IdAndOrder_User_Id(Long orderItemId,
                                                            Long orderId,
                                                            Long userId);
}
