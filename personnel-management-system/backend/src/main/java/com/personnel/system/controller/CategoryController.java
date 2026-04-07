package com.personnel.system.controller;

import com.personnel.system.annotation.LogOperation;
import com.personnel.system.annotation.RequireRole;
import com.personnel.system.entity.Category;
import com.personnel.system.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    @RequireRole("ADMIN")
    @LogOperation(operationType = "QUERY", operationDescription = "查询专业类目")
    public List<Category> findAll() {
        return categoryService.findAll();
    }

    @PostMapping
    @RequireRole("ADMIN")
    @LogOperation(operationType = "CREATE", operationDescription = "创建专业类目")
    public Category save(@RequestBody Category category) {
        return categoryService.save(category);
    }

    @PutMapping("/{id}")
    @RequireRole("ADMIN")
    @LogOperation(operationType = "UPDATE", operationDescription = "更新专业类目")
    public Category update(@PathVariable Integer id, @RequestBody Category category) {
        category.setId(id);
        return categoryService.save(category);
    }

    @DeleteMapping("/{id}")
    @RequireRole("ADMIN")
    @LogOperation(operationType = "DELETE", operationDescription = "删除专业类目")
    public void deleteById(@PathVariable Integer id) {
        categoryService.deleteById(id);
    }
}