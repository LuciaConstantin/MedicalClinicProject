# Medical Clinic Management System
## Project Overview
This application manages a medical clinic, handling patient data, doctor schedules, appointments, diagnostics, and financial reporting.

## Objects
1. Person
2. Patient
3. AdultPatient
4. ChildPatient
5. MemberPatient
6. Doctor
7. MedicalRecord
8. Specialty
9. MedicalService
10. Schedule
11. TimeInterval (Record)
12. Services
13. Appointment
14. Diagnostic
15. Treatment
16. MedicationTreatment
17. PhysiotherapyTreatment
18. HealthInsurance (Enum)
19. Membership (Enum)

## Actions & Queries
1. addAppointment
- if the appointment doesn't exist yet, verify:
- the date of the appointment is a day when the doctor works
- the interval of the appointment is in the working interval of the doctor
- if there are others appointments in that day to that specific doctor, the time intervals don't overlap

2. viewAppointments
- displays information about all the appointments of a specific doctor

3. addDiagnostic
- adds a diagnostic to a specific appointment
- checks if the patients is not allergic to any of the medicine in the treatments

4. doctorsSpecialty
- displays all the doctors for a specific specialty

5. specialtyMedicalServices
- displays all the medical services that a specific specialty treats

6. allPatientAppointments
- all the patient appointments and the diagnostic

7. appointmentReschedule
- reschedule an appointment only if the date is not in the past
- check if the doctor works in the new date and time interval
- or the doctor doesn't have a prior engagement in that interval

8. profitPerDoctor
- gives a raise to the doctor with the highest profit in the last year

9. viewSchedule
- view the schedule of a certain doctor

10. planAppointment
- for a specific specialty checks all the available doctors in a set date interval

11. patientMoney
- calculate the total revenue from patients for the current year

12. addDoctor
13. addPatient
14. displayInformation (Patient and Doctor)

## Interaction between Objects
- Person is inherited by Patient and Doctor
- Patient is inherited by AdultPatient, ChildPatient, MemberPatient
- Patient aggregates medicalRecord
- Doctor aggregates Specialty and Schedule
- Specialty aggregates MedicalService
- Schedule aggregates TimeInterval
- Appointment aggregates Doctor, Patient, MedicalService, TimeInterval, Diagnostic
- Treatment is inherited by MedicationTreatment and PhysiotherapyTreatment
- Diagnostic aggregates Treatment

## System presentation
- The main focus of the project is managing appointments between patients and doctors in a medical clinic. 
Patients can add, reschedule, view previous appointments, or plan a new one based on the availability of doctors.
- The system maintains information about doctors, their specialties, and the medical services they can perform. 
Each doctor has a personalized weekly schedule, and appointments are validated to ensure they fit within their working hours 
and do not overlap with existing bookings.
- Patients are categorized into different types: adults, children, or members, and each patient has a medical record that includes allergies and past diagnostics. 
After a consultation, the doctor can add a diagnostic and assign treatments, with checks to ensure medication does not conflict with known allergies.
- The clinic can also track financial data such as revenue from patients and identify the doctor who generated the most profit in the last year. 
- Various actions and queries are supported, from viewing a doctor’s schedule to exploring available medical services by specialty.

## 10 Tipuri de obiecte
1. Patient
2. Doctor
3. Specialty
4. MedicalRecord
5. MedicalService
6. Schedule
7. Appointment
8. Diagnostic
9. Treatment
10. mai trebuie una cred, idk?


## Interface
Scheduler - addSchedule
- cancelSchedule
- modifySchedule
- viewSchedule

Doctor-
addSchedule- il creez

Appointment
- toate , cred ca e ok


-m-am gandit sa adaug o noua clasa care sa implementeze si ea Scheduler
si de ex recoltarile de probe pentru analize de laborator sa fie intre 7-12
-sau programul clinicii, in anumite zile sa fie modificat
- pot sa adaug not working periods












