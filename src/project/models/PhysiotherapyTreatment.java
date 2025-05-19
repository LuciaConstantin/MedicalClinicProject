package project.models;


import java.util.List;

public class PhysiotherapyTreatment implements Treatment {
    private long id;
    private String exerciseName;
    private int repetitions;
    private String medicalIssues;

    public PhysiotherapyTreatment(String exerciseName, int repetitions, String medicalIssues) {
        this.exerciseName = exerciseName;
        this.repetitions = repetitions;
        this.medicalIssues = medicalIssues;
    }

    public PhysiotherapyTreatment(long id, String exerciseName, int repetitions, String medicalIssues) {
        this.id = id;
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

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public int getRepetitions() {
        return repetitions;
    }

    public void setRepetitions(int repetitions) {
        this.repetitions = repetitions;
    }

    public String getMedicalIssues() {
        return medicalIssues;
    }

    public void setMedicalIssues(String medicalIssues) {
        this.medicalIssues = medicalIssues;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "PhysiotherapyTreatment{" +
               "exerciseName='" + exerciseName + '\'' +
               ", repetitions=" + repetitions +
               ", medicalIssues='" + medicalIssues + '\'' +
               '}';
    }
}
