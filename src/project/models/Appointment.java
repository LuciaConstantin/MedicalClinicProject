package project.models;

import java.time.LocalDate;
import java.time.LocalTime;

public class Appointment {
    private static int nextId = 1;
    private int appointmentId;
    private Doctor doctor;
    private Patient patient;
    private LocalDate appointmentDate;
    private TimeInterval appointmentInterval;
    private MedicalServices medicalService;
    private Diagnostic diagnostic;

    public Appointment(Doctor doctor, Patient patient, LocalDate appointmentDate, LocalTime appointmentStart,  MedicalServices medicalService) {
        this.appointmentId = nextId++;
        this.doctor = doctor;
        this.patient = patient;
        this.appointmentDate = appointmentDate;
        LocalTime appointmentEnd = appointmentStart.plusMinutes(medicalService.getServiceTime());
        this.appointmentInterval = new TimeInterval(appointmentStart, appointmentEnd);
        this.medicalService = medicalService;
        this.diagnostic = null;
    }


    public int getAppointmentId() {

        return appointmentId;
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


}
