package Project;

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
    public void treatmentDescription() {
        System.out.println("Medication treatment description");
        System.out.println("Medication name: " + medicationName + " dosage: " + dosage + " treatmentInterval: " + treatmentInterval);
    }

    @Override
    public void verifyTreatment(MedicalRecord medicalRecord) {
        List<String> allergies = medicalRecord.getAllergies();
        boolean allergyFound = allergies.stream().anyMatch(allergy -> allergy.equals(medicationName));

        if (allergyFound) {
            System.out.println("Allergy found, the medicine can't be used");
        }

    }


}
