package com.mace.sms.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Represents a student with personal information and academic records.
 * Tracks multiple semesters and calculates CGPA.
 */
public class Student implements Serializable, Comparable<Student> {
    @Serial
    private static final long serialVersionUID = 1L;

    private String rollNo;
    private String name;
    private int currentSemester; // 1-8
    private List<Semester> semesters;
    private double cgpa; // Calculated

    /**
     * Creates a new Student.
     *
     * @param rollNo          Student's roll number
     * @param name            Student's full name
     * @param currentSemester Current semester (1-8)
     */
    public Student(String rollNo, String name, int currentSemester) {
        if (rollNo == null || rollNo.trim().isEmpty()) {
            throw new IllegalArgumentException("Roll number cannot be empty");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        if (currentSemester < 1 || currentSemester > 8) {
            throw new IllegalArgumentException("Current semester must be between 1 and 8");
        }
        this.rollNo = rollNo;
        this.name = name;
        this.currentSemester = currentSemester;
        this.semesters = new ArrayList<>();
        this.cgpa = 0.0;
    }

    // Getters
    public String getRollNo() {
        return rollNo;
    }

    public String getName() {
        return name;
    }

    public int getCurrentSemester() {
        return currentSemester;
    }

    public List<Semester> getSemesters() {
        return Collections.unmodifiableList(semesters);
    }

    public double getCgpa() {
        return cgpa;
    }

    // Setters
    public void setCurrentSemester(int currentSemester) {
        if (currentSemester < 1 || currentSemester > 8) {
            throw new IllegalArgumentException("Current semester must be between 1 and 8");
        }
        this.currentSemester = currentSemester;
    }

    /**
     * Calculates CGPA across all completed semesters.
     * CGPA = Σ(all credits × gradePoints) / Σ(all credits)
     *
     * @return Calculated CGPA
     */
    public double calculateCGPA() {
        if (semesters.isEmpty()) {
            return 0.0;
        }

        double totalCreditPoints = semesters.stream()
                .mapToDouble(Semester::getTotalCreditPoints)
                .sum();

        int totalCredits = semesters.stream()
                .mapToInt(Semester::getTotalCredits)
                .sum();

        this.cgpa = totalCredits > 0 ? totalCreditPoints / totalCredits : 0.0;
        return this.cgpa;
    }

    /**
     * Adds a new semester to the student's record.
     *
     * @param semester Semester to add
     * @throws IllegalArgumentException if semester already exists
     */
    public void addSemester(Semester semester) {
        // Check for duplicate semester
        boolean exists = semesters.stream()
                .anyMatch(s -> s.getSemesterNumber() == semester.getSemesterNumber());

        if (exists) {
            throw new IllegalArgumentException(
                    "Semester " + semester.getSemesterNumber() + " already exists for this student");
        }

        semesters.add(semester);
        calculateCGPA();
    }

    /**
     * Gets a semester by its number.
     *
     * @param semesterNumber Semester number (1-8)
     * @return Optional containing the semester if found
     */
    public Optional<Semester> getSemester(int semesterNumber) {
        return semesters.stream()
                .filter(s -> s.getSemesterNumber() == semesterNumber)
                .findFirst();
    }

    /**
     * Updates marks for a subject in a specific semester.
     *
     * @param semesterNumber Semester number
     * @param subjectName    Subject name
     * @param newMarks       New marks value
     * @return true if updated successfully, false otherwise
     */
    public boolean updateSubjectMarks(int semesterNumber, String subjectName, double newMarks) {
        Optional<Semester> semester = getSemester(semesterNumber);
        if (semester.isPresent()) {
            boolean updated = semester.get().updateSubjectMarks(subjectName, newMarks);
            if (updated) {
                calculateCGPA();
            }
            return updated;
        }
        return false;
    }

    /**
     * Gets the total number of credits completed across all semesters.
     *
     * @return Total credits
     */
    public int getTotalCredits() {
        return semesters.stream()
                .mapToInt(Semester::getTotalCredits)
                .sum();
    }

    /**
     * Gets the classification based on CGPA.
     *
     * @return Classification string
     */
    public String getClassification() {
        if (cgpa >= 8.0) {
            return "First Class with Distinction";
        } else if (cgpa >= 6.5) {
            return "First Class";
        } else if (cgpa >= 5.5) {
            return "Second Class";
        } else {
            return "Pass Class";
        }
    }

    /**
     * Gets the equivalent percentage (CGPA × 10).
     *
     * @return Equivalent percentage
     */
    public double getEquivalentPercentage() {
        return cgpa * 10;
    }

    @Override
    public String toString() {
        return "%-15s | %-30s | Sem: %d | CGPA: %.2f".formatted(
                rollNo, name, currentSemester, cgpa);
    }

    /**
     * Compares students by CGPA in descending order (for leaderboard).
     */
    @Override
    public int compareTo(Student other) {
        return Double.compare(other.cgpa, this.cgpa); // Descending order
    }
}
