package Project;

import java.time.LocalDate;
import java.time.Period;

public abstract class Person {
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

}
