package com.personnel.system.dto;

import com.personnel.system.entity.Role;
import lombok.Data;
import java.util.List;

@Data
public class LoginResponseDTO {
    private Integer id;
    private String username;
    private String name;
    private List<Role> roles;
    private String token;
}
