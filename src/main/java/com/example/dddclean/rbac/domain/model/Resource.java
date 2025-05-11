package com.example.dddclean.rbac.domain.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * 资源实体，表示前端页面或API接口
 */
@Entity
@Table(name = "rbac_resources")
@Data
@NoArgsConstructor
public class Resource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String code;

    @Column(nullable = false)
    private String name;

    private String description;

    /**
     * 资源类型：PAGE(前端页面)或API(接口)
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ResourceType type;

    /**
     * 资源路径，对于PAGE类型是前端路由，对于API类型是接口路径
     */
    @Column(nullable = false)
    private String path;

    public Resource(String code, String name, ResourceType type, String path) {
        this.code = code;
        this.name = name;
        this.type = type;
        this.path = path;
    }

    /**
     * 资源类型枚举
     */
    public enum ResourceType {
        PAGE,  // 前端页面
        API    // 后端接口
    }
}