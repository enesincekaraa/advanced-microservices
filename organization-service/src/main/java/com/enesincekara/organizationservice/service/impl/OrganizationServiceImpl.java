package com.enesincekara.organizationservice.service.impl;

import com.enesincekara.organizationservice.dto.request.OrganizationCreateRequest;
import com.enesincekara.organizationservice.dto.response.OrganizationResponse;
import com.enesincekara.organizationservice.exception.OrganizationAlreadyException;
import com.enesincekara.organizationservice.exception.OrganizationNotFoundException;
import com.enesincekara.organizationservice.exception.OrganizationSaveException;
import com.enesincekara.organizationservice.mapper.OrganizationMapper;
import com.enesincekara.organizationservice.repository.OrganizationRepository;
import com.enesincekara.organizationservice.service.OrganizationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrganizationServiceImpl implements OrganizationService {
    private final OrganizationRepository organizationRepository;
    private final OrganizationMapper organizationMapper;
    private static final Logger logger = LoggerFactory.getLogger(OrganizationServiceImpl.class);

    public OrganizationServiceImpl(OrganizationRepository organizationRepository, OrganizationMapper organizationMapper) {
        this.organizationRepository = organizationRepository;
        this.organizationMapper = organizationMapper;
    }

    @Override
    public OrganizationResponse saveOrganization(OrganizationCreateRequest request) {
        boolean exists = organizationRepository.existsOrganizationByOrganizationCode((request.organizationCode()));
        if (exists) {
            logger.error("Organization with code {} already exists", request.organizationCode());
            throw new OrganizationAlreadyException("Organization already exists");
        }
        try {
            var organization = organizationMapper.toOrganization(request);
            organizationRepository.save(organization);
            logger.info("Organization with code {} saved successfully", request.organizationCode());
            return organizationMapper.toOrganizationResponse(organization);
        }catch (Exception e) {
            logger.error("Error occurred while saving organization: {}", e.getMessage());
            throw new OrganizationSaveException("Failed to save organization");
        }

    }

    @Override
    public OrganizationResponse getOrganizationByOrganizationCode(String organizationCode) {
        var organization = organizationRepository.getOrganizationByOrganizationCode(organizationCode).orElseThrow(
                () -> new OrganizationNotFoundException("Organization with code " + organizationCode + " not found")
        );
        logger.info("Organization with code {} found", organizationCode);
        return organizationMapper.toOrganizationResponse(organization);
    }
}
