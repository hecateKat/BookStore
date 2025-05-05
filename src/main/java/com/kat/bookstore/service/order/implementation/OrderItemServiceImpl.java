package com.kat.bookstore.service.order.implementation;

import com.kat.bookstore.dto.order.OrderItemDto;
import com.kat.bookstore.entity.cartitem.CartItem;
import com.kat.bookstore.entity.order.Order;
import com.kat.bookstore.entity.order.OrderItem;
import com.kat.bookstore.exception.EntityNotFoundException;
import com.kat.bookstore.mapper.order.OrderItemMapper;
import com.kat.bookstore.repository.order.OrderItemRepository;
import com.kat.bookstore.service.order.OrderItemService;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final OrderItemMapper orderItemMapper;

    @Transactional
    @Override
    public OrderItem save(CartItem cartItem, Order order) {
        OrderItem orderItem = orderItemMapper.mapCartItemToOrderItem(cartItem);
        orderItem.setOrder(order);
        return orderItemRepository.save(orderItem);
    }

    @Override
    public List<OrderItemDto> getAllByOrderId(Long orderId, Pageable pageable, Long userId) {
        return orderItemRepository.findAllByOrder_User_IdAndOrder_Id(orderId, userId, pageable)
                .stream()
                .map(orderItemMapper::toDto)
                .toList();
    }

    @Override
    public OrderItemDto getByIdAndOrderIdAndUserId(Long orderItemId, Long orderId, Long userId) {
        return orderItemRepository.findByIdAndOrder_IdAndOrder_User_Id(orderItemId, orderId, userId)
                .map(orderItemMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Order item with id=%s and order_id=%s wasn't found "
                                + "for this user", orderItemId, orderId)));
    }
}
