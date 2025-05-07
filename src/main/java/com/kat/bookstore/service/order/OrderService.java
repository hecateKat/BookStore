package com.kat.bookstore.service.order;

import com.kat.bookstore.dto.order.CreateOrderRequestDto;
import com.kat.bookstore.dto.order.OrderDto;
import com.kat.bookstore.dto.order.OrderDtoWithoutOrderItems;
import com.kat.bookstore.dto.order.OrderStatusRequestDto;
import com.kat.bookstore.entity.user.User;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderDto save(CreateOrderRequestDto requestDto, User user);

    List<OrderDtoWithoutOrderItems> getAllByUserId(Long userId, Pageable pageable);

    OrderDtoWithoutOrderItems updateStatusById(Long id, OrderStatusRequestDto requestDto);
}
