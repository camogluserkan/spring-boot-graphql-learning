package com.serkan.GraphQL_Project.controller;

import org.springframework.stereotype.Controller;
import com.serkan.GraphQL_Project.model.Enrollment;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.security.access.prepost.PreAuthorize;

@Controller
public class EnrollmentController {
    // private final StudentService studentService; // a custom directive demo thought


    // For now, we do not need this function because we customized our directive.
    /*
    // In PreAuthorize part, this function only works either you have to be ADMIN or you must be the student whose grade you want.
    @SchemaMapping(typeName = "Enrollment", field = "grade")
    @PreAuthorize("hasRole('ADMIN') or #enrollment.student.id == authentication.name or studentService.is(en)")
    public Double getGrade(Enrollment enrollment){
        System.out.println("Fetching grade for student" + enrollment.getStudent().getName());
        return enrollment.getGrade(); // return the grade you wanted.
    }
     */
}