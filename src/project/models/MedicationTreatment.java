package project.models;

import java.util.List;

public class MedicationTreatment implements Treatment {
    private long id;
    private String medicationName;
    private double dosage;
    private int treatmentInterval;

    public MedicationTreatment(String medicationName, double dosage, int treatmentInterval) {
        this.medicationName = medicationName;
        this.dosage = dosage;
        this.treatmentInterval = treatmentInterval;

    }

    public MedicationTreatment(long id, String medicationName, double dosage, int treatmentInterval) {
        this.id = id;
        this.medicationName = medicationName;
        this.dosage = dosage;
        this.treatmentInterval = treatmentInterval;

    }

    @Override
    public String treatmentType() {
        return "Medication treatment";
    }

    @Override
    public String treatmentDescription() {
        return ("Medication name: " + medicationName + " dosage: " + dosage + " treatmentInterval: " + treatmentInterval);
    }

    @Override
    public Boolean verifyTreatment(MedicalRecord medicalRecord) {
        List<String> allergies = medicalRecord.getAllergies();
        boolean allergyFound = allergies.stream().anyMatch(allergy -> allergy.equals(medicationName));

        if (allergyFound) {
            return false;
        }
        return true;

    }

    public String getMedicationName() {
        return medicationName;
    }

    public void setMedicationName(String medicationName) {
        this.medicationName = medicationName;
    }

    public double getDosage() {
        return dosage;
    }

    public void setDosage(double dosage) {
        this.dosage = dosage;
    }

    public int getTreatmentInterval() {
        return treatmentInterval;
    }

    public void setTreatmentInterval(int treatmentInterval) {
        this.treatmentInterval = treatmentInterval;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "MedicationTreatment{" +
               "medicationName='" + medicationName + '\'' +
               ", dosage=" + dosage +
               ", treatmentInterval=" + treatmentInterval +
               '}';
    }
}
