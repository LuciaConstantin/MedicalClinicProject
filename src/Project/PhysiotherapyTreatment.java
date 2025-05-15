package Project;


import java.util.List;

public class PhysiotherapyTreatment implements Treatment {
    private String exerciseName;
    private int repetitions;
    private String medicalIssues;

    public PhysiotherapyTreatment(int repetitions, String exerciseName, String medicalIssues) {
        this.exerciseName = exerciseName;
        this.repetitions = repetitions;
        this.medicalIssues = medicalIssues;
    }

    @Override
    public String treatmentType() {
        return "Physiotherapy treatment";
    }

    @Override
    public void treatmentDescription() {
        System.out.println("Physiotherapy treatment description");
        System.out.println("Exercise name: " + this.exerciseName + " repetitions: " + this.repetitions + " medical issue: " + this.medicalIssues);
    }

    @Override
    public void verifyTreatment(MedicalRecord medicalRecord) {

        List<String> physicalRestrictions = medicalRecord.getPhysicalRestrictions();
        boolean restrictionFound = physicalRestrictions.stream().anyMatch(restriction -> restriction.contains(medicalIssues));

        if (restrictionFound) {
            System.out.println("Physical restriction found, this treatment may not be appropriate");
        }
    }



}
