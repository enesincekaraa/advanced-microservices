package com.enesincekara.departmetservice.mapper;

import com.enesincekara.departmetservice.dto.request.DepartmentCreateRequest;
import com.enesincekara.departmetservice.dto.response.DepartmentResponse;
import com.enesincekara.departmetservice.model.Department;
import org.springframework.stereotype.Component;

@Component
public class DepartmentMapper {

    public DepartmentResponse toDepartmentResponse(Department department) {
        return new DepartmentResponse(
                department.getDepartmentName(),
                department.getDepartmentDescription(),
                department.getDepartmentCode()
        );
    }

    public Department toDepartment(DepartmentCreateRequest request) {

        Department department = new Department();
        department.setDepartmentName(request.departmentName());
        department.setDepartmentDescription(request.departmentDescription());
        department.setDepartmentCode(request.departmentCode());
        return department;
    }
}
