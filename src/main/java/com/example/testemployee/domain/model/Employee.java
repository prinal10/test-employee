package com.example.testemployee.domain.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Employee {

    @Id
    private Long id;
    private String name;

    public Employee(String name) {
        this.name = name;
    }
}
