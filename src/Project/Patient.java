package Project;

import java.time.LocalDate;

public abstract class Patient extends Person {
    protected MedicalRecord medicalRecord;

    Patient(String firstname, String lastName, String personalID, String email, String phone, LocalDate dateOfBirth, MedicalRecord medicalRecord) {
        super(firstname, lastName, personalID, email, phone, dateOfBirth);
        this.medicalRecord = medicalRecord;
    }

    public MedicalRecord getMedicalRecord() {
        return medicalRecord;
    }

    public abstract double calculateBill(double procedurePrice);

    @Override
    public void displayInformation() {
        System.out.println("Information about the patient");
        System.out.println("Last name: " + this.lastName + ", first name: " + this.firstName +
                ", email: " + this.email + ", age: " + this.getAge() + ", phone: " + this.phone );
    }

}
