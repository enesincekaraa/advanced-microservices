package com.enesincekara.employeeservice.dto.request;

import jakarta.validation.constraints.NotBlank;

public record EmployeeCreateRequest(
        @NotBlank(message = "Firstname is not blank") String firstName,
        @NotBlank(message = "Lastname is not blank") String lastName,
        @NotBlank(message = "Email is not blank") String email,
        @NotBlank(message = "Department code is not blank") String departmentCode
) {
}
