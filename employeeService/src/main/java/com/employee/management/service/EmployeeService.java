package com.employee.management.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.employee.management.dto.Department;
import com.employee.management.dto.EmployeeResponse;
import com.employee.management.entity.Employee;
import com.employee.management.repository.EmployeeRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmployeeService {
	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${department.service.url}")
	private String DEPARTMENT_SERVICE_URL;

    @Autowired
    private EmployeeRepository employeeRepository;

    public List<Employee> getAllEmployees() {
        log.info("Fetching all employees");
        return employeeRepository.findAll();
    }

    public EmployeeResponse getEmployeeById(Long id) {
    	log.info("Fetching employee by id: {}", id);
    	EmployeeResponse response = new EmployeeResponse();
    	Employee employee = employeeRepository.findById(id).orElse(null);
    	if(employee==null) {
    		log.error("EMPLOYEE not present with id :{}",id);
    		return null;
    	}
    	response.setEmployee(employee);
    	response.setDepartment(departmentValidation(employee));
    	return response;
    }

	private Department departmentValidation(Employee employee) {
		ResponseEntity<Department> departmentResponse = restTemplate.getForEntity(DEPARTMENT_SERVICE_URL+employee.getDepartmentId(), Department.class);
    	return departmentResponse.getBody();
	}

    public EmployeeResponse saveEmployee(Employee employee) {
        log.info("Saving employee: {}", employee);
        EmployeeResponse response = new EmployeeResponse();
        response.setDepartment(departmentValidation(employee));
        if(response.getDepartment()!=null) {
        	response.setEmployee(employeeRepository.save(employee));
        	return response;
        }
        else
        	return null;
    }

    public EmployeeResponse updateEmployee(Long id, Employee employee) {
        log.info("Updating employee with id: {}, employee: {}", id, employee);
        Optional<Employee> existingEmployee = employeeRepository.findById(id);
        if (existingEmployee.isPresent()) {
            Employee updatedEmployee = existingEmployee.get();
            EmployeeResponse response = new EmployeeResponse();
            response.setDepartment(departmentValidation(employee));
            updatedEmployee.setName(employee.getName());
            updatedEmployee.setEmail(employee.getEmail());
            Long departmentId = response.getDepartment().getId();
            if(departmentId==null) {
            	log.error("department not valid with id :{}",employee.getDepartmentId());
            	return null;
            }
            updatedEmployee.setDepartmentId(departmentId);
           response.setEmployee(employeeRepository.save(updatedEmployee));
           return response;
        } else {
        	log.error("No such Employee to update with ID : {}",id);
            return null;
        }
    }

    public boolean deleteEmployee(Long id) {
        log.info("Deleting employee with id: {}", id);
        Optional<Employee> employee = employeeRepository.findById(id);
        if (employee.isPresent()) {
            employeeRepository.delete(employee.get());
            return true;
        } else {
        	log.error("No such Employee to delete with ID : {}",id);
            return false;
        }
    }
}
