package project.repository;

import project.models.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Arrays;
import java.util.List;

public class PatientRepository {
    private static PatientRepository instance;

    private PatientRepository() {
    }

    public static PatientRepository getInstance() {
        if (instance == null) {
            instance = new PatientRepository();
        }
        return instance;
    }

    public Optional<MedicalRecord> getMedicalRecordById(Connection connection, long id) {
        String sql = """
                SELECT * 
                FROM medical_records
                WHERE id_patient = ?
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);

            try (ResultSet result = ps.executeQuery()) {
                if (result.next()) {
                    Array allergiesSqlArray = result.getArray("allergies");
                    Array chronicSqlArray = result.getArray("chronic_conditions");
                    Array restrictionsSqlArray = result.getArray("physical_restrictions");

                    String[] allergies = (String[]) allergiesSqlArray.getArray();
                    String[] chronicConditions = (String[]) chronicSqlArray.getArray();
                    String[] physicalRestrictions = (String[]) restrictionsSqlArray.getArray();

                    List<String> allergiesList = Arrays.asList(allergies);
                    List<String> chronicList = Arrays.asList(chronicConditions);
                    List<String> restrictionsList = Arrays.asList(physicalRestrictions);


                    allergiesSqlArray.free();
                    chronicSqlArray.free();
                    restrictionsSqlArray.free();

                    MedicalRecord record = new MedicalRecord(result.getLong("id_patient"), allergiesList, chronicList, restrictionsList);

                    return Optional.of(record);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    public void insertPatient(Connection connection, Patient patient) {
        String sql = """
                    INSERT INTO patients (first_name, last_name, personal_ID,
                                        email, phone, birth_date, patient_type)
                    VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, patient.getFirstName());
            ps.setString(2, patient.getLastName());
            ps.setString(3, patient.getPersonalID());
            ps.setString(4, patient.getEmail());
            ps.setString(5, patient.getPhone());
            LocalDate dob = patient.getDateOfBirth();
            ps.setDate(6, java.sql.Date.valueOf(dob));
            ps.setString(7, patient.getPatientType());

