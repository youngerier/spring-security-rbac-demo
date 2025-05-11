# Spring Boot DDD Clean Architecture 示例应用

这是一个基于Spring Boot的示例应用，展示了领域驱动设计(DDD)和整洁架构(Clean Architecture)的实现方式。

## 项目结构

项目采用了分层架构，主要包含以下几层：

- **表现层(Presentation)**: 处理HTTP请求，包含控制器
- **应用层(Application)**: 协调领域对象完成用例
- **领域层(Domain)**: 包含业务逻辑和规则
- **基础设施层(Infrastructure)**: 提供技术实现，如数据库访问

```
src/main/java/com/example/dddclean/
├── application/        # 应用服务层
├── domain/             # 领域模型层
│   └── model/
│       ├── entity/     # 实体
│       ├── valueobject/ # 值对象
│       ├── repository/ # 仓储接口
│       └── service/    # 领域服务
├── infrastructure/     # 基础设施层
│   └── persistence/    # 持久化实现
└── presentation/       # 表现层
    └── controller/     # 控制器
```

## 技术栈

- Spring Boot 2.7.x
- Spring Data JPA
- H2内存数据库
- Lombok
- Maven

## 如何运行

### 前提条件

- JDK 11或更高版本
- Maven 3.6或更高版本

### 构建和运行

1. 克隆项目到本地

2. 进入项目目录
   ```
   cd ddd-clean
   ```

3. 使用Maven构建项目
   ```
   mvn clean package
   ```

4. 运行应用程序
   ```
   java -jar target/ddd-clean-0.0.1-SNAPSHOT.jar
   ```
   或者使用Maven运行
   ```
   mvn spring-boot:run
   ```

5. 应用程序将在 http://localhost:8080 启动

## API端点

- `GET /api/hello` - 欢迎消息
- `GET /api/users` - 获取所有用户
- `GET /api/users/{id}` - 获取指定ID的用户
- `POST /api/users` - 创建新用户
- `PUT /api/users/{id}` - 更新指定ID的用户
- `DELETE /api/users/{id}` - 删除指定ID的用户

## H2数据库控制台

H2数据库控制台可以通过 http://localhost:8080/h2-console 访问

- JDBC URL: `jdbc:h2:mem:testdb`
- 用户名: `sa`
- 密码: (空)

## 项目特点

- 遵循DDD设计原则
- 实现Clean Architecture分层架构
- 使用内存数据库便于测试和演示
- 提供完整的CRUD操作示例