package com.example.testemployee.service;

import com.example.testemployee.domain.dao.EmployeeDAO;
import com.example.testemployee.domain.model.Employee;
import com.example.testemployee.exceptions.EmployeeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class EmployeeService {

    private final EmployeeDAO employeeDAO;

    public EmployeeService(EmployeeDAO employeeDAO) {
        this.employeeDAO = employeeDAO;
    }

    public Employee getEmployee(Long employeeId) {
        Optional<Employee> employee = employeeDAO.findById(employeeId);
        if (employee.isEmpty()) {
            throw new EmployeeException("Employee with id: " + employeeId + " doesn't exists.");
        }
        log.info("Employee found with employeeId:" + employeeId);
        return employee.get();
    }

    public Employee deleteEmployee(Long employeeId) {
        Employee employee = getEmployee(employeeId);
        employeeDAO.deleteById(employee.getId());
        return employee;

    }

    public Employee addEmployee(Employee employee) {
        log.info("adding employee: " + employee.getName());
        return employeeDAO.save(new Employee(employee.getName()));
    }

    public Employee updateEmployee(Long employeeId, Employee employee) {
        Employee existingEmployee = getEmployee(employeeId);
        existingEmployee.setName(employee.getName());
        log.info("updating employee: " + employee.getName());
        return employeeDAO.saveAndFlush(existingEmployee);
    }
}
