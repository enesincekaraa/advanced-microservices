package com.enesincekara.departmetservice.controller;

import com.enesincekara.departmetservice.dto.request.DepartmentCreateRequest;
import com.enesincekara.departmetservice.dto.response.ApiResponse;
import com.enesincekara.departmetservice.dto.response.DepartmentResponse;
import com.enesincekara.departmetservice.service.DepartmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<DepartmentResponse>> createDepartment(@Valid @RequestBody DepartmentCreateRequest request) {

        var response = departmentService.createDepartment(request);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        "Department created successfully",
                        true,
                        LocalDateTime.now(),
                        HttpStatus.CREATED.value(),
                        response
                )
        );
    }


    @GetMapping("/{departmentCode}")
    public ResponseEntity<ApiResponse<DepartmentResponse>> getDepartment(@PathVariable String departmentCode) {
        var response = departmentService.getDepartment(departmentCode);
        return ResponseEntity.ok(
                new ApiResponse<>(
                        "Department found successfully",
                        true,
                        LocalDateTime.now(),
                        HttpStatus.FOUND.value(),
                        response
                )
        );
    }
}
