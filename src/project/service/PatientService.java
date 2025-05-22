package project.service;

import project.ClinicDAO;
import project.config.ConnectionProvider;
import project.models.Patient;
import project.repository.ClinicRepository;
import project.repository.PatientRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import project.exceptions.*;

public class PatientService extends ClinicDAO<Patient> {
    private final PatientRepository patientRepository = PatientRepository.getInstance();

    public PatientService() {}

    @Override
    public void create(Patient patient){
        try (Connection connection = ConnectionProvider.getConnection()) {
            patientRepository.insertPatient(connection, patient);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Patient getById(long id) {
        Optional<Patient> client = patientRepository.getPatientById(ConnectionProvider.getConnection(), id);
        return client.orElseThrow(PatientException::new);
    }

    public void deleteById(long id) {
        try (Connection connection = ConnectionProvider.getConnection()) {
            patientRepository.deletePatient(connection, id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
