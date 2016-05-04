CREATE OR REPLACE FUNCTION insertpatient (
  in_patient_id BIGINT,
  in_patient_name VARCHAR(32),
  in_patient_surname VARCHAR(32))
  RETURNS VOID
AS $$
BEGIN
  INSERT INTO hospital_patient (patient_id, name, surname)
    VALUES(in_patient_id, in_patient_name, in_patient_surname);
END; $$ language plpgsql;


CREATE OR REPLACE FUNCTION insertcondition (
  in_patient_id BIGINT,
  in_blood_pressure BIGINT,
  in_heart_rate BIGINT,
  in_body_temperature BIGINT)
  RETURNS BOOLEAN
AS $$
DECLARE
  var_client_id BIGINT := NULL;
BEGIN
  INSERT INTO condition_log (patient_id, log_id, measurement_time, blood_pressure, heart_rate, body_temperature)
  VALUES (in_patient_id, nextval('condition_log_id_seq'), now(), in_blood_pressure, in_heart_rate, in_body_temperature);
  RETURN TRUE ;
END; $$ language plpgsql;


CREATE OR REPLACE FUNCTION findlatestcondition (
  in_patient_id BIGINT)
  RETURNS setof condition_log
AS $$ SELECT log.* FROM condition_log log WHERE log.patient_id = in_patient_id ORDER BY log.measurement_time DESC LIMIT 1
$$ language sql;
