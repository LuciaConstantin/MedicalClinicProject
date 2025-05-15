package Project;

import java.time.LocalDate;

public class MemberPatient extends Patient{
    private final String patientType = "member";
    private String membershipNumber;
    private Membership membershipType;


    public MemberPatient(String firstName, String lastName, String personalID, String email, String phone, LocalDate dateOfBirth, MedicalRecord medicalRecord, String membershipNumber, Membership membershipType) {
        super(firstName, lastName, personalID, email, phone,dateOfBirth, medicalRecord);
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
        System.out.println("Information about the child patient");
        System.out.println("Last name: " + this.lastName + ", first name: " + this.firstName +
                ", email: " + this.email + ", age: " + this.getAge() + ", phone: " + this.phone + ", type of memebership" + this.membershipType);
    }
}
