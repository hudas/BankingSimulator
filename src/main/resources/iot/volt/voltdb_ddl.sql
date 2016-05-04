CREATE TABLE CONDITION_LOG (
   PATIENT_ID bigint NOT NULL,
   LOG_ID bigint NOT NULL,
   MEASUREMENT_TIME timestamp NOT NULL,
   BLOOD_PRESSURE bigint NOT NULL,
   HEART_RATE bigint NOT NULL,
   BODY_TEMPERATURE bigint NOT NULL,
   SUGAR_LEVEL bigint NOT NULL,
   PRIMARY KEY (PATIENT_ID, LOG_ID)
);
PARTITION TABLE CONDITION_LOG ON COLUMN PATIENT_ID;
CREATE INDEX VOLTDB_AUTOGEN_IDX_CONDITION_LOG_PATIENT_ID ON CONDITION_LOG (PATIENT_ID);

CREATE TABLE HOSPITAL_PATIENT (
   PATIENT_ID bigint NOT NULL,
   NAME varchar(64) DEFAULT 'Unnamed',
   SURNAME varchar(64) DEFAULT '',
   AGE integer,
   PRIMARY KEY (PATIENT_ID)
);
PARTITION TABLE HOSPITAL_PATIENT ON COLUMN PATIENT_ID;

CREATE TABLE patient_stats (
   patient_id bigint NOT NULL,
   tmp_pressure_sum bigint not null,
   pressure_min bigint not null,
   pressure_max BIGINT not null,

   tmp_rate_sum BIGINT not null,
   rate_min bigint not null,
   rate_max BIGINT not null,

   tmp_temp_sum BIGINT not null,
   temp_min bigint not null,
   temp_max bigint not null,

   measurement_count bigint not null,
   latest_blood_pressure bigint NOT NULL,
   latest_heart_rate bigint NOT NULL,
   latest_body_temperature bigint NOT NULL,
   PRIMARY KEY (PATIENT_ID)
);

