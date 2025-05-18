package project.models;

import java.util.List;

public class MedicationTreatment implements Treatment {
    private String medicationName;
    private double dosage;
    private int treatmentInterval;

    public MedicationTreatment(String medicationName, double dosage, int treatmentInterval) {
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


}
