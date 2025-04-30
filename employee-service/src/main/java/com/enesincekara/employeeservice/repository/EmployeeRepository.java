package com.enesincekara.employeeservice.repository;

import com.enesincekara.employeeservice.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {


    boolean existsByEmail(String email);

    Optional<Employee> getEmployeeByEmail(String email);
}
