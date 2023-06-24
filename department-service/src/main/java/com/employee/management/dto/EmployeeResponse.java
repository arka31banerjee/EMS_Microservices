package com.employee.management.dto;

import com.employee.management.entity.Employee;

import lombok.Data;
@Data
public class EmployeeResponse {
	private Employee employee;
	private Department department;
}
