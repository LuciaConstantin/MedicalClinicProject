package project;

import project.models.*;
import project.service.DoctorService;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        Service service = Service.getInstance();

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
                            //service.viewAppointments(doctor);

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

        s.close();
   }

}

