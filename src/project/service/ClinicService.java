package project.service;

import project.config.ConnectionProvider;
import project.exceptions.MedicalRecordException;
import project.exceptions.MedicalServiceException;
import project.exceptions.MedicalServiceSpecialtyException;
import project.exceptions.PatientException;
import project.models.MedicalRecord;
import project.models.MedicalServices;
import project.models.Patient;
import project.models.Specialty;
import project.repository.ClinicRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ClinicService {
    private final ClinicRepository clinicRepository = ClinicRepository.getInstance();

    public ClinicService() {}

    public void insertMedicalRecord(MedicalRecord medicalRecord) {
        try (Connection connection = ConnectionProvider.getConnection()) {
            clinicRepository.insertDataMedicalRecord(connection, medicalRecord);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

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

    public void insertSpecialtyMedicalServiceDB(long specialtyId, long medicalServiceId) {
        try (Connection connection = ConnectionProvider.getConnection()) {
            clinicRepository.insertSpecialtyMedicalService(connection, specialtyId, medicalServiceId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<MedicalServices> getMedicalServicesForSpecialtyById(long id) {
        Optional<List<MedicalServices> > client = clinicRepository.getMedicalServicesForSpecialty(ConnectionProvider.getConnection(), id);
        return client.orElseThrow(MedicalServiceSpecialtyException::new);
    }


}
