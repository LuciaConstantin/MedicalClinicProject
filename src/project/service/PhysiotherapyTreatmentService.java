package project.service;

import project.ClinicDAO;
import project.config.ConnectionProvider;
import project.models.PhysiotherapyTreatment;
import project.exceptions.*;
import project.repository.PhysiotherapyTreatmentRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;


public class PhysiotherapyTreatmentService extends ClinicDAO<PhysiotherapyTreatment> {
    private final PhysiotherapyTreatmentRepository physiotherapyTreatmentRepository = PhysiotherapyTreatmentRepository.getInstance();

    public PhysiotherapyTreatmentService() {}

    @Override
    public void create(PhysiotherapyTreatment physiotherapyTreatment) {
        try (Connection connection = ConnectionProvider.getConnection()) {
            physiotherapyTreatmentRepository.insertPhysiotherapyTreatment(connection, physiotherapyTreatment);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PhysiotherapyTreatment getById(long id) {
        Optional<PhysiotherapyTreatment> client = physiotherapyTreatmentRepository.getPhysiotherapyTreatmentById(ConnectionProvider.getConnection(), id);
        return client.orElseThrow(PhysiothraphyTreatment::new);
    }



}
