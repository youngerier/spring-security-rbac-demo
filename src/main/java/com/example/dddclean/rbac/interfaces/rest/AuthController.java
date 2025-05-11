package com.example.dddclean.rbac.interfaces.rest;

import com.example.dddclean.rbac.domain.model.User;
import com.example.dddclean.rbac.domain.repository.UserRepository;
import com.example.dddclean.rbac.infrastructure.security.CurrentUser;
import com.example.dddclean.rbac.infrastructure.security.JwtTokenProvider;
import com.example.dddclean.rbac.interfaces.rest.dto.JwtAuthenticationResponse;
import com.example.dddclean.rbac.interfaces.rest.dto.LoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 认证控制器，提供用户登录功能
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserRepository userRepository;

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        // 验证用户名和密码
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        // 设置认证信息到安全上下文
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 生成JWT令牌
        CurrentUser currentUser = (CurrentUser) authentication.getPrincipal();
        String jwt = tokenProvider.generateToken(authentication);
        
        // 获取用户信息
        User user = currentUser.getUser();
        
        // 返回令牌和用户信息
        return ResponseEntity.ok(new JwtAuthenticationResponse(
                jwt,
                user.getId(),
                user.getUsername(),
                user.getName(),
                user.getEmail()
        ));
    }
}