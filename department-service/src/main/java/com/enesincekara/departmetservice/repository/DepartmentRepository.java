package com.enesincekara.departmetservice.repository;

import com.enesincekara.departmetservice.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    boolean existsByDepartmentName(String departmentName);

    Optional<Department> getByDepartmentCode(String departmentCode);
}
