package com.kat.bookstore.dto.order;

import com.kat.bookstore.entity.order.Status;

public record OrderStatusRequestDto(Status status) {
}
