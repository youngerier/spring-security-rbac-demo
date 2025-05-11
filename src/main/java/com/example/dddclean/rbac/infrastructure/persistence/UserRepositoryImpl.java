package com.example.dddclean.rbac.infrastructure.persistence;

import com.example.dddclean.rbac.domain.model.User;
import com.example.dddclean.rbac.domain.repository.UserRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 用户仓储JPA实现
 */
@Repository
public interface UserRepositoryImpl extends UserRepository, JpaRepository<User, Long> {
    
    /**
     * 根据用户名查找用户
     */
    @Override
    Optional<User> findByUsername(String username);
    
    /**
     * 检查用户名是否存在
     */
    @Override
    boolean existsByUsername(String username);
}