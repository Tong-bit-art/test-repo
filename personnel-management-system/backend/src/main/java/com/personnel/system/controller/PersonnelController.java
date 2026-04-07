package com.personnel.system.controller;

import com.personnel.system.annotation.LogOperation;
import com.personnel.system.annotation.RequireRole;
import com.personnel.system.entity.Personnel;
import com.personnel.system.entity.SelectionRecord;
import com.personnel.system.entity.User;
import com.personnel.system.dto.PersonnelFilterDTO;
import com.personnel.system.dto.SelectionRecordDTO;
import com.personnel.system.dto.SelectionRequestDTO;
import com.personnel.system.security.CustomUserDetails;
import com.personnel.system.service.PersonnelService;
import com.personnel.system.service.SelectionRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PersonnelController {
    private final PersonnelService personnelService;
    private final SelectionRecordService selectionRecordService;

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            // 临时返回一个默认用户对象，避免空指针异常
            User defaultUser = new User();
            defaultUser.setId(1);
            defaultUser.setUsername("default");
            return defaultUser;
        }
        try {
            return ((CustomUserDetails) authentication.getPrincipal()).getUser();
        } catch (Exception e) {
            // 类型转换失败时返回默认用户
            User defaultUser = new User();
            defaultUser.setId(1);
            defaultUser.setUsername("default");
            return defaultUser;
        }
    }

    @GetMapping("/admin/personnel")
    @RequireRole("ADMIN")
    @LogOperation(operationType = "QUERY", operationDescription = "查询人员信息")
    public Page<Personnel> findByFilter(@ModelAttribute PersonnelFilterDTO filter) {
        return personnelService.findByFilter(filter, getCurrentUser());
    }

    @PostMapping("/entry/personnel")
    @RequireRole({"ADMIN", "ENTRY"})
    @LogOperation(operationType = "CREATE", operationDescription = "录入人员信息")
    public Personnel save(@RequestBody Personnel personnel) {
        return personnelService.save(personnel, getCurrentUser());
    }

    @DeleteMapping("/admin/personnel/{id}")
    @RequireRole("ADMIN")
    @LogOperation(operationType = "DELETE", operationDescription = "删除人员信息")
    public void deleteById(@PathVariable Integer id) {
        personnelService.deleteById(id);
    }

    @PostMapping("/entry/personnel/import")
    @RequireRole({"ADMIN", "ENTRY"})
    @LogOperation(operationType = "IMPORT", operationDescription = "Excel批量导入人员信息")
    public void importExcel(@RequestParam("file") MultipartFile file) throws IOException {
        personnelService.importExcel(file, getCurrentUser());
    }

    @GetMapping("/admin/personnel/export")
    @RequireRole("ADMIN")
    @LogOperation(operationType = "EXPORT", operationDescription = "导出人员信息")
    public void exportExcel(@ModelAttribute PersonnelFilterDTO filter, HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=personnel.xlsx");
        personnelService.exportExcel(response.getOutputStream(), filter, getCurrentUser());
    }

    @PostMapping("/admin/personnel/select")
    @RequireRole("ADMIN")
    @LogOperation(operationType = "SELECTION", operationDescription = "随机抽取人员")
    public SelectionRecordDTO randomSelection(@RequestBody SelectionRequestDTO request) {
        SelectionRecord record = personnelService.randomSelection(request, getCurrentUser());
        SelectionRecordDTO dto = new SelectionRecordDTO();
        dto.setId(record.getId());
        dto.setUser(record.getUser());
        dto.setSelectionType(record.getSelectionType());
        dto.setCriteria(record.getCriteria());
        dto.setSelectedCount(record.getSelectedCount());
        dto.setCreatedAt(record.getCreatedAt());
        dto.setResults(record.getResults());
        return dto;
    }

    @GetMapping("/admin/selection-records")
    @RequireRole("ADMIN")
    @LogOperation(operationType = "QUERY", operationDescription = "查询抽取记录")
    public List<SelectionRecord> findAllSelectionRecords() {
        return selectionRecordService.findAll();
    }
}