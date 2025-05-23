package project.repository;

import project.models.Diagnostic;
import project.models.MedicationTreatment;
import project.models.PhysiotherapyTreatment;
import project.models.Treatment;
import project.repository.DiagnosticRepository;
import project.repository.PhysiotherapyTreatmentRepository;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DiagnosticRepository {
    private static DiagnosticRepository instance;

    private DiagnosticRepository() {
    }

    public static DiagnosticRepository getInstance() {
        if (instance == null) {
            instance = new DiagnosticRepository();
        }
        return instance;
    }

    public void insertDiagnostic(Connection connection, Diagnostic diagnostic) {

        String sql = """
                INSERT INTO diagnostic (diagnostic_name)
                VALUES (?)
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, diagnostic.getName());


            int insertedRows = ps.executeUpdate();

            System.out.println("Inserted " + insertedRows + " rows in diagnostic");

            ResultSet result = ps.getGeneratedKeys();
            if (result.next()) {
                long diagnosticId = result.getLong(1);
                diagnostic.setId(diagnosticId);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String sql2 = """
                INSERT INTO diagnostic_treatment (diagnostic_id, treatment_id, treatment_type)
                VALUES (?, ?, ?)
                """;

        List<Treatment> treatment = diagnostic.getTreatments();
        for (Treatment t : treatment) {

            try (PreparedStatement ps = connection.prepareStatement(sql2)) {
                ps.setLong(1, diagnostic.getId());
                if (t.treatmentType().equals("Medication treatment")) {
                    MedicationTreatment med = (MedicationTreatment) t;
                    ps.setLong(2, med.getId());
                } else {
                    PhysiotherapyTreatment phy = (PhysiotherapyTreatment) t;
                    ps.setLong(2, phy.getId());
                }
                ps.setString(3, t.treatmentType());
                int insertedRows = ps.executeUpdate();
                ;
                System.out.println("Inserted " + insertedRows + " rows in diagnostic_treatment");

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }

    }

    public Optional<Diagnostic> getDiagnosticById(Connection connection, long id) {
        String sqlDiagnostic = "SELECT * FROM diagnostic WHERE id = ?";
        String sqlTreatments = "SELECT treatment_id, treatment_type FROM diagnostic_treatment WHERE diagnostic_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sqlDiagnostic)) {
            ps.setLong(1, id);

            try (ResultSet result = ps.executeQuery()) {
                if (!result.next()) return Optional.empty();

                String diagnosticName = result.getString("diagnostic_name");
                List<Treatment> treatments = new ArrayList<>();

                try (PreparedStatement ps2 = connection.prepareStatement(sqlTreatments)) {
                    ps2.setLong(1, id);

                    try (ResultSet result2 = ps2.executeQuery()) {
                        while (result2.next()) {
                            long treatmentId = result2.getLong("treatment_id");
                            String type = result2.getString("treatment_type");

                            PhysiotherapyTreatmentRepository phyRepo = PhysiotherapyTreatmentRepository.getInstance();
                            MedicationRepository medRepo = MedicationRepository.getInstance();

                            switch (type) {
                                case "Medication treatment" -> {
                                    Optional<MedicationTreatment> optMed = medRepo.getMedicationTreatmentById(connection, treatmentId);
                                    optMed.ifPresent(treatments::add);
                                }
                                case "Physiotherapy treatment" -> {
                                    Optional<PhysiotherapyTreatment> optPhy = phyRepo.getPhysiotherapyTreatmentById(connection, treatmentId);
                                    optPhy.ifPresent(treatments::add);
                                }
                            }
                        }
                    }
                }
                return Optional.of(new Diagnostic(id, diagnosticName, treatments));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



}
