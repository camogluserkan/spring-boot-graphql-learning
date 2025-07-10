package com.serkan.GraphQL_Project.config;

import com.serkan.GraphQL_Project.directive.IsOwnerOrAdminDirectiveWiring;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

import graphql.scalars.ExtendedScalars;  // for scalar types
import graphql.schema.GraphQLScalarType;

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
}