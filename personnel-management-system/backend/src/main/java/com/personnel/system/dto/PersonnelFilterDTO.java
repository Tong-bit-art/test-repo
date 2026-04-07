package com.personnel.system.dto;

import lombok.Data;

@Data
public class PersonnelFilterDTO {
    private String name;
    private String gender;
    private Integer categoryId;
    private String department;
    private String position;
    private String phone;
    private String email;
    private Integer page = 1;
    private Integer size = 10;
}
