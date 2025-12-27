package com.mace.sms.util;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.mace.sms.model.Semester;
import com.mace.sms.model.Student;
import com.mace.sms.model.Subject;

/**
 * Utility class for displaying formatted output to the console.
 * Provides methods for tabular display and ASCII visualizations.
 */
public class DisplayUtil {

    private static final String HORIZONTAL_LINE = "═══════════════════════════════════════════════════════════════════════════════";
    private static final String DASH_LINE = "───────────────────────────────────────────────────────────────────────────────";

    /**
     * Displays the main menu.
     */
    public static void displayMenu() {
        System.out.println("\n" + HORIZONTAL_LINE);
        System.out.println("           🎓 STUDENT MANAGEMENT SYSTEM - MACE 2024 🎓");
        System.out.println(HORIZONTAL_LINE);
        System.out.println("1. ➕ Add Student / Semester Data");
        System.out.println("2. 📋 Display All Students");
        System.out.println("3. 📊 View Student Report");
        System.out.println("4. ✏️  Update Semester Marks");
        System.out.println("5. 🗑️  Delete Student");
        System.out.println("6. 📈 Show Statistics");
        System.out.println("7. 🏆 Show Leaderboard");
        System.out.println("8. 💾 Save & Exit");
        System.out.println(HORIZONTAL_LINE);
    }

    /**
     * Displays all students in a tabular format.
     *
     * @param students Collection of students
     */
    public static void displayAllStudents(Collection<Student> students) {
        if (students.isEmpty()) {
            System.out.println("\n📭 No students found in the system.");
            return;
        }

        System.out.println("\n" + HORIZONTAL_LINE);
        System.out.println("                         ALL STUDENTS");
        System.out.println(HORIZONTAL_LINE);
        System.out.printf("%-15s | %-30s | %-10s | %-10s | %-10s%n",
                "Roll Number", "Name", "Semester", "CGPA", "Credits");
        System.out.println(DASH_LINE);

        for (Student student : students) {
            System.out.printf("%-15s | %-30s | %10d | %10.2f | %10d%n",
                    student.getRollNo(),
                    student.getName(),
                    student.getCurrentSemester(),
                    student.getCgpa(),
                    student.getTotalCredits());
        }
        System.out.println(HORIZONTAL_LINE);
        System.out.println("Total Students: " + students.size());
    }

    /**
     * Displays detailed report for a single student.
     *
     * @param student Student object
     */
    public static void displayStudentReport(Student student) {
        System.out.println("\n" + HORIZONTAL_LINE);
        System.out.println("                    STUDENT ACADEMIC REPORT");
        System.out.println(HORIZONTAL_LINE);
        System.out.println("Roll Number      : " + student.getRollNo());
        System.out.println("Name             : " + student.getName());
        System.out.println("Current Semester : " + student.getCurrentSemester());
        System.out.println("CGPA             : " + String.format("%.2f", student.getCgpa()));
        System.out.println("Percentage       : " + String.format("%.2f%%", student.getEquivalentPercentage()));
        System.out.println("Classification   : " + student.getClassification());
        System.out.println("Total Credits    : " + student.getTotalCredits());
        System.out.println();

        if (student.getSemesters().isEmpty()) {
            System.out.println("No semester data available.");
            System.out.println(HORIZONTAL_LINE);
            return;
        }

        System.out.println(HORIZONTAL_LINE);
        System.out.println("                   SEMESTER-WISE DETAILS");
        System.out.println(HORIZONTAL_LINE);

        for (Semester semester : student.getSemesters()) {
            System.out.println("\n📚 SEMESTER " + semester.getSemesterNumber() +
                    " - SGPA: " + String.format("%.2f", semester.getSgpa()));
            System.out.println(DASH_LINE);
            System.out.printf("%-30s | %-6s | %-7s | %-5s | %-5s%n",
                    "Subject Name", "Marks", "Credits", "Grade", "GP");
            System.out.println(DASH_LINE);

            for (Subject subject : semester.getSubjects()) {
                System.out.printf("%-30s | %6.2f | %7d | %5s | %5.1f%n",
                        subject.getName(),
                        subject.getMarks(),
                        subject.getCredits(),
                        subject.getGradeLetter(),
                        subject.getGradePoint());
            }

            System.out.println(DASH_LINE);
            System.out.printf("Total Credits: %d | SGPA: %.2f%n",
                    semester.getTotalCredits(), semester.getSgpa());
        }

        // Display SGPA graph
        System.out.println("\n" + HORIZONTAL_LINE);
        System.out.println("                   SGPA PROGRESSION");
        System.out.println(HORIZONTAL_LINE);
        displaySGPAGraph(student);

        System.out.println(HORIZONTAL_LINE);
    }

