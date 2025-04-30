package com.enesincekara.departmetservice.service;

import com.enesincekara.departmetservice.dto.request.DepartmentCreateRequest;
import com.enesincekara.departmetservice.dto.response.DepartmentResponse;

public interface DepartmentService {

    DepartmentResponse createDepartment(DepartmentCreateRequest request);
    DepartmentResponse getDepartment(String departmentCode);


}
