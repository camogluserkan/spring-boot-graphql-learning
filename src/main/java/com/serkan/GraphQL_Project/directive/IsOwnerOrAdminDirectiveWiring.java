package com.serkan.GraphQL_Project.directive;

import com.serkan.GraphQL_Project.exception.ApplicationException;
import com.serkan.GraphQL_Project.model.Enrollment;
import graphql.ErrorClassification;
import graphql.schema.*;
import graphql.schema.idl.SchemaDirectiveWiring;
import graphql.schema.idl.SchemaDirectiveWiringEnvironment;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class IsOwnerOrAdminDirectiveWiring implements  SchemaDirectiveWiring{

    @Override
    public GraphQLFieldDefinition onField (SchemaDirectiveWiringEnvironment<GraphQLFieldDefinition> environment){
        // 1. Take the field and type that directive applied
        GraphQLFieldDefinition field = environment.getElement();
        GraphQLObjectType parentType = (GraphQLObjectType) environment.getFieldsContainer();

        // 2. Take the DataFetcher for that field
        DataFetcher<?> originalDataFetcher = environment.getCodeRegistry().getDataFetcher(parentType, field);

        // 3. Create a new DataFetcher that includes our security logic
        DataFetcher<?> authDataFetcher = (DataFetchingEnvironment dfe) -> {
            // a. Take the identity of the user (Authentication)
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null) {
                throw new ApplicationException("Access Denied: User is not authenticated.", ErrorType.UNAUTHORIZED);
            }

            // b. Has a ADMIN perm?
            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));

            if (isAdmin) // If the user is admin, execute the original DataFetcher and continue.
                return originalDataFetcher.get(dfe);

            // c. If user is not ADMIN, check that he/she is owner. Take the upper object (Enrollment in here).
            Enrollment enrollment = dfe.getSource();
            if (enrollment != null && enrollment.getStudent().getId().equals(authentication.getName())) {
                // If the user that enter with student ID is same, allow it to see that field.
                return originalDataFetcher.get(dfe);
            }

            // d. If all the conditions couldn't be satisfied, deny the access.
            throw new ApplicationException("Access Denied: User is not authenticated.", ErrorType.UNAUTHORIZED);
        };

        // 4. Tell the GraphQL: for this field, now use my new & safe DataFetcher
        environment.getCodeRegistry().dataFetcher(parentType, field, authDataFetcher);
        return field;
    }
}
