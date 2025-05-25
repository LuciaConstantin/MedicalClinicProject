package project.models;

import java.time.LocalDate;

public class MemberPatient extends Patient{
    private String membershipNumber;
    private Membership membershipType;

    public MemberPatient(String firstName, String lastName, String personalID, String email, String phone, LocalDate dateOfBirth, MedicalRecord medicalRecord, String membershipNumber, Membership membershipType) {
        super(firstName, lastName, personalID, email, phone,dateOfBirth, medicalRecord, "member");
        this.membershipNumber = membershipNumber;
        this.membershipType = membershipType;
    }

    public MemberPatient(long id, String firstName, String lastName, String personalID, String email, String phone, LocalDate dateOfBirth, MedicalRecord medicalRecord, String membershipNumber, Membership membershipType) {
        super(id, firstName, lastName, personalID, email, phone,dateOfBirth, medicalRecord, "member");
        this.membershipNumber = membershipNumber;
        this.membershipType = membershipType;
    }

    @Override
    public double calculateBill(double procedurePrice){
        if(this.membershipType == Membership.PLATINUM){
            return 0;
        }
        else if(this.membershipType == Membership.GOLD){
            return procedurePrice - procedurePrice * 0.75;
        }
        else
            return procedurePrice - procedurePrice * 0.5;
    }

    @Override
    public void displayInformation() {
        System.out.println("Information about the member patient");
        System.out.println("Last name: " + this.lastName + ", first name: " + this.firstName +
                ", email: " + this.email + ", age: " + this.getAge() + ", phone: " + this.phone + ", type of memebership" + this.membershipType + "\n");
        getMedicalRecord().displayMedicalRecord();
    }

    public String getPatientType() {
        return patientType;
    }

    public String getMembershipNumber() {
        return membershipNumber;
    }

    public void setMembershipNumber(String membershipNumber) {
        this.membershipNumber = membershipNumber;
    }

    public Membership getMembershipType() {
        return membershipType;
    }

    public void setMembershipType(Membership membershipType) {
        this.membershipType = membershipType;
    }

    @Override
    public String toString() {
        return "MemberPatient{" +
                "patientType='" + patientType + '\'' +
                ", membershipNumber='" + membershipNumber + '\'' +
                ", membershipType=" + membershipType +
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
