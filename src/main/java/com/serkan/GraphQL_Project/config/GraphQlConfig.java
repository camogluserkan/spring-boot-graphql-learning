package com.serkan.GraphQL_Project.config;

import com.serkan.GraphQL_Project.directive.IsOwnerOrAdminDirectiveWiring;
import graphql.GraphQLError;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.DataFetcherExceptionResolver;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;
import reactor.core.publisher.Mono;

import java.util.List;

@Configuration
public class GraphQlConfig {

    private final IsOwnerOrAdminDirectiveWiring isOwnerOrAdminDirectiveWiring;

    public GraphQlConfig(IsOwnerOrAdminDirectiveWiring isOwnerOrAdminDirectiveWiring) {
        this.isOwnerOrAdminDirectiveWiring = isOwnerOrAdminDirectiveWiring;
    }

    /**
     * This Bean will save custom directives to the GraphQL engine
     */
    @Bean
    public RuntimeWiringConfigurer runtimeWiringConfigurer() {
        return wiringBuilder -> wiringBuilder
                .directive("isOwnerOrAdmin", this.isOwnerOrAdminDirectiveWiring);
    }
}