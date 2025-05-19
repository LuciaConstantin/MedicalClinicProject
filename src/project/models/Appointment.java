package project.models;

import java.time.LocalDate;
import java.time.LocalTime;

public class Appointment {
    private long id;
    private Doctor doctor;
    private Patient patient;
    private LocalDate appointmentDate;
    private TimeInterval appointmentInterval;
    private MedicalServices medicalService;
    private Diagnostic diagnostic;

    public Appointment(Doctor doctor, Patient patient, LocalDate appointmentDate, LocalTime appointmentStart,  MedicalServices medicalService) {
        this.doctor = doctor;
        this.patient = patient;
        this.appointmentDate = appointmentDate;
        LocalTime appointmentEnd = appointmentStart.plusMinutes(medicalService.getServiceTime());
        this.appointmentInterval = new TimeInterval(appointmentStart, appointmentEnd);
        this.medicalService = medicalService;
        this.diagnostic = null;
    }

    public Appointment(long id, Doctor doctor, Patient patient, LocalDate appointmentDate, LocalTime appointmentStart,  MedicalServices medicalService) {
        this.id= id;
        this.doctor = doctor;
        this.patient = patient;
        this.appointmentDate = appointmentDate;
        LocalTime appointmentEnd = appointmentStart.plusMinutes(medicalService.getServiceTime());
        this.appointmentInterval = new TimeInterval(appointmentStart, appointmentEnd);
        this.medicalService = medicalService;
        this.diagnostic = null;
    }




    public Doctor getDoctor() {

        return doctor;
    }

    public Patient getPatient() {

        return patient;
    }

    public LocalDate getAppointmentDate() {

        return appointmentDate;
    }

    public TimeInterval getAppointmentInterval() {

        return appointmentInterval;
    }

    public MedicalServices getMedicalService() {

        return medicalService;
    }

    public Diagnostic getDiagnostic() {

        return diagnostic;
    }

    public void setDiagnostic(Diagnostic diagnostic) {

        this.diagnostic = diagnostic;
    }


    public void setAppointmentDate(LocalDate appointmentDate) {

        this.appointmentDate = appointmentDate;
    }

    public void setAppointmentInterval(TimeInterval appointmentInterval) {
        this.appointmentInterval = appointmentInterval;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public void setMedicalService(MedicalServices medicalService) {
        this.medicalService = medicalService;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + id +
                ", doctor=" + doctor +
                ", patient=" + patient +
                ", appointmentDate=" + appointmentDate +
                ", appointmentInterval=" + appointmentInterval +
                ", medicalService=" + medicalService +
                ", diagnostic=" + diagnostic +
                '}';
    }
}
