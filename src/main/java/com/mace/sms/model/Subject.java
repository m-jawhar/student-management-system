package com.mace.sms.model;

import java.io.Serial;
import java.io.Serializable;

/**
 * Represents a subject/course in a semester with marks and credits.
 * Follows Mar Athanasius College of Engineering 2024 regulations.
 */
public class Subject implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String name;
    private double marks; // Out of 100
    private int credits;

    /**
     * Creates a new Subject with the given details.
     *
     * @param name    Subject name
     * @param marks   Marks obtained (0-100)
     * @param credits Credit hours for this subject
     */
    public Subject(String name, double marks, int credits) {
        if (marks < 0 || marks > 100) {
            throw new IllegalArgumentException("Marks must be between 0 and 100");
        }
        if (credits <= 0) {
            throw new IllegalArgumentException("Credits must be positive");
        }
        this.name = name;
        this.marks = marks;
        this.credits = credits;
    }

    // Getters
    public String getName() {
        return name;
    }

    public double getMarks() {
        return marks;
    }

    public int getCredits() {
        return credits;
    }

    // Setters (for updates)
    public void setMarks(double marks) {
        if (marks < 0 || marks > 100) {
            throw new IllegalArgumentException("Marks must be between 0 and 100");
        }
        this.marks = marks;
    }

    /**
     * Calculates the grade letter based on marks percentage.
     *
     * @return Grade letter (S, A+, A, B+, B, C+, C, D, P, F)
     */
    public String getGradeLetter() {
        if (marks >= 90)
            return "S";
        if (marks >= 85)
            return "A+";
        if (marks >= 80)
            return "A";
        if (marks >= 75)
            return "B+";
        if (marks >= 70)
            return "B";
        if (marks >= 65)
            return "C+";
        if (marks >= 60)
            return "C";
        if (marks >= 55)
            return "D";
        if (marks >= 50)
            return "P";
        return "F";
    }

    /**
     * Calculates the grade point based on marks.
     *
     * @return Grade point (0.0 - 10.0)
     */
    public double getGradePoint() {
        if (marks >= 90)
            return 10.0;
        if (marks >= 85)
            return 9.0;
        if (marks >= 80)
            return 8.5;
        if (marks >= 75)
            return 8.0;
        if (marks >= 70)
            return 7.5;
        if (marks >= 65)
            return 7.0;
        if (marks >= 60)
            return 6.5;
        if (marks >= 55)
            return 6.0;
        if (marks >= 50)
            return 5.5;
        return 0.0; // F grade
    }

    /**
     * Calculates credit points (credits × grade point) for this subject.
     *
     * @return Credit points
     */
    public double getCreditPoints() {
        return credits * getGradePoint();
    }

    @Override
    public String toString() {
        return "%-30s | %6.2f | %2d | %2s | %4.1f".formatted(
                name, marks, credits, getGradeLetter(), getGradePoint());
    }
}
