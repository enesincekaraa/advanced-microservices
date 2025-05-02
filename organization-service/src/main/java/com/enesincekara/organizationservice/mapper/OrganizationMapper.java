package com.enesincekara.organizationservice.mapper;

import com.enesincekara.organizationservice.dto.request.OrganizationCreateRequest;
import com.enesincekara.organizationservice.dto.response.OrganizationResponse;
import com.enesincekara.organizationservice.model.Organization;
import org.springframework.stereotype.Component;

@Component
public class OrganizationMapper {


    public Organization toOrganization(OrganizationCreateRequest request) {
         Organization organization = new Organization();
         organization.setOrganizationName(request.organizationName());
            organization.setOrganizationDescription(request.organizationDescription());
            organization.setOrganizationCode(request.organizationCode());
            return organization;
    }



    public OrganizationResponse toOrganizationResponse(Organization organization) {
        return new OrganizationResponse(
                organization.getOrganizationName(),
                organization.getOrganizationDescription(),
                organization.getOrganizationCode(),
                organization.getCreatedAt()
        );
    }

}