    /**
     * Displays ASCII bar chart for SGPA across semesters.
     *
     * @param student Student object
     */
    public static void displaySGPAGraph(Student student) {
        List<Semester> semesters = student.getSemesters();
        if (semesters.isEmpty()) {
            System.out.println("No data to display.");
            return;
        }

        for (Semester semester : semesters) {
            int barLength = (int) (semester.getSgpa() * 5); // Scale: 1 SGPA = 5 chars
            String bar = "█".repeat(Math.max(0, barLength));

            System.out.printf("Sem %d: [%-50s] %.2f%n",
                    semester.getSemesterNumber(),
                    bar,
                    semester.getSgpa());
        }
    }

    /**
     * Displays leaderboard with student rankings.
     *
     * @param students List of students sorted by CGPA
     */
    public static void displayLeaderboard(List<Student> students) {
        if (students.isEmpty()) {
            System.out.println("\n📭 No students found in the system.");
            return;
        }

        System.out.println("\n" + HORIZONTAL_LINE);
        System.out.println("                    🏆 LEADERBOARD - TOP PERFORMERS 🏆");
        System.out.println(HORIZONTAL_LINE);
        System.out.printf("%-5s | %-15s | %-30s | %-10s | %-15s%n",
                "Rank", "Roll Number", "Name", "CGPA", "Classification");
        System.out.println(DASH_LINE);

        int rank = 1;
        for (Student student : students) {
            String medal = "";
            if (rank == 1)
                medal = "🥇";
            else if (rank == 2)
                medal = "🥈";
            else if (rank == 3)
                medal = "🥉";

            System.out.printf("%s%-4d | %-15s | %-30s | %10.2f | %-15s%n",
                    medal,
                    rank,
                    student.getRollNo(),
                    student.getName(),
                    student.getCgpa(),
                    student.getClassification());
            rank++;
        }
        System.out.println(HORIZONTAL_LINE);
    }

    /**
     * Displays statistics about the student population.
     *
     * @param stats Map containing statistics
     */
    public static void displayStatistics(Map<String, Object> stats) {
        System.out.println("\n" + HORIZONTAL_LINE);
        System.out.println("                    📊 CLASS STATISTICS 📊");
        System.out.println(HORIZONTAL_LINE);

        System.out.println("Total Students   : " + stats.get("totalStudents"));
        System.out.println("Average CGPA     : " + String.format("%.2f", stats.get("averageCGPA")));
        System.out.println("Topper           : " + stats.get("topperName"));
        System.out.println("Topper CGPA      : " + String.format("%.2f", stats.get("topperCGPA")));

        System.out.println("\n" + DASH_LINE);
        System.out.println("CLASSIFICATION DISTRIBUTION:");
        System.out.println(DASH_LINE);

        @SuppressWarnings("unchecked")
        Map<String, Long> cgpaDistribution = (Map<String, Long>) stats.get("cgpaDistribution");
        cgpaDistribution.forEach((classification, count) -> {
            int barLength = (int) (count * 2);
            String bar = "█".repeat(Math.max(0, barLength));
            System.out.printf("%-30s: [%-20s] %d%n", classification, bar, count);
        });

        System.out.println("\n" + DASH_LINE);
        System.out.println("SEMESTER DISTRIBUTION:");
        System.out.println(DASH_LINE);

        @SuppressWarnings("unchecked")
        Map<Integer, Long> semesterDistribution = (Map<Integer, Long>) stats.get("semesterDistribution");
        semesterDistribution.forEach((sem, count) -> {
            int barLength = (int) (count * 2);
            String bar = "█".repeat(Math.max(0, barLength));
            System.out.printf("Semester %d: [%-20s] %d%n", sem, bar, count);
        });

        System.out.println(HORIZONTAL_LINE);
    }

    /**
     * Displays a success message.
     *
     * @param message Message to display
     */
    public static void success(String message) {
        System.out.println("\n✅ " + message);
    }

    /**
     * Displays an error message.
     *
     * @param message Message to display
     */
    public static void error(String message) {
        System.out.println("\n❌ " + message);
    }

    /**
     * Displays a warning message.
     *
     * @param message Message to display
     */
    public static void warning(String message) {
        System.out.println("\n⚠️  " + message);
    }

    /**
     * Displays an info message.
     *
     * @param message Message to display
     */
    public static void info(String message) {
        System.out.println("\nℹ️  " + message);
    }

    /**
     * Pauses execution and waits for user to press Enter.
     */
    public static void pressEnterToContinue() {
        System.out.print("\nPress Enter to continue...");
        try {
            System.in.read();
        } catch (Exception e) {
            // Ignore
        }
    }

    /**
     * Clears the console (works on Windows).
     */
    public static void clearScreen() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            // If clearing fails, just print newlines
            System.out.println("\n".repeat(50));
        }
    }
}
