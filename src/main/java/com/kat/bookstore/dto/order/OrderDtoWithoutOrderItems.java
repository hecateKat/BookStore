package com.kat.bookstore.dto.order;

import com.kat.bookstore.entity.order.Status;
import java.math.BigDecimal;

public record OrderDtoWithoutOrderItems(Long id,
                                        Long userId,
                                        String orderDate,
                                        BigDecimal total,
                                        Status status) {
}