            int insertedRows = ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                long patientId = rs.getLong(1);
                patient.setId(patientId);
            }

            System.out.println("Inserted " + insertedRows + " rows in patient");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String sqlMedicalRec = """
                    INSERT INTO  medical_records(id_patient, allergies, chronic_conditions, physical_restrictions)
                    VALUES (?, ?, ?, ?)
                """;

        try (PreparedStatement ps = connection.prepareStatement(sqlMedicalRec)) {
            ps.setLong(1, patient.getId());
            Array allergiesArray = connection.createArrayOf("text", patient.getMedicalRecord().getAllergies().toArray(new String[0]));
            Array chronicArray = connection.createArrayOf("text", patient.getMedicalRecord().getChronicConditions().toArray(new String[0]));
            Array restrictionsArray = connection.createArrayOf("text", patient.getMedicalRecord().getPhysicalRestrictions().toArray(new String[0]));

            ps.setArray(2, allergiesArray);
            ps.setArray(3, chronicArray);
            ps.setArray(4, restrictionsArray);

            int insertedRows = ps.executeUpdate();

            System.out.println("Inserted " + insertedRows + " rows in medical_record");


            allergiesArray.free();
            chronicArray.free();
            restrictionsArray.free();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (patient.getPatientType().equals("adult")) {
            String sql1 = """
                    INSERT INTO adult_patients (patient_id, health_insurance)
                    VALUES (?, ?::health_insurance_type)
                    """;
            try (PreparedStatement ps = connection.prepareStatement(sql1)) {
                AdultPatient aP = (AdultPatient) patient;
                ps.setLong(1, aP.getId());
                ps.setString(2, aP.getTypeOfHealthInsurance().name());

                int insertedRows = ps.executeUpdate();
                System.out.println("Inserted " + insertedRows + " rows in adult_patient");

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        } else if (patient.getPatientType().equals("child")) {
            String sql2 = """
                    INSERT INTO child_patients (patient_id, guardian_name, guardian_id)
                    VALUES (?, ?, ?)
                    """;
            try (PreparedStatement ps = connection.prepareStatement(sql2)) {
                ChildPatient cP = (ChildPatient) patient;
                ps.setLong(1, cP.getId());
                ps.setString(2, cP.getGuardianName());
                ps.setString(3, cP.getGuardianId());

                int insertedRows = ps.executeUpdate();
                System.out.println("Inserted " + insertedRows + " rows in child_patient");

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        } else if (patient.getPatientType().equals("member")) {
            String sql3 = """
                    INSERT INTO member_patients (patient_id, membership_number, membership)
                    VALUES (?, ?, ?::membership_type)
                    """;
            try (PreparedStatement ps = connection.prepareStatement(sql3)) {
                MemberPatient mP = (MemberPatient) patient;
                ps.setLong(1, mP.getId());
                ps.setString(2, mP.getMembershipNumber());
                ps.setString(3, mP.getMembershipType().name());

                int insertedRows = ps.executeUpdate();
                System.out.println("Inserted " + insertedRows + " rows in member_patient");

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }


        }

    }

    public Optional<Patient> getPatientById(Connection connection, long id) {
        String sql = """
                    SELECT *
                    FROM patients
                    WHERE id = ?
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String firstName = rs.getString("first_name");
                    String lastName = rs.getString("last_name");
                    String personalID = rs.getString("personal_ID");
                    String email = rs.getString("email");
                    String phone = rs.getString("phone");
                    LocalDate birthDate = rs.getDate("birth_date").toLocalDate();
                    String patientType = rs.getString("patient_type");

                    Optional<MedicalRecord> optionalMedRec = getMedicalRecordById(connection, id);
                    if (optionalMedRec.isEmpty()) return Optional.empty();
                    MedicalRecord medicalRecord = optionalMedRec.get();

                    switch (patientType) {
                        case "child" -> {
                            String childSql = "SELECT * FROM child_patients WHERE patient_id = ?";
                            try (PreparedStatement childPs = connection.prepareStatement(childSql)) {
                                childPs.setLong(1, id);
                                try (ResultSet childRs = childPs.executeQuery()) {
                                    if (childRs.next()) {
                                        String guardianName = childRs.getString("guardian_name");
                                        String guardianId = childRs.getString("guardian_id");
                                        return Optional.of(new ChildPatient(id, firstName, lastName, personalID, email, phone, birthDate, medicalRecord, guardianName, guardianId));
                                    }
                                }
                            }
                        }
                        case "adult" -> {
                            String adultSql = "SELECT * FROM adult_patients WHERE patient_id = ?";
                            try (PreparedStatement adultPs = connection.prepareStatement(adultSql)) {
                                adultPs.setLong(1, id);
                                try (ResultSet adultRs = adultPs.executeQuery()) {
                                    if (adultRs.next()) {
                                        String insuranceStr = adultRs.getString("health_insurance");
                                        HealthInsurance insurance = HealthInsurance.valueOf(insuranceStr);
                                        return Optional.of(new AdultPatient(id, firstName, lastName, personalID, email, phone, birthDate, medicalRecord, insurance));
                                    }
                                }
                            }
                        }
                        case "member" -> {
                            String memberSql = "SELECT * FROM member_patients WHERE patient_id = ?";
                            try (PreparedStatement memberPs = connection.prepareStatement(memberSql)) {
                                memberPs.setLong(1, id);
                                try (ResultSet memberRs = memberPs.executeQuery()) {
                                    if (memberRs.next()) {
                                        String membershipNumber = memberRs.getString("membership_number");
                                        Membership membership = Membership.valueOf(memberRs.getString("membership"));
                                        return Optional.of(new MemberPatient(id, firstName, lastName, personalID, email, phone, birthDate, medicalRecord, membershipNumber, membership));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }

    public void deletePatient(Connection connection, long id) {

        String sql = """
                DELETE FROM patient 
                WHERE id = ?
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            int deletedClient = ps.executeUpdate();
            System.out.println("Deleted " + deletedClient + " patient");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



}
