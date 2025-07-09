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
                    .message(ex.getMessage()) //
                    .errorType(ex.getErrorType())
                    .extensions(ex.getExtensions())
                    .build();

            return Mono.just(List.of(graphqlError));
        }
        return Mono.empty();
    }
}