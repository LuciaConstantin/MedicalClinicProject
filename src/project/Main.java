package project;

import project.models.*;
import project.service.Service;
import project.service.ServiceCSV;


import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        Service service = Service.getInstance();
        ServiceCSV servCSV = ServiceCSV.getInstance();

        Scanner s = new Scanner(System.in);
        String input;

        StringBuilder sb = new StringBuilder();

        while (true) {
            sb.append("\nEnter number or write 'exit' to quit exit\n" +
                    "1. Add appointment \n" +
                    "2. View appointments for a specific doctor \n" +
                    "3. Add diagnostic and treatments for an appointment\n" +
                    "4. View all the doctors that have a specific specialty\n " +
                    "5. View all the medical services for a specific specialty \n" +
                    "6. View all the appointments of a patient, diagnostic and treatments\n" +
                    "7. Appointment reschedule\n" +
                    "8. Increase the salary of the doctor who generated the highest profit last year\n" +
                    "9. View the schedule for a specific doctor\n" +
                    "10. Plan an appointment\n" +
                    "11. Sum of all payments made by patients this year\n" +
                    "12. Delete a future appointment\n" +
                    "13. Generate invoice for appointment\n" +
                    "14. Add patient\n" +
                    "15. View all patient data");
            System.out.println(sb);

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


                            Builder builder = new AppointmentBuilder();
                            Director director = new Director(builder);

                            Appointment app = director.createInitialAppointment(doctor, patient, appointmentDate, LocalTime.of(appointmentHour, appointmentMinute), medicalService);

                            service.addAppointment(app);
                            //service.viewAppointments(doctor);
                            servCSV.writeCSV("Add appointment");

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
                        servCSV.writeCSV("View appointments for a specific doctor");
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
                        servCSV.writeCSV("Add diagnostic and treatments for an appointment");
                        break;

                    case 4:
                        System.out.println("Enter specialty name: ");
                        String specialtyName = s.nextLine();
                        service.doctorsSpecialty(specialtyName);
                        servCSV.writeCSV("View all the doctors that have a specific specialty");
                        break;

                    case 5:
                        System.out.println("Enter specialty name: ");
                        String spec = s.nextLine();
                        service.specialtyMedicalServices(spec);
                        servCSV.writeCSV("View all the medical services for a specific specialty");
                        break;

                    case 6:
                        System.out.println("First Name: ");
                        String firstName = s.nextLine();
                        System.out.println("Last Name: ");
                        String lastName = s.nextLine();
                        service.allPatientAppointments(firstName, lastName);
                        servCSV.writeCSV("View all the appointments of a patient diagnostic and treatments");
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
                        servCSV.writeCSV("Appointment reschedule");
                        break;
                    case 8:
                        service.profitDoctor();
                        servCSV.writeCSV("Increase the salary of the doctor who generated the highest profit last year.");
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
                        servCSV.writeCSV("View the schedule for a specific doctor");
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
                            servCSV.writeCSV("Plan an appointment");
                        } catch (DateTimeException e) {
                            System.out.println("Invalid date or time format.");
                        } finally {
                            System.out.println("Appointment scheduling attempt finished.\n");
                        }
                        break;
                    case 11:
                        service.patientMoney();
                        servCSV.writeCSV("Sum of all payments made by patients this year");
                        break;
                    case 12:
                        System.out.println("Please enter the appointment id to be deleted: ");
                        long id = Long.parseLong(s.nextLine());
                        Appointment app = service.findAppointment(id);
                        service.deleteAppointment(app);
                        servCSV.writeCSV("Delete a future appointment");
                        break;
                    case 13:
                        System.out.println("Enter appointment id: ");
                        int appId = Integer.parseInt(s.nextLine());
                        service.generateInvoice(appId);
                        servCSV.writeCSV("Generate invoice for appointment");
                        break;

                    case 14:
                        service.addPatient();
                        servCSV.writeCSV("Add new patient");
                        break;
                    case 15:
                        System.out.println("Enter patient first name: ");
                        String fName = s.nextLine();
                        System.out.println("Enter patient last name: ");
                        String lName = s.nextLine();
                        service.viewPatientData(fName, lName);
                        servCSV.writeCSV("View all patient data");
                        break;

                    default:
                        System.out.println("Please Choose a Valid Number");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input, please enter a valid number or 'exit' to quit.");
            }
        }

        s.close();

    }

}

