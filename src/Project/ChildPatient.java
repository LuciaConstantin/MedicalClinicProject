package Project;

import java.time.LocalDate;

public class ChildPatient extends Patient{
    private final String patientType = "child";
    private String guardianName;
    private String guardianId;

    public ChildPatient(String firstName, String lastName, String personalID, String email, String phone, LocalDate dateOfBirth, MedicalRecord medicalRecord, String guardianName, String guardianId) {
        super(firstName, lastName, personalID, email, phone,dateOfBirth, medicalRecord);
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
}
