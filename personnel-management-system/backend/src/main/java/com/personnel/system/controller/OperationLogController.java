package com.personnel.system.controller;

import com.personnel.system.annotation.LogOperation;
import com.personnel.system.annotation.RequireRole;
import com.personnel.system.entity.OperationLog;
import com.personnel.system.repository.OperationLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/admin/operation-logs")
@RequiredArgsConstructor
public class OperationLogController {
    private final OperationLogRepository operationLogRepository;

    @GetMapping
    @RequireRole("ADMIN")
    @LogOperation(operationType = "QUERY", operationDescription = "查询操作日志")
    public List<OperationLog> findAll() {
        return operationLogRepository.findAllByOrderByCreatedAtDesc();
    }

    @GetMapping("/user/{userId}")
    @RequireRole("ADMIN")
    @LogOperation(operationType = "QUERY", operationDescription = "查询用户操作日志")
    public List<OperationLog> findByUserId(@PathVariable Integer userId) {
        return operationLogRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    @GetMapping("/username/{username}")
    @RequireRole("ADMIN")
    @LogOperation(operationType = "QUERY", operationDescription = "按用户名查询操作日志")
    public List<OperationLog> findByUsername(@PathVariable String username) {
        return operationLogRepository.findByUsernameOrderByCreatedAtDesc(username);
    }

    @GetMapping("/type/{operationType}")
    @RequireRole("ADMIN")
    @LogOperation(operationType = "QUERY", operationDescription = "按操作类型查询操作日志")
    public List<OperationLog> findByOperationType(@PathVariable String operationType) {
        return operationLogRepository.findByOperationTypeOrderByCreatedAtDesc(operationType);
    }

    @GetMapping("/date-range")
    @RequireRole("ADMIN")
    @LogOperation(operationType = "QUERY", operationDescription = "按日期范围查询操作日志")
    public List<OperationLog> findByDateRange(
            @RequestParam String startTime,
            @RequestParam String endTime) {
        LocalDateTime start = LocalDateTime.parse(startTime);
        LocalDateTime end = LocalDateTime.parse(endTime);
        return operationLogRepository.findByCreatedAtBetweenOrderByCreatedAtDesc(start, end);
    }
}