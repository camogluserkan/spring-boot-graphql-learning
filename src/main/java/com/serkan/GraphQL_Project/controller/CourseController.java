package com.serkan.GraphQL_Project.controller;

import com.serkan.GraphQL_Project.controller.input.CreateCourseInput;
import com.serkan.GraphQL_Project.controller.input.UpdateCourseInput;
import com.serkan.GraphQL_Project.model.*;
import com.serkan.GraphQL_Project.repository.DataRepository;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// Tell spring that this class is a Controller
@Controller
public class CourseController {
    private final DataRepository dataRepository;
    // Thread-Safe publisher (for real subscription operation)
    private final Sinks.Many<CourseEvent> courseEventSink;
    private final Flux<CourseEvent> courseEventFlux;

    // Spring is going to handle studentSink object and we are initializing in here.
    public CourseController(DataRepository dataRepository, Sinks.Many<CourseEvent> courseEventSink, Flux<CourseEvent> courseEventFlux) {
        this.dataRepository = dataRepository;
        this.courseEventSink = courseEventSink;
        this.courseEventFlux = courseEventFlux;
    }


    @PreAuthorize("hasRole('USER')") // at least user role needed to use the function.
    @QueryMapping // To match the function with the same named field in the query.
    public List<Course> allCourses() {
        return dataRepository.findAllCourses();
    }

    @PreAuthorize("hasRole('USER')")
    @QueryMapping
    public Course courseById(@Argument String id) {
        return dataRepository.findCourseById(id).orElse(null);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @MutationMapping
    public Course createCourse(@Argument CreateCourseInput input) {
        Teacher teacher = dataRepository.findTeacherById(input.teacherId())
                .orElseThrow(() -> new IllegalArgumentException("Teacher not found with ID: " + input.teacherId()));

        // Create the new course with random ID
        Course newCourse = new Course(
                UUID.randomUUID().toString(),
                input.name(),
                teacher.getId(),
                new ArrayList<>() // empty student list
        );
        dataRepository.addCourse(newCourse);

        // Make the event and publish it.
        CourseEvent event = new CourseEvent(MutationType.CREATED, newCourse);
        this.courseEventSink.tryEmitNext(event);

        return newCourse;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @MutationMapping
    public Course updateCourse(@Argument String id, @Argument UpdateCourseInput input) {
        Course updatedCourse = dataRepository.updateCourse(id, input.name(), input.teacherId());

        // Publish it
        CourseEvent event = new CourseEvent(MutationType.UPDATED, updatedCourse);
        this.courseEventSink.tryEmitNext(event);

        return updatedCourse;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @MutationMapping
    public Course deleteCourse(@Argument String id) {
        Course deletedCourse = dataRepository.deleteCourse(id); // Delete it in DB

        // Make a deleted event and PUBLISH it.
        CourseEvent event = new CourseEvent(MutationType.DELETED, deletedCourse);
        this.courseEventSink.tryEmitNext(event);

        return deletedCourse;
    }


    // entry point for "courseEvents" SUBSCRIPTION
    // when a client start subscription, this method will be call.
    @SubscriptionMapping
    public Flux<CourseEvent> courseEvents() {
        System.out.println(">>> New client subscribed to courseEvents!");
        return this.courseEventFlux;
    }
}












