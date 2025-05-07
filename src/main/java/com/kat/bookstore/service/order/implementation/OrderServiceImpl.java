package com.kat.bookstore.service.order.implementation;

import com.kat.bookstore.dto.order.CreateOrderRequestDto;
import com.kat.bookstore.dto.order.OrderDto;
import com.kat.bookstore.dto.order.OrderDtoWithoutOrderItems;
import com.kat.bookstore.dto.order.OrderStatusRequestDto;
import com.kat.bookstore.entity.cartitem.CartItem;
import com.kat.bookstore.entity.order.Order;
import com.kat.bookstore.entity.order.OrderItem;
import com.kat.bookstore.entity.order.Status;
import com.kat.bookstore.entity.user.User;
import com.kat.bookstore.exception.EntityNotFoundException;
import com.kat.bookstore.mapper.order.OrderMapper;
import com.kat.bookstore.repository.order.OrderRepository;
import com.kat.bookstore.service.cartitem.CartItemService;
import com.kat.bookstore.service.order.OrderItemService;
import com.kat.bookstore.service.order.OrderService;
import com.kat.bookstore.service.shoppingcart.ShoppingCartService;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final CartItemService cartItemService;
    private final OrderItemService orderItemService;
    private final ShoppingCartService shoppingCartService;

    @Transactional
    @Override
    public OrderDto save(CreateOrderRequestDto requestDto, User user) {
        Order order = orderMapper.toEntity(requestDto);
        order.setUser(user);
        order.setStatus(Status.PENDING);
        order.setOrderDate(LocalDateTime.now());
        order.setTotal(getTotalAmount(user.getId()));
        Order savedOrder = orderRepository.save(order);
        Set<CartItem> cartItemsFromDB = shoppingCartService
                .getShoppingCart(user.getId()).getCartItems();
        savedOrder.setOrderItems(createOrderItems(cartItemsFromDB, savedOrder));
        Order updatedOrder = orderRepository.save(order);
        clearShoppingCart(cartItemsFromDB, user.getId());
        return orderMapper.toDto(updatedOrder);
    }

    @Override
    public List<OrderDtoWithoutOrderItems> getAllByUserId(Long userId, Pageable pageable) {
        return orderRepository.findAllByUserId(userId, pageable).stream()
                .map(orderMapper::toDtoWithoutOrderItems)
                .toList();
    }

    @Override
    public OrderDtoWithoutOrderItems updateStatusById(Long id, OrderStatusRequestDto requestDto) {
        Order oldOrder = orderRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Can't get order by id = " + id));
        return orderMapper.toDtoWithoutOrderItems(
                orderMapper.updateStatusFromDto(oldOrder, requestDto));
    }

    private BigDecimal getTotalAmount(Long userId) {
        return shoppingCartService.getShoppingCart(userId).getCartItems().stream()
                .map(item -> item.getBook()
                        .getPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void clearShoppingCart(Set<CartItem> cartItemSet, Long userId) {
        for (CartItem cartItem: cartItemSet) {
            cartItemService.deleteById(cartItem.getId(), userId);
        }
    }

    private Set<OrderItem> createOrderItems(Set<CartItem> cartItemSet, Order order) {
        Set<OrderItem> orderItemSet = new HashSet<>();
        for (CartItem cartItem: cartItemSet) {
            orderItemSet.add(orderItemService.save(cartItem, order));
        }
        return orderItemSet;
    }
}
