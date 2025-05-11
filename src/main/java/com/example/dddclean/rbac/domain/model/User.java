package com.example.dddclean.rbac.domain.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * 用户实体
 */
@Entity
@Table(name = "rbac_users")
@Data
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    private String name;

    private String email;

    private boolean enabled = true;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "rbac_user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * 添加角色
     */
    public void addRole(Role role) {
        roles.add(role);
    }

    /**
     * 移除角色
     */
    public void removeRole(Role role) {
        roles.remove(role);
    }

    /**
     * 获取用户所有权限
     */
    public Set<Permission> getAllPermissions() {
        Set<Permission> permissions = new HashSet<>();
        for (Role role : roles) {
            permissions.addAll(role.getPermissions());
        }
        return permissions;
    }

    /**
     * 获取用户可访问的所有资源
     */
    public Set<Resource> getAllAccessibleResources() {
        Set<Resource> resources = new HashSet<>();
        for (Permission permission : getAllPermissions()) {
            resources.add(permission.getResource());
        }
        return resources;
    }
}