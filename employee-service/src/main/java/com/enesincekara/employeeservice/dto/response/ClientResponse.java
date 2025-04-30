package com.enesincekara.employeeservice.dto.response;

public record ClientResponse(
        EmployeeResponse employeeResponse,
        ApiResponse<DepartmentResponse> departmentResponse
) {
}
