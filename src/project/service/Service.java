package project.service;

import project.ClinicDAO;
import project.models.*;
import project.serviceDB.AppointmnetService;
import project.serviceDB.DoctorService;
import project.serviceDB.PatientService;

import javax.print.Doc;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class Service {
    private static Service controller = null;

    private Service() { }

    public static synchronized Service getInstance() {
        if (controller == null)
            controller = new Service();
        return controller;
    }

    private List<Appointment> loadAppointments(){
        AppointmnetService appointmnetServ = new AppointmnetService();
        return appointmnetServ.getAll();
    }

    private Set<Doctor> loadDoctors(){
        DoctorService doctorServ = new DoctorService();
        return doctorServ.getAll();
    }

    private Set<Patient> loadPatients(){
        PatientService patientServ = new PatientService();
        return patientServ.getAll();
    }


    public Appointment findAppointment(long appointmentId) {
        List<Appointment> appointments = loadAppointments();
        return appointments.stream().filter(appointment -> appointment.getId() == appointmentId).findFirst().orElse(null);
    }

    public void addAppointment(Appointment appointment) {
        List<Appointment> appointments = loadAppointments();
        Set<Doctor> doctors = loadDoctors();
        Set<Patient> patients = loadPatients();

        try {
            for (Appointment app : appointments) {
                if (app.equals(appointment)) {
                    throw new AppointmentException("Appointment already exists");
                }
            }

            if (!doctors.contains(appointment.getDoctor())) {
                throw new AppointmentException("Doctor doesn't exist");
            }

            if (!patients.contains(appointment.getPatient())) {
                throw new AppointmentException("Patient doesn't exist");
            }


            int appointmentDay = appointment.getAppointmentDate().getDayOfWeek().getValue() - 1;
            TimeInterval[] doctorSchedule = appointment.getDoctor().getSchedule().getSchedule()[appointmentDay];

            if (doctorSchedule.length == 0) {
                throw new AppointmentException("The doctor does not work on this day, appointment failed");
            }

            boolean goodInterval = false;
            for (TimeInterval workInterval : doctorSchedule) {

                if (workInterval.contains(appointment.getAppointmentInterval())) {
                    goodInterval = true;
                    break;
                }
            }

            if (!goodInterval) {
                throw new AppointmentException("The appointment interval is not within the doctor's working hours, appointment failed");
            }

            for (Appointment app : appointments) {
                if (app.getDoctor().equals(appointment.getDoctor()) &&
                        app.getAppointmentDate().equals(appointment.getAppointmentDate()) &&
                        app.getAppointmentInterval().start().isBefore(appointment.getAppointmentInterval().end()) &&
                        appointment.getAppointmentInterval().start().isBefore(app.getAppointmentInterval().end())) {
                    throw new AppointmentException("Appointment interval overlaps with another appointment");
                }
            }

            ClinicDAO<Appointment> app = new AppointmnetService();
            appointments.add(appointment);
            app.create(appointment);

            System.out.println("Appointment successfully added!");

        } catch (AppointmentException e) {
            System.out.println("Failed to add appointment: " + e.getMessage());
        }

    }

    public void addDiagnostic(Appointment appointment) {
        Scanner scanner = new Scanner(System.in);
        if(appointment.getDiagnostic() != null){
            System.out.println("The appointment already has a diagnostic");
            return;
        }
        System.out.println("Add a diagnostic to appointment");
        System.out.println("Please enter the name of the diagnostic to be added: ");
        String name = scanner.nextLine();
        System.out.println("Please add the doctor notes: ");
        String doctorNotes = scanner.nextLine();

        List<Treatment> treatmentList = new ArrayList<>();

        String verify = "0";
        while (!verify.equals("q")) {
            System.out.println("If the all the treatments were added, please enter q");
            System.out.println("Please enter the type of the treatment to be added: \n" +
                    "for Medication Treatment type m and for Physical Treatment type p");
            verify = scanner.nextLine();

            if (verify.equals("m")) {
                try {
                    System.out.println("Please enter the name of the treatment to be added: ");
                    String medicationName = scanner.nextLine();
                    System.out.println("Please enter the dosage: ");
                    double dosage = Double.parseDouble(scanner.nextLine());
                    System.out.println("Please enter the treatment interval: ");
                    int treatmentInterval = Integer.parseInt(scanner.nextLine());
                    Treatment medicalTreatment = new MedicationTreatment(medicationName, dosage, treatmentInterval);

                    if (medicalTreatment.verifyTreatment(appointment.getPatient().getMedicalRecord())) {
                        treatmentList.add(medicalTreatment);
                    } else {
                        System.out.println("The patient is allergic to: " + medicationName + " the medication won't be added");
                    }
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException(e);
                }


            } else if (verify.equals("p")) {
                try {
                    System.out.println("Please add the exercise name: ");
                    String exerciseName = scanner.nextLine();
                    System.out.println("Please enter the sets for the exercise: ");
                    int sets = Integer.parseInt(scanner.nextLine());
                    System.out.println("Please enter the medical issue that is treated: ");
                    String medicalIssue = scanner.nextLine();

                    Treatment physiotherapyTreatment = new PhysiotherapyTreatment(exerciseName, sets, medicalIssue);

                    if (physiotherapyTreatment.verifyTreatment(appointment.getPatient().getMedicalRecord())) {
                        treatmentList.add(physiotherapyTreatment);
                    } else {
                        System.out.println("The patient can't do this type of exercise");
                    }

                } catch (IllegalArgumentException e) {
                    throw new RuntimeException(e);
                }


            } else if (verify.equals("q")) {
                System.out.println("Exit treatments");
            } else {
                System.out.println("The treatment type is invalid");
                return;
            }

        }

        Diagnostic diagnostic = new Diagnostic(name, doctorNotes, treatmentList);
        appointment.setDiagnostic(diagnostic);
        AppointmnetService app = new AppointmnetService();

        app.update(appointment);

        System.out.println("Diagnostic added to appointment");
    }

    public List<Appointment> sortAppointments() {
        List<Appointment> appointments = loadAppointments();
        appointments.sort(Comparator.comparing(Appointment::getAppointmentDate));
        return appointments;
    }

    public void allPatientAppointments(String firstName, String lastName) {
        List<Appointment> appointments = loadAppointments();
        Set<Patient> patients = loadPatients();
        sortAppointments();

        StringBuilder sb = new StringBuilder();
        for (Patient patient : patients) {
            if (patient.getFirstName().equalsIgnoreCase(firstName) && patient.getLastName().equalsIgnoreCase(lastName)) {
                for (Appointment appointment : appointments) {
                    if (appointment.getPatient().equals(patient)) {
                        sb.append("\nAppointment date: " + appointment.getAppointmentDate()
                                + " doctor: " + appointment.getDoctor().getFirstName() + " " + appointment.getDoctor().getLastName()
                                + (appointment.getDiagnostic() != null && appointment.getDiagnostic().getName() != null
                                ? " diagnostic: " + appointment.getDiagnostic().getName()
                                : "\n"));
                        if (appointment.getDiagnostic() != null) {
                            for (Treatment treatment : appointment.getDiagnostic().getTreatments()) {
                                sb.append("\n Treatment type: " + treatment.treatmentType() + "\n Treatment description: \n " + treatment.treatmentDescription() + "\n");
                            }
                        } else {
                            sb.append("No diagnostic added for this appointment.\n");
                        }

                    }
                }
            }
        }
        if (!sb.isEmpty()) {
            System.out.println(sb);
        } else {
            System.out.println("No appointments found for this patient");
        }
    }

    public Doctor findDoctor(String doctorName) {
        Set<Doctor> doctors = loadDoctors();
        for (Doctor d : doctors) {
            if ((d.getFirstName() + " " + d.getLastName()).equalsIgnoreCase(doctorName)) {
                return d;
            }
        }

        return null;
    }

    public Patient findPatient(String patientName) {
        Set<Patient> patients = loadPatients();
        for (Patient p : patients) {
            if ((p.getFirstName() + " " + p.getLastName()).equalsIgnoreCase(patientName)) {
                return p;
            }
        }

        return null;
    }

    public MedicalServices getServiceByName(String name, Doctor doc) {
        for (MedicalServices service : doc.getSpecialty().getMedicalServices()) {
            if (service.getServiceName().equalsIgnoreCase(name.trim())) {
                return service;
            }
        }
        return null;
    }

    public void viewAppointments(Doctor doctor) {
        List<Appointment> appointments = loadAppointments();
        sortAppointments();
        StringBuilder sb = new StringBuilder();
        System.out.println("\nThe appoinments for doctor " + doctor.getFirstName() + " " + doctor.getLastName() + " are:");
        for (Appointment app : appointments) {
            if (app.getDoctor().equals(doctor)) {
                sb.append("\nPatient name: " + app.getPatient().getLastName() + " " + app.getPatient().getFirstName() + "\n"
                        + "Date: " + app.getAppointmentDate().toString() + "\n"
                        + "Time interval: " + app.getAppointmentInterval().printInterval() + "\n"
                        + "Medical service: " + app.getMedicalService().getServiceName() + "\n");
            }
        }
        if (sb.length() > 0) {
            System.out.println(sb);
        } else {
            System.out.println("No appointments found");
        }
    }

    public void doctorsSpecialty(String specialty) {
        Set<Doctor> doctors = loadDoctors();
        StringBuilder sb = new StringBuilder();

        System.out.println("Doctors for the " + specialty + " specialty: ");
        for (Doctor doctor : doctors) {
            if (doctor.getSpecialty().getSpecialtyName().equalsIgnoreCase(specialty)) {
                sb.append("Doctor: " + doctor.getFirstName() + " " + doctor.getLastName() + "\n");
            }
        }

        if (!sb.isEmpty()) {
            System.out.println(sb);
        } else {
            System.out.println("No Doctors found for this specialty");
        }

    }

    public void specialtyMedicalServices(String specialty) {
        Specialty spec = null;
        Set<Doctor> doctors = loadDoctors();
        for (Doctor doctor : doctors) {
            if (doctor.getSpecialty().getSpecialtyName().equals(specialty)) {
                spec = doctor.getSpecialty();
            }
        }

        if (spec == null) {
            System.out.println("The specialty " + specialty + " doesn't exist");
            return;
        }
        for (int i = 0; i < spec.getMedicalServices().length; i++)
            System.out.println("Medical service name: " + spec.getMedicalServices()[i].getServiceName() + ", price: " + spec.getMedicalServices()[i].getServicePrice());

    }

    public void appointmentReschedule(String patientName, String doctorName, LocalDate appointmentDate, LocalDate rescheduleDate, LocalTime rescheduleTime) {
        List<Appointment> appointments = loadAppointments();
        boolean appointmentFound = false;
        Appointment appointment = null;

        if (rescheduleDate.isBefore(LocalDate.now())) {
            System.out.println("The reschedule date is in the past, can't reschedule the appointment");
            return;
        } else if (appointmentDate.isBefore(LocalDate.now())) {
            System.out.println("the appointment has already taken place");
            return;
        }

        for (Appointment app : appointments) {
            if ((app.getDoctor().getFirstName() + " " + app.getDoctor().getLastName()).equals(doctorName)
                    && app.getAppointmentDate().equals(appointmentDate) &&
                    (app.getPatient().getFirstName() + " " + app.getPatient().getLastName()).equals(patientName)) {

                appointment = app;
                appointmentFound = true;
                break;
            }
        }


        if (!appointmentFound) {
            System.out.println("Appointment not found.");
            return;
        }

        int appointmentDay = rescheduleDate.getDayOfWeek().getValue() - 1;
        TimeInterval[] doctorSchedule = appointment.getDoctor().getSchedule().getSchedule()[appointmentDay];

        if (doctorSchedule.length == 0) {
            System.out.println("The doctor does not work on this day, the reschedule of the appointment failed.");
            return;
        }

        LocalTime appointmentEnd = rescheduleTime.plusMinutes(appointment.getMedicalService().getServiceTime());

        boolean goodInterval = false;
        for (TimeInterval workInterval : doctorSchedule) {
            if (!rescheduleTime.isBefore(workInterval.start()) && !appointmentEnd.isAfter(workInterval.end())) {
                goodInterval = true;
                break;
            }
        }

        if (!goodInterval) {
            System.out.println("The appointment interval is not within the doctor's working hours, reschedule failed.");
            return;
        }

        for (Appointment app : appointments) {
            if (app.getDoctor().equals(appointment.getDoctor()) &&
                    app.getAppointmentDate().equals(rescheduleDate) &&
                    app.getAppointmentInterval().start().isBefore(appointmentEnd) &&
                    rescheduleTime.isBefore(app.getAppointmentInterval().end())) {

                System.out.println("Appointment interval overlaps with another appointment.");
                return;
            }
        }

        TimeInterval reschTime = new TimeInterval(rescheduleTime, appointmentEnd);


        appointment.setAppointmentDate(rescheduleDate);
        appointment.setAppointmentInterval(reschTime);
        AppointmnetService appointmnetService = new AppointmnetService();

        appointmnetService.reschedule(appointment);

        System.out.println("Appointment successfully rescheduled to: " + rescheduleDate + " at " + rescheduleTime);

    }

    public void profitDoctor() {
        List<Appointment> appointments = loadAppointments();
        if (appointments.isEmpty()) {
            System.out.println("No appointments found ");
            return;
        }

        Map<Doctor, Double> doctorProfit = new HashMap<>();

        int lastYear = LocalDate.now().getYear() - 1;
        for (Appointment appointment : appointments) {
            if (appointment.getAppointmentDate().getYear() == lastYear) {
                doctorProfit.put(
                        appointment.getDoctor(),
                        doctorProfit.getOrDefault(appointment.getDoctor(), 0.0) + appointment.getMedicalService().getServicePrice()
                );
            }
        }

        Doctor topDoctor = null;
        double maxProfit = 0;

        for (Map.Entry<Doctor, Double> entry : doctorProfit.entrySet()) {
            if (entry.getValue() > maxProfit) {
                maxProfit = entry.getValue();
                topDoctor = entry.getKey();
            }
        }

        if (topDoctor != null) {
            topDoctor.setSalary(topDoctor.getSalary() * 1.1);
            System.out.println("Doctor " + topDoctor.getFirstName() + " " + topDoctor.getLastName() +
                    " had the highest profit " + maxProfit + " and got a 10% raise. New salary: " + topDoctor.getSalary());

            DoctorService doctorService = new DoctorService();
            doctorService.updateSalary(topDoctor);

        }
        else{
            System.out.println("There were no appointments found for last year");
        }
    }

    public void viewSchedule(Doctor doctor) {
        System.out.println("\nThe schedule for doctor: " + doctor.getFirstName() + " " + doctor.getLastName());
        doctor.getSchedule().printSchedule();

    }

    public void planAppointment(String specialtyName, LocalDate startInterval, LocalDate endInterval) {
        StringBuilder sb = new StringBuilder();
        Set<Doctor> doctors = loadDoctors();

        List<Doctor> potentialDoctors = new ArrayList<>();
        boolean existsDoctor = false;
        for (Doctor doctor : doctors) {
            if (doctor.getSpecialty().getSpecialtyName().equals(specialtyName)) {
                potentialDoctors.add(doctor);
                existsDoctor = true;
            }
        }
        if (!existsDoctor) {
            System.out.println("There aren't any doctors that have this specialty");
            return;
        }

        boolean availableDate = false;
        sb.append("The doctors that are available in the interval: \n");
        for (Doctor potentialDoctor : potentialDoctors) {
            Schedule doctorSchedule = potentialDoctor.getSchedule();

            for (LocalDate date = startInterval; !date.isAfter(endInterval); date = date.plusDays(1)) {
                int dayOfWeek = date.getDayOfWeek().getValue() - 1;
                TimeInterval[] dailySchedule = doctorSchedule.getSchedule()[dayOfWeek];

                if (dailySchedule.length == 0) {
                    continue;
                }

                availableDate = true;
                sb.append("The doctor: " + potentialDoctor.getFirstName() + " " + potentialDoctor.getLastName() +
                        " works on this date " + date + " in the intervals: ");
                for (int i = 0; i < dailySchedule.length; i++) {
                    sb.append(dailySchedule[i].start() + " - " + dailySchedule[i].end() + "\n");
                }

            }

        }

        if (!availableDate) {
            System.out.println("No doctor can fit the medical service in this interval");

        } else {
            System.out.println(sb);
        }
    }

    public void patientMoney() {
        List<Appointment> appointments = loadAppointments();
        double totalMoneyPatients = 0;
        double serviceMoney = 0;
        double totalAppointmentsIncome = 0;
        for (Appointment appointment : appointments) {
            if (LocalDate.now().getYear() == appointment.getAppointmentDate().getYear()) {
                serviceMoney = appointment.getMedicalService().getServicePrice();
                totalMoneyPatients += appointment.getPatient().calculateBill(serviceMoney);
                totalAppointmentsIncome += appointment.getMedicalService().getServicePrice();

            }
        }
        System.out.println("Total money from patients this year is: " + totalMoneyPatients);
        System.out.println("Total money from appointments " + totalAppointmentsIncome);
    }

    public void deleteAppointment(Appointment appointment) {
        List<Appointment> appointments = loadAppointments();
        if(appointment.getAppointmentDate().isAfter(LocalDate.now()) && appointment.getDiagnostic() == null) {
            AppointmnetService appointmnetService = new AppointmnetService();
            appointmnetService.delete(appointment.getId());
            appointments.remove(appointment);
        }
        else{
            System.out.println("The appointment already happened, it can't be deleted.");
        }
    }

    public void generateInvoice(long appointmentId) {
        Appointment apt = findAppointment(appointmentId);
        if (apt == null) {
            System.out.println("Appointment not found");
            return;
        }
        double toPay = apt.getPatient().calculateBill(apt.getMedicalService().getServicePrice());
        StringBuilder sb = new StringBuilder();
        sb.append("Invoice for appointment: " + appointmentId + "\n" + "Date: " + apt.getAppointmentDate() + "\n" + "Medical service: " + apt.getMedicalService().getServiceName() + "\n"
        + "Price without discount: " + apt.getMedicalService().getServicePrice() + "\n" + "Amount to pay " + toPay + "\n") ;

        System.out.println(sb);
    }

    public void addPatient(){
        Scanner s = new Scanner(System.in);
        try {
            System.out.println("Enter patient data: ");
            System.out.println("Enter first name: ");
            String fName = s.nextLine();
            System.out.println("Enter last name: ");
            String lName = s.nextLine();
            System.out.println("Enter personalId: ");
            String personalId = s.nextLine();
            System.out.println("Enter email: ");
            String email = s.nextLine();
            System.out.println("Enter phone number: ");
            String phoneNumber = s.nextLine();
            System.out.println("Enter birth date: ");
            String birthDate = s.nextLine();
            LocalDate bDate = LocalDate.parse(birthDate);

            String text = "...";
            List<String> allergies = new ArrayList<>();
            while (!text.equalsIgnoreCase("q")) {
                System.out.println("Enter allergies: ");
                text = s.nextLine();
                if (!text.equalsIgnoreCase("q")) {
                    allergies.add(text);
                }
            }

            text = "...";
            List<String> chronicConditions = new ArrayList<>();
            while (!text.equalsIgnoreCase("q")) {
                System.out.println("Enter chronic conditions: ");
                text = s.nextLine();
                if (!text.equalsIgnoreCase("q")) {
                    chronicConditions.add(text);
                }
            }

            text = "...";
            List<String> physicalRestrictions = new ArrayList<>();
            while (!text.equalsIgnoreCase("q")) {
                System.out.println("Enter physicalRestrictions: ");
                text = s.nextLine();
                if (!text.equalsIgnoreCase("q")) {
                    chronicConditions.add(text);
                }
            }

            MedicalRecord medRec = new MedicalRecord(allergies, chronicConditions, physicalRestrictions);
            System.out.println("Enter patient type adult/child/member: ");
            String patientType = s.nextLine();

            Object[] obj = new Object[3];
            switch (patientType) {
                case "child" -> {
                    System.out.println("Enter guardian name: ");
                    obj[0] = s.nextLine();
                    System.out.println("Enter guardian id: ");
                    obj[1] = s.nextLine();
                }
                case "adult" -> {
                    System.out.println("Enter type of health insurance PUBLIC/UNINSURED ");
                    obj[0] = HealthInsurance.valueOf(s.nextLine());
                }
                case "member" -> {
                    System.out.println("Enter membership number");
                    obj[0] = s.nextLine();
                    System.out.println("Enter membership type: ");
                    obj[1] = Membership.valueOf(s.nextLine());
                }
                default -> {
                    System.out.println("Invalid type of patient");
                    return;
                }
            }

            PatientFactory patFactory = new PatientFactory();
            Patient patient = patFactory.createPatient(patientType, 0, fName, lName, personalId, email, phoneNumber, bDate, medRec, obj);

            if(patient == null) {
                System.out.println("The data is not correct try again.");
                return;
            }

            PatientService patientService = new PatientService();
            patientService.create(patient);

        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public void viewPatientData(String firstName, String lastName) {
        Patient patient = findPatient(firstName + " " +lastName);
        patient.displayInformation();
    }

}




