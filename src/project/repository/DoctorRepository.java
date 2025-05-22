package project.repository;

import project.models.Doctor;
import project.models.Schedule;
import project.models.Specialty;
import project.models.TimeInterval;

import java.sql.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Optional;

public class DoctorRepository {
    private static DoctorRepository instance;

    private DoctorRepository() {
    }

    public static DoctorRepository getInstance() {
        if (instance == null) {
            instance = new DoctorRepository();
        }
        return instance;
    }

    public void insertDoctor(Connection connection, Doctor doctor) {

        String sql = """
                INSERT INTO doctor (first_name, last_name, personal_ID,
                                           email, phone, birth_date, hire_date,specialty_id, salary )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, doctor.getFirstName());
            ps.setString(2, doctor.getLastName());
            ps.setString(3, doctor.getPersonalID());
            ps.setString(4, doctor.getEmail());
            ps.setString(5, doctor.getPhone());
            LocalDate dob = doctor.getDateOfBirth();
            ps.setDate(6, java.sql.Date.valueOf(dob));
            LocalDate hd = doctor.getHireDate();
            ps.setDate(7, java.sql.Date.valueOf(hd));
            ps.setLong(8, doctor.getSpecialty().getId());
            ps.setDouble(9, doctor.getSalary());

            int insertedRows = ps.executeUpdate();
            System.out.println("Inserted " + insertedRows + " rows in doctor");


            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                long generatedId = rs.getLong(1);
                doctor.setId(generatedId);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String sql3 = """
                INSERT INTO schedule (doctor_id, day, start_time, end_time)
                VALUES (?, ?, ?, ?)
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql3)) {
            TimeInterval[][] timeInterval = doctor.getSchedule().getSchedule();
            for (int day = 0; day < timeInterval.length; day++) {
                TimeInterval[] intervals = timeInterval[day];
                for (int interval = 0; interval < intervals.length; interval++) {
                    TimeInterval specificInterval = intervals[interval];
                    if (specificInterval != null) {
                        ps.setLong(1, doctor.getId());
                        ps.setInt(2, day);
                        ps.setTime(3, Time.valueOf(specificInterval.start()));
                        ps.setTime(4, Time.valueOf(specificInterval.end()));
                        ps.addBatch();
                    }
                }

            }

            ps.executeBatch();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public Optional<Doctor> getDoctorById(Connection connection, long id) {
        String sql = """
                SELECT d.id, d.first_name, d.last_name, d.personal_ID, d.email, d.phone,
                d.birth_date, d.hire_date, d.salary,  sh.day, sh.start_time, sh.end_time,
                sy.id, sy.specialty_name, sy.starting_salary
                FROM schedule sh JOIN  doctor d ON d.id = sh.doctor_id 
                JOIN specialty sy ON d.specialty_id = sy.id
                WHERE d.id = ?
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                Doctor doctor = null;
                Schedule schedule = new Schedule();
                boolean found = false;

                while (rs.next()) {
                    if (!found) {
                        Specialty specialty = new Specialty(
                                rs.getLong("id"),
                                rs.getString("specialty_name"),
                                rs.getDouble("starting_salary")
                        );

                        doctor = new Doctor(
                                rs.getString("first_name"),
                                rs.getString("last_name"),
                                rs.getString("personal_ID"),
                                rs.getString("email"),
                                rs.getString("phone"),
                                rs.getDate("birth_date").toLocalDate(),
                                rs.getDate("hire_date").toLocalDate(),
                                specialty,
                                schedule
                        );
                        doctor.setId(rs.getLong("id"));
                        doctor.setSalary(rs.getDouble("salary"));
                        schedule.setId(rs.getLong("id"));
                        found = true;
                    }

                    int dayIndex = rs.getInt("day");
                    Time start = rs.getTime("start_time");
                    Time end = rs.getTime("end_time");

                    if (start != null && end != null) {
                        String day = DayOfWeek.of(dayIndex + 1).name();
                        schedule.addToSchedule(day, start.toLocalTime(), end.toLocalTime());
                    }

                }

                return found ? Optional.of(doctor) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
