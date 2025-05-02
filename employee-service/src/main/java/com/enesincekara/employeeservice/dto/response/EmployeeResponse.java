package com.enesincekara.employeeservice.dto.response;

public record EmployeeResponse(
        String firstName,
        String lastName,
        String email,
        String departmentCode,
        String organizationCode
) {
}
