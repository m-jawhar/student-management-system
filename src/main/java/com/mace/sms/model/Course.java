package com.mace.sms.model;

import java.io.Serial;
import java.io.Serializable;

/**
 * Represents a course in the course catalog.
 * Courses are templates that can be assigned to students.
 */
public class Course implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String courseCode;
    private String name;
    private int credits;
    private String instructor;

    /**
     * Creates a new Course for the catalog.
     *
     * @param courseCode Course code (e.g., CS101, MATH201)
     * @param name       Course name
     * @param credits    Credit hours for this course
     * @param instructor Instructor/Professor name
     */
    public Course(String courseCode, String name, int credits, String instructor) {
        if (courseCode == null || courseCode.trim().isEmpty()) {
            throw new IllegalArgumentException("Course code cannot be empty");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Course name cannot be empty");
        }
        if (credits <= 0) {
            throw new IllegalArgumentException("Credits must be positive");
        }
        if (instructor == null || instructor.trim().isEmpty()) {
            throw new IllegalArgumentException("Instructor name cannot be empty");
        }
        this.courseCode = courseCode;
        this.name = name;
        this.credits = credits;
        this.instructor = instructor;
    }

    // Getters
    public String getCourseCode() {
        return courseCode;
    }

    public String getName() {
        return name;
    }

    public int getCredits() {
        return credits;
    }

    public String getInstructor() {
        return instructor;
    }

    // Setters
    public void setInstructor(String instructor) {
        if (instructor == null || instructor.trim().isEmpty()) {
            throw new IllegalArgumentException("Instructor name cannot be empty");
        }
        this.instructor = instructor;
    }

    @Override
    public String toString() {
        return "%-10s | %-30s | %2d | %-20s".formatted(
                courseCode, name, credits, instructor);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Course course = (Course) obj;
        return courseCode.equals(course.courseCode);
    }

    @Override
    public int hashCode() {
        return courseCode.hashCode();
    }
}
