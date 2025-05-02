package com.enesincekara.organizationservice.dto.request;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public record OrganizationCreateRequest(
        @NotBlank(message = "Organization name is not blank ")String organizationName,
        String organizationDescription,
        @NotBlank(message = "Organization code is not blank ") String organizationCode
) {
}
