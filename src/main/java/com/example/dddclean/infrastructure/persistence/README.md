# 基础设施层 - 持久化

这个目录包含应用程序的持久化实现，负责数据的存储和检索。

## 目录结构

- `entity/` - 包含JPA实体类
- `repository/` - 包含Spring Data JPA仓储实现
- `mapper/` - 包含JPA实体与领域对象的转换器

## 设计原则

基础设施层应该：

1. 实现领域层定义的仓储接口
2. 处理数据持久化的技术细节
3. 与特定技术框架（如JPA）相关联
4. 将技术实现与领域模型隔离

## 示例实现

```java
@Repository
public class JpaUserRepository implements UserRepository {
    private final UserJpaRepository userJpaRepository;
    private final UserMapper userMapper;
    
    // 构造函数注入依赖
    
    @Override
    public User findById(UserId id) {
        return userJpaRepository.findById(id.getValue())
            .map(userMapper::toDomain)
            .orElse(null);
    }
    
    @Override
    public void save(User user) {
        UserJpaEntity entity = userMapper.toEntity(user);
        userJpaRepository.save(entity);
    }
}
```