package com.example.dddclean.domain.model.repository;

import com.example.dddclean.domain.model.entity.User;
import java.util.List;
import java.util.Optional;

/**
 * 用户仓储接口
 * 定义在领域层，由基础设施层实现
 */
public interface UserRepository {
    
    /**
     * 保存用户
     * @param user 用户实体
     * @return 保存后的用户实体
     */
    User save(User user);
    
    /**
     * 根据ID查找用户
     * @param id 用户ID
     * @return 用户实体（可能不存在）
     */
    Optional<User> findById(Long id);
    
    /**
     * 查找所有用户
     * @return 用户列表
     */
    List<User> findAll();
    
    /**
     * 根据ID删除用户
     * @param id 用户ID
     */
    void deleteById(Long id);
    
    /**
     * 根据用户名查找用户
     * @param username 用户名
     * @return 用户实体（可能不存在）
     */
    Optional<User> findByUsername(String username);
}