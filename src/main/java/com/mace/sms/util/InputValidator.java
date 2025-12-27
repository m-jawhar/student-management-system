package com.mace.sms.util;

import java.util.Scanner;

/**
 * Utility class for validating user inputs.
 * Provides methods for input validation and sanitization.
 */
public class InputValidator {

    /**
     * Validates if a string is not null or empty.
     *
     * @param input String to validate
     * @return true if valid, false otherwise
     */
    public static boolean isNotEmpty(String input) {
        return input != null && !input.trim().isEmpty();
    }

    /**
     * Validates roll number format (alphanumeric, not empty).
     *
     * @param rollNo Roll number to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidRollNumber(String rollNo) {
        if (!isNotEmpty(rollNo)) {
            return false;
        }
        // Allow alphanumeric characters, hyphens, and underscores
        return rollNo.matches("^[A-Za-z0-9_-]+$");
    }

    /**
     * Validates semester number (1-8).
     *
     * @param semester Semester number to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidSemester(int semester) {
        return semester >= 1 && semester <= 8;
    }

    /**
     * Validates marks (0-100).
     *
     * @param marks Marks to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidMarks(double marks) {
        return marks >= 0 && marks <= 100;
    }

    /**
     * Validates credits (positive integer).
     *
     * @param credits Credits to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidCredits(int credits) {
        return credits > 0;
    }

    /**
     * Reads a non-empty string from scanner with validation.
     *
     * @param scanner Scanner object
     * @param prompt  Prompt to display
     * @return Valid non-empty string
     */
    public static String readNonEmptyString(Scanner scanner, String prompt) {
        String input;
        do {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("❌ Input cannot be empty. Please try again.");
            }
        } while (input.isEmpty());
        return input;
    }

    /**
     * Reads a valid roll number from scanner.
     *
     * @param scanner Scanner object
     * @param prompt  Prompt to display
     * @return Valid roll number
     */
    public static String readRollNumber(Scanner scanner, String prompt) {
        String rollNo;
        do {
            System.out.print(prompt);
            rollNo = scanner.nextLine().trim();
            if (!isValidRollNumber(rollNo)) {
                System.out.println("❌ Invalid roll number. Use alphanumeric characters only.");
            }
        } while (!isValidRollNumber(rollNo));
        return rollNo;
    }

    /**
     * Reads a valid semester number from scanner.
     *
     * @param scanner Scanner object
     * @param prompt  Prompt to display
     * @return Valid semester number (1-8)
     */
    public static int readSemester(Scanner scanner, String prompt) {
        int semester;
        do {
            System.out.print(prompt);
            while (!scanner.hasNextInt()) {
                System.out.print("❌ Invalid input. Enter a number (1-8): ");
                scanner.nextLine();
            }
            semester = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (!isValidSemester(semester)) {
                System.out.println("❌ Semester must be between 1 and 8.");
            }
        } while (!isValidSemester(semester));
        return semester;
    }

    /**
     * Reads valid marks from scanner.
     *
     * @param scanner Scanner object
     * @param prompt  Prompt to display
     * @return Valid marks (0-100)
     */
    public static double readMarks(Scanner scanner, String prompt) {
        double marks;
        do {
            System.out.print(prompt);
            while (!scanner.hasNextDouble()) {
                System.out.print("❌ Invalid input. Enter a number (0-100): ");
                scanner.nextLine();
            }
            marks = scanner.nextDouble();
            scanner.nextLine(); // Consume newline

            if (!isValidMarks(marks)) {
                System.out.println("❌ Marks must be between 0 and 100.");
            }
        } while (!isValidMarks(marks));
        return marks;
    }

    /**
     * Reads valid credits from scanner.
     *
     * @param scanner Scanner object
     * @param prompt  Prompt to display
     * @return Valid credits (positive integer)
     */
    public static int readCredits(Scanner scanner, String prompt) {
        int credits;
        do {
            System.out.print(prompt);
            while (!scanner.hasNextInt()) {
                System.out.print("❌ Invalid input. Enter a positive integer: ");
                scanner.nextLine();
            }
            credits = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (!isValidCredits(credits)) {
                System.out.println("❌ Credits must be a positive integer.");
            }
        } while (!isValidCredits(credits));
        return credits;
    }

    /**
     * Reads an integer from scanner with validation.
     *
     * @param scanner Scanner object
     * @param prompt  Prompt to display
     * @return Valid integer
     */
    public static int readInt(Scanner scanner, String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.out.print("❌ Invalid input. Enter a valid number: ");
            scanner.nextLine();
        }
        int value = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        return value;
    }

    /**
     * Reads a yes/no confirmation from scanner.
     *
     * @param scanner Scanner object
     * @param prompt  Prompt to display
     * @return true for yes, false for no
     */
    public static boolean readConfirmation(Scanner scanner, String prompt) {
        System.out.print(prompt + " (y/n): ");
        String response = scanner.nextLine().trim().toLowerCase();
        return response.equals("y") || response.equals("yes");
    }

    /**
     * Validates email format (basic validation).
     *
     * @param email Email to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidEmail(String email) {
        if (!isNotEmpty(email)) {
            return false;
        }
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }

    /**
     * Sanitizes string input by trimming and removing special characters.
     *
     * @param input Input string
     * @return Sanitized string
     */
    public static String sanitize(String input) {
        if (input == null) {
            return "";
        }
        return input.trim().replaceAll("[^A-Za-z0-9\\s._-]", "");
    }
}
