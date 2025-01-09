package com.codegym.foody.repository;

import com.codegym.foody.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IOrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
    @Query("SELECT o FROM Order o WHERE " +
            "(:keyword IS NULL OR :keyword = '' OR CAST(o.id AS string) LIKE %:keyword% OR " +
            "o.user.fullname LIKE %:keyword% OR " +
            "o.user.phone LIKE %:keyword%) AND " +
            "(:restaurantId IS NULL OR o.restaurant.id = :restaurantId)")
    Page<Order> searchOrders(@Param("keyword") String keyword,
                             @Param("restaurantId") Long restaurantId,
                             Pageable pageable);

}
