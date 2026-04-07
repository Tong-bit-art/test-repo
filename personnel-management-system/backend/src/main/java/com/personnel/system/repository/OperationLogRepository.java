package com.personnel.system.repository;

import com.personnel.system.entity.OperationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OperationLogRepository extends JpaRepository<OperationLog, Integer> {
    List<OperationLog> findByUserIdOrderByCreatedAtDesc(Integer userId);
    List<OperationLog> findByUsernameOrderByCreatedAtDesc(String username);
    List<OperationLog> findByOperationTypeOrderByCreatedAtDesc(String operationType);
    List<OperationLog> findByCreatedAtBetweenOrderByCreatedAtDesc(LocalDateTime startTime, LocalDateTime endTime);
    List<OperationLog> findAllByOrderByCreatedAtDesc();
}