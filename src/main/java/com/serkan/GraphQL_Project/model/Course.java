package com.serkan.GraphQL_Project.model;

import lombok.*;

import java.util.List;
import java.time.LocalDate;

// I used Lombok to create necessary constructors and functions
@Getter
@Setter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Course implements Searchable{
    private String id;
    private String name;
    private String teacherId;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Enrollment> enrollments;

    private LocalDate creationDate;

    @Override
    public String getDisplayName(){
        return this.name;
    }
}