package project.repository;

import project.models.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ClinicRepository {
    private static ClinicRepository instance;

    private ClinicRepository() {
    }

    public static ClinicRepository getInstance() {
        if (instance == null) {
            instance = new ClinicRepository();
        }
        return instance;
    }

    public void insertDataMedicalRecord(Connection connection, MedicalRecord medicalRecord) {

        String sql = """
                INSERT INTO  medical_record(allergies, chronic_conditions, physical_restrictions)
                VALUES (?, ?, ?)
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            Array allergiesArray = connection.createArrayOf("text", medicalRecord.getAllergies().toArray(new String[0]));
            Array chronicArray = connection.createArrayOf("text", medicalRecord.getChronicConditions().toArray(new String[0]));
            Array restrictionsArray = connection.createArrayOf("text", medicalRecord.getPhysicalRestrictions().toArray(new String[0]));

            ps.setArray(1, allergiesArray);
            ps.setArray(2, chronicArray);
            ps.setArray(3, restrictionsArray);

            int insertedRows = ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    long generatedId = rs.getLong(1);
                    medicalRecord.setId(generatedId);
                }
            }

            System.out.println("Inserted " + insertedRows + " rows in medical record");

            allergiesArray.free();
            chronicArray.free();
            restrictionsArray.free();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public Optional<MedicalRecord> getMedicalRecordById(Connection connection, long id) {
        String sql = """
                SELECT * 
                FROM medical_record 
                WHERE id = ?
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

                    MedicalRecord record = new MedicalRecord(result.getLong("id"), allergiesList, chronicList, restrictionsList);

                    return Optional.of(record);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }


    public void insertPatient(Connection connection, Patient patient) {
        if (patient.getPatientType().equals("child")) {
            String sql = """
                    INSERT INTO child_patient (first_name, last_name, personal_ID,
                                               email, phone, birth_date, patient_type,
                                               medical_record_id,guardian_name, guardian_id)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                    """;
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ChildPatient pat = (ChildPatient) patient;
                ps.setString(1, pat.getFirstName());
                ps.setString(2, pat.getLastName());
                ps.setString(3, pat.getPersonalID());
                ps.setString(4, pat.getEmail());
                ps.setString(5, pat.getPhone());
                LocalDate dob = pat.getDateOfBirth();
                ps.setDate(6, java.sql.Date.valueOf(dob));
                ps.setString(7, pat.getPatientType());
                ps.setLong(8, pat.getMedicalRecord().getId());
                ps.setString(9, pat.getGuardianName());
                ps.setString(10, pat.getGuardianId());

                int insertedRows = ps.executeUpdate();
                ;
                System.out.println("Inserted " + insertedRows + " rows in child_patient");

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        } else if (patient.getPatientType().equals("adult")) {
            String sql = """
                    INSERT INTO adult_patient (first_name, last_name, personal_ID,
                                               email, phone, birth_date, patient_type,
                                               medical_record_id, health_insurance)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?::health_insurance_type)
                    """;
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                AdultPatient adult = (AdultPatient) patient;
                ps.setString(1, adult.getFirstName());
                ps.setString(2, adult.getLastName());
                ps.setString(3, adult.getPersonalID());
                ps.setString(4, adult.getEmail());
                ps.setString(5, adult.getPhone());
                LocalDate dob = adult.getDateOfBirth();
                ps.setDate(6, java.sql.Date.valueOf(dob));
                ps.setString(7, adult.getPatientType());
                ps.setLong(8, adult.getMedicalRecord().getId());
                ps.setString(9, adult.getTypeOfHealthInsurance().name());

                int insertedRows = ps.executeUpdate();
                ;
                System.out.println("Inserted " + insertedRows + " rows in adult_patient");

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        } else if (patient.getPatientType().equals("member")) {
            String sql = """
                    INSERT INTO member_patient (first_name, last_name, personal_ID,
                                               email, phone, birth_date, patient_type,
                                               medical_record_id, membership_number, membership)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?::membership_type)
                    """;
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                MemberPatient member = (MemberPatient) patient;
                ps.setString(1, member.getFirstName());
                ps.setString(2, member.getLastName());
                ps.setString(3, member.getPersonalID());
                ps.setString(4, member.getEmail());
                ps.setString(5, member.getPhone());
                LocalDate dob = member.getDateOfBirth();
                ps.setDate(6, java.sql.Date.valueOf(dob));
                ps.setString(7, member.getPatientType());
                ps.setLong(8, member.getMedicalRecord().getId());
                ps.setString(9, member.getMembershipNumber());
                ps.setString(10, member.getMembershipType().name());

                int insertedRows = ps.executeUpdate();
                ;
                System.out.println("Inserted " + insertedRows + " rows in member_patient");

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }

    }

    public Optional<Patient> getPatientById(Connection connection, long id) {
        String[] patientTables = {"child_patient", "adult_patient", "member_patient"};

        for (String table : patientTables) {
            String sql = "SELECT * FROM " + table + " WHERE id = ?";

            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setLong(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        long idPatient = rs.getLong("id");
                        String firstName = rs.getString("first_name");
                        String lastName = rs.getString("last_name");
                        String personalID = rs.getString("personal_ID");
                        String email = rs.getString("email");
                        String phone = rs.getString("phone");
                        LocalDate birthDate = rs.getDate("birth_date").toLocalDate();
                        long medRecordId = rs.getLong("medical_record_id");
                        String patientType = rs.getString("patient_type");

                        Optional<MedicalRecord> optionalMedRec = getMedicalRecordById(connection, medRecordId);
                        if (optionalMedRec.isEmpty()) return Optional.empty();
                        MedicalRecord medicalRecord = optionalMedRec.get();

                        switch (patientType) {
                            case "child" -> {
                                String guardianName = rs.getString("guardian_name");
                                String guardianId = rs.getString("guardian_id");
                                return Optional.of(new ChildPatient(idPatient, firstName, lastName, personalID, email, phone, birthDate, medicalRecord, guardianName, guardianId));
                            }
                            case "adult" -> {
                                String insuranceStr = rs.getString("health_insurance");
                                HealthInsurance insurance = HealthInsurance.valueOf(insuranceStr);
                                return Optional.of(new AdultPatient(idPatient, firstName, lastName, personalID, email, phone, birthDate, medicalRecord, insurance));
                            }
                            case "member" -> {
                                String membershipNumber = rs.getString("membership_number");
                                Membership membership = Membership.valueOf(rs.getString("membership"));
                                return Optional.of(new MemberPatient(idPatient, firstName, lastName, personalID, email, phone, birthDate, medicalRecord, membershipNumber, membership));
                            }
                        }
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return Optional.empty();
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

            int insertedRows =  ps.executeUpdate();;
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

        try (PreparedStatement ps = connection.prepareStatement(sql))
        {
            ps.setLong(1, id);
            try (ResultSet result = ps.executeQuery())
            {
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

    public void insertSpecialty(Connection connection, Specialty specialty) {

        String sql = """
                    INSERT INTO specialty (specialty_name, starting_salary)
                    VALUES (?, ?)
                    """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, specialty.getSpecialtyName());
            ps.setDouble(2, specialty.getStartingSalary());

            int insertedRows =  ps.executeUpdate();;
            System.out.println("Inserted " + insertedRows + " rows in specialty");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Specialty> getSpecialtyById(Connection connection, long id) {
        String sql = """
                SELECT * 
                FROM specialty 
                WHERE id = ?
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql))
        {
            ps.setLong(1, id);
            try (ResultSet result = ps.executeQuery())
            {
                if (result.next()) {
                    return Optional.of(new Specialty(
                            result.getLong("id"),
                            result.getString("specialty_name"),
                            result.getDouble("starting_salary")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    public void insertSpecialtyMedicalService(Connection connection,long specialtyId, long medicalServiceId) {
        String sql = """
                    INSERT INTO specialty_medical_service ( specialty_id, medical_service_id )
                    VALUES (?, ?)
                    """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1,specialtyId );
            ps.setLong(2, medicalServiceId);

            int insertedRows =  ps.executeUpdate();;
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

    /*
    public void insertSchedule(Connection connection, Schedule schedule) {

        String sql = """
                    INSERT INTO schedule (day_of_week, start_hour, start_minute, end_hour, end_minute)
                    VALUES (?, ?, ?, ?, ?)
                    """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setTime(1, schedule.getSchedule().start());
            ps.setDouble(2, specialty.getStartingSalary());

            int insertedRows =  ps.executeUpdate();;
            System.out.println("Inserted " + insertedRows + " rows in specialty");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

     */
}
