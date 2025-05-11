package com.example.dddclean.rbac.domain.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * 权限实体
 */
@Entity
@Table(name = "rbac_permissions")
@Data
@NoArgsConstructor
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "resource_id", nullable = false)
    private Resource resource;

    // 操作类型：READ, WRITE, DELETE, EXECUTE等
    @Column(nullable = false)
    private String operation;

    public Permission(String name, Resource resource, String operation) {
        this.name = name;
        this.resource = resource;
        this.operation = operation;
    }

    /**
     * 检查是否有权限访问指定资源
     */
    public boolean canAccess(Resource resource, String requiredOperation) {
        return this.resource.equals(resource) && this.operation.equals(requiredOperation);
    }
}