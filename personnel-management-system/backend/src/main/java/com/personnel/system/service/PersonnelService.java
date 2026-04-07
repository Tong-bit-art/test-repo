package com.personnel.system.service;

import com.personnel.system.entity.Personnel;
import com.personnel.system.entity.SelectionRecord;
import com.personnel.system.entity.SelectionResult;
import com.personnel.system.entity.User;
import com.personnel.system.dto.PersonnelFilterDTO;
import com.personnel.system.dto.SelectionRequestDTO;
import com.personnel.system.repository.PersonnelRepository;
import com.personnel.system.repository.SelectionRecordRepository;
import com.personnel.system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.criteria.Predicate;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class PersonnelService {
    private final PersonnelRepository personnelRepository;
    private final SelectionRecordRepository selectionRecordRepository;
    private final UserRepository userRepository;

    public Page<Personnel> findByFilter(PersonnelFilterDTO filter, User currentUser) {
        Specification<Personnel> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter != null) {
                if (filter.getName() != null && !filter.getName().isEmpty()) {
                    predicates.add(cb.like(root.get("name"), "%" + filter.getName() + "%"));
                }
                if (filter.getGender() != null && !filter.getGender().isEmpty()) {
                    predicates.add(cb.equal(root.get("gender"), filter.getGender()));
                }
                if (filter.getCategoryId() != null) {
                    predicates.add(cb.equal(root.get("category").get("id"), filter.getCategoryId()));
                }
                if (filter.getDepartment() != null && !filter.getDepartment().isEmpty()) {
                    predicates.add(cb.like(root.get("department"), "%" + filter.getDepartment() + "%"));
                }
                if (filter.getPosition() != null && !filter.getPosition().isEmpty()) {
                    predicates.add(cb.like(root.get("position"), "%" + filter.getPosition() + "%"));
                }
                if (filter.getPhone() != null && !filter.getPhone().isEmpty()) {
                    predicates.add(cb.like(root.get("phone"), "%" + filter.getPhone() + "%"));
                }
                if (filter.getEmail() != null && !filter.getEmail().isEmpty()) {
                    predicates.add(cb.like(root.get("email"), "%" + filter.getEmail() + "%"));
                }
            }

            // 录入员只能查看自己录入的人员
            if (currentUser != null && currentUser.getRoles() != null && 
                currentUser.getRoles().stream().anyMatch(role -> "ENTRY".equals(role.getName()))) {
                predicates.add(cb.equal(root.get("createdBy").get("id"), currentUser.getId()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Pageable pageable = PageRequest.of(filter != null ? filter.getPage() - 1 : 0, filter != null ? filter.getSize() : 1000);
        return personnelRepository.findAll(spec, pageable);
    }

    public Personnel save(Personnel personnel, User currentUser) {
        personnel.setCreatedBy(currentUser);
        return personnelRepository.save(personnel);
    }

    public void deleteById(Integer id) {
        personnelRepository.deleteById(id);
    }

    public Personnel findById(Integer id) {
        return personnelRepository.findById(id).orElse(null);
    }

    public void importExcel(MultipartFile file, User currentUser) throws IOException {
        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                Personnel personnel = new Personnel();
                personnel.setName(getCellValue(row.getCell(0)));
                personnel.setGender(getCellValue(row.getCell(1)));
                personnel.setAge(Integer.parseInt(getCellValue(row.getCell(2))));
                // 这里需要处理category_id的转换，暂时略过
                personnel.setDepartment(getCellValue(row.getCell(4)));
                personnel.setPosition(getCellValue(row.getCell(5)));
                personnel.setPhone(getCellValue(row.getCell(6)));
                personnel.setEmail(getCellValue(row.getCell(7)));
                personnel.setAddress(getCellValue(row.getCell(8)));
                personnel.setCreatedBy(currentUser);

                personnelRepository.save(personnel);
            }
        }
    }

    public void exportExcel(OutputStream outputStream, PersonnelFilterDTO filter, User currentUser) throws IOException {
        List<Personnel> personnelList = findByFilter(filter, currentUser).getContent();

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("人员信息");
            Row headerRow = sheet.createRow(0);
            String[] headers = {"姓名", "性别", "年龄", "专业类目", "部门", "职位", "电话", "邮箱", "地址"};
            for (int i = 0; i < headers.length; i++) {
                headerRow.createCell(i).setCellValue(headers[i]);
            }

            for (int i = 0; i < personnelList.size(); i++) {
                Personnel personnel = personnelList.get(i);
                Row row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(personnel.getName());
                row.createCell(1).setCellValue(personnel.getGender());
                row.createCell(2).setCellValue(personnel.getAge());
                row.createCell(3).setCellValue(personnel.getCategory() != null ? personnel.getCategory().getName() : "");
                row.createCell(4).setCellValue(personnel.getDepartment());
                row.createCell(5).setCellValue(personnel.getPosition());
                row.createCell(6).setCellValue(personnel.getPhone());
                row.createCell(7).setCellValue(personnel.getEmail());
                row.createCell(8).setCellValue(personnel.getAddress());
            }

            workbook.write(outputStream);
        }
    }

    public SelectionRecord randomSelection(SelectionRequestDTO request, User currentUser) {
        List<Personnel> candidates = new ArrayList<>();

        switch (request.getSelectionType()) {
            case "pure_random":
                // 纯随机抽取，获取所有人员
                candidates = new ArrayList<>(personnelRepository.findAll());
                break;
            case "specified_range":
                // 指定范围抽取，根据筛选条件
                // 为了获取所有符合条件的人员，设置page为1，size为一个足够大的值
                PersonnelFilterDTO rangeFilter = request.getFilter();
                if (rangeFilter != null) {
                    rangeFilter.setPage(1);
                    rangeFilter.setSize(1000); // 设置一个足够大的值，确保获取所有符合条件的人员
                    candidates = new ArrayList<>(findByFilter(rangeFilter, currentUser).getContent());
                } else {
                    // 如果filter为null，获取所有人员
                    candidates = new ArrayList<>(personnelRepository.findAll());
                }
                break;
            case "specified_personnel":
                // 指定人员抽取
                candidates = new ArrayList<>(personnelRepository.findAllById(request.getPersonnelIds()));
                break;
        }

        // 随机抽取指定人数
        List<Personnel> selected = new ArrayList<>();
        Random random = new Random();
        int count = Math.min(request.getCount(), candidates.size());

        for (int i = 0; i < count; i++) {
            int index = random.nextInt(candidates.size());
            selected.add(candidates.remove(index));
        }

        // 记录抽取结果
        SelectionRecord record = new SelectionRecord();
        
        // 如果currentUser为null，使用admin用户（id=1）
        if (currentUser == null) {
            currentUser = userRepository.findById(1).orElse(null);
        }
        record.setUser(currentUser);
        
        record.setSelectionType(request.getSelectionType());
        record.setCriteria(request.getFilter() != null ? request.getFilter().toString() : "");
        record.setSelectedCount(selected.size());
        record.setResults(new ArrayList<>());
        record = selectionRecordRepository.save(record);

        // 保存抽取结果
        for (Personnel personnel : selected) {
            SelectionResult result = new SelectionResult();
            result.setRecord(record);
            result.setPersonnel(personnel);
            result.setCreatedAt(java.time.LocalDateTime.now());
            record.getResults().add(result);
        }
        selectionRecordRepository.save(record);

        return record;
    }

    private String getCellValue(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf((int) cell.getNumericCellValue());
            default:
                return "";
        }
    }
}
