package Project;

public class Specialty {
    private String specialtyName;
    private String [] commonConditions;
    private double startingSalary;
    private MedicalServices[] medicalServices;

    public Specialty() {
    }

    public Specialty(String specialtyName, String [] commonConditions, double startingSalary, MedicalServices[] medicalServices) {
        this.specialtyName = specialtyName;
        this.commonConditions = commonConditions;
        this.startingSalary = startingSalary;
        this.medicalServices = medicalServices;

    }

    public String getSpecialtyName() {

        return specialtyName;
    }

    public MedicalServices[] getMedicalServices() {
        return medicalServices;
    }

    public double getStartingSalary() {

        return startingSalary;
    }
}
