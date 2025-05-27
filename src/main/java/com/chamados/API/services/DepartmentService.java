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

    public void updateDepartment(Integer id, Department updatedDepartment) {
        Department department = departmentRepository.findById(id).orElse(null);
        assert department != null;
        department.setName(updatedDepartment.getName());
        departmentRepository.save(department);
    }

    public Object findAllDepartments() {
        return departmentRepository.findAll();
    }

    public void insertDepartment(Department department) {
        departmentRepository.save(department);
    }
}
