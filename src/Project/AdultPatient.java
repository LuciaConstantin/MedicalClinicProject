package Project;

import java.time.LocalDate;

public class AdultPatient extends Patient{
    private final String patientType = "adult";
    private HealthInsurance typeOfHealthInsurance;

    public AdultPatient(String firstName, String lastName, String personalID, String email, String phone, LocalDate dateOfBirth, MedicalRecord medicalRecord, HealthInsurance healthInsurance) {
        super(firstName, lastName, personalID, email, phone,dateOfBirth, medicalRecord);
        this.typeOfHealthInsurance = healthInsurance;
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
}
