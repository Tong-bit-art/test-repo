package com.personnel.system.aspect;

import com.personnel.system.annotation.RequireRole;
import com.personnel.system.security.CustomUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

// @Aspect
// @Component
@Slf4j
public class RoleCheckAspect {

    @Before("@annotation(requireRole)")
    public void checkRole(JoinPoint joinPoint, RequireRole requireRole) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            log.warn("未认证的用户尝试访问需要权限的接口: {}", joinPoint.getSignature());
            throw new AccessDeniedException("用户未登录");
        }

        if ("anonymousUser".equals(authentication.getPrincipal())) {
            log.warn("匿名用户尝试访问需要权限的接口: {}", joinPoint.getSignature());
            throw new AccessDeniedException("用户未登录");
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String[] requiredRoles = requireRole.value();
        
        boolean hasRequiredRole = userDetails.getAuthorities().stream()
                .anyMatch(authority -> {
                    String role = authority.getAuthority();
                    for (String requiredRole : requiredRoles) {
                        if (role.equals("ROLE_" + requiredRole)) {
                            return true;
                        }
                    }
                    return false;
                });

        if (!hasRequiredRole) {
            log.warn("用户 {} 尝试访问需要 {} 角色的接口: {}", 
                    userDetails.getUsername(), 
                    String.join(", ", requiredRoles), 
                    joinPoint.getSignature());
            throw new AccessDeniedException("权限不足，需要角色: " + String.join(", ", requiredRoles));
        }

        log.debug("用户 {} 通过权限检查，访问接口: {}", userDetails.getUsername(), joinPoint.getSignature());
    }
}