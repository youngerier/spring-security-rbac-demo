package com.example.dddclean.rbac.infrastructure.persistence;

import com.example.dddclean.rbac.domain.model.Role;
import com.example.dddclean.rbac.domain.repository.RoleRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 角色仓储JPA实现
 */
@Repository
public interface RoleRepositoryImpl extends RoleRepository, JpaRepository<Role, Long> {
    
    /**
     * 根据角色名称查找角色
     */
    @Override
    Optional<Role> findByName(String name);
    
    /**
     * 检查角色名是否存在
     */
    @Override
    boolean existsByName(String name);
}