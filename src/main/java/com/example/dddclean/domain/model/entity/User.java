package com.example.dddclean.domain.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 用户实体
 * 领域模型中的核心实体
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    private String username;
    private String email;
    private String password;
    
    /**
     * 验证用户数据是否有效
     * @return 是否有效
     */
    public boolean isValid() {
        return username != null && !username.trim().isEmpty() 
            && email != null && email.contains("@");
    }
    
    /**
     * 更新用户信息
     * @param username 新用户名
     * @param email 新邮箱
     */
    public void updateProfile(String username, String email) {
        if (username != null && !username.trim().isEmpty()) {
            this.username = username;
        }
        
        if (email != null && email.contains("@")) {
            this.email = email;
        }
    }
}