package project.models;

import java.time.LocalDate;

public class ChildPatient extends Patient{
    private String guardianName;
    private String guardianId;

    public ChildPatient(String firstName, String lastName, String personalID, String email, String phone, LocalDate dateOfBirth, MedicalRecord medicalRecord, String guardianName, String guardianId) {
        super(firstName, lastName, personalID, email, phone,dateOfBirth, medicalRecord, "child");
        this.guardianName = guardianName;
        this.guardianId = guardianId;
    }

    public ChildPatient(long id, String firstName, String lastName, String personalID, String email, String phone, LocalDate dateOfBirth, MedicalRecord medicalRecord, String guardianName, String guardianId) {
        super(id, firstName, lastName, personalID, email, phone,dateOfBirth, medicalRecord, "child");
        this.guardianName = guardianName;
        this.guardianId = guardianId;
    }

    @Override
    public double calculateBill(double procedurePrice){
        double finalePrice = procedurePrice- procedurePrice * 0.3;
        return finalePrice;
    }

    @Override
    public void displayInformation() {
        System.out.println("Information about the child patient");
        System.out.println("Last name: " + this.lastName + ", first name: " + this.firstName +
                ", email: " + this.email + ", age: " + this.getAge() + ", phone: " + this.phone + ", guradian name: " + this.guardianName);
    }


    public String getGuardianName() {
        return guardianName;
    }

    public void setGuardianName(String guardianName) {
        this.guardianName = guardianName;
    }

    public String getGuardianId() {
        return guardianId;
    }

    public void setGuardianId(String guardianId) {
        this.guardianId = guardianId;
    }

    @Override
    public String toString() {
        return "ChildPatient{" +
                "patientType='" + patientType + '\'' +
                ", guardianName='" + guardianName + '\'' +
                ", guardianId='" + guardianId + '\'' +
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
