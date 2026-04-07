package com.personnel.system.controller;

import com.personnel.system.annotation.LogOperation;
import com.personnel.system.annotation.RequireRole;
import com.personnel.system.entity.User;
import com.personnel.system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    @RequireRole("ADMIN")
    @LogOperation(operationType = "QUERY", operationDescription = "查询用户列表")
    public List<User> findAll() {
        return userService.findAll();
    }

    @PostMapping
    @RequireRole("ADMIN")
    @LogOperation(operationType = "CREATE", operationDescription = "创建用户")
    public User save(@RequestBody User user) {
        return userService.save(user);
    }

    @DeleteMapping("/{id}")
    @RequireRole("ADMIN")
    @LogOperation(operationType = "DELETE", operationDescription = "删除用户")
    public void deleteById(@PathVariable Integer id) {
        userService.deleteById(id);
    }
}
