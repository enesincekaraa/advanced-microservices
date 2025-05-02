package com.enesincekara.employeeservice.dto.response;

import java.time.LocalDateTime;

public record OrganizationResponse(
        String organizationName,
        String organizationDescription,
        String organizationCode,
        LocalDateTime createdAt
) {
}
