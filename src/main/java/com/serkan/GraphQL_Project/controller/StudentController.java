package com.serkan.GraphQL_Project.controller;

import com.serkan.GraphQL_Project.model.Course;
import com.serkan.GraphQL_Project.model.Student;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class StudentController {

    /*
    @PreAuthorize("hasRole('ADMIN')")
    @SchemaMapping(typeName = "Course", field = "students")
    public List<Student> getStudentsForCourse(Course course) {
        System.out.println("Fetching students for course '" + course.getName() + "' with ADMIN role via StudentController.");
        return course.getStudents();
    }
     */

}