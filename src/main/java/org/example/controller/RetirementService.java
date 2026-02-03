package org.example.controller;

import org.example.entity.Person;
import org.example.validator.PersonValidator;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class RetirementService {

    @Inject
    PersonValidator validator;

    public Map<String, Object> calculateYearsUntilRetirement(Person person) {
        // Validate person
        validator.validate(person);

        // Extract gender and birth date
        String gender = validator.extractGender(person.getPin());
        LocalDate birthDate = validator.extractBirthDate(person.getPin());

        // Calculate current age
        LocalDate currentDate = LocalDate.now();
        int currentAge = Period.between(birthDate, currentDate).getYears();

        // Calculate retirement age based on Romanian law
        int retirementAge = calculateRetirementAge(gender, birthDate);

        // Calculate years until retirement
        int yearsUntilRetirement = retirementAge - currentAge;

        // Calculate months of work needed (if applicable)
        int requiredWorkMonths = calculateRequiredWorkMonths(gender);
        int remainingWorkMonths = Math.max(0, requiredWorkMonths - person.getNumberOfWorkMonths());

        // Build response
        Map<String, Object> result = new HashMap<>();
        result.put("name", person.getName() + " " + person.getSurname());
        result.put("gender", gender);
        result.put("birthDate", birthDate.toString());
        result.put("currentAge", currentAge);
        result.put("retirementAge", retirementAge);
        result.put("yearsUntilRetirement", yearsUntilRetirement);
        result.put("currentWorkMonths", person.getNumberOfWorkMonths());
        result.put("requiredWorkMonths", requiredWorkMonths);
        result.put("remainingWorkMonths", remainingWorkMonths);

        if (yearsUntilRetirement <= 0) {
            result.put("status", "Eligible for retirement");
        } else {
            result.put("status", "Not yet eligible for retirement");
        }

        return result;
    }

    private int calculateRetirementAge(String gender, LocalDate birthDate) {
        int birthYear = birthDate.getYear();

        switch (gender) {
            case "Male":
                // Romanian retirement age for men (as of 2026)
                // Gradually increasing to 65
                if (birthYear <= 1957) {
                    return 65;
                } else if (birthYear <= 1960) {
                    return 64;
                } else if (birthYear <= 1963) {
                    return 64;
                } else {
                    return 65;
                }

            case "Female":
                // Romanian retirement age for women (as of 2026)
                // Gradually increasing to 63
                if (birthYear <= 1960) {
                    return 61;
                } else if (birthYear <= 1963) {
                    return 62;
                } else if (birthYear <= 1966) {
                    return 62;
                } else {
                    return 63;
                }

            default:
                throw new IllegalArgumentException("Invalid gender: " + gender);
        }
    }

    private int calculateRequiredWorkMonths(String gender) {
        switch (gender) {
            case "Male":
                // Men require 35 years (420 months) of work contribution
                return 420;
            case "Female":
                // Women require 30 years (360 months) of work contribution
                return 360;
            default:
                throw new IllegalArgumentException("Invalid gender: " + gender);
        }
    }
}
