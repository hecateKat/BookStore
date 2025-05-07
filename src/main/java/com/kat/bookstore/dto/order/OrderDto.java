package com.kat.bookstore.dto.order;

import com.kat.bookstore.entity.order.Status;
import java.math.BigDecimal;
import java.util.Set;

public record OrderDto(Long id,
                       Long userId,
                       Set<OrderItemDto> orderItems,
                       String orderDate,
                       BigDecimal total,
                       Status status) {
}
