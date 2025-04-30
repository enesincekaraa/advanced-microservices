package com.enesincekara.employeeservice.service;

import com.enesincekara.employeeservice.dto.request.EmployeeCreateRequest;
import com.enesincekara.employeeservice.dto.response.ClientResponse;
import com.enesincekara.employeeservice.dto.response.EmployeeResponse;

public interface EmployeeService {


    EmployeeResponse createEmployee(EmployeeCreateRequest request);

    ClientResponse getEmployeeByEmail(String email);
}
