package com.personnel.system.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.personnel.system.annotation.LogOperation;
import com.personnel.system.dto.LoginRequestDTO;
import com.personnel.system.entity.User;
import com.personnel.system.repository.UserRepository;
import com.personnel.system.security.CustomUserDetails;
import com.personnel.system.service.OperationLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class OperationLogAspect {
    private final OperationLogService operationLogService;
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;

    @Around("@annotation(logOperation)")
    public Object logAround(ProceedingJoinPoint joinPoint, LogOperation logOperation) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = null;
        Exception exception = null;

        try {
            result = joinPoint.proceed();
            return result;
        } catch (Exception e) {
            exception = e;
            throw e;
        } finally {
            long executionTime = System.currentTimeMillis() - startTime;
            try {
                recordLog(joinPoint, logOperation, result, exception, executionTime);
            } catch (Exception e) {
                log.error("记录操作日志失败", e);
            }
        }
    }

    private void recordLog(ProceedingJoinPoint joinPoint, LogOperation logOperation,
                          Object result, Exception exception, long executionTime) {
        try {
            HttpServletRequest request = getCurrentRequest();
            if (request == null) {
                return;
            }

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = null;

            // 尝试从 SecurityContextHolder 获取用户
            if (authentication != null && authentication.isAuthenticated() && 
                !"anonymousUser".equals(authentication.getPrincipal())) {
                try {
                    user = ((CustomUserDetails) authentication.getPrincipal()).getUser();
                } catch (Exception e) {
                    // 类型转换失败，继续尝试其他方式
                }
            }

            // 如果无法从 SecurityContextHolder 获取用户，尝试从方法参数中获取
            if (user == null) {
                Object[] args = joinPoint.getArgs();
                if (args != null && args.length > 0) {
                    for (Object arg : args) {
                        if (arg instanceof LoginRequestDTO) {
                            String username = ((LoginRequestDTO) arg).getUsername();
                            user = userRepository.findByUsername(username).orElse(null);
                            break;
                        }
                    }
                }
            }

            // 如果还是无法获取用户，使用默认用户（id=1）
            if (user == null) {
                user = userRepository.findById(1).orElse(null);
                if (user == null) {
                    return;
                }
            }

            String operationType = logOperation.operationType();
            String operationDescription = logOperation.operationDescription();
            String requestMethod = request.getMethod();
            String requestUrl = request.getRequestURI();
            String requestParams = logOperation.logParams() ? getParams(joinPoint, request) : "参数已隐藏";
            String ipAddress = getClientIpAddress(request);

            if (exception != null) {
                operationLogService.logFailure(user, operationType, operationDescription,
                    requestMethod, requestUrl, requestParams, ipAddress,
                    exception.getMessage(), executionTime);
            } else {
                operationLogService.logSuccess(user, operationType, operationDescription,
                    requestMethod, requestUrl, requestParams, ipAddress, executionTime);
            }
        } catch (Exception e) {
            log.error("记录操作日志异常", e);
        }
    }

    private HttpServletRequest getCurrentRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes != null ? attributes.getRequest() : null;
    }

    private String getParams(ProceedingJoinPoint joinPoint, HttpServletRequest request) {
        try {
            if ("GET".equalsIgnoreCase(request.getMethod())) {
                return request.getQueryString();
            } else {
                Object[] args = joinPoint.getArgs();
                if (args.length > 0) {
                    return objectMapper.writeValueAsString(args);
                }
                return "";
            }
        } catch (Exception e) {
            return "参数解析失败";
        }
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}