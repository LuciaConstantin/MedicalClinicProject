package project.models;

import java.time.LocalDate;

public class AdultPatient extends Patient {
    private HealthInsurance typeOfHealthInsurance;

    public AdultPatient(String firstName, String lastName, String personalID, String email, String phone, LocalDate dateOfBirth, MedicalRecord medicalRecord, HealthInsurance healthInsurance) {
        super(firstName, lastName, personalID, email, phone,dateOfBirth, medicalRecord, "adult");
        this.typeOfHealthInsurance = healthInsurance;
    }

    public AdultPatient(long id, String firstName, String lastName, String personalID, String email, String phone,
                        LocalDate dateOfBirth, MedicalRecord medicalRecord, HealthInsurance insurance) {
        super(id, firstName, lastName, personalID, email, phone, dateOfBirth, medicalRecord, "adult");
        this.typeOfHealthInsurance = insurance;
    }

    @Override
    public double calculateBill(double procedurePrice){
        double finalePrice;
        if(this.typeOfHealthInsurance == HealthInsurance.PUBLIC){
            finalePrice = procedurePrice - 0.2 * procedurePrice;
        }
        else{
            finalePrice = procedurePrice;
        }
        return finalePrice;
    }

    @Override
    public void displayInformation() {
        System.out.println("Information about the child patient");
        System.out.println("Last name: " + this.lastName + ", first name: " + this.firstName +
                ", email: " + this.email + ", age: " + this.getAge() + ", phone: " + this.phone + ", type of health insurance: " + this.typeOfHealthInsurance.toString().toLowerCase());
    }

    public String getPatientType() {
        return patientType;
    }

    public HealthInsurance getTypeOfHealthInsurance() {
        return typeOfHealthInsurance;
    }

    public void setTypeOfHealthInsurance(HealthInsurance typeOfHealthInsurance) {
        this.typeOfHealthInsurance = typeOfHealthInsurance;
    }

    @Override
    public String toString() {
        return "AdultPatient{" +
                "patientType='" + patientType + '\'' +
                ", typeOfHealthInsurance=" + typeOfHealthInsurance +
                ", id=" + id +
                ", medicalRecord=" + medicalRecord +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", personalID='" + personalID + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                '}';
    }
}
