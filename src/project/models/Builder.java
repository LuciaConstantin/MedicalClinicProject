package project.models;

import java.time.LocalDate;
import java.time.LocalTime;

public interface Builder {
    void setId(long id);
    void setDoctor(Doctor doctor);
    void setPatient(Patient patient);
    void setAppointmentDate(LocalDate date);
    void setStartTime(LocalTime startTime);
    void setMedicalServices(MedicalServices medicalServices);
    void setDiagnostic(Diagnostic diagnostic);
    void setInvoiceAmount(double invoiceAmount);
    Appointment build();
}
