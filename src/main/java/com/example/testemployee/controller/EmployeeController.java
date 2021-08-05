package com.example.testemployee.controller;

import com.example.testemployee.domain.model.Employee;
import com.example.testemployee.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@RestController
@Slf4j
@RequestMapping("/v1/employee")
public class EmployeeController implements com.example.testemployee.controller.swaggerdoc.EmployeeSwaggerDoc {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Override
    @GetMapping(value = "/{employeeId}")
    public Employee getEmployee(@PathVariable("employeeId") Long employeeId) {
        return employeeService.getEmployee(employeeId);
    }

    @Override
    @PostMapping
    public Employee addEmployee(@Valid @RequestBody Employee employee) {
        return employeeService.addEmployee(employee);
    }

    @Override
    @PutMapping(value = "/{employeeId}")
    public Employee updateEmployee(@PathVariable("employeeId") Long employeeId, @Valid @RequestBody Employee employee) {
        return employeeService.updateEmployee(employeeId, employee);
    }

    @Override
    @DeleteMapping(value = "/{employeeId}")
    public Employee deleteEmployee(@PathVariable("employeeId") Long employeeId) {
        return employeeService.deleteEmployee(employeeId);
    }

}
