package project.serviceDB;


import project.ClinicDAO;
import project.config.ConnectionProvider;
import project.models.Specialty;
import project.exceptions.*;
import project.repository.SpecialtyRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

public class SpecialtyService extends ClinicDAO<Specialty> {
    private final SpecialtyRepository specialtyRepository = SpecialtyRepository.getInstance();

    public SpecialtyService() {}

    @Override
    public void create(Specialty specialty){
        try (Connection connection = ConnectionProvider.getConnection()) {
            specialtyRepository.insertSpecialty(connection, specialty);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Specialty getById(long id) {
        Optional<Specialty> client = specialtyRepository.getSpecialtyById(ConnectionProvider.getConnection(), id);
        return client.orElseThrow(MedicalServiceException::new);
    }




}
