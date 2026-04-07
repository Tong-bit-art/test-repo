package com.personnel.system.dto;

import lombok.Data;
import java.util.List;

@Data
public class SelectionRequestDTO {
    private String selectionType; // pure_random, specified_range, specified_personnel
    private PersonnelFilterDTO filter; // 筛选条件，用于specified_range
    private List<Integer> personnelIds; // 指定的人员ID，用于specified_personnel
    private Integer count; // 抽取人数
}
