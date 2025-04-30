package com.enesincekara.departmetservice.service.impl;

import com.enesincekara.departmetservice.dto.request.DepartmentCreateRequest;
import com.enesincekara.departmetservice.dto.response.DepartmentResponse;
import com.enesincekara.departmetservice.exception.DepartmentAlreadyException;
import com.enesincekara.departmetservice.exception.DepartmentNotFoundException;
import com.enesincekara.departmetservice.mapper.DepartmentMapper;
import com.enesincekara.departmetservice.repository.DepartmentRepository;
import com.enesincekara.departmetservice.service.DepartmentService;
import org.springframework.stereotype.Service;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository, DepartmentMapper departmentMapper) {
        this.departmentRepository = departmentRepository;
        this.departmentMapper = departmentMapper;
    }

    @Override
    public DepartmentResponse createDepartment(DepartmentCreateRequest request) {
        boolean exists = departmentRepository.existsByDepartmentName(request.departmentName());

        if (exists) {
            throw new DepartmentAlreadyException("Department already exists with name : " + request.departmentName());
        }

        try {
            var department = departmentMapper.toDepartment(request);
            var savedDepartment = departmentRepository.save(department);
            return departmentMapper.toDepartmentResponse(savedDepartment);

        } catch (Exception e) {
            throw new IllegalArgumentException("Error occurred while creating department: " + e.getMessage());
        }

    }

    @Override
    public DepartmentResponse getDepartment(String departmentCode) {
        var department = departmentRepository.getByDepartmentCode(departmentCode).orElseThrow(()->
                new DepartmentNotFoundException("Department not found with code: " + departmentCode));
        return departmentMapper.toDepartmentResponse(department);

    }


}
