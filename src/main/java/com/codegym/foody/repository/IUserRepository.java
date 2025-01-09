package com.codegym.foody.repository;

import com.codegym.foody.model.Role;
import com.codegym.foody.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByPhone(String phone);
    List<User> findByRole(Role role);

    // Query Roles
    @Query("SELECT u FROM User u WHERE u.role IN :roles")
    Page<User> findAllByRoleIn(@Param("roles") List<Role> roles, Pageable pageable);

    @Query("SELECT u FROM User u WHERE (u.role IN :roles) AND " +
            "(LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(u.fullname) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<User> findByRoleAndKeyword(@Param("roles") List<Role> roles,
                                    @Param("keyword") String keyword,
                                    Pageable pageable);
}
