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

## API接口

- `POST /api/auth/login` - 用户登录
- `GET /api/users/me/resources` - 获取当前用户可访问的资源
- `GET /api/users/me/permissions` - 获取当前用户的权限
- `GET /api/resources` - 获取所有资源列表（管理员）
- `GET /api/roles` - 获取所有角色列表（管理员）
- `POST /api/roles` - 创建新角色（管理员）
- `PUT /api/roles/{id}` - 更新角色信息（管理员）
- `POST /api/users/{id}/roles` - 为用户分配角色（管理员）