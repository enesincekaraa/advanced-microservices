package com.enesincekara.departmetservice.dto.request;


import jakarta.validation.constraints.NotBlank;

public record DepartmentCreateRequest(
        @NotBlank(message = "Department name is not blank ") String departmentName,
        @NotBlank(message = " Department description is not blank ") String departmentDescription,
        @NotBlank(message = " Department code is not blank ") String departmentCode
) {
}
