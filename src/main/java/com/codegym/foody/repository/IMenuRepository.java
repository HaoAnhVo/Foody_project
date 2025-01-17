package com.codegym.foody.repository;

import com.codegym.foody.model.Category;
import com.codegym.foody.model.Menu;
import com.codegym.foody.model.Order;
import com.codegym.foody.model.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface IMenuRepository extends JpaRepository<Menu, Long> {
    @Query("SELECT m FROM Menu m WHERE (:keyword IS NULL OR LOWER(m.name) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "AND (:categoryId IS NULL OR m.category.id = :categoryId) " +
            "AND (:restaurantId IS NULL OR m.restaurant.id = :restaurantId)")
    Page<Menu> searchMenus(@Param("keyword") String keyword,
                           @Param("categoryId") Long categoryId,
                           @Param("restaurantId") Long restaurantId,
                           Pageable pageable);
    List<Menu> findByCategoryId(Long categoryId);
    List<Menu> findByRestaurantId(Long menuId);
    @Query("SELECT m FROM Menu m WHERE m.restaurant.user.id = :merchantId " +
            "AND (:restaurantId IS NULL OR m.restaurant.id = :restaurantId) " +
            "AND (:keyword IS NULL OR m.name LIKE %:keyword%)")
    Page<Menu> findAllMenusByMerchantId(@Param("merchantId") Long merchantId,
                                        @Param("restaurantId") Long restaurantId,
                                        @Param("keyword") String keyword,
                                        Pageable pageable);
}
