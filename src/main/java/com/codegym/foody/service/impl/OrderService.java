package com.codegym.foody.service.impl;

import com.codegym.foody.model.Order;
import com.codegym.foody.model.enumable.OrderStatus;
import com.codegym.foody.repository.IOrderRepository;
import com.codegym.foody.service.IOrderService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class OrderService implements IOrderService {
    @Autowired
    private IOrderRepository orderRepository;

    @Override
    public Page<Order> searchOrders(String keyword, Long restaurantId, Pageable pageable) {
        Long orderId = null;
        if (keyword != null && keyword.matches("\\d+")) {
            orderId = Long.valueOf(keyword);
        }
        return orderRepository.searchOrders(orderId != null ? orderId.toString() : keyword, restaurantId, pageable);
    }

    @Override
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy đơn hàng"));
    }

    @Override
    public Order updateOrderStatus(Long id, OrderStatus status) {
        Order order = getOrderById(id);
        order.setStatus(status);
        return orderRepository.save(order);
    }

    @Override
    public void cancelOrder(Long id) {
        Order order = getOrderById(id);
        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }
}
