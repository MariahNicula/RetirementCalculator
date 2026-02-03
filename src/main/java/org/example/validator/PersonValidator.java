package org.example.validator;
import org.example.entity.Person;


import org.example.exception.InvalidPINException;
import org.example.exception.InvalidNameException;
import org.example.exception.InvalidDateException;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.time.LocalDate;

@ApplicationScoped
public class PersonValidator {

    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z]+$");
    private static final Pattern PIN_PATTERN = Pattern.compile("^([1-9])(\\d{2})(\\d{2})(\\d{2})(\\d{2})(\\d{3})(\\d)$");

    public void validate(Person person) {
        validateNames(person.getName(), person.getSurname());
        validatePIN(person.getPin());
    }

    private void validateNames(String name, String surname) {
        if (!NAME_PATTERN.matcher(name).matches()) {
            throw new InvalidNameException("Name must contain only letters: " + name);
        }
        if (!NAME_PATTERN.matcher(surname).matches()) {
            throw new InvalidNameException("Surname must contain only letters: " + surname);
        }
    }

    public void validatePIN(String pin) {
        // Validate PIN length
        if (pin == null || pin.length() != 13) {
            throw new InvalidPINException("PIN must be exactly 13 characters long");
        }

        // Validate PIN format using regex
        Matcher matcher = PIN_PATTERN.matcher(pin);
        if (!matcher.matches()) {
            throw new InvalidPINException("PIN format is invalid");
        }

        // Extract components based on indexes (0-based)
        char genderChar = pin.charAt(0);  // Index 0
        String yearStr = pin.substring(1, 3);    // Indexes 1-2
        String monthStr = pin.substring(3, 5);   // Indexes 3-4
        String dayStr = pin.substring(5, 7);     // Indexes 5-6

        // Validate gender (odd for male, even for female)
        int genderDigit = Character.getNumericValue(genderChar);
        if (genderDigit < 1 || genderDigit > 9) {
            throw new InvalidPINException("Gender digit must be between 1 and 9");
        }

        // Validate date components
        validateDate(yearStr, monthStr, dayStr, genderDigit);
    }

    private void validateDate(String yearStr, String monthStr, String dayStr, int genderDigit) {
        try {
            int year = Integer.parseInt(yearStr);
            int month = Integer.parseInt(monthStr);
            int day = Integer.parseInt(dayStr);

            // Validate month
            if (month < 1 || month > 12) {
                throw new InvalidDateException("Month must be between 1 and 12, got: " + month);
            }

            // Validate day
            if (day < 1 || day > 31) {
                throw new InvalidDateException("Day must be between 1 and 31, got: " + day);
            }

            // Determine full year based on gender digit
            int fullYear = determineFullYear(year, genderDigit);

            // Validate the complete date
            LocalDate birthDate = LocalDate.of(fullYear, month, day);

            // Check if birth date is not in the future
            if (birthDate.isAfter(LocalDate.now())) {
                throw new InvalidDateException("Birth date cannot be in the future: " + birthDate);
            }

        } catch (NumberFormatException e) {
            throw new InvalidDateException("Invalid date format in PIN");
        } catch (java.time.DateTimeException e) {
            throw new InvalidDateException("Invalid date: " + dayStr + "/" + monthStr + "/" + yearStr);
        }
    }

    private int determineFullYear(int year, int genderDigit) {
        // Romanian PIN system:
        // 1, 2: 1900-1999
        // 3, 4: 1800-1899
        // 5, 6: 2000-2099
        // 7, 8: Residents (2000-2099)
        // 9: Foreigners
        switch (genderDigit) {
            case 1:
            case 2:
                return 1900 + year;
            case 3:
            case 4:
                return 1800 + year;
            case 5:
            case 6:
            case 7:
            case 8:
                return 2000 + year;
            case 9:
                return 2000 + year; // Assuming foreigners use 2000s
            default:
                throw new InvalidPINException("Invalid gender digit: " + genderDigit);
        }
    }

    public String extractGender(String pin) {
        int genderDigit = Character.getNumericValue(pin.charAt(0));
        return (genderDigit % 2 == 1) ? "Male" : "Female";
    }

    public LocalDate extractBirthDate(String pin) {
        String yearStr = pin.substring(1, 3);
        String monthStr = pin.substring(3, 5);
        String dayStr = pin.substring(5, 7);
        int genderDigit = Character.getNumericValue(pin.charAt(0));

        int year = Integer.parseInt(yearStr);
        int month = Integer.parseInt(monthStr);
        int day = Integer.parseInt(dayStr);
        int fullYear = determineFullYear(year, genderDigit);

        return LocalDate.of(fullYear, month, day);
    }
}