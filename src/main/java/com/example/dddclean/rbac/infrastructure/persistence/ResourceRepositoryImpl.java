package com.example.dddclean.rbac.infrastructure.persistence;

import com.example.dddclean.rbac.domain.model.Resource;
import com.example.dddclean.rbac.domain.repository.ResourceRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 资源仓储JPA实现
 */
@Repository
public interface ResourceRepositoryImpl extends ResourceRepository, JpaRepository<Resource, Long> {
    
    /**
     * 根据资源类型查找资源
     */
    @Override
    List<Resource> findByType(Resource.ResourceType type);
    
    /**
     * 根据资源代码查找资源
     */
    @Override
    Resource findByCode(String code);
    
    /**
     * 根据路径查找资源
     */
    @Override
    List<Resource> findByPathContaining(String path);
}