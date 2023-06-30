package com.api.gateway.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallbackController {

    @GetMapping("/employee/fallback")
    public String fallbackForEmployeeService() {
        return "Employee Service :: SERVER DOWN";
    }

    @GetMapping("/department/fallback")
    public String fallbackForDepartmentService() {
        return "Department Service :: SERVER DOWN";
    }
}
