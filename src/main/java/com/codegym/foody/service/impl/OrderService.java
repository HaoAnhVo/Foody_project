package com.codegym.foody.service.impl;

import com.codegym.foody.model.Order;
import com.codegym.foody.model.OrderStatus;
import com.codegym.foody.repository.IOrderRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderService {
    @Autowired
    private IOrderRepository orderRepository;

    public Page<Order> searchOrders(String keyword, Long restaurantId, Pageable pageable) {
        Long orderId = null;
        if (keyword != null && keyword.matches("\\d+")) {
            orderId = Long.valueOf(keyword);
        }
        return orderRepository.searchOrders(orderId != null ? orderId.toString() : keyword, restaurantId, pageable);
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy đơn hàng"));
    }

    public Order updateOrderStatus(Long id, OrderStatus status) {
        Order order = getOrderById(id);
        order.setStatus(status);
        return orderRepository.save(order);
    }

    public void cancelOrder(Long id) {
        Order order = getOrderById(id);
        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }
}
