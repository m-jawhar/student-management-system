# Student Management System

A Java console application for managing student academic records with CGPA/SGPA calculation based on Mar Athanasius College of Engineering (MACE) 2024 regulations.

## 📋 Features

1. **Add Student/Semester** - Register new students and add semester data
2. **Display All Students** - View complete student list with CGPA
3. **View Student Report** - Detailed semester-wise academic report
4. **Update Marks** - Modify subject marks with automatic GPA recalculation
5. **Delete Student** - Remove student records
6. **Statistics** - View average CGPA, classification distribution
7. **Leaderboard** - Students ranked by CGPA
8. **Auto-Save** - Persistent storage in JSON format

## 🎓 Grading System (MACE 2024)

| Marks Range | Grade | Grade Point |
| ----------- | ----- | ----------- |
| 90-100      | S     | 10.0        |
| 85-89       | A+    | 9.0         |
| 80-84       | A     | 8.5         |
| 75-79       | B+    | 8.0         |
| 70-74       | B     | 7.5         |
| 65-69       | C+    | 7.0         |
| 60-64       | C     | 6.5         |
| 55-59       | D     | 6.0         |
| 50-54       | P     | 5.5         |
| Below 50    | F     | 0.0         |

### GPA Calculations

- **SGPA** (Semester Grade Point Average) = Σ(credits × gradePoints) / Σ(credits)
- **CGPA** (Cumulative Grade Point Average) = Σ(all semester SGPAs × credits) / Σ(all credits)

### Classifications

- **First Class with Distinction**: CGPA ≥ 8.0
- **First Class**: CGPA ≥ 6.5
- **Second Class**: CGPA ≥ 5.5
- **Pass Class**: CGPA < 5.5

## 🚀 Quick Start

### Prerequisites

- **Java 17 or higher**
- **Maven 3.6+** (optional - for building)

### Running the Application

#### Option 1: Using Java directly

```bash
# If you have the compiled JAR
java -jar target/student-management-system-1.0.0.jar
```

#### Option 2: Using Maven

```bash
mvn clean compile exec:java
```

#### Option 3: Compile and run manually

```bash
javac -d target/classes -cp "target/classes:lib/*" src/main/java/com/mace/sms/*.java src/main/java/com/mace/sms/**/*.java
java -cp "target/classes:lib/*" com.mace.sms.Main
```

## 📦 Project Structure

```
src/
├── main/java/com/mace/sms/
│   ├── Main.java              # Application entry point
│   ├── StudentManager.java    # Central management class
│   ├── model/
│   │   ├── Student.java       # Student entity
│   │   ├── Semester.java      # Semester entity
│   │   └── Subject.java       # Subject entity
│   └── util/
│       ├── InputValidator.java # Input validation
│       └── DisplayUtil.java    # Display formatting
└── test/java/com/mace/sms/
    └── StudentManagementTest.java # JUnit test suite

data/
└── students.json              # Persistent data storage
```

## 🏗️ Architecture

The system follows a simple layered architecture:

- **Presentation Layer**: `Main.java`, `DisplayUtil`
- **Business Logic**: `StudentManager` (combines all management functions)
- **Data Model**: `Student`, `Semester`, `Subject`
- **Utilities**: `InputValidator`
- **Persistence**: JSON file storage using Gson

## 💻 Code Examples

### Creating a Student

```java
// Create a student
Student student = new Student("CS001", "John Doe", 1);

// Create a semester
Semester semester = new Semester(1);

// Add subjects
semester.addSubject(new Subject("Mathematics", 90, 4));
semester.addSubject(new Subject("Physics", 85, 3));

// Add semester to student
student.addSemester(semester);

// CGPA is automatically calculated
System.out.println("CGPA: " + student.getCgpa());
```

### Using StudentManager

```java
StudentManager manager = new StudentManager();

// Add student
manager.addStudent(student);

// Get student
Optional<Student> found = manager.getStudent("CS001");

// Update marks
manager.updateSubjectMarks("CS001", 1, "Mathematics", 95);

// Get leaderboard
List<Student> topStudents = manager.getLeaderboard();

// Save data
manager.saveStudents();
```

## 🧪 Testing

Run the test suite:

```bash
mvn test
```

The project includes 20 comprehensive tests covering:

- Subject grade calculations
- SGPA calculations
- CGPA calculations
- Student operations
- Manager functions
- MACE 2024 grading accuracy

## 📊 Sample Data

The system comes with sample data (`data/students.json`) containing 5 students with varying GPAs to demonstrate all features.

## 🛠️ Building from Source

```bash
# Clean and compile
mvn clean compile

# Run tests
mvn test

# Package as JAR
mvn package

# The executable JAR will be in target/ directory
```

## 📝 Technical Details

### Technologies Used

- **Java 17**: Modern Java features (records not used for simplicity)
- **Maven**: Build automation
- **Gson 2.10.1**: JSON serialization
- **JUnit 5.10.1**: Unit testing

### Design Principles

- **Clean Code**: Readable and maintainable
- **OOP**: Proper encapsulation and abstraction
- **Input Validation**: All inputs validated
- **Error Handling**: Comprehensive exception handling
- **Automatic GPA Calculation**: GPAs recalculate on any change

## 🎯 Key Learning Outcomes

This project demonstrates:

1. **Object-Oriented Programming**: Classes, encapsulation, inheritance
2. **Collections Framework**: ArrayList, HashMap, Optional, Streams
3. **File I/O**: JSON persistence with Gson
4. **Exception Handling**: Try-catch blocks, custom validations
5. **Testing**: JUnit 5 unit tests
6. **Maven**: Dependency management and build lifecycle
7. **Data Structures**: Efficient student/semester/subject organization
8. **Algorithm Design**: GPA calculation algorithms
9. **User Interface**: Menu-driven console application
10. **Code Organization**: Proper package structure

## 📄 License

This project is licensed under the MIT License.

## 👨‍💻 Author

Academic project demonstrating Java programming concepts and student management system implementation.

## 🤝 Contributing

This is an academic project. Feel free to fork and enhance for your own learning purposes.

---

**Note**: This simplified version removes unnecessary complexity (Builder pattern, Repository pattern, multiple services) while maintaining all core functionality for academic demonstration purposes.
