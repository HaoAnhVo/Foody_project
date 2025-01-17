package com.codegym.foody.repository;

import com.codegym.foody.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IOrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByMenuId(Long menuId);
}
