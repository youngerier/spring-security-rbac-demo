package com.example.dddclean.rbac.infrastructure.security;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * JWT令牌提供者，用于生成和验证JWT令牌
 */
@Component
public class JwtTokenProvider {

    @Value("${app.jwt.secret:JWTSuperSecretKey}")
    private String jwtSecret;

    @Value("${app.jwt.expiration:86400000}")
    private int jwtExpirationInMs;

    /**
     * 生成JWT令牌
     */
    public String generateToken(Authentication authentication) {
        CurrentUser userPrincipal = (CurrentUser) authentication.getPrincipal();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    /**
     * 从JWT令牌中获取用户名
     */
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    /**
     * 验证JWT令牌
     */
    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).build().parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            // 无效的JWT签名
        } catch (MalformedJwtException ex) {
            // 无效的JWT令牌
        } catch (ExpiredJwtException ex) {
            // 过期的JWT令牌
        } catch (UnsupportedJwtException ex) {
            // 不支持的JWT令牌
        } catch (IllegalArgumentException ex) {
            // JWT声明字符串为空
        }
        return false;
    }
}