package com.example.dddclean.rbac.interfaces.rest;

import com.example.dddclean.rbac.application.service.ResourceService;
import com.example.dddclean.rbac.domain.model.Resource;
import com.example.dddclean.rbac.domain.model.User;
import com.example.dddclean.rbac.infrastructure.security.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 资源控制器，提供获取用户可访问资源的API
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ResourceController {

    private final ResourceService resourceService;

    /**
     * 获取当前用户可访问的所有资源
     */
    @GetMapping("/users/me/resources")
    public ResponseEntity<List<ResourceDTO>> getCurrentUserResources(@AuthenticationPrincipal CurrentUser currentUser) {
        User user = currentUser.getUser();
        Set<Resource> resources = user.getAllAccessibleResources();
        
        List<ResourceDTO> resourceDTOs = resources.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(resourceDTOs);
    }
    
    /**
     * 获取当前用户可访问的前端页面
     */
    @GetMapping("/users/me/pages")
    public ResponseEntity<List<ResourceDTO>> getCurrentUserPages(@AuthenticationPrincipal CurrentUser currentUser) {
        User user = currentUser.getUser();
        Set<Resource> resources = user.getAllAccessibleResources();
        
        List<ResourceDTO> pageResources = resources.stream()
                .filter(r -> r.getType() == Resource.ResourceType.PAGE)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(pageResources);
    }
    
    /**
     * 获取当前用户可访问的API接口
     */
    @GetMapping("/users/me/apis")
    public ResponseEntity<List<ResourceDTO>> getCurrentUserApis(@AuthenticationPrincipal CurrentUser currentUser) {
        User user = currentUser.getUser();
        Set<Resource> resources = user.getAllAccessibleResources();
        
        List<ResourceDTO> apiResources = resources.stream()
                .filter(r -> r.getType() == Resource.ResourceType.API)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(apiResources);
    }
    
    /**
     * 将资源实体转换为DTO
     */
    private ResourceDTO convertToDTO(Resource resource) {
        return new ResourceDTO(
                resource.getId(),
                resource.getCode(),
                resource.getName(),
                resource.getType().toString(),
                resource.getPath(),
                resource.getDescription()
        );
    }
    
    /**
     * 资源数据传输对象
     */
    public static class ResourceDTO {
        private Long id;
        private String code;
        private String name;
        private String type;
        private String path;
        private String description;
        
        public ResourceDTO(Long id, String code, String name, String type, String path, String description) {
            this.id = id;
            this.code = code;
            this.name = name;
            this.type = type;
            this.path = path;
            this.description = description;
        }

        public Long getId() {
            return id;
        }

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }

        public String getPath() {
            return path;
        }

        public String getDescription() {
            return description;
        }
    }
}