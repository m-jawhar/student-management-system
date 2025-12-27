package com.mace.sms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

import com.mace.sms.model.Semester;
import com.mace.sms.model.Student;
import com.mace.sms.model.Subject;
import com.mace.sms.util.DisplayUtil;
import com.mace.sms.util.InputValidator;

/**
 * Main application class for Student Management System.
 * Provides a menu-driven console interface for managing student records.
 */
public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static StudentManager manager;

    public static void main(String[] args) {
        manager = new StudentManager();

        System.out.println("\n" + "═".repeat(79));
        System.out.println("      Welcome to Student Management System - MACE 2024");
        System.out.println("═".repeat(79));
        System.out.println("Loaded " + manager.getAllStudents().size() + " students.\n");

        boolean running = true;
        while (running) {
            try {
                DisplayUtil.displayMenu();
                int choice = InputValidator.readInt(scanner, "Enter your choice (1-8): ");

                switch (choice) {
                    case 1 -> addStudentOrSemester();
                    case 2 -> displayAllStudents();
                    case 3 -> viewStudentReport();
                    case 4 -> updateSemesterMarks();
                    case 5 -> deleteStudent();
                    case 6 -> showStatistics();
                    case 7 -> showLeaderboard();
                    case 8 -> {
                        saveAndExit();
                        running = false;
                    }
                    default -> System.out.println("❌ Invalid choice. Please enter 1-8.");
                }
            } catch (Exception e) {
                System.out.println("❌ Error: " + e.getMessage());
            }
        }
        scanner.close();
    }

    private static void addStudentOrSemester() {
        System.out.println("\n" + "═".repeat(79));
        System.out.println("                    ADD STUDENT / SEMESTER DATA");
        System.out.println("═".repeat(79));
        System.out.println("1. Add New Student");
        System.out.println("2. Add Semester to Existing Student");
        System.out.println("═".repeat(79));

        int choice = InputValidator.readInt(scanner, "Enter choice (1-2): ");

        if (choice == 1) {
            addNewStudent();
        } else if (choice == 2) {
            addSemesterToStudent();
        }
    }

    private static void addNewStudent() {
        try {
            String rollNo = InputValidator.readRollNumber(scanner, "Enter Roll Number: ");

            if (manager.getStudent(rollNo).isPresent()) {
                System.out.println("❌ Student with roll number " + rollNo + " already exists!");
                return;
            }

            String name = InputValidator.readNonEmptyString(scanner, "Enter Student Name: ");
            int currentSem = InputValidator.readSemester(scanner, "Enter Current Semester (1-8): ");

            Student student = new Student(rollNo, name, currentSem);

            boolean addData = InputValidator.readConfirmation(scanner, "Add semester data now?");
            if (addData) {
                List<Semester> semesters = collectSemesterData();
                semesters.forEach(student::addSemester);
            }

            manager.addStudent(student);
            System.out.println("✅ Student added! Roll: " + rollNo + ", CGPA: " +
                    "%.2f".formatted(student.getCgpa()));
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    private static void addSemesterToStudent() {
        try {
            String rollNo = InputValidator.readRollNumber(scanner, "Enter Roll Number: ");

            Optional<Student> studentOpt = manager.getStudent(rollNo);
            if (studentOpt.isEmpty()) {
                System.out.println("❌ Student not found: " + rollNo);
                return;
            }

            Student student = studentOpt.get();
            int semNum = InputValidator.readSemester(scanner, "Enter Semester Number (1-8): ");

            if (student.getSemester(semNum).isPresent()) {
                System.out.println("❌ Semester " + semNum + " already exists!");
                return;
            }

            Semester semester = collectOneSemester(semNum);
            manager.addSemesterToStudent(rollNo, semester);

            System.out.println("✅ Semester " + semNum + " added! New CGPA: " +
                    "%.2f".formatted(student.getCgpa()));
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    private static List<Semester> collectSemesterData() {
        List<Semester> semesters = new ArrayList<>();
        int count = InputValidator.readInt(scanner, "How many semesters to add? ");

        for (int i = 0; i < count; i++) {
            int semNum = InputValidator.readSemester(scanner, "Semester " + (i + 1) + " number: ");
            semesters.add(collectOneSemester(semNum));
        }
        return semesters;
    }

    private static Semester collectOneSemester(int semNum) {
        Semester semester = new Semester(semNum);
        int subjectCount = InputValidator.readInt(scanner, "Number of subjects: ");

        for (int i = 1; i <= subjectCount; i++) {
            System.out.println("\n--- Subject " + i + " ---");
            String name = InputValidator.readNonEmptyString(scanner, "Subject name: ");
            double marks = InputValidator.readMarks(scanner, "Marks (0-100): ");
            int credits = InputValidator.readCredits(scanner, "Credits: ");

            semester.addSubject(new Subject(name, marks, credits));
        }
        return semester;
    }

    private static void displayAllStudents() {
        Collection<Student> students = manager.getAllStudents();

        if (students.isEmpty()) {
            System.out.println("\n❌ No students found.");
            return;
        }

        DisplayUtil.displayAllStudents(students);
    }

    private static void viewStudentReport() {
        try {
            String rollNo = InputValidator.readRollNumber(scanner, "Enter Roll Number: ");

            Optional<Student> studentOpt = manager.getStudent(rollNo);
            if (studentOpt.isEmpty()) {
                System.out.println("❌ Student not found: " + rollNo);
                return;
            }

            DisplayUtil.displayStudentReport(studentOpt.get());

            boolean export = InputValidator.readConfirmation(scanner, "Export to file?");
            if (export) {
                if (manager.exportStudentReport(rollNo)) {
                    System.out.println("✅ Report exported to data/" + rollNo + "_report.txt");
                } else {
                    System.out.println("❌ Export failed");
                }
            }
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    private static void updateSemesterMarks() {
        try {
            String rollNo = InputValidator.readRollNumber(scanner, "Enter Roll Number: ");

            Optional<Student> studentOpt = manager.getStudent(rollNo);
            if (studentOpt.isEmpty()) {
                System.out.println("❌ Student not found: " + rollNo);
                return;
            }

            Student student = studentOpt.get();
            System.out.println("\nAvailable Semesters:");
            student.getSemesters().forEach(s -> System.out.println("  Semester " + s.getSemesterNumber() +
                    " - SGPA: " + "%.2f".formatted(s.getSgpa())));

            int semNum = InputValidator.readSemester(scanner, "Semester number: ");

            Optional<Semester> semesterOpt = student.getSemester(semNum);
            if (semesterOpt.isEmpty()) {
                System.out.println("❌ Semester " + semNum + " not found");
                return;
            }

            Semester semester = semesterOpt.get();
            System.out.println("\nSubjects:");
            semester.getSubjects().forEach(s -> System.out.println("  " + s.getName() + " - " + s.getMarks()));

            String subjectName = InputValidator.readNonEmptyString(scanner, "Subject name: ");
            double newMarks = InputValidator.readMarks(scanner, "New marks: ");

            if (manager.updateSubjectMarks(rollNo, semNum, subjectName, newMarks)) {
                System.out.println("✅ Marks updated! New CGPA: " +
                        "%.2f".formatted(student.getCgpa()));
            } else {
                System.out.println("❌ Subject not found");
            }
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    private static void deleteStudent() {
        try {
            String rollNo = InputValidator.readRollNumber(scanner, "Enter Roll Number: ");

            Optional<Student> studentOpt = manager.getStudent(rollNo);
            if (studentOpt.isEmpty()) {
                System.out.println("❌ Student not found: " + rollNo);
                return;
            }

            Student student = studentOpt.get();
            System.out.println("\nStudent: " + student.getName() + " (" + rollNo + ")");

            boolean confirm = InputValidator.readConfirmation(scanner, "Delete this student?");
            if (confirm) {
                manager.deleteStudent(rollNo);
                System.out.println("✅ Student deleted");
            } else {
                System.out.println("❌ Deletion cancelled");
            }
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    private static void showStatistics() {
        Map<String, Object> stats = manager.getStatistics();

        if ((int) stats.get("totalStudents") == 0) {
            System.out.println("\n❌ No students to analyze.");
            return;
        }

        System.out.println("\n" + "═".repeat(79));
        System.out.println("                         STATISTICS");
        System.out.println("═".repeat(79));
        System.out.println("Total Students: " + stats.get("totalStudents"));
        System.out.println("Average CGPA: " + "%.2f".formatted((double) stats.get("averageCGPA")));
        System.out.println("Highest CGPA: " + "%.2f".formatted((double) stats.get("maxCGPA")));
        System.out.println("Lowest CGPA: " + "%.2f".formatted((double) stats.get("minCGPA")));

        System.out.println("\nClassification Distribution:");
        @SuppressWarnings("unchecked")
        Map<String, Long> dist = (Map<String, Long>) stats.get("classificationDistribution");
        dist.forEach((classification, count) -> System.out.println("  " + classification + ": " + count));

        System.out.println("═".repeat(79));
    }

    private static void showLeaderboard() {
        List<Student> leaderboard = manager.getLeaderboard();

        if (leaderboard.isEmpty()) {
            System.out.println("\n❌ No students found.");
            return;
        }

        DisplayUtil.displayLeaderboard(leaderboard);
    }

    private static void saveAndExit() {
        System.out.println("\n💾 Saving data...");
        manager.saveStudents();
        System.out.println("✅ Data saved successfully!");
        System.out.println("\n👋 Thank you for using Student Management System!");
        System.out.println("═".repeat(79));
    }
}
