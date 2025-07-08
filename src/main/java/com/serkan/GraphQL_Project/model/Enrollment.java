package com.serkan.GraphQL_Project.model;

import lombok.*;


// This class is going to hold the grade that student take from that course.
@Getter
@Setter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Enrollment {
    private Student student;

    // These 2 annotations break infinity loop that I faced while developing the program.
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Course course;

    private double grade;
}