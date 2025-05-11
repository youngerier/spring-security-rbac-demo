package com.example.dddclean.infrastructure.persistence.repository;

import com.example.dddclean.domain.model.entity.User;
import com.example.dddclean.domain.model.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 用户仓储的内存实现
 * 在实际项目中，这里应该使用JPA或其他ORM框架实现
 * 这里使用内存实现，方便演示
 */
@Repository
public class JpaUserRepository implements UserRepository {

    // 使用内存Map模拟数据库
    private final Map<Long, User> users = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public User save(User user) {
        if (user.getId() == null) {
            // 新用户，分配ID
            User newUser = User.builder()
                    .id(idGenerator.getAndIncrement())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .password(user.getPassword())
                    .build();
            users.put(newUser.getId(), newUser);
            return newUser;
        } else {
            // 更新现有用户
            users.put(user.getId(), user);
            return user;
        }
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public void deleteById(Long id) {
        users.remove(id);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return users.values().stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();
    }
}