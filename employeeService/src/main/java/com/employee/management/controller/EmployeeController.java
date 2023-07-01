package com.employee.management.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.employee.management.dto.EmployeeResponse;
import com.employee.management.entity.Employee;
import com.employee.management.service.EmployeeService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
	
	@Autowired
	private EmployeeService employeeService;

	@GetMapping("/all")
	public ResponseEntity<List<Employee>> getEmployees(){
		List<Employee> employees = employeeService.getAllEmployees();
		return ResponseEntity.ok(employees);
	}
    @GetMapping("/find/{id}")
    @CircuitBreaker(name = "fallbackMechanismEmpService", fallbackMethod = "fallbackEmployeeService")
    public ResponseEntity<EmployeeResponse> getEmployeeById(@PathVariable Long id) {
    	EmployeeResponse emp = employeeService.getEmployeeById(id);
    	if(emp!=null)
    		return ResponseEntity.ok(emp);
    	else
    		return ResponseEntity.notFound().build();
    }
    
    @PostMapping("/save")
    public ResponseEntity<EmployeeResponse> saveEmployee(@RequestBody Employee employee) {
        EmployeeResponse savedEmployee = employeeService.saveEmployee(employee);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEmployee);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<EmployeeResponse> updateEmployee(@PathVariable Long id, @RequestBody Employee employee) {
        EmployeeResponse updatedEmployee = employeeService.updateEmployee(id, employee);
        if (updatedEmployee != null) {
            return ResponseEntity.ok(updatedEmployee);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable Long id) {
        boolean isDeleted = employeeService.deleteEmployee(id);
        if (isDeleted) {
            return ResponseEntity.ok("Employee with ID: "+id+" deleted permanently");
        }else {
            return ResponseEntity.notFound().build();
        }
    }
    
    private EmployeeResponse fallbackEmployeeService(Throwable throwable) {
    	EmployeeResponse response = new EmployeeResponse();
    	response.setMessage("Can't Process request! Please try after 5 mins");
        return response;
    }
}

