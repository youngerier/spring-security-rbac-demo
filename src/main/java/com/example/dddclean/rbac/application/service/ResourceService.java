package com.example.dddclean.rbac.application.service;

import com.example.dddclean.rbac.domain.model.Resource;
import com.example.dddclean.rbac.domain.model.User;
import com.example.dddclean.rbac.domain.repository.ResourceRepository;
import com.example.dddclean.rbac.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * 资源应用服务
 */
@Service
@RequiredArgsConstructor
public class ResourceService {

    private final ResourceRepository resourceRepository;
    private final UserRepository userRepository;

    /**
     * 获取所有资源
     */
    public List<Resource> getAllResources() {
        return resourceRepository.findAll();
    }

    /**
     * 根据ID获取资源
     */
    public Optional<Resource> getResourceById(Long id) {
        return resourceRepository.findById(id);
    }

    /**
     * 根据类型获取资源
     */
    public List<Resource> getResourcesByType(Resource.ResourceType type) {
        return resourceRepository.findByType(type);
    }

    /**
     * 获取用户可访问的所有资源
     */
    public Set<Resource> getUserAccessibleResources(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));
        return user.getAllAccessibleResources();
    }

    /**
     * 创建新资源
     */
    public Resource createResource(Resource resource) {
        return resourceRepository.save(resource);
    }

    /**
     * 更新资源
     */
    public Resource updateResource(Resource resource) {
        if (!resourceRepository.existsById(resource.getId())) {
            throw new IllegalArgumentException("资源不存在");
        }
        return resourceRepository.save(resource);
    }

    /**
     * 删除资源
     */
    public void deleteResource(Long id) {
        resourceRepository.deleteById(id);
    }
}