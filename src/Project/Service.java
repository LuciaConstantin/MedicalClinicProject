package Project;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Service {
    private static Service controller = null;
    private List <Appointment> appointments;
    private Set<Doctor> doctors;
    private Set<Patient> patients;

    private Service() {
        this.appointments = new ArrayList<>();
        this.doctors = new HashSet<>();
        this.patients = new HashSet<>();
    }

    public static synchronized Service getInstance() {
        if (controller == null)
            controller = new Service();
        return controller;
    }

    public Appointment findAppointment(int appointmentId){
        return appointments.stream().filter(appointment -> appointment.getAppointmentId() == appointmentId).findFirst().orElse(null);

    }

    public void addDoctor(Doctor doctor) {

        doctors.add(doctor);
    }

    public void addPatient(Patient patient) {
        patients.add(patient);
    }


    public void addAppointment(Appointment appointment) {
        for (Appointment app : appointments) {
            if (app.equals(appointment)) {
                System.out.println("Appointment already exists");
                return;
            }
        }

        if(!doctors.contains(appointment.getDoctor())) {
            System.out.println("Doctor doesn't exist");
            return;
        }

        if(!patients.contains(appointment.getPatient())) {
            System.out.println("Patient doesn't exist");
            return;
        }

        int appointmentDay = appointment.getAppointmentDate().getDayOfWeek().getValue() - 1;
        TimeInterval[] doctorSchedule = appointment.getDoctor().getSchedule().getSchedule()[appointmentDay];

        if (doctorSchedule.length == 0) {
            System.out.println("The doctor does not work on this day, appointment failed");
            return;
        }

        boolean goodInterval = false;
        LocalTime appointmentStart = appointment.getAppointmentInterval().start();
        LocalTime appointmentEnd = appointment.getAppointmentInterval().end();

        for (TimeInterval workInterval : doctorSchedule) {
            LocalTime workStart = workInterval.start();
            LocalTime workEnd = workInterval.end();

            if (!appointmentStart.isBefore(workStart) && !appointmentEnd.isAfter(workEnd)) {
                goodInterval = true;
                break;
            }
        }

        if (!goodInterval) {
            System.out.println("The appointment interval is not within the doctor's working hours, appointment failed");
            return;
        }

        for (Appointment app : appointments) {
            if (app.getDoctor().equals(appointment.getDoctor()) &&
                    app.getAppointmentDate().equals(appointment.getAppointmentDate()) &&
                    app.getAppointmentInterval().start().isBefore(appointment.getAppointmentInterval().end()) &&
                    appointment.getAppointmentInterval().start().isBefore(app.getAppointmentInterval().end())) {
                System.out.println("Appointment interval overlaps with another appointment");
                return;
            }
        }

        appointments.add(appointment);
        //patients.add(appointment.getPatient());

        System.out.println("Appointment successfully added!");
    }




    /*
    public Doctor findDoctor(String doctorName) {
        for (Doctor d : doctors) {
            if((d.getFirstName() + " "+ d.getLastName()).equals(doctorName)) {
                return d;
            }
        }

        return null;
    }

    public Patient findPatient(String patientName) {
        for (Patient p : patients) {
            if((p.getFirstName() + " " + p.getLastName()).equals(patientName)) {
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

    public Appointment findAppointment(int appointmentId) {
        for (Appointment ap : appointments) {
            if (ap.getAppointmentId() == appointmentId) {
                return ap;
            }
        }
        return null;
    }

    public Appointment findAppointment2(int appointmentId){

        return appointments.stream().filter(appointment -> appointment.getAppointmentId().equals(appointmentId)).findFirst().orElse(Null);

    }

    public void addAppointment(Appointment appointment) {
        for (Appointment app : appointments) {
            if (app.equals(appointment)) {
                System.out.println("Appointment already exists");
                return;
            }
        }

        if(!doctors.contains(appointment.getDoctor())) {
            System.out.println("Doctor doesn't exist");
            return;
        }

        if(!patients.contains(appointment.getPatient())) {
            System.out.println("Patient doesn't exist");
            return;
        }

        int appointmentDay = appointment.getAppointmentDate().getDayOfWeek().getValue() - 1;
        TimeInterval[] doctorSchedule = appointment.getDoctor().getSchedule().getSchedule()[appointmentDay];

        if (doctorSchedule.length == 0) {
            System.out.println("The doctor does not work on this day, appointment failed");
            return;
        }

        boolean goodInterval = false;
        LocalTime appointmentStart = appointment.getAppointmentInterval().start();
        LocalTime appointmentEnd = appointment.getAppointmentInterval().end();

        for (TimeInterval workInterval : doctorSchedule) {
            LocalTime workStart = workInterval.start();
            LocalTime workEnd = workInterval.end();

            if (!appointmentStart.isBefore(workStart) && !appointmentEnd.isAfter(workEnd)) {
                goodInterval = true;
                break;
            }
        }

        if (!goodInterval) {
            System.out.println("The appointment interval is not within the doctor's working hours, appointment failed");
            return;
        }

        for (Appointment app : appointments) {
            if (app.getDoctor().equals(appointment.getDoctor()) &&
                    app.getAppointmentDate().equals(appointment.getAppointmentDate()) &&
                    app.getAppointmentInterval().start().isBefore(appointment.getAppointmentInterval().end()) &&
                    appointment.getAppointmentInterval().start().isBefore(app.getAppointmentInterval().end())) {
                System.out.println("Appointment interval overlaps with another appointment");
                return;
            }
        }

        appointments.add(appointment);
       //patients.add(appointment.getPatient());

        System.out.println("Appointment successfully added!");
    }

    public List<Appointment> sortAppointments() {
        appointments.sort(Comparator.comparing(Appointment::getAppointmentDate));
        return appointments;
    }

    public void viewAppointments(Doctor doctor) {
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

    public void addDiagnostic(Appointment appointment) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Add a diagnostic to appointment");
        System.out.println("Please enter the name of the diagnostic to be added: ");
        String name = scanner.nextLine();
        System.out.println("How many treatments would you like to add this diagnostic?");
        int treatments = Integer.parseInt(scanner.nextLine());

        Treatment[] treatmentArray = new Treatment[treatments];
        for (int i = 0; i < treatments; i++) {
            System.out.println("Please enter the type of the treatment to be added: ");
            String type = scanner.nextLine();
            System.out.println("Please enter the treatment interval: ");
            int interval = Integer.parseInt(scanner.nextLine());
            System.out.println("Please enter the doctor notes: ");
            String doctorNotes = scanner.nextLine();

            if (type.equals("Med")) {
                System.out.println("Please enter the name of the treatment to be added: ");
                String medicationName = scanner.nextLine();

                boolean okMedicine = true;
                for (int j = 0; j < appointment.getPatient().getMedicalRecord().getAllergies().length; j++) {
                    if (medicationName.equals(appointment.getPatient().getMedicalRecord().getAllergies()[j])) {
                        System.out.println("The patient has an allergy to this medicine");
                        okMedicine = false;
                        i--;
                    }
                }
                if (okMedicine) {
                    System.out.println("Please enter the dosage of the treatment to be added: ");
                    double dosage = Double.parseDouble(scanner.nextLine());
                    Treatment treatment = new MedicationTreatment(interval, doctorNotes, medicationName, dosage);
                    treatmentArray[i] = treatment;
                }
            } else if (type.equals("Phy")) {
                System.out.println("Please enter the name of the exercise to be added: ");
                String exerciseName = scanner.nextLine();
                System.out.println("Please enter the exercise number of repetitions to be added: ");
                int repetitions = Integer.parseInt(scanner.nextLine());
                Treatment treatment = new PhysiotherapyTreatment(interval, doctorNotes, repetitions, exerciseName);
                treatmentArray[i] = treatment;
            } else {
                System.out.println("The treatment type is invalid");
                return;
            }

        }

        System.out.println("Please enter the possible cause of the disease");
        String possibleCause = scanner.nextLine();

        Diagnostic diagnostic = new Diagnostic(name, treatmentArray, possibleCause);

        appointment.setDiagnostic(diagnostic);
        System.out.println("Diagnostic added to appointment");
    }

    public void addDoctor(Doctor doctor) {

        doctors.add(doctor);
    }

    public void doctorsSpecialty(String specialty) {
        StringBuilder sb = new StringBuilder();

        System.out.println("Doctors for the " + specialty + " specialty: ");
        for (Doctor doctor : doctors) {
            if (doctor.getSpecialty().getSpecialtyName().equals(specialty)) {
                sb.append("Doctor: " + doctor.getFirstName() + " " + doctor.getLastName() + "\n");
            }
        }

        if (!sb.isEmpty()) {
            System.out.println(sb);
        }
        else{
            System.out.println("No Doctors found for this specialty");
        }

    }

    public void specialtyMedicalServices(String specialty) {
        Specialty spec = null;
        for (Doctor doctor : doctors) {
            if (doctor.getSpecialty().getSpecialtyName().equals(specialty)) {
                spec = doctor.getSpecialty();
            }
        }

        if(spec == null) {
            System.out.println("The specialty " + specialty + " doesn't exist");
            return;
        }
        for (int i = 0; i < spec.getMedicalServices().length; i++)
            System.out.println("Medical service name: " + spec.getMedicalServices()[i].getServiceName() + ", price: " + spec.getMedicalServices()[i].getServicePrice());

    }

    public void increaseSalary() {
        LocalDate today = LocalDate.now();

        for (Doctor doctor : doctors) {
            int yearsWorked = today.getYear() - doctor.getHireDate().getYear();

            if (yearsWorked >= 1) {
                double newSalary = doctor.getSalary() * 1.1;
                doctor.setSalary(newSalary);
                System.out.println("Salary increased for Dr. " + doctor.getFirstName() + " " + doctor.getLastName() + " to: " + newSalary);
            }
        }
    }

    public void addPatient(Patient patient) {
        patients.add(patient);
    }

    public void allPatientAppointments(String firstName, String lastName) {
        StringBuilder sb = new StringBuilder();
        for (Patient patient : patients) {
            if (patient.getFirstName().equals(firstName) && patient.getLastName().equals(lastName)) {
                for (Appointment appointment : appointments) {
                    if (appointment.getPatient().equals(patient)) {
                        sb.append("\nAppointment date: " + appointment.getAppointmentDate()
                                + " doctor: " + appointment.getDoctor().getFirstName() + " " + appointment.getDoctor().getLastName()
                                + (appointment.getDiagnostic() != null && appointment.getDiagnostic().getName() != null
                                ? " diagnostic: " + appointment.getDiagnostic().getName()
                                : "") + " ");
                        if (appointment.getDiagnostic() != null) {
                            for (Treatment treatment : appointment.getDiagnostic().getTreatments()) {
                                sb.append("Treatment type: " + treatment.treatmentType() + " treatment explained: " + treatment.treatmentDescription() + " ");
                            }
                        } else {
                            sb.append("No diagnostic added for this appointment.\n");
                        }

                    }
                }
            }
        }
        if(!sb.isEmpty()) {
            System.out.println(sb);
        }
        else{
            System.out.println("No appointments found for this patient");
        }

    }

    public void appointmentReschedule(String patientName, String doctorName, LocalDate appointmentDate, LocalDate rescheduleDate, LocalTime rescheduleTime) {

        boolean appointmentFound = false;
        int appointmentIndex = -1;
        Appointment appointment = null;


        if (rescheduleDate.isBefore(LocalDate.now())) {
            System.out.println("The reschedule date is in the past, can't reschedule the appointment");
            return;
        } else if (appointmentDate.isBefore(LocalDate.now())) {
            System.out.println("the appointment has already taken place");
            return;
        }

        for(Appointment app: appointments) {
            if ((app.getDoctor().getFirstName() + " " + app.getDoctor().getLastName()).equals(doctorName)
                    && app.getAppointmentDate().equals(appointmentDate) &&
                    (app.getPatient().getFirstName() + " " + app.getPatient().getLastName()).equals(patientName)) {

                appointment = app;
                appointmentIndex = appointments.indexOf(app);
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

        appointments.get(appointmentIndex).setAppointmentDate(rescheduleDate);
        appointments.get(appointmentIndex).setAppointmentInterval(reschTime);
        System.out.println("Appointment successfully rescheduled to: " + rescheduleDate + " at " + rescheduleTime);

    }

    public void profitDoctor() {
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
        }
    }

    public void viewSchedule(Doctor doctor) {
        System.out.println("\nThe schedule for doctor: " + doctor.getFirstName() + " " + doctor.getLastName());
        doctor.getSchedule().printSchedule();

    }

    public void planAppointment(String specialtyName, LocalDate startInterval, LocalDate endInterval) {
        StringBuilder sb = new StringBuilder();
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

    public void patientMoney(){
        double totalMoney = 0;
        double serviceMoney = 0;
        for(Appointment appointment : appointments){
            if(LocalDate.now().getYear() == appointment.getAppointmentDate().getYear()){
                serviceMoney = appointment.getMedicalService().getServicePrice();
                totalMoney += appointment.getPatient().calculateBill(serviceMoney);
            }
        }
        System.out.println("Total money from patients this year is: " + totalMoney);
    }


     */

}




