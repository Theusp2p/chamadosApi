package com.chamados.API.services;

import com.chamados.API.repositories.DepartmentRepository;
import com.chamados.API.entities.Department;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public Department findById(int id) {
        return departmentRepository.findById(id).orElse(null);
    }

    public List<Department> findAll() {
        return departmentRepository.findAll();
    }
}
