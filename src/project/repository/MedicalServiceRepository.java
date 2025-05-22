package project.repository;

import project.models.MedicalServices;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class MedicalServiceRepository {
    private static MedicalServiceRepository instance;

    private MedicalServiceRepository() {
    }

    public static MedicalServiceRepository getInstance() {
        if (instance == null) {
            instance = new MedicalServiceRepository();
        }
        return instance;
    }

    public void insertMedicalService(Connection connection, MedicalServices medicalService) {

        String sql = """
                INSERT INTO medical_service (service_name , service_price, service_time)
                VALUES (?, ?, ?)
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, medicalService.getServiceName());
            ps.setDouble(2, medicalService.getServicePrice());
            ps.setInt(3, medicalService.getServiceTime());

            int insertedRows = ps.executeUpdate();
            ;
            System.out.println("Inserted " + insertedRows + " rows in medical_service");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<MedicalServices> getMedicalServiceById(Connection connection, long id) {
        String sql = """
                SELECT * 
                FROM medical_service 
                WHERE id = ?
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet result = ps.executeQuery()) {
                if (result.next()) {
                    return Optional.of(new MedicalServices(
                            result.getLong("id"),
                            result.getString("service_name"),
                            result.getDouble("service_price"),
                            result.getInt("service_time")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }


}
