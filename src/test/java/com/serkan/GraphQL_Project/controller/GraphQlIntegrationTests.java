package com.serkan.GraphQL_Project.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.security.test.context.support.WithMockUser;
import com.serkan.GraphQL_Project.controller.input.CreateCourseInput;

import org.springframework.graphql.execution.ErrorType;
import static org.assertj.core.api.Assertions.assertThat;
import graphql.GraphQLError;

import org.springframework.graphql.ResponseError;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;

import java.util.Map;

@SpringBootTest
@AutoConfigureGraphQlTester
public class GraphQlIntegrationTests {

    @Autowired
    private GraphQlTester graphQlTester;

    @Test
    void contextLoads() {
        // Only to see is the test system works?
    }

    // A normal query test
    @Test
    @WithMockUser(username = "user", roles = {"USER"}) // Start the test with "user" role
    void shouldReturnCourseById() {
        // 1. Request
        // we are writing the query in .document()
        this.graphQlTester.document(
                    """
                    query {
                        courseById(id: "C1") {
                            id
                            name
                            teacher {
                                id
                                name
                            }
                        }
                    }
                    """
                )
                .execute() // 2. Execute it
                .path("data.courseById.name").entity(String.class).isEqualTo("GraphQL'e Giriş")
                .path("data.courseById.teacher.id").entity(String.class).isEqualTo("T1")
                .path("data.courseById.teacher.name").entity(String.class).isEqualTo("Prof. Dr. Can");

    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"}) // Start the test with "ADMIN" role
    void shouldCreateCourseWhenUserIsAdmin() {
        // 1. Request
        Map<String, Object> input = Map.of(
                "name", "Test ile Oluşturulan Kurs",
                "teacherId", "T1"
        );

        this.graphQlTester
                // Call the query from a file by name
                .documentName("createCourseMutation")
                // Giving the necessary 'input' by creating a Java object
                .variable("input", input)
                .execute() // 2. Execute
                // 3. Assert it
                .path("data.createCourse.name").entity(String.class).isEqualTo("Test ile Oluşturulan Kurs")
                .path("data.createCourse.teacher.name").entity(String.class).isEqualTo("Prof. Dr. Can")
                .path("errors").pathDoesNotExist(); // Verify that there is NO error block
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"}) // Start with user role
    void shouldFailToCreateCourseWhenUserIsNotAdmin() {

        Map<String, Object> inputMap = Map.of(
                "name", "Kullanıcının Oluşturmaya Çalıştığı Kurs",
                "teacherId", "T2"
        );

        this.graphQlTester
                .documentName("createCourseMutation")
                .variable("input", inputMap)
                .execute()
                .errors()
                .expect(error -> error.getErrorType() == ErrorType.FORBIDDEN)
                .expect(error -> "Access is denied".equals(error.getMessage()))
                .verify()
                .path("data.createCourse").pathDoesNotExist();
    }




}