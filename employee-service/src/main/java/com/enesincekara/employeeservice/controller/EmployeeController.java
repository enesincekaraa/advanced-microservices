package com.enesincekara.employeeservice.controller;

import com.enesincekara.employeeservice.dto.request.EmployeeCreateRequest;
import com.enesincekara.employeeservice.dto.response.ApiResponse;
import com.enesincekara.employeeservice.dto.response.ClientResponse;
import com.enesincekara.employeeservice.dto.response.EmployeeResponse;
import com.enesincekara.employeeservice.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;

    }


    @PostMapping("/create")
    public ResponseEntity<ApiResponse<EmployeeResponse>> createEmployee(@Valid @RequestBody EmployeeCreateRequest request) {
        var employee = employeeService.createEmployee(request);
        return ResponseEntity.ok(
                new ApiResponse<>(
                        "Employee created successfully",
                        true,
                                LocalDateTime.now(),
                                HttpStatus.CREATED.value(),
                                employee
                )
        );
    }


    @GetMapping("/{email}")
    public ResponseEntity<ApiResponse<ClientResponse>> getEmployeeByEmail(@PathVariable String email) {

        var response = employeeService.getEmployeeByEmail(email);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        "Employee retrieved successfully",
                        true,
                        LocalDateTime.now(),
                        HttpStatus.OK.value(),
                        response
                )
        );
    }
}
