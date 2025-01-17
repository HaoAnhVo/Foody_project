package com.codegym.foody.repository;

import com.codegym.foody.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByMenuId(Long menuId);

    List<CartItem> findAllByCartId(Long cartId);

    @Query("select count(a.menu.id) from CartItem a join Cart b on a.cart.id = b.id where a.cart.id = :cartId and b.user.id = :userId")
    Long getNumProduct(@Param("cartId") Long cartId, @Param("userId") Long userId);
}
