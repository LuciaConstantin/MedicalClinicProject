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
    health_insurance health_insurance_type
)INHERITS (patient);

CREATE TABLE child_patient(
    guardian_name varchar(120),
    guardian_id varchar(11)
)INHERITS (patient);

CREATE TABLE member_patient(
    membership_number varchar(1000),
    membership membership_type
)INHERITS (patient);

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

CREATE TABLE schedule(
    id BIGSERIAL PRIMARY KEY,
    day_of_week INT,
    start_hour INT,
    start_minute INT,
    end_hour INT,
    end_minute INT
);



























