package project;

import project.models.*;
import project.service.ClinicService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        ClinicService serviceDataBase = new ClinicService();

        String[] allergies1 = {"Pollen", "Almonds", "Penicillin"};
        List<String> allergies2 = Arrays.asList(allergies1);
        String[] chronicConditions1 = {"Thyroid disease", "Migraines"};
        List<String> chronicConditions = Arrays.asList(chronicConditions1);
        String[] physicalRestri = {"knee", "back issues"};
        List<String> physicalRestrictions = Arrays.asList(physicalRestri);

        MedicalRecord medicalRecord1 = new MedicalRecord(allergies2, chronicConditions, physicalRestrictions);

        System.out.println(serviceDataBase.getMedicalRecordUsingId(4));

        MedicalRecord medRec = serviceDataBase.getMedicalRecordUsingId(4);

        Patient patient1 = new AdultPatient("Mirela", "Soare", "608345678986", "mireLALALA@gmail.com",
                "0734567898", LocalDate.parse("1965-11-09"), medRec, HealthInsurance.PUBLIC);
        Patient patient2 = new MemberPatient("Andrei", "Popescu", "508345678986", "andpo@gmail.com",
                "0734567898", LocalDate.parse("2000-11-09"), medRec, "10", Membership.GOLD);

        Patient newPatient = serviceDataBase.getPatientByIdDB(7);
        System.out.println(newPatient.getId());

        MedicalServices medicalService1 = new MedicalServices("ultrasound", 100, 35);

        MedicalServices medicalService2 = new MedicalServices("endocrinology consult", 400, 30);
        //serviceDataBase.insertMedicalService(medicalService2);

        MedicalServices med = serviceDataBase.getMedicalServiceByIdDB(3);
        System.out.println(med);
        System.out.println(med.getId());

        Specialty specialty2 = new Specialty("Cardiology", 75000);
        //serviceDataBase.insertSpecialtyDB(specialty2);

        Specialty newSpecialty = serviceDataBase.getSpecialtyByIdDB(2);
        System.out.println(newSpecialty);
        System.out.println("aici "+newSpecialty.getId());

        //serviceDataBase.insertSpecialtyMedicalServiceDB(newSpecialty.getId(), 1);
        List<MedicalServices> m = serviceDataBase.getMedicalServicesForSpecialtyById(newSpecialty.getId());
        newSpecialty.setMedicalServices(m.toArray(new MedicalServices[m.size()]));
        System.out.println(newSpecialty);

        Specialty newSpecialty2 = serviceDataBase.getSpecialtyByIdDB(1);




        /*
        String[] endocrinologyConditions = {"thyroid problems", "infertility", "bone disorders"};


        MedicalServices medicalServices1 = new MedicalServices("ultrasound", 100, 35);


        MedicalServices[] medicalServices = {medicalServices1, medicalServices2};

        Schedule schedule1 = new Schedule();
        schedule1.addToSchedule("Monday", LocalTime.of(9, 0), LocalTime.of(11, 0));
        schedule1.addToSchedule("Monday", LocalTime.of(13, 0), LocalTime.of(16, 0));
        schedule1.addToSchedule("Friday", LocalTime.of(17, 0), LocalTime.of(18, 0));

        Schedule schedule2 = new Schedule();
        schedule2.addToSchedule("Monday", LocalTime.of(9, 0), LocalTime.of(20, 0));

        Specialty specialty = new Specialty("Endocrinology", endocrinologyConditions, 70000, medicalServices);


        Doctor doctor1 = new Doctor("Ana", "Anghel", "608653345678", "ana@gmail.com", "078656789", LocalDate.parse("1976-01-07"),
                LocalDate.parse("2010-11-07"), "strada Soarelui nr. 9", specialty, schedule1);
        Doctor doctor2 = new Doctor("Ava", "Ina", "608657845678", "avaaaa@gmail.com", "078600089", LocalDate.parse("1985-01-07"),
                LocalDate.parse("2018-01-08"), "strada Soarelui nr. 9", specialty, schedule1);

        String[] allergies1 = {"Pollen", "Almonds", "Penicillin"};
        List<String> allergies2 = Arrays.asList(allergies1);
        String[] chronicConditions1 = {"Thyroid disease", "Migraines"};
        List<String> chronicConditions = Arrays.asList(chronicConditions1);
        String[] physicalRestri = {"knee", "back issues"};
        List<String> physicalRestrictions = Arrays.asList(physicalRestri);

        MedicalRecord patientRecord1 = new MedicalRecord(allergies2, chronicConditions, physicalRestrictions);
        Patient patient1 = new AdultPatient("Mirela", "Soare", "608345678986", "mireLALALA@gmail.com",
                "0734567898", LocalDate.parse("1965-11-09"), patientRecord1, HealthInsurance.PUBLIC);
        Patient patient2 = new MemberPatient("Andrei", "Popescu", "508345678986", "andpo@gmail.com",
                "0734567898", LocalDate.parse("2000-11-09"), patientRecord1, "10", Membership.GOLD);


        Appointment appointment = new Appointment(doctor1, patient1, LocalDate.parse("2025-03-03"), LocalTime.of(9, 0), medicalServices1);
        Appointment appointment2 = new Appointment(doctor1, patient1, LocalDate.parse("2024-04-08"), LocalTime.of(9, 25), medicalServices2);
        Appointment appointment3 = new Appointment(doctor1, patient1, LocalDate.parse("2024-04-08"), LocalTime.of(21, 30), medicalServices2);

        Service service = Service.getInstance();

        service.addDoctor(doctor1);
        service.addDoctor(doctor2);
        service.addPatient(patient1);
        service.addPatient(patient2);
        service.addAppointment(appointment);
        service.addAppointment(appointment2);
        service.addAppointment(appointment3);






        Scanner s = new Scanner(System.in);
        String input;

        while (true) {
            System.out.println("Enter number or write 'exit' to quit");
            System.out.println("1. Add appointment");
            System.out.println("2. View appointments for a specific doctor");
            System.out.println("3. Add diagnostic for an appointment");
            System.out.println("4. View all the doctors that have a specific specialty");
            System.out.println("5. View all the medical treatments for a specific specialty");
            System.out.println("6. View all the appointments of a patient");
            System.out.println("7. Appointment reschedule");
            System.out.println("8. Increase the salary of the doctor who generated the highest profit last year.");
            System.out.println("9. View the schedule for a specific doctor");
            System.out.println("10. Plan an appointment");
            System.out.println("11. Sum of all payments made by patients this year.");

            input = s.nextLine();

            if (input.equalsIgnoreCase("exit")) {
                break;
            }
            try {
                int menu = Integer.parseInt(input);
                switch (menu) {
                    case 1:
                        try {
                            System.out.println("Enter doctor name: First Name + Last Name");
                            String doctorName = s.nextLine();

                            System.out.println("Enter patient name: First Name + Last Name");
                            String patientName = s.nextLine();

                            System.out.println("Enter appointment date (e.g. 2025-03-03):");
                            String appointmentDateInput = s.nextLine();
                            LocalDate appointmentDate = LocalDate.parse(appointmentDateInput);

                            System.out.println("Enter appointment hour (0-23):");
                            int appointmentHour = Integer.parseInt(s.nextLine());

                            System.out.println("Enter appointment minute (0-59):");
                            int appointmentMinute = Integer.parseInt(s.nextLine());

                            System.out.println("Enter medical service:");
                            String medService = s.nextLine();

                            Doctor doctor = service.findDoctor(doctorName);
                            if (doctor == null) {
                                System.out.println("Doctor not found");
                                break;
                            }

                            Patient patient = service.findPatient(patientName);
                            if (patient == null) {
                                System.out.println("Patient not found");
                                break;
                            }

                            MedicalServices medicalService = service.getServiceByName(medService, doctor);
                            if (medicalService == null) {
                                System.out.println("The doctor doesn't treat that affection");
                                break;
                            }

                            Appointment app = new Appointment(doctor, patient, appointmentDate, LocalTime.of(appointmentHour, appointmentMinute), medicalService);

                            service.addAppointment(app);
                            service.viewAppointments(doctor);

                        } catch (NumberFormatException e) {
                            System.out.println("Invalid number entered for hour or minute.");
                        } catch (DateTimeException e) {
                            System.out.println("Invalid date or time format.");
                        } catch (Exception e) {
                            System.out.println("Something went wrong: " + e.getMessage());
                        } finally {
                            System.out.println("Appointment scheduling attempt finished.\n");
                        }
                        break;

                    case 2:
                        System.out.println("Enter doctor name: First Name + Last Name");
                        String doctorName = s.nextLine();
                        Doctor doc = service.findDoctor(doctorName);
                        if (doc == null) {
                            System.out.println("Doctor not found");
                            break;
                        }
                        service.viewAppointments(doc);
                        break;

                    case 3:
                        System.out.println("Enter appointment id: ");
                        int appointmentId = Integer.parseInt(s.nextLine());
                        Appointment ap = service.findAppointment(appointmentId);
                        if (ap == null) {
                            System.out.println("Appointment not found");
                            break;
                        }
                        service.addDiagnostic(ap);
                        break;

                    case 4:
                        System.out.println("Enter appointment specialty name: ");
                        String specialtyName = s.nextLine();
                        service.doctorsSpecialty(specialtyName);
                        break;

                    case 5:
                        System.out.println("Enter appointment specialty name: ");
                        String spec = s.nextLine();
                        service.specialtyMedicalServices(spec);
                        break;
                    case 6:
                        System.out.println("First Name");
                        String firstName = s.nextLine();
                        System.out.println("Last Name");
                        String lastName = s.nextLine();
                        service.allPatientAppointments(firstName, lastName);
                        break;
                    case 7:
                        System.out.println("Patient name: ");
                        String patientName = s.nextLine();
                        System.out.println("Doctor name: ");
                        String docName = s.nextLine();
                        System.out.println("Current appointment date:");
                        String appointmentDate = s.nextLine();
                        System.out.println("New appointment date: ");
                        String appointmentDateNew = s.nextLine();
                        System.out.println("New appointment hour: ");
                        int appointmentHour = Integer.parseInt(s.nextLine());
                        System.out.println("New appointment minute: ");
                        int appointmentMinute = Integer.parseInt(s.nextLine());
                        service.appointmentReschedule(patientName, docName, LocalDate.parse(appointmentDate), LocalDate.parse(appointmentDateNew), LocalTime.of(appointmentHour, appointmentMinute));
                        break;
                    case 8:
                        service.profitDoctor();
                        break;
                    case 9:
                        System.out.println("Enter doctor name: First Name + Last Name");
                        docName = s.nextLine();
                        Doctor doctor = service.findDoctor(docName);
                        if (doctor == null) {
                            System.out.println("Doctor not found");
                            break;
                        }
                        service.viewSchedule(doctor);
                        break;
                    case 10:
                        try {
                            System.out.println("Specialty name: ");
                            specialtyName = s.nextLine();
                            System.out.println("Start date: ");
                            String startDate = s.nextLine();
                            System.out.println("End date: ");
                            String endDate = s.nextLine();
                            service.planAppointment(specialtyName, LocalDate.parse(startDate), LocalDate.parse(endDate));
                        } catch (DateTimeException e) {
                            System.out.println("Invalid date or time format.");
                        }
                        finally {
                            System.out.println("Appointment scheduling attempt finished.\n");
                        }
                        break;
                    case 11:
                        service.patientMoney();
                        break;
                    default:
                        System.out.println("Please Choose a Valid Number");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input, please enter a valid number or 'exit' to quit.");
            }
        }

        s.close();*/
   }

}

