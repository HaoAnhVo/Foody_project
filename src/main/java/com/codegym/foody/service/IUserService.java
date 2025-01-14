package com.codegym.foody.service;

import com.codegym.foody.model.enumable.Role;
import com.codegym.foody.model.User;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface IUserService extends IGeneralService<User> {
    Page<User> findUsersWithPaginationAndRoleFilter(List<Role> roles, int page, int size, String keyword);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    String encodePassword(String password);
    List<User> findMerchants();
    Optional<User> findByUsername(String username);
    void updateStatusAndRelatedEntities(Long userId, boolean status);
}
