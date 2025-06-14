package project.serviceDB;

import project.ClinicDAO;
import project.config.ConnectionProvider;
import project.exceptions.AppointmentException;
import project.models.Appointment;
import project.repository.AppointmentRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class AppointmnetService extends ClinicDAO<Appointment> {
    private final AppointmentRepository appointmentRepository = AppointmentRepository.getInstance();

    public AppointmnetService() {}

    @Override
    public void create(Appointment appointment) {
        try (Connection connection = ConnectionProvider.getConnection()) {
            appointmentRepository.insertAppointment(connection, appointment);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Appointment getById(long id) {
        Optional<Appointment> client = appointmentRepository.getAppointmentById(ConnectionProvider.getConnection(), id);
        return client.orElseThrow(AppointmentException::new);
    }


    public void delete(long id) {
        try (Connection connection = ConnectionProvider.getConnection()) {
            appointmentRepository.deleteAppointment(connection, id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void update(Appointment appointment) {
        try (Connection connection = ConnectionProvider.getConnection()) {
            appointmentRepository.updateAppointmentDiagnostic(connection, appointment);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void reschedule(Appointment appointment) {
        try (Connection connection = ConnectionProvider.getConnection()) {
            appointmentRepository.rescheduleAppointment(connection, appointment);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Appointment> getAll(){
        Optional<List<Appointment>> client = appointmentRepository.getAllData(ConnectionProvider.getConnection());
        return client.orElseThrow(AppointmentException::new);
    }


}
