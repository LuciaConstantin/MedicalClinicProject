package project.service;

import project.ClinicDAO;
import project.config.ConnectionProvider;
import project.models.MedicationTreatment;
import project.repository.MedicationRepository;
import project.exceptions.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

public class MedicationTreatmentService extends ClinicDAO<MedicationTreatment> {
    private final MedicationRepository medicationTreatmentRepository = MedicationRepository.getInstance();

    public MedicationTreatmentService() {}

    @Override
    public void create(MedicationTreatment medicationTreatment) {
        try (Connection connection = ConnectionProvider.getConnection()) {
            medicationTreatmentRepository.insertMedicationTreatment(connection, medicationTreatment);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public MedicationTreatment getById(long id) {
        Optional<MedicationTreatment> client = medicationTreatmentRepository.getMedicationTreatmentById(ConnectionProvider.getConnection(), id);
        return client.orElseThrow(MedicationTreatmentException::new);
    }




}
