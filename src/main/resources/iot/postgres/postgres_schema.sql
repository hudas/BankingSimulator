
CREATE TABLE hospital_patient (
   patient_id bigint NOT NULL,
   name varchar(64) DEFAULT 'Unnamed',
   surname varchar(64) DEFAULT '',
   PRIMARY KEY (PATIENT_ID)
);


CREATE TABLE condition_log (
   patient_id bigint NOT NULL,
   log_id bigint NOT NULL,
   measurement_time timestamp NOT NULL,
   blood_pressure bigint NOT NULL,
   heart_rate bigint NOT NULL,
   body_temperature bigint NOT NULL,
   PRIMARY KEY (PATIENT_ID, LOG_ID)
);

CREATE SEQUENCE condition_log_id_seq;
CREATE INDEX condition_time ON condition_log(measurement_time DESC);

