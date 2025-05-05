package com.kat.bookstore.service.order;

import com.kat.bookstore.dto.order.OrderItemDto;
import com.kat.bookstore.entity.cartitem.CartItem;
import com.kat.bookstore.entity.order.Order;
import com.kat.bookstore.entity.order.OrderItem;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface OrderItemService {
    OrderItem save(CartItem cartItem, Order order);

    List<OrderItemDto> getAllByOrderId(Long orderId, Pageable pageable, Long userId);

    OrderItemDto getByIdAndOrderIdAndUserId(Long orderItemId, Long orderId, Long userId);
}
