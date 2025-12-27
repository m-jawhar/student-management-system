package com.mace.sms;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mace.sms.model.Semester;
import com.mace.sms.model.Student;

/**
 * Central manager for all student operations.
 * Handles data storage, grading logic, and student management.
 */
public class StudentManager {
    private static final String DATA_FILE = "data/students.json";
    private final Map<String, Student> students;
    private final Gson gson;

    public StudentManager() {
        this.students = new HashMap<>();
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        loadStudents();
    }

    // ==================== STUDENT OPERATIONS ====================

    /**
     * Adds a new student to the system.
     */
    public void addStudent(Student student) {
        if (students.containsKey(student.getRollNo())) {
            throw new IllegalArgumentException("Student with roll number " + student.getRollNo() + " already exists");
        }
        students.put(student.getRollNo(), student);
    }

    /**
     * Adds a semester to an existing student.
     */
    public boolean addSemesterToStudent(String rollNo, Semester semester) {
        Student student = students.get(rollNo);
        if (student == null) {
            return false;
        }
        student.addSemester(semester);
        return true;
    }

    /**
     * Gets a student by roll number.
     */
    public Optional<Student> getStudent(String rollNo) {
        return Optional.ofNullable(students.get(rollNo));
    }

    /**
     * Gets all students.
     */
    public Collection<Student> getAllStudents() {
        return Collections.unmodifiableCollection(students.values());
    }

    /**
     * Deletes a student by roll number.
     */
    public boolean deleteStudent(String rollNo) {
        return students.remove(rollNo) != null;
    }

    /**
     * Updates marks for a subject in a specific semester.
     */
    public boolean updateSubjectMarks(String rollNo, int semesterNumber, String subjectName, double newMarks) {
        Student student = students.get(rollNo);
        if (student == null) {
            return false;
        }
        return student.updateSubjectMarks(semesterNumber, subjectName, newMarks);
    }

    /**
     * Searches for students by name (case-insensitive partial match).
     */
    public List<Student> searchByName(String searchTerm) {
        String lowerSearchTerm = searchTerm.toLowerCase();
        return students.values().stream()
                .filter(s -> s.getName().toLowerCase().contains(lowerSearchTerm))
                .sorted()
                .collect(Collectors.toList());
    }

    // ==================== GRADING OPERATIONS ====================

    /**
     * Gets grade letter based on marks.
     */
    public String getGradeLetter(double marks) {
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
     * Gets grade point based on marks.
     */
    public double getGradePoint(double marks) {
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
        return 0.0;
    }

    /**
     * Gets the classification based on CGPA.
     */
    public String getClassification(double cgpa) {
        if (cgpa >= 8.0)
            return "First Class with Distinction";
        if (cgpa >= 6.5)
            return "First Class";
        if (cgpa >= 5.5)
            return "Second Class";
        return "Pass Class";
    }

    // ==================== STATISTICS & LEADERBOARD ====================

    /**
     * Gets the leaderboard (students sorted by CGPA).
     */
    public List<Student> getLeaderboard() {
        return students.values().stream()
                .sorted()
                .collect(Collectors.toList());
    }

    /**
     * Gets statistics about all students.
     */
    public Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();

        if (students.isEmpty()) {
            stats.put("totalStudents", 0);
            return stats;
        }

        List<Student> allStudents = new ArrayList<>(students.values());

        // Basic counts
        stats.put("totalStudents", allStudents.size());

        // CGPA statistics
        double avgCGPA = allStudents.stream()
                .mapToDouble(Student::getCgpa)
                .average()
                .orElse(0.0);
        stats.put("averageCGPA", avgCGPA);

        double maxCGPA = allStudents.stream()
                .mapToDouble(Student::getCgpa)
                .max()
                .orElse(0.0);
        stats.put("maxCGPA", maxCGPA);

        double minCGPA = allStudents.stream()
                .mapToDouble(Student::getCgpa)
                .min()
                .orElse(0.0);
        stats.put("minCGPA", minCGPA);

        // Classification distribution
        Map<String, Long> classDistribution = allStudents.stream()
                .collect(Collectors.groupingBy(
                        s -> getClassification(s.getCgpa()),
                        Collectors.counting()));
        stats.put("classificationDistribution", classDistribution);

        return stats;
    }

    // ==================== FILE OPERATIONS ====================

    /**
     * Saves all students to JSON file.
     */
    public void saveStudents() {
        try {
            File dataFile = new File(DATA_FILE);
            dataFile.getParentFile().mkdirs();

            try (FileWriter writer = new FileWriter(dataFile)) {
                gson.toJson(new ArrayList<>(students.values()), writer);
            }
        } catch (IOException e) {
            System.err.println("Error saving students: " + e.getMessage());
        }
    }

    /**
     * Loads students from JSON file.
     */
    private void loadStudents() {
        File dataFile = new File(DATA_FILE);
        if (!dataFile.exists()) {
            return;
        }

        try (FileReader reader = new FileReader(dataFile)) {
            Type studentListType = new TypeToken<ArrayList<Student>>() {
            }.getType();
            List<Student> loadedStudents = gson.fromJson(reader, studentListType);

            if (loadedStudents != null) {
                for (Student student : loadedStudents) {
                    student.calculateCGPA(); // Recalculate in case of changes
                    students.put(student.getRollNo(), student);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading students: " + e.getMessage());
        }
    }

    /**
     * Exports a student's complete report to a text file.
     */
    public boolean exportStudentReport(String rollNo) {
        Student student = students.get(rollNo);
        if (student == null) {
            return false;
        }

        try {
            File exportFile = new File("data/" + rollNo + "_report.txt");
            exportFile.getParentFile().mkdirs();

            try (FileWriter writer = new FileWriter(exportFile)) {
                writer.write("=".repeat(80) + "\n");
                writer.write("STUDENT ACADEMIC REPORT\n");
                writer.write("=".repeat(80) + "\n\n");
                writer.write("Roll Number: " + student.getRollNo() + "\n");
                writer.write("Name: " + student.getName() + "\n");
                writer.write("Current Semester: " + student.getCurrentSemester() + "\n");
                writer.write("CGPA: " + String.format("%.2f", student.getCgpa()) + "\n");
                writer.write("Classification: " + student.getClassification() + "\n");
                writer.write("\n" + "=".repeat(80) + "\n\n");

                for (Semester semester : student.getSemesters()) {
                    writer.write("Semester " + semester.getSemesterNumber() +
                            " - SGPA: " + String.format("%.2f", semester.getSgpa()) + "\n");
                    writer.write("-".repeat(80) + "\n");

                    semester.getSubjects().forEach(subject -> {
                        try {
                            writer.write(subject.toString() + "\n");
                        } catch (IOException e) {
                            // Ignore
                        }
                    });
                    writer.write("\n");
                }

                writer.write("=".repeat(80) + "\n");
                writer.write("End of Report\n");
            }

            return true;
        } catch (IOException e) {
            System.err.println("Error exporting report: " + e.getMessage());
            return false;
        }
    }
}
