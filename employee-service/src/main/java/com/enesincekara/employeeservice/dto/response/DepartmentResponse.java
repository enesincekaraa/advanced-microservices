package com.enesincekara.employeeservice.dto.response;

public record DepartmentResponse(
        String departmentName,
        String departmentDescription,
        String departmentCode
) {
}
