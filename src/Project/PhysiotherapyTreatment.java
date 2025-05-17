package Project;


import java.util.List;

public class PhysiotherapyTreatment implements Treatment {
    private String exerciseName;
    private int repetitions;
    private String medicalIssues;

    public PhysiotherapyTreatment(String exerciseName, int repetitions, String medicalIssues) {
        this.exerciseName = exerciseName;
        this.repetitions = repetitions;
        this.medicalIssues = medicalIssues;
    }

    @Override
    public String treatmentType() {
        return "Physiotherapy treatment";
    }

    @Override
    public String treatmentDescription() {

        return ("Exercise name: " + this.exerciseName + " repetitions: " + this.repetitions + " medical issue: " + this.medicalIssues);
    }

    @Override
    public Boolean verifyTreatment(MedicalRecord medicalRecord) {

        List<String> physicalRestrictions = medicalRecord.getPhysicalRestrictions();
        boolean restrictionFound = physicalRestrictions.stream().anyMatch(restriction -> restriction.contains(medicalIssues));

        if (restrictionFound) {
            return false;
        }
        return true;
    }

    public String getExerciseName() {
        return exerciseName;
    }



}
