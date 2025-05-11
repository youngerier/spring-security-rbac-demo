# RBAC模块设计

本模块实现了基于角色的访问控制(Role-Based Access Control)系统，遵循DDD和Clean Architecture设计原则。

## 模块结构

```
rbac/
├── domain/                 # 领域层
│   ├── model/              # 领域模型
│   │   ├── User.java       # 用户实体
│   │   ├── Role.java       # 角色实体
│   │   ├── Permission.java # 权限实体
│   │   └── Resource.java   # 资源实体(前端页面/API)
│   ├── repository/         # 仓储接口
│   └── service/            # 领域服务
├── application/            # 应用层
│   ├── dto/                # 数据传输对象
│   └── service/            # 应用服务
├── infrastructure/         # 基础设施层
│   ├── config/             # 配置类
│   ├── persistence/        # 持久化实现
│   └── security/           # 安全相关实现
└── interfaces/             # 接口层
    ├── rest/               # REST API
    └── dto/                # 接口数据传输对象
```

## 核心功能

1. 用户身份认证与授权
2. 基于角色的权限管理
3. 资源访问控制（前端页面和API接口）
4. 权限检查与验证

## 数据库表结构

- 用户表（rbac_users）：存储用户基本信息
- 角色表（rbac_roles）：定义系统角色
- 资源表（rbac_resources）：定义受控资源（页面/API）
- 权限表（rbac_permissions）：定义资源的操作权限
- 用户角色关联表（rbac_user_roles）：用户与角色多对多关系
- 角色权限关联表（rbac_role_permissions）：角色与权限多对多关系

详细建表SQL见`src/main/resources/schema.sql`。

## 初始化数据说明

- 初始化数据位于`src/main/resources/data.sql`
- 包含初始用户（admin、user）、角色（ROLE_ADMIN、ROLE_USER）、资源、权限及关联关系
- 可通过数据库初始化脚本自动导入

## 主要API接口

### 用户认证与资源权限

- `POST /api/auth/login`
  - 描述：用户登录，获取JWT Token
  - 请求参数：
    ```json
    {
      "username": "admin",
      "password": "123456"
    }
    ```
  - 返回示例：
    ```json
    {
      "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6..."
    }
    ```

- `GET /api/users/me/resources`
  - 描述：获取当前用户可访问的资源
  - 返回示例：
    ```json
    [
      {"id":1,"code":"dashboard","type":"PAGE","path":"/dashboard"},
      {"id":6,"code":"api_users","type":"API","path":"/api/users/**"}
    ]
    ```

- `GET /api/users/me/permissions`
  - 描述：获取当前用户的权限
  - 返回示例：
    ```json
    [
      {"id":1,"name":"user_read","resourceId":6,"operation":"READ"}
    ]
    ```

### 管理员接口

- `GET /api/resources`  获取所有资源列表（需管理员权限）
- `GET /api/roles`      获取所有角色列表（需管理员权限）
- `POST /api/roles`     创建新角色（需管理员权限）
  - 请求参数：
    ```json
    {
      "name": "ROLE_MANAGER",
      "description": "管理者角色"
    }
    ```
- `PUT /api/roles/{id}` 更新角色信息（需管理员权限）
- `POST /api/users/{id}/roles` 为用户分配角色（需管理员权限）
  - 请求参数：
    ```json
    {
      "roleIds": [1,2]
    }
    ```

## 权限控制说明

- 所有接口均需JWT Token认证
- 管理员接口需具备ROLE_ADMIN角色
- 资源和权限控制基于RBAC模型自动判定

## 启动与测试步骤

1. 数据库准备：确保MySQL已启动，执行`schema.sql`和`data.sql`初始化表结构及数据
2. 配置数据库连接信息（application.yml）
3. 启动Spring Boot应用：
   ```bash
   ./mvnw spring-boot:run
   ```
4. 使用Postman等工具测试API接口
5. 登录后携带Token访问受保护资源

## 参考文件
- `src/main/resources/schema.sql`：数据库表结构
- `src/main/resources/data.sql`：初始化数据
- `application.yml`：数据库及安全配置