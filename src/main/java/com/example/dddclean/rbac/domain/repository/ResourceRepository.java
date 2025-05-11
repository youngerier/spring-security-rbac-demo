package com.example.dddclean.rbac.domain.repository;

import com.example.dddclean.rbac.domain.model.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 资源仓储接口
 */
@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {
    
    /**
     * 根据资源类型查找资源
     */
    List<Resource> findByType(Resource.ResourceType type);
    
    /**
     * 根据资源代码查找资源
     */
    Resource findByCode(String code);
    
    /**
     * 根据路径查找资源
     */
    List<Resource> findByPathContaining(String path);
}