package com.codegym.foody.repository;

import com.codegym.foody.model.Menu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface IMenuRepository extends JpaRepository<Menu, Long> {
    @Query("SELECT m FROM Menu m WHERE (:keyword IS NULL OR LOWER(m.name) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "AND (:categoryId IS NULL OR m.category.id = :categoryId) " +
            "AND (:restaurantId IS NULL OR m.restaurant.id = :restaurantId)")
    Page<Menu> searchMenus(@Param("keyword") String keyword,
                           @Param("categoryId") Long categoryId,
                           @Param("restaurantId") Long restaurantId,
                           Pageable pageable);
}
