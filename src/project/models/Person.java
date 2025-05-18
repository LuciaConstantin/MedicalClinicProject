package project.models;

import java.time.LocalDate;
import java.time.Period;

public abstract class Person {
    protected long id;
    protected String firstName;
    protected String lastName;
    protected String personalID;
    protected String email;
    protected String phone;
    protected LocalDate dateOfBirth;

    Person(String firstName, String lastName, String personalID, String email, String phone, LocalDate dateOfBirth) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.personalID = personalID;
        this.email = email;
        this.phone = phone;
        this.dateOfBirth = dateOfBirth;
    }

    Person(long id, String firstName, String lastName, String personalID, String email, String phone, LocalDate dateOfBirth) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.personalID = personalID;
        this.email = email;
        this.phone = phone;
        this.dateOfBirth = dateOfBirth;
    }

    public abstract void displayInformation();

    public int getAge(){
        return(Period.between(dateOfBirth, LocalDate.now()).getYears());
    }

    public String getFirstName() {

        return firstName;
    }

    public String getLastName() {

        return lastName;
    }

    public String getPersonalID() {

        return personalID;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPersonalID(String personalID) {
        this.personalID = personalID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
