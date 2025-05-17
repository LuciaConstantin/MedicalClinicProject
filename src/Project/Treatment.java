package Project;

public interface Treatment {
    String treatmentType();
    String treatmentDescription();
    Boolean verifyTreatment(MedicalRecord record);
}
