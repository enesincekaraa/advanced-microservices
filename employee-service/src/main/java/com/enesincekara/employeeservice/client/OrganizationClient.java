package com.enesincekara.employeeservice.client;

import com.enesincekara.employeeservice.dto.response.ApiResponse;
import com.enesincekara.employeeservice.dto.response.OrganizationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "ORGANIZATION-SERVICE"
)
public interface OrganizationClient {

    @GetMapping("/api/v1/organizations/{organizationCode}")
    ApiResponse<OrganizationResponse> getOrganization(@PathVariable("organizationCode") String organizationCode);
}
