package com.company;

import org.voltdb.SQLStmt;
import org.voltdb.VoltProcedure;
import org.voltdb.VoltTable;
import org.voltdb.VoltTableRow;

import java.sql.ResultSet;

public class InsertConditionWithStats extends VoltProcedure {
    public final SQLStmt insertCondition = new SQLStmt(
            "INSERT INTO condition_log(patient_id, log_id, measurement_time, blood_pressure, heart_rate, body_temperature)" +
                    " VALUES(?, ?, ?, ?, ?, ?);");

    public final SQLStmt selectCondition = new SQLStmt(
            "SELECT * FROM patient_stats WHERE patient_id = ?;");

    public final SQLStmt updateCondition = new SQLStmt(
            "UPDATE patient_stats SET " +
                    " tmp_pressure_sum = COALESCE(tmp_pressure_sum + ?, ?)," +
                    " pressure_min = ?, " +
                    " pressure_max = ?, " +
                    " tmp_rate_sum = COALESCE(tmp_rate_sum + ?, ?)," +
                    " rate_min = ?, " +
                    " rate_max = ?, " +
                    " tmp_temp_sum = COALESCE(tmp_temp_sum + ?, ?)," +
                    " temp_min = ?, " +
                    " temp_max = ?, " +
                    " measurement_count = ?" +
                    " WHERE patient_id = ? ");

    public long run(long patientId, long logId, long bloodPressure, long hearRate, long bodyTemperature) throws VoltProcedure.VoltAbortException {
        voltQueueSQL(insertCondition, patientId, logId, getTransactionTime(), bloodPressure, hearRate, bodyTemperature);
        voltQueueSQL(selectCondition, patientId);
        VoltTable[] resultSet = voltExecuteSQL();

        VoltTable table = resultSet[1];
        VoltTableRow row = table.fetchRow(0);

        Long pressureMin = row.getLong("pressure_min");
        Long pressureMax = row.getLong("pressure_max");

        if (pressureMin == null || pressureMax == null || pressureMin < 0 || pressureMax < 0) {
            pressureMin = hearRate;
            pressureMax = hearRate;
        } else {
            if (bloodPressure < pressureMin) {
                pressureMin = bloodPressure;
            } else if (bloodPressure > pressureMax) {
                pressureMax = bloodPressure;
            }
        }

        Long rateMin = row.getLong("rate_min");
        Long rateMax = row.getLong("rate_max");

        if (rateMin == null || rateMin < 0 || rateMax == null || rateMax < 0) {
            rateMin = hearRate;
            rateMax = hearRate;
        } else {
            if (hearRate < rateMin) {
                rateMin = hearRate;
            } else if (hearRate > rateMax) {
                rateMax = hearRate;
            }
        }

        Long tempMin = row.getLong("temp_min");
        Long tempMax = row.getLong("temp_max");

        if (tempMin == null || tempMax == null || tempMin < 0 || tempMax < 0) {
            tempMin = bodyTemperature;
            tempMax = bodyTemperature;
        } else {
            if (bodyTemperature < tempMin) {
                tempMin = bodyTemperature;
            } else if (bodyTemperature > tempMax) {
                tempMax = bodyTemperature;
            }
        }

        Long count = row.getLong("measurement_count");

        if (count < 0 || count == null) {
            count = 1L;
        } else {
            count++;
        }

        voltQueueSQL(updateCondition,
                bloodPressure, bloodPressure, pressureMin, pressureMax,
                hearRate, hearRate, rateMin, rateMax,
                bodyTemperature, bodyTemperature, tempMin, tempMax,
                count,
                patientId);

        voltExecuteSQL();

        return 0;
    }
}