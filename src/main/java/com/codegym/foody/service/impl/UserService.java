package com.codegym.foody.service.impl;

import com.codegym.foody.model.Menu;
import com.codegym.foody.model.Restaurant;
import com.codegym.foody.model.enumable.Role;
import com.codegym.foody.model.User;
import com.codegym.foody.repository.IMenuRepository;
import com.codegym.foody.repository.IOrderRepository;
import com.codegym.foody.repository.IRestaurantRepository;
import com.codegym.foody.repository.IUserRepository;
import com.codegym.foody.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService {
    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private IRestaurantRepository restaurantRepository;

    @Autowired
    private IMenuRepository menuRepository;

    @Autowired
    private IOrderRepository orderRepository;

    @Override
    public Page<User> findUsersWithPaginationAndRoleFilter(List<Role> roles, int page, int size, String keyword) {
        Pageable pageable = PageRequest.of(page, size);
        if (keyword != null && !keyword.trim().isEmpty()) {
            return userRepository.findByRoleAndKeyword(roles, keyword, pageable);
        }
        return userRepository.findAllByRoleIn(roles, pageable);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @Override
    public boolean existsByPhone(String phone) {
        return userRepository.findByPhone(phone).isPresent();
    }

    @Override
    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    @Override
    public User getById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản này trên hệ thống!"));
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public void update(User user) {
        if (userRepository.existsById(user.getId())) {
            userRepository.save(user);
        } else {
            throw new RuntimeException("Không tìm thấy người dùng");
        }
    }

    @Override
    public void delete(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Người dùng không tồn tại."));

        if (!restaurantRepository.findByUserId(id).isEmpty()) {
            throw new IllegalArgumentException("Không thể xóa người dùng vì vẫn còn liên kết với nhà hàng.");
        }

        if (!orderRepository.findByUserId(id).isEmpty()) {
            throw new IllegalArgumentException("Không thể xóa người dùng vì vẫn còn liên kết với đơn hàng.");
        }

        userRepository.delete(user);
    }

    @Override
    public long getTotal() {
        return userRepository.count();
    }

    @Override
    public int getPage(Long userId) {
        int pageSize = 10;
        List<User> sortedUsers = userRepository.findAll();
        int index = sortedUsers.indexOf(userRepository.findById(userId).orElse(null));
        return index / pageSize;
    }

    @Override
    public List<User> findMerchants() {
        return userRepository.findByRole(Role.MERCHANT);
    }

    @Override
    @Transactional
    public void updateStatusAndRelatedEntities(Long userId, boolean status) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Người dùng không tồn tại"));

        user.setStatus(status);
        userRepository.save(user);

        if (user.getRole() == Role.MERCHANT) {
            List<Restaurant> restaurants = restaurantRepository.findByUserId(userId);
            for (Restaurant restaurant : restaurants) {
                restaurant.setStatus(status);
                restaurantRepository.save(restaurant);

                List<Menu> menus = menuRepository.findByRestaurantId(restaurant.getId());
                for (Menu menu : menus) {
                    menu.setStatus(status);
                    menuRepository.save(menu);
                }
            }
        }
    }

}
