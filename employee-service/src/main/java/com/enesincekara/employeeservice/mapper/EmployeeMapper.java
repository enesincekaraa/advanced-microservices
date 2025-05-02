package com.enesincekara.employeeservice.mapper;

import com.enesincekara.employeeservice.dto.request.EmployeeCreateRequest;
import com.enesincekara.employeeservice.dto.response.EmployeeResponse;
import com.enesincekara.employeeservice.model.Employee;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {

    public EmployeeResponse toEmployeeResponse(Employee model) {
        return new EmployeeResponse(
                model.getFirstName(),
                model.getLastName(),
                model.getEmail(),
                model.getDepartmentCode(),
                model.getOrganizationCode()
        );
    }

    public Employee toEmployee(EmployeeCreateRequest request) {
        Employee employee = new Employee();
        employee.setFirstName(request.firstName());
        employee.setLastName(request.lastName());
        employee.setEmail(request.email());
        employee.setDepartmentCode(request.departmentCode());
        employee.setOrganizationCode(request.organizationCode());
        return employee;
    }
}
