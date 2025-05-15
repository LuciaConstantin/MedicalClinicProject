package Project;

public interface Treatment {
    String treatmentType();
    void treatmentDescription();
    void verifyTreatment(MedicalRecord record);
}
