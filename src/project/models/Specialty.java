package project.models;

import java.util.Arrays;

public class Specialty {
    private long id;
    private String specialtyName;
    private double startingSalary;
    private MedicalServices[] medicalServices;

    public Specialty() {
    }

    public Specialty(String specialtyName,  double startingSalary, MedicalServices[] medicalServices) {
        this.specialtyName = specialtyName;
        this.startingSalary = startingSalary;
        this.medicalServices = medicalServices;

    }

    public Specialty(long id, String specialtyName,  double startingSalary) {
        this.id = id;
        this.specialtyName = specialtyName;
        this.startingSalary = startingSalary;

    }

    public Specialty(String specialtyName,  double startingSalary) {
        this.specialtyName = specialtyName;
        this.startingSalary = startingSalary;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setSpecialtyName(String specialtyName) {
        this.specialtyName = specialtyName;
    }

    public void setStartingSalary(double startingSalary) {
        this.startingSalary = startingSalary;
    }

    public void setMedicalServices(MedicalServices[] medicalServices) {
        this.medicalServices = medicalServices;
    }

    @Override
    public String toString() {
        return "Specialty{" +
                "specialtyName='" + specialtyName + '\'' +
                ", startingSalary=" + startingSalary +
                ", medicalServices=" + Arrays.toString(medicalServices) +
                '}';
    }
}
