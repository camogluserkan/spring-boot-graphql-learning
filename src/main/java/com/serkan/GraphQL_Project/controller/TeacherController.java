package com.serkan.GraphQL_Project.controller;

import com.serkan.GraphQL_Project.model.Course;
import com.serkan.GraphQL_Project.model.Teacher;
import com.serkan.GraphQL_Project.repository.DataRepository;
import org.springframework.graphql.data.method.annotation.BatchMapping; // To solve N+1 problem
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class TeacherController {

    private final DataRepository dataRepository;

    public TeacherController(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }


    // This method will be data bringer and it will call only 1 time, not N time.
    // So, we will solve "N+1 Problem" in here.
    @BatchMapping(typeName = "Course", field = "teacher")
    public Map<Course, Teacher> getTeachersForCourses(List<Course> courses) {
        System.out.println("Fetching teachers for " + courses.size() + " courses in a SINGLE BATCH!");

        // Gather all the teacher infos.
        List<String> teacherIds = courses.stream()
                .map(Course::getTeacherId)
                .distinct()
                .collect(Collectors.toList());

        // Bring all necessary teachers from `DataRepository` in a single time.
        List<Teacher> teachers = dataRepository.findAllTeachersByIds(teacherIds);

        // For fast access, creating a Map: teacherId -> Teacher object
        Map<String, Teacher> teacherMap = teachers.stream()
                .collect(Collectors.toMap(Teacher::getId, teacher -> teacher));

        // Every course will match its teacher and the map are going to return.
        return courses.stream()
                .collect(Collectors.toMap(
                        course -> course, // KEy
                        course -> teacherMap.get(course.getTeacherId()), // Value: The teacher found in the map
                        (existingValue, newValue) -> existingValue // Merge Function
                ));
    }
}