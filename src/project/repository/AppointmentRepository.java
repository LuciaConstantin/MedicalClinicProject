package project.repository;

import project.models.*;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class AppointmentRepository {
    private static AppointmentRepository instance;

    private AppointmentRepository() {
    }

    public static AppointmentRepository getInstance() {
        if (instance == null) {
            instance = new AppointmentRepository();
        }
        return instance;
    }

    public void insertAppointment(Connection connection, Appointment appointment) {

        String sql = """
                INSERT INTO appointment (doctor_id, patient_id, appointment_date, 
                                        medical_service_id, diagnostic_id)
                VALUES (?, ?, ?, ?, ?)
                """;
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, appointment.getDoctor().getId());
            ps.setLong(2, appointment.getPatient().getId());
            LocalDate dA = appointment.getAppointmentDate();
            ps.setDate(3, java.sql.Date.valueOf(dA));
            ps.setLong(4, appointment.getMedicalService().getId());
            if (appointment.getDiagnostic() != null) {
                ps.setLong(5, appointment.getDiagnostic().getId());
            } else {
                ps.setNull(5, Types.NULL);
            }

            int insertedRows = ps.executeUpdate();

            System.out.println("Inserted " + insertedRows + " rows in appointment");

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                long appointmentId = rs.getLong(1);
                appointment.setId(appointmentId);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String sql2 = """
                INSERT INTO appointment_time_interval (appointment_id, start_appointment, end_appointment)
                VALUES (?, ?, ?)
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql2)) {
            ps.setLong(1, appointment.getId());
            Time timeStart = Time.valueOf(appointment.getAppointmentInterval().start());
            ps.setTime(2, timeStart);
            Time timeEnd = Time.valueOf(appointment.getAppointmentInterval().end());
            ps.setTime(3, timeEnd);

            int insertedRows = ps.executeUpdate();

            System.out.println("Inserted " + insertedRows + " rows in appointment_time_interval");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Appointment> getAppointmentById(Connection connection, long id) {
        String sqlAppointment = "SELECT * FROM appointment WHERE id = ?";
        String sqlInterval = "SELECT start_appointment, end_appointment FROM appointment_time_interval WHERE appointment_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sqlAppointment)) {
            ps.setLong(1, id);

            try (ResultSet result = ps.executeQuery()) {
                if (!result.next()) return Optional.empty();

                DoctorRepository docRepo = DoctorRepository.getInstance();
                PatientRepository patientRepo = PatientRepository.getInstance();
                MedicalServiceRepository medRepo = MedicalServiceRepository.getInstance();
                DiagnosticRepository diRepo = DiagnosticRepository.getInstance();

                Optional<Doctor> optDoc = docRepo.getDoctorById(connection, result.getLong("doctor_id"));
                Optional<Patient> optPat = patientRepo.getPatientById(connection, result.getLong("patient_id"));
                LocalDate date = result.getDate("appointment_date").toLocalDate();
                Optional<MedicalServices> optServ = medRepo.getMedicalServiceById(connection, result.getLong("medical_service_id"));

                Optional<Diagnostic> optDiag = Optional.empty();
                long diagId = result.getLong("diagnostic_id");
                Diagnostic diag = null;
                if (!result.wasNull()) {
                    optDiag = diRepo.getDiagnosticById(connection, diagId);
                    diag = optDiag.get();
                }

                Doctor doc = optDoc.get();
                Patient pat = optPat.get();
                MedicalServices serv = optServ.get();


                try (PreparedStatement ps2 = connection.prepareStatement(sqlInterval)) {
                    ps2.setLong(1, id);

                    try (ResultSet result2 = ps2.executeQuery()) {
                        if (result2.next()) {
                            LocalTime startT = result2.getTime("start_appointment").toLocalTime();
                            LocalTime endT = result2.getTime("end_appointment").toLocalTime();

                            TimeInterval interval = new TimeInterval(startT, endT);

                            Builder builder = new AppointmentBuilder();
                            Director director = new Director(builder);

                            Appointment app;
                            if (diag != null) {
                                app = director.createCompleteAppointment(id, doc, pat, date, startT, serv, diag);
                            } else {
                                app = director.createAppointmentWithoutDiagnostic(id, doc, pat,date, startT, serv);

                            }

                            return Optional.of(app);
                        }
                    }
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteAppointment(Connection connection, long id) {

        String sql = """
                DELETE FROM appointment 
                WHERE id = ?
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            int deletedClient = ps.executeUpdate();
            System.out.println("Deleted " + deletedClient + " appointment");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateAppointmentDiagnostic(Connection connection, Appointment appointment) {
        String sql = """
                UPDATE appointment 
                SET diagnostic_id = ?
                WHERE id = ?
                """;

        DiagnosticRepository diagRepo = DiagnosticRepository.getInstance();
        diagRepo.insertDiagnostic(connection, appointment.getDiagnostic());

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, appointment.getDiagnostic().getId());
            ps.setLong(2, appointment.getId());

            int insertedRows = ps.executeUpdate();
            System.out.println("Updated " + insertedRows + " rows in appointment");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void rescheduleAppointment(Connection connection, Appointment appointment) {
        String sql = """
                UPDATE appointment 
                SET appointment_date = ?
                WHERE id = ?
                """;

        String sql2 = """
                UPDATE appointment_time_interval 
                SET start_appointment = ?, end_appointment = ?
                WHERE appointment_id = ?
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql2)) {
            Time timeStart = Time.valueOf(appointment.getAppointmentInterval().start());
            Time timeEnd = Time.valueOf(appointment.getAppointmentInterval().end());

            ps.setTime(1, timeStart);
            ps.setTime(2, timeEnd);
            ps.setLong(3, appointment.getId());

            int insertedRows = ps.executeUpdate();

            System.out.println("Updated " + insertedRows + " rows in appointment_time_interval");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try (PreparedStatement ps1 = connection.prepareStatement(sql)) {
            Date date = Date.valueOf(appointment.getAppointmentDate());
            ps1.setDate(1, date);
            ps1.setLong(2, appointment.getId());


            int insertedRows = ps1.executeUpdate();
            System.out.println("Updated " + insertedRows + " rows in appointment");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<List<Appointment>> getAllData(Connection connection) {
        List<Appointment> appointments = new ArrayList<>();
        String sql = """
                SELECT id
                FROM appointment
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            try (ResultSet result = ps.executeQuery()) {
                while (result.next()) {
                    long id = result.getLong(1);
                    Optional<Appointment> app = getAppointmentById(connection, id);
                    app.ifPresent(appointments::add);
                }
                return appointments.isEmpty() ? Optional.empty() : Optional.of(appointments);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

}
