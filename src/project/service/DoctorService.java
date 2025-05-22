package project.service;

import project.ClinicDAO;
import project.config.ConnectionProvider;
import project.models.Doctor;
import project.repository.DoctorRepository;
import project.exceptions.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

public class DoctorService extends ClinicDAO<Doctor> {
    private final DoctorRepository doctorRepository = DoctorRepository.getInstance();

    public DoctorService() {}

    @Override
    public void create(Doctor doctor) {
        try (Connection connection = ConnectionProvider.getConnection()) {
            doctorRepository.insertDoctor(connection, doctor);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Doctor getById(long id) {
        Optional<Doctor> client = doctorRepository.getDoctorById(ConnectionProvider.getConnection(), id);
        return client.orElseThrow(DoctorException::new);
    }

}
