package project.repository;

import project.models.PhysiotherapyTreatment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class PhysiotherapyTreatmentRepository {
    private static PhysiotherapyTreatmentRepository instance;

    private PhysiotherapyTreatmentRepository() {
    }

    public static PhysiotherapyTreatmentRepository getInstance() {
        if (instance == null) {
            instance = new PhysiotherapyTreatmentRepository();
        }
        return instance;
    }

    public void insertPhysiotherapyTreatment(Connection connection, PhysiotherapyTreatment phy) {

        String sql = """
                INSERT INTO physiotherapy_treatment (exercise_name, repetitions, medical_issues)
                VALUES (?, ?, ?)
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, phy.getExerciseName());
            ps.setInt(2, phy.getRepetitions());
            ps.setString(3, phy.getMedicalIssues());

            int insertedRows = ps.executeUpdate();
            ;
            System.out.println("Inserted " + insertedRows + " rows in physiotherapy_treatment");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<PhysiotherapyTreatment> getPhysiotherapyTreatmentById(Connection connection, long id) {
        String sql = """
                SELECT * 
                FROM physiotherapy_treatment 
                WHERE id = ?
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet result = ps.executeQuery()) {
                if (result.next()) {
                    return Optional.of(new PhysiotherapyTreatment(
                            result.getLong("id"),
                            result.getString("exercise_name"),
                            result.getInt("repetitions"),
                            result.getString("medical_issues")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }



}
