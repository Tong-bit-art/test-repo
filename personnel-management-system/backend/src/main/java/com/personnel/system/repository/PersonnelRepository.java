package com.personnel.system.repository;

import com.personnel.system.entity.Personnel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PersonnelRepository extends JpaRepository<Personnel, Integer>, JpaSpecificationExecutor<Personnel> {
}
