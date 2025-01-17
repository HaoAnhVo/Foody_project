package com.codegym.foody.repository;

import com.codegym.foody.model.Restaurant;
import com.codegym.foody.model.enumable.ApprovalStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IRestaurantRepository extends JpaRepository<Restaurant, Long> {
    Page<Restaurant> findByNameContainingIgnoreCase(String keyword, Pageable pageable);
    List<Restaurant> findByUserId(Long userId);
    Page<Restaurant> findByUserId(Long userId, Pageable pageable);
    List<Restaurant> findByApprovalStatus(ApprovalStatus status);
    long countByApprovalStatus(ApprovalStatus status);

    @Query("SELECT r FROM Restaurant r WHERE r.user.id = :userId AND :keyword IS NULL OR LOWER(r.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Restaurant> findByUserIdAndKeyword(@Param("userId") Long userId, @Param("keyword") String keyword, Pageable pageable);
}
