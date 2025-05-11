package com.example.dddclean.rbac.infrastructure.security;

import com.example.dddclean.rbac.domain.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Spring Security的UserDetails实现，包装领域模型中的User实体
 */
public class CurrentUser implements UserDetails {

    private final User user;

    public CurrentUser(User user) {
        this.user = user;
    }

    /**
     * 获取用户的权限列表
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 将用户角色转换为Spring Security的GrantedAuthority
        Collection<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
        
        // 添加用户的所有权限
        user.getAllPermissions().forEach(permission -> {
            String permissionAuthority = permission.getResource().getCode() + ":" + permission.getOperation();
            authorities.add(new SimpleGrantedAuthority(permissionAuthority));
        });
        
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }

    /**
     * 获取原始用户实体
     */
    public User getUser() {
        return user;
    }
}