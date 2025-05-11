# 应用服务层

这个目录包含应用程序的应用服务，负责协调领域对象完成用户用例。

## 目录结构

- `dto/` - 包含数据传输对象
- `mapper/` - 包含DTO与领域对象的转换器
- `service/` - 包含应用服务实现

## 设计原则

应用服务应该：

1. 协调领域对象完成用例
2. 不包含业务规则
3. 处理事务边界
4. 转换领域对象和DTO
5. 处理安全、日志等横切关注点

## 示例服务

```java
public interface UserService {
    UserDTO createUser(CreateUserCommand command);
    UserDTO getUserById(Long id);
    List<UserDTO> getAllUsers();
    void deleteUser(Long id);
}
```