package com.serkan.GraphQL_Project.exception;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.graphql.execution.DataFetcherExceptionResolver;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class ExceptionResolver implements DataFetcherExceptionResolver {

    @Override
    public Mono<List<GraphQLError>> resolveException(Throwable exception, DataFetchingEnvironment environment) {

        if (exception instanceof ApplicationException ex) {
            GraphQLError graphqlError = GraphqlErrorBuilder.newError(environment)
                    .message(ex.getMessage()) // Take the message from our exception
                    .errorType(ex.getErrorType()) // Take the ErrorType from our exception
                    .extensions(ex.getExtensions()) // Take the extra information from our exception
                    .build();

            return Mono.just(List.of(graphqlError));
        }
        return Mono.empty();
    }
}