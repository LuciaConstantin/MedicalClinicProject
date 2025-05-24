package project.models;

import java.time.LocalDate;
import java.util.Objects;


public abstract class Patient extends Person {
    protected MedicalRecord medicalRecord;
    protected String patientType;


    Patient(String firstname, String lastName, String personalID, String email, String phone, LocalDate dateOfBirth, MedicalRecord medicalRecord, String patientType) {
        super(firstname, lastName, personalID, email, phone, dateOfBirth);
        this.medicalRecord = medicalRecord;
        this.patientType = patientType;
    }

    Patient(long id, String firstname, String lastName, String personalID, String email, String phone, LocalDate dateOfBirth, MedicalRecord medicalRecord, String patientType) {
        super(id, firstname, lastName, personalID, email, phone, dateOfBirth);
        this.medicalRecord = medicalRecord;
        this.patientType = patientType;
    }


    public abstract double calculateBill(double procedurePrice);

    @Override
    public void displayInformation() {
        System.out.println("Information about the patient");
        System.out.println("Last name: " + this.lastName + ", first name: " + this.firstName +
                ", email: " + this.email + ", age: " + this.getAge() + ", phone: " + this.phone );
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setMedicalRecord(MedicalRecord medicalRecord) {
        this.medicalRecord = medicalRecord;
    }

    public MedicalRecord getMedicalRecord() {
         return medicalRecord;
    }

    public String getPatientType() {
        return patientType;
    }

    public void setPatientType(String patientType) {
        this.patientType = patientType;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Patient patient = (Patient) o;
        return id == patient.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


}
