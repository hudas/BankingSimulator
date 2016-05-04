
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


CREATE TABLE patient_stats (
   patient_id bigint NOT NULL,
   tmp_pressure_sum bigint not null DEFAULT 0,
   pressure_min bigint not null DEFAULT 0,
   pressure_max BIGINT not null DEFAULT 0,

   tmp_rate_sum BIGINT not null DEFAULT 0,
   rate_min bigint not null DEFAULT 0,
   rate_max BIGINT not null DEFAULT 0,

   tmp_temp_sum BIGINT not null DEFAULT 0,
   temp_min bigint not null DEFAULT 0,
   temp_max bigint not null DEFAULT 0,

   measurement_count bigint not null DEFAULT 0,
   latest_blood_pressure bigint NOT NULL DEFAULT 0,
   latest_heart_rate bigint NOT NULL DEFAULT 0,
   latest_body_temperature bigint NOT NULL DEFAULT 0,
   PRIMARY KEY (PATIENT_ID)
);

CREATE SEQUENCE condition_log_id_seq;
CREATE INDEX condition_time ON condition_log(measurement_time DESC);

