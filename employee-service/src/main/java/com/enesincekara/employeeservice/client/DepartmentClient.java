package com.enesincekara.employeeservice.client;


import com.enesincekara.employeeservice.dto.response.ApiResponse;
import com.enesincekara.employeeservice.dto.response.DepartmentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "DEPARTMENT-SERVICE"
)
public interface DepartmentClient {

    @GetMapping("/api/v1/departments/{departmentCode}")
    ApiResponse<DepartmentResponse> getDepartment(@PathVariable("departmentCode") String departmentCode);
}
