package project.models;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;
import java.util.Scanner;

public class Doctor extends Person {
    private LocalDate hireDate;
    private Specialty specialty;
    private double salary;
    private Schedule schedule;

    public Doctor(String firstName, String lastName, String personalID, String email, String phone,
           LocalDate dateOfBirth, LocalDate hireDate, Specialty specialty, Schedule schedule) {
        super(firstName, lastName,personalID, email, phone, dateOfBirth);
        this.hireDate = hireDate;
        this.specialty = specialty;
        this.salary = this.specialty.getStartingSalary();
        this.schedule = schedule;

    }

    public Doctor(long id, String firstName, String lastName, String personalID, String email, String phone,
                  LocalDate dateOfBirth, LocalDate hireDate, Specialty specialty, Schedule schedule) {
        super(id, firstName, lastName,personalID, email, phone, dateOfBirth);
        this.hireDate = hireDate;
        this.specialty = specialty;
        this.salary = this.specialty.getStartingSalary();
        this.schedule = schedule;

    }


    public Schedule getSchedule() {
        return schedule;
    }

    public Specialty getSpecialty() {
        return specialty;
    }

    public double getSalary() {
        return salary;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    @Override
    public void displayInformation() {
        System.out.println("Information about Doctor");
        System.out.println("First Name: " + firstName + ", last name: " + lastName +
                ", specialty: " + specialty.getSpecialtyName() + ", salary: " + salary);

    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    public void setSpecialty(Specialty specialty) {
        this.specialty = specialty;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    @Override
    public String toString() {
        return "Doctor{" +
               "hireDate=" + hireDate +
               ", specialty=" + specialty +
               ", salary=" + salary +
               ", schedule=" + schedule +
               ", id=" + id +
               ", firstName='" + firstName + '\'' +
               ", lastName='" + lastName + '\'' +
               ", personalID='" + personalID + '\'' +
               ", email='" + email + '\'' +
               ", phone='" + phone + '\'' +
               ", dateOfBirth=" + dateOfBirth +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Doctor doctor = (Doctor) o;
        return id == doctor.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}