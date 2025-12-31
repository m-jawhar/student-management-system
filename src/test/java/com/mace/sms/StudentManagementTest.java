package com.mace.sms;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.mace.sms.model.Semester;
import com.mace.sms.model.Student;
import com.mace.sms.model.Subject;

/**
 * Essential test suite covering core MACE 2024 grading and GPA calculations.
 */
class StudentManagementTest {

    // ==================== SUBJECT TESTS ====================

    @Test
    void testSubjectGradeS() {
        Subject subject = new Subject("MATH101", "Mathematics", "Dr. Smith", 95, 4);
        assertEquals("S", subject.getGradeLetter());
        assertEquals(10.0, subject.getGradePoint());
        assertEquals(40.0, subject.getCreditPoints());
    }

    @Test
    void testSubjectGradeA() {
        Subject subject = new Subject("PHY101", "Physics", "Dr. Johnson", 82, 3);
        assertEquals("A", subject.getGradeLetter());
        assertEquals(8.5, subject.getGradePoint());
        assertEquals(25.5, subject.getCreditPoints());
    }

    @Test
    void testSubjectGradeF() {
        Subject subject = new Subject("CHEM101", "Chemistry", "Dr. Brown", 45, 3);
        assertEquals("F", subject.getGradeLetter());
        assertEquals(0.0, subject.getGradePoint());
        assertEquals(0.0, subject.getCreditPoints());
    }

    @Test
    void testSubjectInvalidMarks() {
        assertThrows(IllegalArgumentException.class,
                () -> new Subject("TEST101", "Test", "Dr. Test", -10, 3));
        assertThrows(IllegalArgumentException.class,
                () -> new Subject("TEST101", "Test", "Dr. Test", 110, 3));
    }

    @Test
    void testSubjectMarksUpdate() {
        Subject subject = new Subject("MATH101", "Math", "Dr. Davis", 50, 3);
        assertEquals("P", subject.getGradeLetter());

        subject.setMarks(90);
        assertEquals("S", subject.getGradeLetter());
        assertEquals(10.0, subject.getGradePoint());
    }

    // ==================== SEMESTER TESTS ====================

    @Test
    void testSemesterSGPACalculation() {
        Semester semester = new Semester(1);
        semester.addSubject(new Subject("MATH101", "Math", "Dr. Smith", 90, 4)); // S = 10.0, CP = 40
        semester.addSubject(new Subject("PHY101", "Physics", "Dr. Johnson", 85, 3)); // A+ = 9.0, CP = 27
        semester.addSubject(new Subject("CHEM101", "Chemistry", "Dr. Brown", 75, 3)); // B+ = 8.0, CP = 24

        // SGPA = (40 + 27 + 24) / (4 + 3 + 3) = 91 / 10 = 9.1
        assertEquals(9.1, semester.getSgpa(), 0.01);
        assertEquals(10, semester.getTotalCredits());
    }

    @Test
    void testSemesterWithFailedSubject() {
        Semester semester = new Semester(1);
        semester.addSubject(new Subject("MATH101", "Math", "Dr. Smith", 90, 4)); // 40 CP
        semester.addSubject(new Subject("PHY101", "Physics", "Dr. Johnson", 40, 3)); // 0 CP

        // SGPA = 40 / 7 = 5.71
        assertEquals(5.71, semester.getSgpa(), 0.01);
    }

    @Test
    void testSemesterInvalidNumber() {
        assertThrows(IllegalArgumentException.class, () -> new Semester(0));
        assertThrows(IllegalArgumentException.class, () -> new Semester(9));
    }

    @Test
    void testSemesterUpdateMarks() {
        Semester semester = new Semester(1);
        semester.addSubject(new Subject("MATH101", "Math", "Dr. Smith", 80, 4));

        double initialSGPA = semester.getSgpa();
        assertEquals(8.5, initialSGPA, 0.01);

        semester.updateSubjectMarks("Math", 90);
        assertEquals(10.0, semester.getSgpa(), 0.01);
    }

