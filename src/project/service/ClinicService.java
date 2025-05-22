package project.service;

import project.config.ConnectionProvider;
import project.exceptions.*;
import project.models.*;
import project.repository.ClinicRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ClinicService {
    private final ClinicRepository clinicRepository = ClinicRepository.getInstance();

    public ClinicService() {}



    public MedicalRecord getMedicalRecordUsingId(long id) {
        Optional<MedicalRecord> client = clinicRepository.getMedicalRecordById(ConnectionProvider.getConnection(), id);
        return client.orElseThrow(MedicalRecordException::new);
    }

    public void insertPatientDB(Patient patient) {
        try (Connection connection = ConnectionProvider.getConnection()) {
            clinicRepository.insertPatient(connection, patient);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Patient getPatientByIdDB(long id) {
        Optional<Patient> client = clinicRepository.getPatientById(ConnectionProvider.getConnection(), id);
        return client.orElseThrow(PatientException::new);
    }

    public void insertMedicalService(MedicalServices medicalService) {
        try (Connection connection = ConnectionProvider.getConnection()) {
            clinicRepository.insertMedicalService(connection, medicalService);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public MedicalServices getMedicalServiceByIdDB(long id) {
        Optional<MedicalServices> client = clinicRepository.getMedicalServiceById(ConnectionProvider.getConnection(), id);
        return client.orElseThrow(MedicalServiceException::new);
    }

    public void insertSpecialtyDB(Specialty specialty) {
        try (Connection connection = ConnectionProvider.getConnection()) {
            clinicRepository.insertSpecialty(connection, specialty);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public Specialty getSpecialtyByIdDB(long id) {
        Optional<Specialty> client = clinicRepository.getSpecialtyById(ConnectionProvider.getConnection(), id);
        return client.orElseThrow(MedicalServiceException::new);
    }

    public void insertDoctorDb(Doctor doctor) {
        try (Connection connection = ConnectionProvider.getConnection()) {
            clinicRepository.insertDoctor(connection, doctor);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Doctor getDoctorByIdDb(long id) {
        Optional<Doctor> client = clinicRepository.getDoctorById(ConnectionProvider.getConnection(), id);
        return client.orElseThrow(DoctorException::new);
    }


    public void insertMedicationTreatmentDb(MedicationTreatment med) {
        try (Connection connection = ConnectionProvider.getConnection()) {
            clinicRepository.insertMedicationTreatment(connection, med);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public MedicationTreatment getMedicationTreatmentDb(long id) {
        Optional<MedicationTreatment> client = clinicRepository.getMedicationTreatmentById(ConnectionProvider.getConnection(), id);
        return client.orElseThrow(MedicationTreatmentException::new);
    }

    public void insertPhysiotherapyTreatmentDb(PhysiotherapyTreatment phy) {
        try (Connection connection = ConnectionProvider.getConnection()) {
            clinicRepository.insertPhysiotherapyTreatment(connection, phy);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public PhysiotherapyTreatment getPhysiotherapyTreatmentDb(long id) {
        Optional<PhysiotherapyTreatment> client = clinicRepository.getPhysiotherapyTreatmentById(ConnectionProvider.getConnection(), id);
        return client.orElseThrow(PhysiothraphyTreatment::new);
    }

    public void insertDiagnosticDb(Diagnostic diagnostic) {
        try (Connection connection = ConnectionProvider.getConnection()) {
            clinicRepository.insertDiagnostic(connection, diagnostic);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Diagnostic getDiagnosticDb(long id) {
        Optional<Diagnostic> client = clinicRepository.getDiagnosticById(ConnectionProvider.getConnection(), id);
        return client.orElseThrow(PhysiothraphyTreatment::new);
    }


    public void insertAppointmentDb(Appointment appointment) {
        try (Connection connection = ConnectionProvider.getConnection()) {
            clinicRepository.insertAppointment(connection, appointment);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Appointment getAppointmentDb(long id) {
        Optional<Appointment> client = clinicRepository.getAppointmentById(ConnectionProvider.getConnection(), id);
        return client.orElseThrow(PhysiothraphyTreatment::new);
    }

    public void deleteAppointmentById(long id) {
        try (Connection connection = ConnectionProvider.getConnection()) {
            clinicRepository.deleteAppointment(connection, id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deletePatientById(long id) {
        try (Connection connection = ConnectionProvider.getConnection()) {
            clinicRepository.deletePatient(connection, id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void updateAppointmentDiagnosticBd(long appointmentId, long diagnosticId ) {
        try (Connection connection = ConnectionProvider.getConnection()) {
            clinicRepository.updateAppointmentDiagnostic(connection, appointmentId, diagnosticId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void rescheduleAppointmentDb(Appointment appointment) {
        try (Connection connection = ConnectionProvider.getConnection()) {
            clinicRepository.rescheduleAppointment(connection, appointment);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
