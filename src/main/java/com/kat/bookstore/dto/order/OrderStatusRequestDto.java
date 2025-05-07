package com.kat.bookstore.dto.order;

import com.kat.bookstore.entity.order.Status;
import jakarta.validation.constraints.NotNull;

public record OrderStatusRequestDto(@NotNull Status status) {
}