    // ==================== STUDENT TESTS ====================

    @Test
    void testStudentCGPACalculation() {
        Student student = new Student("CS001", "John Doe", 2);

        // Semester 1
        Semester sem1 = new Semester(1);
        sem1.addSubject(new Subject("MATH101", "Math", "Dr. Smith", 90, 4)); // 40 CP
        sem1.addSubject(new Subject("PHY101", "Physics", "Dr. Johnson", 85, 3)); // 27 CP
        student.addSemester(sem1);

        double sgpa1 = 67.0 / 7.0; // 9.57
        assertEquals(sgpa1, student.getCgpa(), 0.01);

        // Semester 2
        Semester sem2 = new Semester(2);
        sem2.addSubject(new Subject("CHEM101", "Chemistry", "Dr. Brown", 80, 3)); // 25.5 CP
        sem2.addSubject(new Subject("BIO101", "Biology", "Dr. Wilson", 75, 4)); // 32 CP
        student.addSemester(sem2);

        // CGPA = (67 + 25.5 + 32) / (7 + 7) = 124.5 / 14 = 8.89
        assertEquals(8.89, student.getCgpa(), 0.01);
    }

    @Test
    void testStudentClassification() {
        Student student1 = new Student("CS001", "John", 1);
        Semester sem1 = new Semester(1);
        sem1.addSubject(new Subject("MATH101", "Math", "Dr. Smith", 90, 4));
        student1.addSemester(sem1);

        assertEquals("First Class with Distinction", student1.getClassification());

        Student student2 = new Student("CS002", "Jane", 1);
        Semester sem2 = new Semester(1);
        sem2.addSubject(new Subject("MATH101", "Math", "Dr. Smith", 70, 4));
        student2.addSemester(sem2);

        assertEquals("First Class", student2.getClassification());
    }

    @Test
    void testStudentDuplicateSemester() {
        Student student = new Student("CS001", "John", 2);
        Semester sem1 = new Semester(1);
        sem1.addSubject(new Subject("MATH101", "Math", "Dr. Smith", 90, 4));

        student.addSemester(sem1);

        Semester duplicate = new Semester(1);
        duplicate.addSubject(new Subject("PHY101", "Physics", "Dr. Johnson", 85, 3));

        assertThrows(IllegalArgumentException.class,
                () -> student.addSemester(duplicate));
    }

    @Test
    void testStudentUpdateSubjectMarks() {
        Student student = new Student("CS001", "John", 1);
        Semester sem = new Semester(1);
        sem.addSubject(new Subject("MATH101", "Math", "Dr. Smith", 80, 4));
        student.addSemester(sem);

        double initialCGPA = student.getCgpa();
        assertEquals(8.5, initialCGPA, 0.01);

        boolean updated = student.updateSubjectMarks(1, "Math", 90);
        assertTrue(updated);
        assertEquals(10.0, student.getCgpa(), 0.01);
    }

    // ==================== STUDENT MANAGER TESTS ====================

    @Test
    void testManagerAddAndRetrieveStudent() {
        StudentManager manager = new StudentManager();
        Student student = new Student("CS001", "John Doe", 1);

        manager.addStudent(student);
        assertTrue(manager.getStudent("CS001").isPresent());
        assertEquals("John Doe", manager.getStudent("CS001").get().getName());
    }

    @Test
    void testManagerDeleteStudent() {
        StudentManager manager = new StudentManager();
        Student student = new Student("CS001", "John", 1);

        manager.addStudent(student);
        assertTrue(manager.getStudent("CS001").isPresent());

        boolean deleted = manager.deleteStudent("CS001");
        assertTrue(deleted);
        assertFalse(manager.getStudent("CS001").isPresent());
    }

