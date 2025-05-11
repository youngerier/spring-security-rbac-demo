# Spring Security 结构概览 (基于 JWT)

本文档概述了本项目中 Spring Security 的配置和核心组件，主要围绕使用 JSON Web Tokens (JWT) 实现无状态认证和授权。

## 核心组件

### 1. `SecurityConfig.java`
这是 Spring Security 的主配置类。
- **注解**: `@Configuration`, `@EnableWebSecurity`, `@EnableGlobalMethodSecurity(prePostEnabled = true)`。
- **主要 Bean 配置**:
    - `PasswordEncoder`: 使用 `BCryptPasswordEncoder` 对密码进行加密和验证。
    - `DaoAuthenticationProvider`: 结合 `UserDetailsService` 和 `PasswordEncoder` 进行身份验证。
    - `AuthenticationManager`: 处理认证请求的核心接口。
    - `SecurityFilterChain`: 定义了安全过滤器链，是安全配置的核心。
        - **CSRF**: 禁用 (因为 JWT 是无状态的)。
        - **CORS**: 启用。
        - **Session Management**: 设置为 `STATELESS`，不使用 HTTP Session。
        - **Exception Handling**: 使用 `JwtAuthenticationEntryPoint` 处理认证异常 (如401错误)。
        - **Authorization Rules**:
            - `/api/auth/**` 和 `/h2-console/**`: 允许所有访问 (`permitAll()`)。
            - 其他所有请求 (`anyRequest()`): 需要认证 (`authenticated()`)。
        - **Filter Integration**: `JwtTokenFilter` 被添加到 `UsernamePasswordAuthenticationFilter` 之前。

### 2. JWT (JSON Web Token) 相关组件
- **`JwtTokenProvider.java`**:
    - **职责**: 生成、解析和验证 JWT。
    - `generateToken(Authentication authentication)`: 用户成功登录后生成 JWT。
    - `getUsernameFromToken(String token)`: 从 JWT 中提取用户名。
    - `validateToken(String authToken)`: 验证 JWT 的签名、有效期等。
- **`JwtTokenFilter.java`** (继承 `OncePerRequestFilter`):
    - **职责**: 拦截所有 HTTP 请求，检查是否存在有效的 JWT。
    - **流程**:
        1. 从 `Authorization` Header 中提取 "Bearer " Token。
        2. 如果 Token 有效 (通过 `JwtTokenProvider` 验证):
            a. 从 Token 中获取用户名。
            b. 使用 `UserDetailsServiceImpl` 加载用户详情 (`UserDetails`)。
            c. 创建 `UsernamePasswordAuthenticationToken` 并设置到 `SecurityContextHolder`。
- **`JwtAuthenticationEntryPoint.java`** (实现 `AuthenticationEntryPoint`):
    - **职责**: 处理认证失败的情况。
    - **行为**: 当未认证用户尝试访问受保护资源时，返回 HTTP 401 Unauthorized 错误。

### 3. 用户认证与授权细节
- **`UserDetailsServiceImpl.java`** (实现 `UserDetailsService`):
    - **职责**: 根据用户名从数据源 (如数据库) 加载用户特定的数据。
    - `loadUserByUsername(String username)`:
        - 通过 `UserRepository` 查找用户。
        - 如果找到，将领域模型 `User` 包装成 `CurrentUser` (一个 `UserDetails` 实现) 返回。
        - 如果未找到，抛出 `UsernameNotFoundException`。
- **`CurrentUser.java`** (实现 `UserDetails`):
    - **职责**: Spring Security 使用的当前用户信息载体。
    - 包装了领域对象 `User`。
    - 提供方法如 `getUsername()`, `getPassword()`, `getAuthorities()` (将用户的角色和权限转换为 `GrantedAuthority` 列表), 以及账户状态 (enabled, locked 等)。

## 认证流程

### A. 用户登录流程 (例如 `/api/auth/login`)
1.  **客户端**: 发送包含用户名和密码的 POST 请求。
2.  **`AuthController`**:
    - 接收请求。
    - 调用 `AuthenticationManager.authenticate()` 并传入 `UsernamePasswordAuthenticationToken`。
3.  **`AuthenticationManager`** (通常是 `ProviderManager`):
    - 委托给 `DaoAuthenticationProvider`。
4.  **`DaoAuthenticationProvider`**:
    - 使用 `UserDetailsServiceImpl` 加载用户信息。
    - 使用 `PasswordEncoder` 验证密码。
