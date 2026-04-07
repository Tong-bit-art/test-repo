package com.personnel.system.service;

import com.personnel.system.entity.SelectionRecord;
import com.personnel.system.repository.SelectionRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SelectionRecordService {
    private final SelectionRecordRepository selectionRecordRepository;

    public List<SelectionRecord> findAll() {
        return selectionRecordRepository.findAll();
    }

    public SelectionRecord findById(Integer id) {
        return selectionRecordRepository.findById(id).orElse(null);
    }
}
