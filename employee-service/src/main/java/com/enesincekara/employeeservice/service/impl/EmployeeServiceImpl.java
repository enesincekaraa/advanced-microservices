package com.enesincekara.employeeservice.service.impl;

import com.enesincekara.employeeservice.client.DepartmentClient;
import com.enesincekara.employeeservice.client.OrganizationClient;
import com.enesincekara.employeeservice.dto.request.EmployeeCreateRequest;
import com.enesincekara.employeeservice.dto.response.*;
import com.enesincekara.employeeservice.exception.EmployeeAlreadyExistsException;
import com.enesincekara.employeeservice.mapper.EmployeeMapper;
import com.enesincekara.employeeservice.repository.EmployeeRepository;
import com.enesincekara.employeeservice.service.EmployeeService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EmployeeServiceImpl implements EmployeeService {


    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final DepartmentClient client;
    private final OrganizationClient organizationClient;
    private static final Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, EmployeeMapper employeeMapper, DepartmentClient client, OrganizationClient organizationClient) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
        this.client = client;
        this.organizationClient = organizationClient;
    }

    @Override
    public EmployeeResponse createEmployee(EmployeeCreateRequest request) {
       boolean exists = employeeRepository.existsByEmail((request.email()));
       if (exists) {
              throw new EmployeeAlreadyExistsException("Employee already exists with email: " + request.email());
         }
          try {
                var employee = employeeMapper.toEmployee(request);
                var savedEmployee = employeeRepository.save(employee);
                return employeeMapper.toEmployeeResponse(savedEmployee);
          } catch (Exception e) {
                throw new IllegalArgumentException("Error occurred while creating employee: " + e.getMessage());
       }


    }

    @CircuitBreaker(name = "${spring.application.name}",fallbackMethod = "getDefaultDepartment")
    @Retry(name = "${spring.application.name}", fallbackMethod = "getDefaultDepartment")
    @Override
    public ClientResponse getEmployeeByEmail(String email) {
        logger.info("Fetching employee with email: {}", email);
        var employee = employeeRepository.getEmployeeByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found with email: " + email));

        var employeeResponse = employeeMapper.toEmployeeResponse(employee);

        if (employee.getDepartmentCode().isEmpty() && employee.getDepartmentCode().isEmpty()){
            logger.error("Department code is empty for employee with email: {}", email);
            throw new IllegalArgumentException("Employee not found with email: " + email);
        }



        var department = client.getDepartment(employee.getDepartmentCode());
        var organization = organizationClient.getOrganization(employee.getOrganizationCode());

      return new ClientResponse(
              employeeResponse,
                new ApiResponse<>(
                        department.message(),
                        department.success(),
                        department.timestamp(),
                        department.statusCode(),
                        department.data()
                ),
                new ApiResponse<>(
                        organization.message(),
                        organization.success(),
                        organization.timestamp(),
                        organization.statusCode(),
                        organization.data()
                )
      );
    }

    public ClientResponse getDefaultDepartment(String email,Exception e) {
        logger.error("Error occurred while fetching department for employee with email: {}. Error: {}", email, e.getMessage());
        var employee = employeeRepository.getEmployeeByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found with email: " + email));

        var employeeResponse = employeeMapper.toEmployeeResponse(employee);

        if (employee.getDepartmentCode() == null){
            throw new IllegalArgumentException("Employee not found with email: " + email);
        }
//        var department = client.getDepartment(employee.getDepartmentCode());

        LocalDateTime now = LocalDateTime.now();
        String formattedDateTime = now.toString();
        DepartmentResponse department = new DepartmentResponse(
                "Department not found",
                "Department not found",
                formattedDateTime
        );
        OrganizationResponse organization = new OrganizationResponse(
                "Organization not found",
                "Organization not found",
                "Organization not found",
                now

        );

        return new ClientResponse(
                employeeResponse,
                new ApiResponse<>(
                        "Department not found",
                        false,
                        now,
                        HttpStatus.NOT_FOUND.value(),
                        department
                ),
                new ApiResponse<>(
                        "Organization not found",
                        false,
                        now,
                        HttpStatus.NOT_FOUND.value(),
                        organization
                )
        );


    }

}