    @Test
    void testManagerLeaderboard() {
        StudentManager manager = new StudentManager();

        // Clear existing students from JSON file
        var rollNos = manager.getAllStudents().stream()
                .map(s -> s.getRollNo())
                .toList();
        rollNos.forEach(manager::deleteStudent);

        Student s1 = new Student("CS001", "Alice", 1);
        Semester sem1 = new Semester(1);
        sem1.addSubject(new Subject("MATH102", "Math", "Dr. Lee", 90, 4));
        s1.addSemester(sem1);

        Student s2 = new Student("CS002", "Bob", 1);
        Semester sem2 = new Semester(1);
        sem2.addSubject(new Subject("MATH101", "Math", "Dr. Smith", 80, 4));
        s2.addSemester(sem2);

        manager.addStudent(s1);
        manager.addStudent(s2);

        var leaderboard = manager.getLeaderboard();
        assertEquals(2, leaderboard.size());
        assertEquals("Alice", leaderboard.getFirst().getName()); // Higher CGPA first
        assertEquals("Bob", leaderboard.get(1).getName());
    }

    @Test
    void testManagerStatistics() {
        StudentManager manager = new StudentManager();

        // Clear existing students from JSON file
        var rollNos = manager.getAllStudents().stream()
                .map(s -> s.getRollNo())
                .toList();
        rollNos.forEach(manager::deleteStudent);

        Student s1 = new Student("CS001", "Alice", 1);
        Semester sem1 = new Semester(1);
        sem1.addSubject(new Subject("MATH201", "Math", "Dr. Taylor", 90, 4));
        s1.addSemester(sem1);

        Student s2 = new Student("CS002", "Bob", 1);
        Semester sem2 = new Semester(1);
        sem2.addSubject(new Subject("MATH201", "Math", "Dr. Taylor", 60, 4));
        s2.addSemester(sem2);

        manager.addStudent(s1);
        manager.addStudent(s2);

        var stats = manager.getStatistics();
        assertEquals(2, stats.get("totalStudents"));
        assertEquals(8.25, (double) stats.get("averageCGPA"), 0.01); // (10 + 6.5) / 2
        assertEquals(10.0, (double) stats.get("maxCGPA"), 0.01);
        assertEquals(6.5, (double) stats.get("minCGPA"), 0.01);
    }

    // ==================== GRADING SYSTEM TESTS ====================

    @Test
    void testGradingSystemAccuracy() {
        StudentManager manager = new StudentManager();

        // Test all grade boundaries
        assertEquals("S", manager.getGradeLetter(90));
        assertEquals("A+", manager.getGradeLetter(85));
        assertEquals("A", manager.getGradeLetter(80));
        assertEquals("B+", manager.getGradeLetter(75));
        assertEquals("B", manager.getGradeLetter(70));
        assertEquals("C+", manager.getGradeLetter(65));
        assertEquals("C", manager.getGradeLetter(60));
        assertEquals("D", manager.getGradeLetter(55));
        assertEquals("P", manager.getGradeLetter(50));
        assertEquals("F", manager.getGradeLetter(49));
    }

    @Test
    void testGradePointMapping() {
        StudentManager manager = new StudentManager();

        assertEquals(10.0, manager.getGradePoint(90));
        assertEquals(9.0, manager.getGradePoint(85));
        assertEquals(8.5, manager.getGradePoint(80));
        assertEquals(8.0, manager.getGradePoint(75));
        assertEquals(7.5, manager.getGradePoint(70));
        assertEquals(7.0, manager.getGradePoint(65));
        assertEquals(6.5, manager.getGradePoint(60));
        assertEquals(6.0, manager.getGradePoint(55));
        assertEquals(5.5, manager.getGradePoint(50));
        assertEquals(0.0, manager.getGradePoint(0));
    }

    @Test
    void testClassificationBoundaries() {
        StudentManager manager = new StudentManager();

        assertEquals("First Class with Distinction", manager.getClassification(8.0));
        assertEquals("First Class", manager.getClassification(6.5));
        assertEquals("Second Class", manager.getClassification(5.5));
        assertEquals("Pass Class", manager.getClassification(5.0));
    }
}
