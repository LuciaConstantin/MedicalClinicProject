package project.models;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;
import java.util.Scanner;

public class Doctor extends Person implements Scheduler {
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


    public void addSchedule() {
        Scanner s = new Scanner(System.in);
        System.out.println("Enter a new work day program");
        try{
            System.out.println("Day of the week: ");
            String day = s.nextLine();
            System.out.println("Start hour: ");
            System.out.println("Enter start hour (0-23):");
            int sHour = Integer.parseInt(s.nextLine());
            System.out.println("Enter start minute (0-59):");
            int sMinute = Integer.parseInt(s.nextLine());

            System.out.println("End hour: ");
            System.out.println("Enter end hour (0-23):");
            int eHour = Integer.parseInt(s.nextLine());
            System.out.println("Enter end minute (0-59):");
            int eMinute = Integer.parseInt(s.nextLine());

            if(sHour > eHour || (sHour == eHour && sMinute > eMinute)) {
                System.out.println("The chosen time interval is not correct");
                return;
            }

            schedule.addToSchedule(day,  LocalTime.of(sHour, sMinute), LocalTime.of(eHour, eMinute));
        } catch (DateTimeException e) {
            System.out.println("Invalid time format.");}
        catch(IllegalArgumentException e){
            System.out.println("Invalid argument type.");
        }

    }

    public void viewSchedule() {
        System.out.print("\nSchedule for doctor: " + firstName + " " + lastName + "\n");
        schedule.printSchedule();
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