package com.serkan.GraphQL_Project.model;

import lombok.AllArgsConstructor;
import lombok.Data;

// this class exist to represent subscription event object
@Data
@AllArgsConstructor
public class CourseEvent {
    private MutationType mutation;
    private Course course;
}