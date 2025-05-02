package com.enesincekara.organizationservice.service;

import com.enesincekara.organizationservice.dto.request.OrganizationCreateRequest;
import com.enesincekara.organizationservice.dto.response.OrganizationResponse;

import java.util.Optional;

public interface OrganizationService {

    OrganizationResponse saveOrganization(OrganizationCreateRequest request);

    OrganizationResponse getOrganizationByOrganizationCode(String organizationCode);
}
