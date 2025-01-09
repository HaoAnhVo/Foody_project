package com.codegym.foody.repository;

import com.codegym.foody.model.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IRestaurantRepository extends JpaRepository<Restaurant, Long> {
    Page<Restaurant> findByNameContainingIgnoreCase(String keyword, Pageable pageable);
    List<Restaurant> findByUserId(Long userId);
}
