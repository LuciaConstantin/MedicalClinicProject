package Project;

import java.util.List;

public final class MedicalRecord {
    private List<String> allergies;
    private List<String> chronicConditions;
    private List<String> physicalRestrictions;

    MedicalRecord(List allergies, List chronicConditions, List physicalRestrictions) {
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
                System.out.println(allergy);
            }
        }

        if (chronicConditions != null) {
            System.out.println("Chronic Conditions: ");
            for (String chronicCondition : chronicConditions) {
                System.out.println(chronicCondition);
            }
        }

        if (physicalRestrictions != null) {
            System.out.println("Physical Restrictions: ");
            for (String restriction : physicalRestrictions) {
                System.out.println(restriction);
            }
        }

    }


}
