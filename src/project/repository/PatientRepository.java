package project.repository;

import project.models.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;

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


                    String table = switch (patientType) {
                        case "adult" -> "adult_patients";
                        case "child" -> "child_patients";
                        case "member" -> "member_patients";
                        default -> throw new IllegalArgumentException("Unknown patient type");
                    };

                    String SQLPat = "SELECT * FROM " + table + " WHERE patient_id = ?";

                    Object[] obj = new Object[3];
                    switch (patientType) {
                        case "child" -> {
                            try (PreparedStatement p = connection.prepareStatement(SQLPat)) {
                                p.setLong(1, id);
                                try (ResultSet res = p.executeQuery()) {
                                    if (res.next()) {
                                        String guardianName = res.getString("guardian_name");
                                        String guardianId = res.getString("guardian_id");

                                        obj[0] = guardianName;
                                        obj[1] = guardianId;
                                    }
                                }
                            }
                        }
                        case "adult" -> {
                            try (PreparedStatement p = connection.prepareStatement(SQLPat)) {
                                p.setLong(1, id);
                                try (ResultSet res = p.executeQuery()) {
                                    if (res.next()) {
                                        String insuranceStr = res.getString("health_insurance");
                                        HealthInsurance insurance = HealthInsurance.valueOf(insuranceStr);
                                        obj[0] = insurance;
                                    }
                                }
                            }
                        }
                        case "member" -> {
                            try (PreparedStatement p = connection.prepareStatement(SQLPat)) {
                                p.setLong(1, id);
                                try (ResultSet res = p.executeQuery()) {
                                    if (res.next()) {
                                        String membershipNumber = res.getString("membership_number");
                                        Membership membership = Membership.valueOf(res.getString("membership"));
                                        obj[0] = membershipNumber;
                                        obj[1] = membership;
                                    }
                                }
                            }
                        }


                    }

                    PatientFactory patientFactory = new PatientFactory();
                    Patient pat = patientFactory.createPatient(patientType, id, firstName, lastName, personalID, email, phone, birthDate, medicalRecord, obj);
                    return Optional.of(pat);


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

    public Optional<Set<Patient>> getAllData(Connection connection) {
        Set<Patient> patients = new HashSet<>();
        String sql = """
                SELECT id
                FROM patients
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            try (ResultSet result = ps.executeQuery()) {
                while (result.next()) {
                    long id = result.getLong(1);
                    Optional<Patient> pat = getPatientById(connection, id);
                    pat.ifPresent(patients::add);
                }
                return patients.isEmpty() ? Optional.empty() : Optional.of(patients);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

}
