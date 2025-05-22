package project.repository;

import project.models.MedicalServices;
import project.models.Specialty;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SpecialtyRepository {
    private static SpecialtyRepository instance;

    private SpecialtyRepository() {
    }

    public static SpecialtyRepository getInstance() {
        if (instance == null) {
            instance = new SpecialtyRepository();
        }
        return instance;
    }

    public void insertSpecialty(Connection connection, Specialty specialty) {

        String sql = """
                INSERT INTO specialty (specialty_name, starting_salary)
                VALUES (?, ?)
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, specialty.getSpecialtyName());
            ps.setDouble(2, specialty.getStartingSalary());

            int insertedRows = ps.executeUpdate();

            System.out.println("Inserted " + insertedRows + " rows in specialty");

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                long specialtyId = rs.getLong(1);
                specialty.setId(specialtyId);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (specialty.getMedicalServices() != null) {
            for (MedicalServices medicalService : specialty.getMedicalServices()) {
                insertSpecialtyMedicalService(connection, specialty.getId(), medicalService.getId());
            }
        }

    }

    public Optional<Specialty> getSpecialtyById(Connection connection, long id) {
        String sql = """
                SELECT * 
                FROM specialty 
                WHERE id = ?
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);

            try (ResultSet result = ps.executeQuery()) {
                if (result.next()) {

                    Optional<List<MedicalServices>> medicalServices = getMedicalServicesForSpecialty(connection, id);
                    MedicalServices[] medicalServiceArray = new MedicalServices[0];

                    if (medicalServices.isPresent()) {
                        List<MedicalServices> medSArr = medicalServices.get();
                        medicalServiceArray = medSArr.toArray(new MedicalServices[0]);
                    }


                    return Optional.of(new Specialty(
                            result.getLong("id"),
                            result.getString("specialty_name"),
                            result.getDouble("starting_salary"),
                            medicalServiceArray
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    public void insertSpecialtyMedicalService(Connection connection, long specialtyId, long medicalServiceId) {
        String sql = """
                INSERT INTO specialty_medical_service ( specialty_id, medical_service_id )
                VALUES (?, ?)
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, specialtyId);
            ps.setLong(2, medicalServiceId);

            int insertedRows = ps.executeUpdate();

            System.out.println("Inserted " + insertedRows + " rows in specialty_medical_service");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public Optional<List<MedicalServices>> getMedicalServicesForSpecialty(Connection connection, long specialtyId) {
        String sql = """
                    SELECT ms.id, ms.service_name, ms.service_price, ms.service_time
                    FROM specialty_medical_service sms
                    JOIN medical_service ms ON sms.medical_service_id = ms.id
                    WHERE sms.specialty_id = ?
                """;

        List<MedicalServices> services = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, specialtyId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    MedicalServices service = new MedicalServices(
                            rs.getLong("id"),
                            rs.getString("service_name"),
                            rs.getDouble("service_price"),
                            rs.getInt("service_time")
                    );
                    services.add(service);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return services.isEmpty() ? Optional.empty() : Optional.of(services);
    }


}
