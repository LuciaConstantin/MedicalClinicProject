package project.repository;

import project.models.*;

import java.sql.*;
import java.time.DayOfWeek;
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

            int insertedRows =  ps.executeUpdate();
            System.out.println("Inserted " + insertedRows + " rows in doctor");


            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                long generatedId = rs.getLong(1);
                doctor.setId(generatedId);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String sql2 = """
                    INSERT INTO schedule (doctor_id)
                    VALUES (?)
                    """;

        try (PreparedStatement ps = connection.prepareStatement(sql2, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, doctor.getId());

            int insertedRows =  ps.executeUpdate();
            System.out.println("Inserted " + insertedRows + " rows in schedule");


            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                long scheduleId = rs.getLong(1);
                doctor.getSchedule().setId(scheduleId);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        String sql3 = """
                    INSERT INTO time_interval (schedule_id, day, start_time, end_time)
                    VALUES (?, ?, ?, ?)
                    """;

        try (PreparedStatement ps = connection.prepareStatement(sql3)) {
            TimeInterval[][] timeInterval = doctor.getSchedule().getSchedule();
            for(int day=0; day<timeInterval.length; day++) {
                TimeInterval[] intervals = timeInterval[day];
                for(int interval=0; interval<intervals.length; interval++) {
                    TimeInterval specificInterval = intervals[interval];
                    if(specificInterval != null) {
                        ps.setLong(1, doctor.getSchedule().getId());
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
        SELECT 
            d.id as doctor_id,
            d.first_name, d.last_name, d.personal_ID, d.email, d.phone,
            d.birth_date, d.hire_date, d.salary,
            s.id as schedule_id,
            t.day, t.start_time, t.end_time,
            sp.id as specialty_id, sp.specialty_name, sp.starting_salary
        FROM doctor d
        LEFT JOIN schedule s ON d.id = s.doctor_id
        LEFT JOIN time_interval t ON s.id = t.schedule_id
        LEFT JOIN specialty sp ON d.specialty_id = sp.id
        WHERE d.id = ?
        ORDER BY t.day, t.start_time
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
                                rs.getLong("specialty_id"),
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
                        doctor.setId(rs.getLong("doctor_id"));
                        doctor.setSalary(rs.getDouble("salary"));
                        schedule.setId(rs.getLong("schedule_id"));
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

    public void insertMedicationTreatment(Connection connection, MedicationTreatment medicationTreatment) {

        String sql = """
                    INSERT INTO medication_treatment (medication_name, dosage, treatment_interval)
                    VALUES (?, ?, ?)
                    """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, medicationTreatment.getMedicationName());
            ps.setDouble(2, medicationTreatment.getDosage());
            ps.setInt(3, medicationTreatment.getTreatmentInterval());

            int insertedRows =  ps.executeUpdate();;
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

        try (PreparedStatement ps = connection.prepareStatement(sql))
        {
            ps.setLong(1, id);
            try (ResultSet result = ps.executeQuery())
            {
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

    public void insertPhysiotherapyTreatment(Connection connection, PhysiotherapyTreatment phy) {

        String sql = """
                    INSERT INTO physiotherapy_treatment (exercise_name, repetitions, medical_issues)
                    VALUES (?, ?, ?)
                    """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, phy.getExerciseName());
            ps.setInt(2, phy.getRepetitions());
            ps.setString(3, phy.getMedicalIssues());

            int insertedRows =  ps.executeUpdate();;
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

        try (PreparedStatement ps = connection.prepareStatement(sql))
        {
            ps.setLong(1, id);
            try (ResultSet result = ps.executeQuery())
            {
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

    public void insertDiagnostic(Connection connection, Diagnostic diagnostic) {

        String sql = """
                    INSERT INTO diagnostic (diagnostic_name)
                    VALUES (?)
                    """;

        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, diagnostic.getName());


            int insertedRows =  ps.executeUpdate();;
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
        for(Treatment t : treatment) {

            try (PreparedStatement ps = connection.prepareStatement(sql2)) {
                ps.setLong(1, diagnostic.getId());
                if(t.treatmentType().equals("Medication treatment")){
                    MedicationTreatment med = (MedicationTreatment) t;
                    ps.setLong(2, med.getId());
                }
                else {
                    PhysiotherapyTreatment phy = (PhysiotherapyTreatment) t;
                    ps.setLong(2, phy.getId());
                }
                ps.setString(3, t.treatmentType());
                int insertedRows =  ps.executeUpdate();;
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

                            switch (type) {
                                case "Medication treatment" -> {
                                    Optional <MedicationTreatment> optMed = getMedicationTreatmentById(connection, treatmentId);
                                    optMed.ifPresent(treatments::add);
                                }
                                case "Physiotherapy treatment" -> {
                                    Optional <PhysiotherapyTreatment> optPhy = getPhysiotherapyTreatmentById(connection, treatmentId);
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

    public void insertAppointment(Connection connection, Appointment appointment) {

            String sql = """
                    INSERT INTO child_patient (doctor_id, patient_id, appointment_date, 
                                               appointment_interval_id, medical_service_id, diagnostic_id)
                    VALUES (?, ?, ?, ?, ?, ?)
                    """;
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setLong(1, appointment.getDoctor().getId());
                ps.setLong(2, appointment.getPatient().getId());
                LocalDate dA = appointment.getAppointmentDate();
                ps.setDate(3, java.sql.Date.valueOf(dA));
                ps.setLong(4, appointment.getAppointmentInterval().getId());

                int insertedRows = ps.executeUpdate();

                System.out.println("Inserted " + insertedRows + " rows in child_patient");

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }



    }

}
