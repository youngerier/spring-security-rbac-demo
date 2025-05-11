-- 初始化RBAC模块数据

-- 清除已有数据（按照外键关系顺序删除）
DELETE FROM rbac_user_roles;
DELETE FROM rbac_role_permissions;
DELETE FROM rbac_permissions;
DELETE FROM rbac_resources;
DELETE FROM rbac_roles;
DELETE FROM rbac_users;

-- 创建初始用户
INSERT INTO rbac_users (id, username, password, name, email, enabled) VALUES
(1, 'admin', '$2a$10$EqKcp1WBKSHUVFRmPOlRCOeRbG0vUQl8MJU9JbWUwJkLs7cxAUJAe', '系统管理员', 'admin@example.com', true),
(2, 'user', '$2a$10$EqKcp1WBKSHUVFRmPOlRCOeRbG0vUQl8MJU9JbWUwJkLs7cxAUJAe', '普通用户', 'user@example.com', true);

-- 创建角色
INSERT INTO rbac_roles (id, name, description) VALUES
(1, 'ROLE_ADMIN', '系统管理员角色'),
(2, 'ROLE_USER', '普通用户角色');

-- 创建资源
INSERT INTO rbac_resources (id, code, name, type, path, description) VALUES
-- 前端页面资源
(1, 'dashboard', '控制台', 'PAGE', '/dashboard', '系统控制台页面'),
(2, 'user_management', '用户管理', 'PAGE', '/users', '用户管理页面'),
(3, 'role_management', '角色管理', 'PAGE', '/roles', '角色管理页面'),
(4, 'resource_management', '资源管理', 'PAGE', '/resources', '资源管理页面'),
(5, 'profile', '个人中心', 'PAGE', '/profile', '用户个人中心页面'),
-- API接口资源
(6, 'api_users', '用户API', 'API', '/api/users/**', '用户相关API'),
(7, 'api_roles', '角色API', 'API', '/api/roles/**', '角色相关API'),
(8, 'api_resources', '资源API', 'API', '/api/resources/**', '资源相关API'),
(9, 'api_user_resources', '用户资源API', 'API', '/api/users/me/resources', '获取当前用户资源API'),
(10, 'api_user_pages', '用户页面API', 'API', '/api/users/me/pages', '获取当前用户可访问页面API'),
(11, 'api_user_apis', '用户接口API', 'API', '/api/users/me/apis', '获取当前用户可访问接口API');

-- 创建权限
INSERT INTO rbac_permissions (id, name, resource_id, operation, description) VALUES
-- 管理员权限
(1, 'view_dashboard', 1, 'READ', '查看控制台'),
(2, 'manage_users', 2, 'READ', '查看用户管理页面'),
(3, 'manage_roles', 3, 'READ', '查看角色管理页面'),
(4, 'manage_resources', 4, 'READ', '查看资源管理页面'),
(5, 'view_profile', 5, 'READ', '查看个人中心'),
(6, 'api_users_read', 6, 'READ', '读取用户API'),
(7, 'api_users_write', 6, 'WRITE', '写入用户API'),
(8, 'api_roles_read', 7, 'READ', '读取角色API'),
(9, 'api_roles_write', 7, 'WRITE', '写入角色API'),
(10, 'api_resources_read', 8, 'READ', '读取资源API'),
(11, 'api_resources_write', 8, 'WRITE', '写入资源API'),
(12, 'api_user_resources_read', 9, 'READ', '读取当前用户资源'),
(13, 'api_user_pages_read', 10, 'READ', '读取当前用户页面'),
(14, 'api_user_apis_read', 11, 'READ', '读取当前用户接口'),
-- 普通用户权限
(15, 'view_dashboard_user', 1, 'READ', '普通用户查看控制台'),
(16, 'view_profile_user', 5, 'READ', '普通用户查看个人中心'),
(17, 'api_user_resources_read_user', 9, 'READ', '普通用户读取当前用户资源'),
(18, 'api_user_pages_read_user', 10, 'READ', '普通用户读取当前用户页面'),
(19, 'api_user_apis_read_user', 11, 'READ', '普通用户读取当前用户接口');

-- 角色-权限关联
-- 管理员角色权限
INSERT INTO rbac_role_permissions (role_id, permission_id) VALUES
(1, 1), (1, 2), (1, 3), (1, 4), (1, 5), (1, 6), (1, 7), (1, 8), (1, 9), (1, 10), (1, 11), (1, 12), (1, 13), (1, 14);

-- 普通用户角色权限
INSERT INTO rbac_role_permissions (role_id, permission_id) VALUES
(2, 15), (2, 16), (2, 17), (2, 18), (2, 19);

-- 用户-角色关联
INSERT INTO rbac_user_roles (user_id, role_id) VALUES
(1, 1),  -- admin用户拥有管理员角色
(2, 2);  -- user用户拥有普通用户角色