package com.codegym.foody.service.impl;

import com.codegym.foody.model.Menu;
import com.codegym.foody.model.Restaurant;
import com.codegym.foody.model.User;
import com.codegym.foody.model.enumable.ApprovalStatus;
import com.codegym.foody.repository.IMenuRepository;
import com.codegym.foody.repository.IOrderRepository;
import com.codegym.foody.repository.IRestaurantRepository;
import com.codegym.foody.service.IRestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantService implements IRestaurantService {
    @Autowired
    private IRestaurantRepository restaurantRepository;

    @Autowired
    private IOrderRepository orderRepository;

    @Autowired
    private IMenuRepository menuRepository;

    @Override
    public Page<Restaurant> findWithPaginationAndKeyword(String keyword, Pageable pageable) {
        if (keyword != null && !keyword.trim().isEmpty()) {
            return restaurantRepository.findByNameContainingIgnoreCase(keyword, pageable);
        }
        return restaurantRepository.findAll(pageable);
    }

    @Override
    public List<Restaurant> findByUserId(Long userId) {
        return restaurantRepository.findByUserId(userId);
    }

    @Override
    public void save(Restaurant restaurant) {
        restaurantRepository.save(restaurant);
    }

    @Override
    public void update(Restaurant restaurant) {
        if (restaurantRepository.existsById(restaurant.getId())) {
            restaurantRepository.save(restaurant);
        } else {
            throw new RuntimeException("Không tìm thấy nhà hàng");
        }
    }

    @Override
    public void delete(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Nhà hàng không tồn tại"));

        if (!orderRepository.findByRestaurantId(id).isEmpty()) {
            throw new IllegalArgumentException("Không thể xóa nhà hàng vì vẫn còn liên kết với đơn hàng.");
        }

        if (!menuRepository.findByRestaurantId(id).isEmpty()) {
            throw new IllegalArgumentException("Không thể xóa nhà hàng vì vẫn còn liên kết với món ăn.");
        }

        restaurantRepository.delete(restaurant);
    }

    @Override
    public long getTotal() {
        return restaurantRepository.count();
    }

    @Override
    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    @Override
    public Restaurant getById(Long id) {
        return restaurantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhà hàng"));
    }

    @Override
    public Page<Restaurant> findByUserId(Long userId, Pageable pageable) {
        return restaurantRepository.findByUserId(userId, pageable);
    }

    @Override
    public Page<Restaurant> findByUserIdAndKeyword(Long userId, String keyword, Pageable pageable) {
        return restaurantRepository.findByUserIdAndKeyword(userId, keyword, pageable);
    }

    @Override
    public List<Restaurant> findByApprovalStatus(ApprovalStatus status) {
        return restaurantRepository.findByApprovalStatus(status);
    }

    @Override
    public long countByApprovalStatus(ApprovalStatus status) {
        return restaurantRepository.countByApprovalStatus(status);
    }

    @Override
    public int getPage(Long restaurantId) {
        int pageSize = 10;
        List<Restaurant> sortedRestaurants = restaurantRepository.findAll();
        int index = sortedRestaurants.indexOf(restaurantRepository.findById(restaurantId).orElse(null));
        return index / pageSize;
    }

}
