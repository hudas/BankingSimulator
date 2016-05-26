-- VoltDB Schema Definition

CREATE TABLE CONDITION_LOG (
   PATIENT_ID bigint NOT NULL,
   LOG_ID bigint NOT NULL,
   MEASUREMENT_TIME timestamp NOT NULL,
   BLOOD_PRESSURE bigint NOT NULL,
   HEART_RATE bigint NOT NULL,
   BODY_TEMPERATURE bigint NOT NULL,
   PRIMARY KEY (PATIENT_ID, LOG_ID)
);
PARTITION TABLE CONDITION_LOG ON COLUMN PATIENT_ID;
CREATE INDEX VOLTDB_AUTOGEN_IDX_CONDITION_LOG_PATIENT_ID ON CONDITION_LOG (PATIENT_ID);
CREATE INDEX MEASURED_TIME_CONDITION_LOG ON CONDITION_LOG (MEASUREMENT_TIME DESC );

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
   tmp_pressure_sum bigint DEFAULT 0 not null ,
   pressure_min bigint DEFAULT 0 not null ,
   pressure_max BIGINT DEFAULT 0 not null ,

   tmp_rate_sum BIGINT DEFAULT 0 not null ,
   rate_min bigint DEFAULT 0 not null ,
   rate_max BIGINT DEFAULT 0 not null ,

   tmp_temp_sum BIGINT DEFAULT 0 not null ,
   temp_min bigint DEFAULT 0 not null ,
   temp_max bigint DEFAULT 0 not null ,

   measurement_count bigint DEFAULT 0 not null ,
   latest_blood_pressure bigint DEFAULT 0 NOT NULL ,
   latest_heart_rate bigint DEFAULT 0 NOT NULL ,
   latest_body_temperature bigint DEFAULT 0 NOT NULL ,
   PRIMARY KEY (PATIENT_ID)
);

PARTITION TABLE patient_stats ON COLUMN PATIENT_ID;

CREATE PROCEDURE FastDelete
AS DELETE FROM condition_log WHERE patient_id = ? and log_id > 10000000;

PARTITION PROCEDURE FastDelete ON TABLE condition_log COLUMN PATIENT_ID;

-- VIEW:
CREATE VIEW LATEST_MEASUREMENT (
      PATIENT_ID,
      MEASUREMENT_COUNT,
      MEASUREMENT_TIME,
      LOG_ID
) AS
   SELECT PATIENT_ID,COUNT(*),MAX(MEASUREMENT_TIME),MAX(LOG_ID)FROM CONDITION_LOG GROUP BY PATIENT_ID;

