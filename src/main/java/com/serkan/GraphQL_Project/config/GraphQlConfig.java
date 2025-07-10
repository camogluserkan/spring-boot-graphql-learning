package com.serkan.GraphQL_Project.config;

import com.serkan.GraphQL_Project.directive.IsOwnerOrAdminDirectiveWiring;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

import graphql.scalars.ExtendedScalars;  // for scalar types
import graphql.schema.GraphQLScalarType;

import com.serkan.GraphQL_Project.model.Course;
import com.serkan.GraphQL_Project.model.Student;
import com.serkan.GraphQL_Project.model.Teacher;
import graphql.schema.GraphQLObjectType;
import graphql.schema.TypeResolver;


@Configuration
public class GraphQlConfig {

    private final IsOwnerOrAdminDirectiveWiring isOwnerOrAdminDirectiveWiring;

    public GraphQlConfig(IsOwnerOrAdminDirectiveWiring isOwnerOrAdminDirectiveWiring) {
        this.isOwnerOrAdminDirectiveWiring = isOwnerOrAdminDirectiveWiring;
    }

    // New bean: we are declaring a standard date bean that the extended-scalars library provide us
    @Bean
    public GraphQLScalarType dateScalar() {
        return ExtendedScalars.Date;
    }

    /**
     * This Bean will save custom directives to the GraphQL engine
     */
    @Bean
    public RuntimeWiringConfigurer runtimeWiringConfigurer(GraphQLScalarType dateScalar) {
        return wiringBuilder -> wiringBuilder
                .directive("isOwnerOrAdmin", this.isOwnerOrAdminDirectiveWiring)
                .scalar(dateScalar); // give new scalar type to the wiring too.
    }

    // Implementing my own typeResolver because had some problems with imports and dependencies..
    @Bean
    public TypeResolver typeResolver() {
        return env -> {
            // 1. Take the object (it could be Student-Teacher-Course
            Object javaObject = env.getObject();

            // Check every object with instanceof and return the true one.
            if (javaObject instanceof Student)
                return env.getSchema().getObjectType("Student");
             else if (javaObject instanceof Teacher)
                return env.getSchema().getObjectType("Teacher");
             else if (javaObject instanceof Course)
                return env.getSchema().getObjectType("Course");

            // If there is no match, return null -- No expect to come here.
            return null;
        };
    }
}