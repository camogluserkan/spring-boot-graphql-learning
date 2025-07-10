package com.serkan.GraphQL_Project.repository;

import com.serkan.GraphQL_Project.model.*;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository // To tell string this is DB
public class DataRepository {

    private final List<Course> courses = new ArrayList<>();
    private final List<Student> allStudents = new ArrayList<>();
    private final List<Teacher> allTeachers = new ArrayList<>();

    // To initialize db after start the program.
    @PostConstruct
    private void init() {
        Teacher teacher1 = new Teacher("T1", "Prof. Dr. Can");
        Teacher teacher2 = new Teacher("T2", "Doç. Dr. Zeynep");
        allTeachers.add(teacher1);
        allTeachers.add(teacher2);

        Student student1 = new Student("user", "Ali", "Veli", StudentStatus.ACTIVE);
        Student student2 = new Student("S2", "Ayşe", "Yılmaz", StudentStatus.ACTIVE);
        Student student3 = new Student("S3", "Mehmet", "Kaya", StudentStatus.GRADUATED);
        allStudents.addAll(List.of(student1, student2, student3));

        Course course1 = new Course("C1", "GraphQL'e Giriş", teacher1.getId(), new ArrayList<>(), LocalDate.of(2024,9,1));
        Course course2 = new Course("C2", "Spring Boot Derinlemesine", teacher2.getId(), new ArrayList<>(), LocalDate.of(2025,2,15));
        courses.addAll(List.of(course1, course2));

        Enrollment enrollment1 = new Enrollment(student1, course1, 95.5);
        Enrollment enrollment2 = new Enrollment(student2, course1, 88.0);
        course1.getEnrollments().addAll(List.of(enrollment1, enrollment2));

        Enrollment enrollment3 = new Enrollment(student3, course2, 91.0);
        course2.getEnrollments().add(enrollment3);
    }

    public List<Course> findAllCourses() {
        return courses;
    }

    public Optional<Course> findCourseById(String id) {
        return courses.stream().filter(c -> c.getId().equals(id)).findFirst();
    }

    public Optional<Student> findStudentById(String id) {
        return allStudents.stream().filter(s -> s.getId().equals(id)).findFirst();
    }

    public Optional<Teacher> findTeacherById(String id){
        return allTeachers.stream().
                filter(t -> t.getId().equals(id))
                .findFirst();
    }

    public List<Teacher> findAllTeachersByIds(List<String> ids) {
        // This log is going to show that batch operation is only calling 1 time.
        System.out.println("Batch fetching teachers for IDs: " + ids);
        return allTeachers.stream()
                .filter(teacher -> ids.contains(teacher.getId()))
                .collect(Collectors.toList());
    }

    // For CREATE Mutation
    public Course addCourse(Course course){
        this.courses.add(course);
        return course;
    }

    // For UPDATE Mutation
    public Course updateCourse(String courseId, String newName, String newTeacherId) {
        Course courseToUpdate = findCourseById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found for update!"));

        if (newName != null && !newName.isEmpty()) {
            courseToUpdate.setName(newName);
        }
        if (newTeacherId != null && !newTeacherId.isEmpty()) {
            courseToUpdate.setTeacherId(newTeacherId);
        }
        return courseToUpdate;
    }

    // For DELETE Mutation
    public Course deleteCourse(String id) {
        Course courseToRemove = findCourseById(id)
                .orElseThrow(() -> new IllegalArgumentException("Course not found for deletion!"));

        // Remove it from the list
        this.courses.remove(courseToRemove);

        // Return the removedCourse to inform program.
        return courseToRemove;
    }
}