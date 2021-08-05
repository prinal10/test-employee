package com.example.testemployee.service;

import com.example.testemployee.domain.dao.EmployeeDAO;
import com.example.testemployee.domain.model.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmployeeService {

    private final EmployeeDAO employeeDAO;

    public EmployeeService(EmployeeDAO employeeDAO) {
        this.employeeDAO = employeeDAO;
    }

    public Employee getEmployee(Long employeeId) {
        return null;
    }

    public Employee deleteEmployee(Long employeeId) {
        return null;
    }

    public Employee addEmployee(Employee employee) {
        return null;
    }

    public Employee updateEmployee(Employee employee) {
        return null;
    }
}
