package project.models;

import java.util.List;

public final class MedicalRecord {
    private long id;
    private List<String> allergies;
    private List<String> chronicConditions;
    private List<String> physicalRestrictions;

    public MedicalRecord(List allergies, List chronicConditions, List physicalRestrictions) {
        this.allergies = allergies;
        this.chronicConditions = chronicConditions;
        this.physicalRestrictions = physicalRestrictions;

    }

    public MedicalRecord(long id, List allergies, List chronicConditions, List physicalRestrictions) {
        this.id = id;
        this.allergies = allergies;
        this.chronicConditions = chronicConditions;
        this.physicalRestrictions = physicalRestrictions;

    }

    public List<String> getAllergies() {
        return allergies;
    }

    public List<String> getPhysicalRestrictions() {
        return physicalRestrictions;
    }

    public void displayMedicalRecord() {
        System.out.println("Medical Record");

        if (allergies != null) {
            System.out.println("Allergies: ");
            for (String allergy : allergies) {
                System.out.printf(allergy + ", ");
            }
        }

        if (chronicConditions != null) {
            System.out.println("\nChronic Conditions: ");
            for (String chronicCondition : chronicConditions) {
                System.out.printf(chronicCondition +", ");
            }
        }

        if (physicalRestrictions != null) {
            System.out.println("\nPhysical Restrictions: ");
            for (String restriction : physicalRestrictions) {
                System.out.printf(restriction +", ");
            }
        }

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setAllergies(List<String> allergies) {
        this.allergies = allergies;
    }

    public List<String> getChronicConditions() {
        return chronicConditions;
    }

    public void setChronicConditions(List<String> chronicConditions) {
        this.chronicConditions = chronicConditions;
    }

    public void setPhysicalRestrictions(List<String> physicalRestrictions) {
        this.physicalRestrictions = physicalRestrictions;
    }

    @Override
    public String toString() {
        return "MedicalRecord{" +
                "id=" + id +
                ", allergies=" + allergies +
                ", chronicConditions=" + chronicConditions +
                ", physicalRestrictions=" + physicalRestrictions +
                '}';
    }
}
