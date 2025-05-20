CREATE TABLE medical_record(
    id BIGSERIAL PRIMARY KEY,
    allergies TEXT[],
    chronic_conditions TEXT[],
    physical_restrictions TEXT[]
);

CREATE TABLE patient(
    id BIGSERIAL PRIMARY KEY,
    first_name varchar(40),
    last_name varchar(40),
    personal_ID varchar(30) UNIQUE,
    email varchar(100),
    phone varchar(11),
    birth_date date,
    patient_type varchar(25),
    medical_record_id BIGINT,
    FOREIGN KEY(medical_record_id) REFERENCES medical_record(id) ON DELETE SET NULL

);

CREATE TYPE health_insurance_type AS ENUM ('PUBLIC', 'UNINSURED');
CREATE TYPE membership_type AS ENUM ('PLATINUM', 'GOLD', 'SILVER');

CREATE TABLE adult_patient(
    patient_id BIGINT,
    health_insurance health_insurance_type,
    FOREIGN KEY (patient_id) REFERENCES patient(id) ON DELETE CASCADE
);

CREATE TABLE child_patient(
    patient_id BIGINT,
    guardian_name varchar(120),
    guardian_id varchar(11),
    FOREIGN KEY (patient_id) REFERENCES patient(id) ON DELETE CASCADE
);

CREATE TABLE member_patient(
    patient_id BIGINT,
    membership_number varchar(1000),
    membership membership_type,
    FOREIGN KEY (patient_id) REFERENCES patient(id) ON DELETE CASCADE
);

CREATE TABLE medical_service(
    id BIGSERIAL PRIMARY KEY,
    service_name varchar(70),
    service_price numeric(9, 2),
    service_time decimal
);

CREATE TABLE specialty(
    id BIGSERIAL PRIMARY KEY,
    specialty_name varchar(100),
    starting_salary numeric(9, 2)
);

CREATE TABLE specialty_medical_service(
    specialty_id BIGINT,
    FOREIGN KEY(specialty_id) REFERENCES specialty(id) ON DELETE CASCADE,
    medical_service_id BIGINT,
    FOREIGN KEY(medical_service_id) REFERENCES medical_service(id) ON DELETE CASCADE

);

CREATE TABLE doctor(
    id BIGSERIAL PRIMARY KEY,
    first_name varchar(40),
    last_name varchar(40),
    personal_ID varchar(30) UNIQUE,
    email varchar(100),
    phone varchar(11),
    birth_date date,
    hire_date date,
    specialty_id BIGINT,
    salary numeric(9, 2),
    FOREIGN KEY(specialty_id) REFERENCES specialty(id) ON DELETE SET NULL
);

CREATE TABLE schedule(
    id BIGSERIAL PRIMARY KEY,
    doctor_id bigint NOT NULL,
    FOREIGN KEY(doctor_id) REFERENCES doctor(id) ON DELETE CASCADE
);

CREATE TABLE time_interval(
    id BIGSERIAL PRIMARY KEY,
    schedule_id bigint,
    day int,
    start_time time,
    end_time time,
    FOREIGN KEY(schedule_id) REFERENCES schedule(id) ON DELETE CASCADE

);

CREATE TABLE medication_treatment(
    id BIGSERIAL PRIMARY KEY,
    medication_name varchar(50),
    dosage numeric(9, 3),
    treatment_interval int
);

CREATE TABLE physiotherapy_treatment(
    id BIGSERIAL PRIMARY KEY,
    exercise_name varchar(50),
    repetitions int,
    medical_issues varchar(200)
);

CREATE TABLE diagnostic(
    id BIGSERIAL PRIMARY KEY,
    diagnostic_name varchar(100)
);

CREATE TABLE diagnostic_treatment(
    diagnostic_id BIGINT,
    treatment_id BIGINT,
    treatment_type varchar(50),
    FOREIGN KEY (diagnostic_id) REFERENCES diagnostic(id) ON DELETE SET NULL,
    PRIMARY KEY (diagnostic_id, treatment_id, treatment_type)
);

CREATE TABLE appointment(
                            id BIGSERIAL PRIMARY KEY,
                            doctor_id BIGINT,
                            patient_id BIGINT,
                            appointment_date DATE,
                            medical_service_id BIGINT,
                            diagnostic_id BIGINT,
                            FOREIGN KEY (doctor_id) REFERENCES doctor(id) ON DELETE SET NULL,
                            FOREIGN KEY (patient_id) REFERENCES patient(id) ON DELETE SET NULL,
                            FOREIGN KEY (medical_service_id) REFERENCES medical_service(id) ON DELETE SET NULL,
                            FOREIGN KEY (diagnostic_id) REFERENCES diagnostic(id) ON DELETE SET NULL
);

CREATE TABLE appointment_time_interval(
    id BIGSERIAL PRIMARY KEY,
    appointment_id BIGINT NOT NULL,
    start_appointment time,
    end_appointment time,
    FOREIGN KEY (appointment_id) REFERENCES appointment(id) ON DELETE CASCADE
);






























