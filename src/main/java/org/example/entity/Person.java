package org.example.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
//import com.fasterxml.jackson.annotation.JsonProperty;


public class Person {

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @NotBlank(message = "Surname cannot be blank")
    @JsonProperty("surname")
    private String surname;

    @NotNull(message = "PIN cannot be null")
    @JsonProperty("pin")
    private String pin;

    @NotNull(message = "Number of work months cannot be null")
    @JsonProperty("numberOfWorkMonths")
    private Integer numberOfWorkMonths;

    // Default constructor
    public Person() {
    }

    // Constructor with all fields
    public Person(String name, String surname, String pin, Integer numberOfWorkMonths) {
        this.name = name;
        this.surname = surname;
        this.pin = pin;
        this.numberOfWorkMonths = numberOfWorkMonths;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public Integer getNumberOfWorkMonths() {
        return numberOfWorkMonths;
    }

    public void setNumberOfWorkMonths(Integer numberOfWorkMonths) {
        this.numberOfWorkMonths = numberOfWorkMonths;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", pin='" + pin + '\'' +
                ", numberOfWorkMonths=" + numberOfWorkMonths +
                '}';
    }
}