package project.repository;

import project.models.MedicationTreatment;

import java.sql.*;
import java.util.Optional;

public class MedicationRepository {
    private static MedicationRepository instance;

    private MedicationRepository() {
    }

    public static MedicationRepository getInstance() {
        if (instance == null) {
            instance = new MedicationRepository();
        }
        return instance;
    }

    public void insertMedicationTreatment(Connection connection, MedicationTreatment medicationTreatment) {

        String sql = """
                INSERT INTO medication_treatment (medication_name, dosage, treatment_interval)
                VALUES (?, ?, ?)
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, medicationTreatment.getMedicationName());
            ps.setDouble(2, medicationTreatment.getDosage());
            ps.setInt(3, medicationTreatment.getTreatmentInterval());

            int insertedRows = ps.executeUpdate();


            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    long generatedId = rs.getLong(1);
                    medicationTreatment.setId(generatedId);
                }
            }

            System.out.println("Inserted " + insertedRows + " rows in medication_treatment");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<MedicationTreatment> getMedicationTreatmentById(Connection connection, long id) {
        String sql = """
                SELECT * 
                FROM medication_treatment 
                WHERE id = ?
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet result = ps.executeQuery()) {
                if (result.next()) {
                    return Optional.of(new MedicationTreatment(
                            result.getLong("id"),
                            result.getString("medication_name"),
                            result.getDouble("dosage"),
                            result.getInt("treatment_interval")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }


}
