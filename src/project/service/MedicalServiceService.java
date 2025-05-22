package project.service;

import project.ClinicDAO;
import project.config.ConnectionProvider;
import project.models.MedicalServices;
import project.repository.MedicalServiceRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import project.exceptions.*;

public class MedicalServiceService extends ClinicDAO<MedicalServices> {
    private final MedicalServiceRepository medicalServiceRepository = MedicalServiceRepository.getInstance();

    public MedicalServiceService() {}

    @Override
    public void create(MedicalServices medicalService) {
        try (Connection connection = ConnectionProvider.getConnection()) {
            medicalServiceRepository.insertMedicalService(connection, medicalService);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public MedicalServices getById(long id) {
        Optional<MedicalServices> client = medicalServiceRepository.getMedicalServiceById(ConnectionProvider.getConnection(), id);
        return client.orElseThrow(MedicalServiceException::new);
    }

}
