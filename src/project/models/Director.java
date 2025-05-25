package project.models;

import java.time.LocalDate;
import java.time.LocalTime;

public class Director {
    private Builder builder;

    public Director(Builder builder) {
        this.builder = builder;
    }

    public Appointment createCompleteAppointment(long id, Doctor doctor, Patient patient, LocalDate appointmentDate, LocalTime appointmentStart, MedicalServices medicalService, Diagnostic diagnostic) {
        builder.setId(id);
        builder.setDoctor(doctor);
        builder.setPatient(patient);
        builder.setAppointmentDate(appointmentDate);
        builder.setStartTime(appointmentStart);
        builder.setMedicalServices(medicalService);
        builder.setDiagnostic(diagnostic);
        return builder.build();
    }


    public Appointment createInitialAppointment(Doctor doctor, Patient patient, LocalDate appointmentDate, LocalTime appointmentStart, MedicalServices medicalService) {
        builder.setDoctor(doctor);
        builder.setPatient(patient);
        builder.setAppointmentDate(appointmentDate);
        builder.setStartTime(appointmentStart);
        builder.setMedicalServices(medicalService);
        return builder.build();
    }

    public Appointment createAppointmentWithoutDiagnostic(long id, Doctor doctor, Patient patient, LocalDate appointmentDate, LocalTime appointmentStart, MedicalServices medicalService) {
        builder.setId(id);
        builder.setDoctor(doctor);
        builder.setPatient(patient);
        builder.setAppointmentDate(appointmentDate);
        builder.setStartTime(appointmentStart);
        builder.setMedicalServices(medicalService);
        builder.setDiagnostic(null);

        return builder.build();
    }
}
