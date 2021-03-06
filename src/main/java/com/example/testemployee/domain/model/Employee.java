package com.example.testemployee.domain.model;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Schema(name = "Employee json with all employee info.")
public class Employee {

    @Id
    @Schema(
            title = "Employee Id",
            description = "Employee identification number as per the database.",
            example = "1"
    )
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotEmpty
    @Schema(
            title = "Employee Name",
            description = "Employee name to be saved in the employee database.",
            example = "Elon Musk."
    )
    private String name;

    public Employee(String name) {
        this.name = name;
    }
}
