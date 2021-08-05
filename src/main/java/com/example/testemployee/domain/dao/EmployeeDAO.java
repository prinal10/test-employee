package com.example.testemployee.domain.dao;

import com.example.testemployee.domain.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeDAO extends JpaRepository<Employee, Long> {
}
