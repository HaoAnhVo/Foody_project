package com.codegym.foody.service.impl;

import com.codegym.foody.model.Role;
import com.codegym.foody.model.User;
import com.codegym.foody.repository.IUserRepository;
import com.codegym.foody.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements IUserService {
    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
}
