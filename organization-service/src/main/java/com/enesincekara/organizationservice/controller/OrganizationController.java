package com.enesincekara.organizationservice.controller;

import com.enesincekara.organizationservice.dto.request.OrganizationCreateRequest;
import com.enesincekara.organizationservice.dto.response.ApiResponse;
import com.enesincekara.organizationservice.dto.response.OrganizationResponse;
import com.enesincekara.organizationservice.service.OrganizationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/organizations")
public class OrganizationController {

    private final OrganizationService organizationService;

    public OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }


    @PostMapping("/create")
    public ResponseEntity<ApiResponse<OrganizationResponse>> createOrganization(@Valid @RequestBody OrganizationCreateRequest request) {
        var response = organizationService.saveOrganization(request);
        return ResponseEntity.ok(
                new ApiResponse<>(
                        "Organization created successfully",
                        true,
                        LocalDateTime.now(),
                        201,
                        response
                ));

    }

    @GetMapping("/{organizationCode}")
    public ResponseEntity<ApiResponse<OrganizationResponse>> getOrganization(@PathVariable String organizationCode) {
        var response = organizationService.getOrganizationByOrganizationCode(organizationCode);
        return ResponseEntity.ok(
                new ApiResponse<>(
                        "Organization found successfully",
                        true,
                        LocalDateTime.now(),
                        200,
                        response
                ));
    }


}
