CREATE OR REPLACE FUNCTION insertpatient (
  in_patient_id BIGINT,
  in_patient_name VARCHAR(32),
  in_patient_surname VARCHAR(32))
  RETURNS VOID
AS $$
BEGIN
  INSERT INTO hospital_patient (patient_id, name, surname)
    VALUES(in_patient_id, in_patient_name, in_patient_surname);
  INSERT INTO patient_stats(patient_id)
  VALUES(in_patient_id);
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

CREATE OR REPLACE FUNCTION insertconditionstats (
  in_patient_id BIGINT,
  in_blood_pressure BIGINT,
  in_heart_rate BIGINT,
  in_body_temperature BIGINT)
  RETURNS BOOLEAN
AS $$
DECLARE
  var_patient_id BIGINT := NULL;

  var_tmp_pressure_sum bigint := null;
  var_pressure_min bigint := null;
  var_pressure_max BIGINT := null;

  var_tmp_rate_sum BIGINT := null;
  var_rate_min bigint := null;
  var_rate_max BIGINT := null;

  var_tmp_temp_sum BIGINT := null;
  var_temp_min bigint := null;
  var_temp_max bigint := null;

  var_measurement_count bigint := null;
BEGIN
  SELECT patient_id INTO var_patient_id FROM hospital_patient WHERE patient_id = in_patient_id;

  IF var_patient_id IS NULL THEN
    RETURN FALSE;
  END IF;

  SELECT tmp_pressure_sum, pressure_min, pressure_max,
    tmp_rate_sum, rate_min, rate_max,
    tmp_temp_sum, temp_min, temp_max,
    measurement_count
    INTO
      var_tmp_pressure_sum, var_pressure_min, var_pressure_max,
      var_tmp_rate_sum, var_rate_min, var_rate_max,
      var_tmp_temp_sum, var_temp_min, var_temp_max,
    var_measurement_count
   FROM patient_stats WHERE patient_id = in_patient_id;

  var_tmp_pressure_sum := var_tmp_pressure_sum + in_blood_pressure;

  IF in_blood_pressure < var_pressure_min THEN
    var_pressure_min := in_blood_pressure;
  END IF;

  IF in_blood_pressure > var_pressure_max THEN
    var_pressure_max := in_blood_pressure;
  END IF;

  var_tmp_rate_sum := var_tmp_rate_sum + in_heart_rate;

  IF in_heart_rate < var_rate_min THEN
    var_rate_min := in_heart_rate;
  END IF;

  IF in_heart_rate > var_rate_max THEN
    var_pressure_max := in_heart_rate;
  END IF;

  var_tmp_temp_sum := var_tmp_temp_sum + in_body_temperature;

  IF in_body_temperature < var_temp_min THEN
    var_temp_min := in_body_temperature;
  END IF;

  IF in_body_temperature > var_temp_max THEN
    var_temp_max := in_body_temperature;
  END IF;

  var_measurement_count := var_measurement_count + 1;

  UPDATE patient_stats
    SET
      tmp_pressure_sum = var_tmp_pressure_sum,
      tmp_rate_sum = var_tmp_rate_sum,
      tmp_temp_sum = var_tmp_temp_sum,

      pressure_min = var_pressure_min,
      pressure_max = var_pressure_max,

      rate_min = var_rate_min,
      rate_max = var_rate_max,

      temp_min = var_temp_min,
      temp_max = var_temp_max,
      measurement_count = var_measurement_count,

      latest_blood_pressure = in_blood_pressure,
      latest_heart_rate = in_heart_rate,
      latest_body_temperature = in_body_temperature
  WHERE patient_id = in_patient_id;

  RETURN TRUE ;
END; $$ language plpgsql;


CREATE OR REPLACE FUNCTION findlatestcondition (
  in_patient_id BIGINT)
  RETURNS setof condition_log
AS $$ SELECT log.* FROM condition_log log WHERE log.patient_id = in_patient_id ORDER BY log.measurement_time DESC LIMIT 1
$$ language sql;

CREATE OR REPLACE FUNCTION findlatestconditionstats (
  in_patient_id BIGINT)
  RETURNS setof condition_log
AS $$
  SELECT log.*
    FROM condition_log log
      WHERE log.patient_id = in_patient_id
            and log.measurement_time > NOW() - INTERVAL '15 MINUTES'
  ORDER BY log.measurement_time DESC
$$ language sql;


CREATE OR REPLACE FUNCTION dailyconditionstats()
  RETURNS TABLE (patient_id bigint,
                 avg_blood_pressure NUMERIC, max_blood_pressure BIGINT, min_blood_pressure BIGINT,
                 avg_heart_rate NUMERIC, max_heart_rate BIGINT, min_heart_rate BIGINT,
                 avg_body_temperature NUMERIC, max_body_temperature BIGINT, min_body_temperature BIGINT)
AS $$
SELECT
  log.patient_id patient_id,
  AVG(log.blood_pressure) avg_blood_pressure, MAX(log.blood_pressure) max_blood_pressure, MIN(log.blood_pressure) min_blood_pressure,
  AVG(log.heart_rate) avg_heart_rate, MAX(log.heart_rate) max_heart_rate, MIN(log.heart_rate) min_heart_rate,
  AVG(log.body_temperature) avg_body_temperature, MAX(log.body_temperature) max_body_temperature, MIN(log.body_temperature) min_body_temperature
FROM condition_log log
WHERE DATE(log.measurement_time) = DATE(NOW())
GROUP BY log.patient_id;
$$ language sql;