package com.department.management.service;

import com.department.management.entity.Department;
import com.department.management.repository.DepartmentRepository;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    public List<Department> getAllDepartments() {
    	 log.info("Fetching all departments");
        return departmentRepository.findAll();
    }

    public Department getDepartmentById(Long id) {
    	log.info("Fetching department by id: {}", id);
        return departmentRepository.findById(id).orElse(null);
    }

    public Department saveDepartment(Department department) {
    	log.info("Saving department: {}", department);
        return departmentRepository.save(department);
    }

    public Department updateDepartment(Long id, Department department) {
    	log.info("Updating department with id: {}, department: {}", id, department);
        Optional<Department> existingDepartment = departmentRepository.findById(id);
        if (existingDepartment.isPresent()) {
            Department updatedDepartment = existingDepartment.get();
            updatedDepartment.setName(department.getName());
            return departmentRepository.save(updatedDepartment);
        } else {
            return null;
        }
    }

    public boolean deleteDepartment(Long id) {
    	log.info("Deleting department with id: {}", id);
        Optional<Department> department = departmentRepository.findById(id);
        if (department.isPresent()) {
            departmentRepository.delete(department.get());
            return true;
        } else {
            return false;
        }
    }
}
