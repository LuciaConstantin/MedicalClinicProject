package project.models;

import java.time.LocalDate;

public class PatientFactory {
    public static Patient createPatient(String patientType, long id, String firstName, String lastName, String personalID, String email, String phone, LocalDate birthDate, MedicalRecord medicalRecord, Object... args) {
        switch (patientType) {
            case "adult" ->{
                return new AdultPatient(id, firstName,  lastName, personalID, email, phone,
                        birthDate,  medicalRecord, (HealthInsurance) args[0]);
            }
            case "child" ->{
                return new ChildPatient(id, firstName, lastName, personalID,
                        email,  phone, birthDate,  medicalRecord, (String) args[0], (String) args[1]);
            }
            case "member" ->{
                return new MemberPatient(id, firstName,  lastName,  personalID,
                        email,  phone,  birthDate,  medicalRecord, (String) args[0], (Membership) args[1]);
            }
        }
        return null;
    }
}
