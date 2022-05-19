package com.govtech.assignment.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "users")
@Entity
@Getter
@Setter
public class User {
    @Id
    private String name;

    @Column(nullable = false)
    private Double salary;
}
