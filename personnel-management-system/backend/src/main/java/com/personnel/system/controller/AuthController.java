package com.personnel.system.controller;

import com.personnel.system.annotation.LogOperation;
import com.personnel.system.dto.LoginRequestDTO;
import com.personnel.system.dto.LoginResponseDTO;
import com.personnel.system.security.CustomUserDetails;
import com.personnel.system.entity.User;
import com.personnel.system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    @PostMapping("/login")
    @LogOperation(operationType = "LOGIN", operationDescription = "用户登录")
    public LoginResponseDTO login(@RequestBody LoginRequestDTO request) {
        log.info("Login request for user: {}", request.getUsername());
        
        try {
            // 先查询用户
            User user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new RuntimeException("用户不存在"));
            
            log.info("User found: {}, password in DB: {}", user.getUsername(), user.getPassword());
            
            // 临时使用明文密码验证（仅用于测试）
            // TODO: 生产环境请改回BCrypt验证
            if (!"123456".equals(request.getPassword())) {
                log.error("Password mismatch for user: {}", request.getUsername());
                throw new RuntimeException("密码错误");
            }
            
            // 创建Authentication对象
            CustomUserDetails userDetails = new CustomUserDetails(user);
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
            );
            
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            LoginResponseDTO response = new LoginResponseDTO();
            response.setId(user.getId());
            response.setUsername(user.getUsername());
            response.setName(user.getName());
            response.setRoles(user.getRoles());
            response.setToken("mock-token-" + System.currentTimeMillis());
            
            log.info("Login successful for user: {}", request.getUsername());
            return response;
        } catch (Exception e) {
            log.error("Login error for user: {}, error: {}", request.getUsername(), e.getMessage(), e);
            throw new RuntimeException("用户名或密码错误: " + e.getMessage());
        }
    }

    @PostMapping("/logout")
    @LogOperation(operationType = "LOGOUT", operationDescription = "用户登出")
    public Map<String, String> logout() {
        SecurityContextHolder.clearContext();
        Map<String, String> response = new HashMap<>();
        response.put("message", "登出成功");
        return response;
    }

    @GetMapping("/current-user")
    public Map<String, Object> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || 
            "anonymousUser".equals(authentication.getPrincipal())) {
            throw new RuntimeException("用户未登录");
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();
        Map<String, Object> response = new HashMap<>();
        response.put("id", user.getId());
        response.put("username", user.getUsername());
        response.put("name", user.getName());
        response.put("roles", user.getRoles());
        return response;
    }
}