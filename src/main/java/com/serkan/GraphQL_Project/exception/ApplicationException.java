package com.serkan.GraphQL_Project.exception;

import org.springframework.graphql.execution.ErrorType;

import java.util.Collections;
import java.util.Map;

public class ApplicationException extends RuntimeException {

    private final ErrorType errorType;
    private final Map<String, Object> extensions;

    public ApplicationException(String message, ErrorType errorType, Map<String, Object> extensions) {
        super(message);
        this.errorType = errorType;
        this.extensions = extensions;
    }

    public ApplicationException(String message, ErrorType errorType) {
        this(message, errorType, Collections.emptyMap());
    }

    public ErrorType getErrorType() {
        return errorType;
    }

    public Map<String, Object> getExtensions() {
        return extensions;
    }
}