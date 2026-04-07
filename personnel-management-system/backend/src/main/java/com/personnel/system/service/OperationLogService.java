package com.personnel.system.service;

import com.personnel.system.entity.OperationLog;
import com.personnel.system.entity.User;
import com.personnel.system.repository.OperationLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class OperationLogService {
    private final OperationLogRepository operationLogRepository;

    @Transactional
    public void logOperation(User user, String operationType, String operationDescription,
                             String requestMethod, String requestUrl, String requestParams,
                             String ipAddress, String status, String errorMessage, Long executionTime) {
        OperationLog operationLog = new OperationLog();
        operationLog.setUser(user);
        operationLog.setUsername(user.getUsername());
        operationLog.setOperationType(operationType);
        operationLog.setOperationDescription(operationDescription);
        operationLog.setRequestMethod(requestMethod);
        operationLog.setRequestUrl(requestUrl);
        operationLog.setRequestParams(requestParams);
        operationLog.setIpAddress(ipAddress);
        operationLog.setStatus(status);
        operationLog.setErrorMessage(errorMessage);
        operationLog.setExecutionTime(executionTime);
        operationLog.setCreatedAt(LocalDateTime.now());

        operationLogRepository.save(operationLog);
        log.info("操作日志记录成功: 用户={}, 操作类型={}, 描述={}", user.getUsername(), operationType, operationDescription);
    }

    @Transactional
    public void logSuccess(User user, String operationType, String operationDescription,
                          String requestMethod, String requestUrl, String requestParams,
                          String ipAddress, Long executionTime) {
        logOperation(user, operationType, operationDescription, requestMethod, requestUrl,
                     requestParams, ipAddress, "SUCCESS", null, executionTime);
    }

    @Transactional
    public void logFailure(User user, String operationType, String operationDescription,
                         String requestMethod, String requestUrl, String requestParams,
                         String ipAddress, String errorMessage, Long executionTime) {
        logOperation(user, operationType, operationDescription, requestMethod, requestUrl,
                     requestParams, ipAddress, "FAILURE", errorMessage, executionTime);
    }
}