5.  **认证成功**:
    - `authenticate()` 返回一个包含 `CurrentUser` (作为 principal) 的 `Authentication` 对象。
    - `AuthController` 将此 `Authentication` 对象设置到 `SecurityContextHolder`。
    - `AuthController` 调用 `JwtTokenProvider.generateToken()` 生成 JWT。
6.  **响应**: `AuthController` 将 JWT (通常在 `JwtAuthenticationResponse` DTO 中) 返回给客户端。

### B. 访问受保护资源流程
1.  **客户端**: 在请求的 `Authorization` Header 中携带 JWT (例如, `Authorization: Bearer <token>`)。
2.  **`JwtTokenFilter`**:
    - 拦截请求，提取 JWT。
    - 使用 `JwtTokenProvider` 验证 JWT。
3.  **JWT 有效**:
    - 从 JWT 中获取用户名。
    - 使用 `UserDetailsServiceImpl` 加载 `CurrentUser`。
    - 创建 `UsernamePasswordAuthenticationToken` (此时密码通常为 null，因为认证已通过 JWT 完成)。
    - 将此 `Authentication` 对象设置到 `SecurityContextHolder`。
4.  **请求处理**: 请求继续流向目标控制器。
5.  **授权**: 如果控制器方法上有 `@PreAuthorize` 等注解，Spring Security 会检查 `SecurityContextHolder` 中的权限。
6.  **JWT 无效/缺失**:
    - 如果访问的是受保护资源，`SecurityContextHolder` 中没有有效的 `Authentication`。
    - `JwtAuthenticationEntryPoint` 会被触发，返回 401 Unauthorized。

## 类关系简图

```mermaid
graph TD
    subgraph "HTTP Request Flow"
        ClientRequest -->|1. Request with/without JWT| JwtTokenFilter
        JwtTokenFilter -->|2. Extracts JWT| JwtTokenProvider_Validate[JwtTokenProvider: validateToken]
        JwtTokenProvider_Validate -- "3. If Valid" --> JwtTokenFilter
        JwtTokenFilter -->|4. Gets Username| JwtTokenProvider_GetUser[JwtTokenProvider: getUsernameFromToken]
        JwtTokenFilter -->|5. Loads UserDetails| UserDetailsServiceImpl_Load[UserDetailsServiceImpl: loadUserByUsername]
        UserDetailsServiceImpl_Load -->|Returns UserDetails| CurrentUser_Impl[CurrentUser implements UserDetails]
        JwtTokenFilter -->|6. Sets Authentication| SecurityContextHolder
        SecurityContextHolder -->|7. Checked by| ProtectedResource[Controller/Service with @PreAuthorize]
        ClientRequest -->|If Unauthenticated & Accessing Protected| JwtAuthenticationEntryPoint["JwtAuthenticationEntryPoint (401)"]
    end

    subgraph "Login Flow (/api/auth/login)"
        LoginClientRequest[Client: POST /api/auth/login with credentials] --> AuthController
        AuthController -->|Calls authenticate()| AuthenticationManager
        AuthenticationManager -->|Delegates to| DaoAuthenticationProvider
        DaoAuthenticationProvider -->|Uses| UserDetailsServiceImpl_Login[UserDetailsServiceImpl: loadUserByUsername]
        DaoAuthenticationProvider -->|Uses| PasswordEncoder_Login[PasswordEncoder: matches]
        UserDetailsServiceImpl_Login -->|Returns UserDetails| CurrentUser_Login[CurrentUser]
        DaoAuthenticationProvider -- "Authentication Successful" --> AuthController_AuthSuccess[AuthController: Receives Authentication Object]
        AuthController_AuthSuccess -->|Sets in| SecurityContextHolder_Login[SecurityContextHolder]
        AuthController_AuthSuccess -->|Generates Token| JwtTokenProvider_Generate[JwtTokenProvider: generateToken]
        JwtTokenProvider_Generate -->|Returns JWT| AuthController_Response[AuthController: Returns JwtAuthenticationResponse]
        AuthController_Response --> LoginClientResponse[Client: Receives JWT]
    end

    SecurityConfig -->|Configures| DaoAuthenticationProvider
    SecurityConfig -->|Configures| PasswordEncoder_Bean[PasswordEncoder]
    SecurityConfig -->|Configures| JwtTokenFilter
    SecurityConfig -->|Configures| JwtAuthenticationEntryPoint
    SecurityConfig -->|Configures| AuthenticationManager

    UserDetailsServiceImpl_Load -->|Uses| UserRepository
    CurrentUser_Impl -.->|Wraps| UserDomain[User (Domain Model)]