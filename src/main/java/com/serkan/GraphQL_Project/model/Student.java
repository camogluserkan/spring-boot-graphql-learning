package com.serkan.GraphQL_Project.model;

import lombok.AllArgsConstructor;
import lombok.Data;

// I used Lombok to create necessary constructors and functions
@Data
@AllArgsConstructor
public class Student implements Searchable, Entity{
    private String id;
    private String name;
    private String surname;
    private StudentStatus status;

    @Override
    public String getDisplayName() { // override for searchable interface
        return this.name + " " + this.surname;
    }
}