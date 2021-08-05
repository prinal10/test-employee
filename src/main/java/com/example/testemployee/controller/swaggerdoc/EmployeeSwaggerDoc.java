package com.example.testemployee.controller.swaggerdoc;

import com.example.testemployee.domain.model.Employee;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Tag(name = "Employee API", description = "Employee API for getting and adding employee info.")
@Validated
public interface EmployeeSwaggerDoc {

    @Operation(summary = "Get employee info",
            description = "API to retrieve employee info based on the employee id.")
    Employee getEmployee(Long employeeId);

    @Operation(summary = "Add employee info",
            description = "API to add employee info.")
    Employee addEmployee(@Valid Employee employee);

    @Operation(summary = "Update employee info",
            description = "API to update employee info based on the employee id.")
    Employee updateEmployee(Long employeeId, @Valid Employee employee);

    @Operation(summary = "Delete employee info",
            description = "API to delete employee info based on the employee id.")
    Employee deleteEmployee(Long employeeId);
}
