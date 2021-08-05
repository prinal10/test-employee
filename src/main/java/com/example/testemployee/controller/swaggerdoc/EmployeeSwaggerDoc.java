package com.example.testemployee.controller.swaggerdoc;

import com.example.testemployee.domain.model.Employee;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Tag(name = "Employee API", description = "Employee API for getting and adding employee info.")
@SecurityScheme(
        name = "basicAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "basic"
)
@Validated
public interface EmployeeSwaggerDoc {

    @Operation(summary = "Get employee info",
            description = "API to retrieve employee info based on the employee id.",
            security = {@SecurityRequirement(name = "basicAuth")})
    Employee getEmployee(Long employeeId);

    @Operation(summary = "Add employee info",
            description = "API to add employee info.", security = {@SecurityRequirement(name = "basicAuth")})
    Employee addEmployee(@Valid Employee employee);

    @Operation(summary = "Update employee info",
            description = "API to update employee info based on the employee id.",
            security = {@SecurityRequirement(name = "basicAuth")})
    Employee updateEmployee(Long employeeId, @Valid Employee employee);

    @Operation(summary = "Delete employee info",
            description = "API to delete employee info based on the employee id.",
            security = {@SecurityRequirement(name = "basicAuth")})
    Employee deleteEmployee(Long employeeId);
}
