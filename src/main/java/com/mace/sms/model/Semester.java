package com.mace.sms.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a semester containing multiple subjects.
 * Calculates SGPA (Semester Grade Point Average) for the semester.
 */
public class Semester implements Serializable {
    private static final long serialVersionUID = 1L;

    private int semesterNumber; // 1-8
    private List<Subject> subjects;
    private double sgpa; // Calculated

    /**
     * Creates a new Semester.
     *
     * @param semesterNumber Semester number (1-8)
     */
    public Semester(int semesterNumber) {
        if (semesterNumber < 1 || semesterNumber > 8) {
            throw new IllegalArgumentException("Semester number must be between 1 and 8");
        }
        this.semesterNumber = semesterNumber;
        this.subjects = new ArrayList<>();
        this.sgpa = 0.0;
    }

    // Getters
    public int getSemesterNumber() {
        return semesterNumber;
    }

    public List<Subject> getSubjects() {
        return Collections.unmodifiableList(subjects);
    }

    public double getSgpa() {
        return sgpa;
    }

    /**
     * Calculates SGPA for this semester.
     * SGPA = Σ(credits × gradePoints) / Σ(credits)
     *
     * @return Calculated SGPA
     */
    public double calculateSGPA() {
        if (subjects.isEmpty()) {
            return 0.0;
        }

        double totalCreditPoints = subjects.stream()
                .mapToDouble(Subject::getCreditPoints)
                .sum();

        int totalCredits = subjects.stream()
                .mapToInt(Subject::getCredits)
                .sum();

        this.sgpa = totalCredits > 0 ? totalCreditPoints / totalCredits : 0.0;
        return this.sgpa;
    }

    /**
     * Gets the total credits for this semester.
     *
     * @return Total credits
     */
    public int getTotalCredits() {
        return subjects.stream()
                .mapToInt(Subject::getCredits)
                .sum();
    }

    /**
     * Gets the total credit points for this semester.
     *
     * @return Total credit points
     */
    public double getTotalCreditPoints() {
        return subjects.stream()
                .mapToDouble(Subject::getCreditPoints)
                .sum();
    }

    /**
     * Adds a subject to the semester and recalculates SGPA.
     *
     * @param subject Subject to add
     */
    public void addSubject(Subject subject) {
        subjects.add(subject);
        calculateSGPA();
    }

    /**
     * Updates an existing subject's marks and recalculates SGPA.
     *
     * @param subjectName Name of the subject to update
     * @param newMarks    New marks value
     * @return true if subject was found and updated, false otherwise
     */
    public boolean updateSubjectMarks(String subjectName, double newMarks) {
        for (Subject subject : subjects) {
            if (subject.getName().equalsIgnoreCase(subjectName)) {
                subject.setMarks(newMarks);
                calculateSGPA();
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format("Semester %d: SGPA = %.2f (%d credits, %d subjects)",
                semesterNumber, sgpa, getTotalCredits(), subjects.size());
    }
}
