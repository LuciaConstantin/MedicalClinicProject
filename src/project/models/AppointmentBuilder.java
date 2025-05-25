package project.models;

import java.time.LocalDate;
import java.time.LocalTime;

public class AppointmentBuilder implements Builder {
    private Appointment appointment;
    private long id;
    private Doctor doctor;
    private Patient patient;
    private LocalDate appointmentDate;
    private LocalTime startTime;
    private MedicalServices medicalService;
    private Diagnostic diagnostic;

    public AppointmentBuilder() {
        this.appointment= new Appointment();
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    @Override
    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public void setMedicalServices(MedicalServices medicalService) {
        this.medicalService = medicalService;
    }

    @Override
    public void setDiagnostic(Diagnostic diagnostic) {
        this.diagnostic = diagnostic;
    }

    @Override
    public Appointment build() {
        Appointment appointment = new Appointment();
        appointment.setId(id);
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setAppointmentDate(appointmentDate);
        appointment.setMedicalService(medicalService);

        if (startTime != null && medicalService != null) {
            LocalTime endTime = startTime.plusMinutes(medicalService.getServiceTime());
            TimeInterval interval = new TimeInterval(startTime, endTime);
            appointment.setAppointmentInterval(interval);
        }

        if (diagnostic != null) {
            appointment.setDiagnostic(diagnostic);
        }

        return appointment;
    }

}
