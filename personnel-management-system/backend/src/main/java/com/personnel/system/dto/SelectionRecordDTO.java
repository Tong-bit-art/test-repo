package com.personnel.system.dto;

import com.personnel.system.entity.SelectionResult;
import com.personnel.system.entity.User;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class SelectionRecordDTO {
    private Integer id;
    private User user;
    private String selectionType;
    private String criteria;
    private Integer selectedCount;
    private LocalDateTime createdAt;
    private List<SelectionResult> results;
}