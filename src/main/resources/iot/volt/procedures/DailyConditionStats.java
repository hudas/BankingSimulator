package com.company;

import org.voltdb.SQLStmt;
import org.voltdb.VoltProcedure;
import org.voltdb.VoltTable;

public class DailyConditionStats extends VoltProcedure {

    public final SQLStmt dailyStats = new SQLStmt(
            "SELECT " +
                    "  log.patient_id patient_id, " +
                    "  AVG(log.blood_pressure) avg_blood_pressure, MAX(log.blood_pressure) max_blood_pressure, MIN(log.blood_pressure) min_blood_pressure, " +
                    "  AVG(log.heart_rate) avg_heart_rate, MAX(log.heart_rate) max_heart_rate, MIN(log.heart_rate) min_heart_rate, " +
                    "  AVG(log.body_temperature) avg_body_temperature, MAX(log.body_temperature) max_body_temperature, MIN(log.body_temperature) min_body_temperature " +
                    "FROM condition_log log " +
                    " WHERE YEAR(log.measurement_time) = YEAR(NOW) " +
                    "   AND MONTH(log.measurement_time) = MONTH(NOW) " +
                    "   AND DAY(log.measurement_time) = DAY(NOW) " +
                    "GROUP BY log.patient_id;");

    public VoltTable run() throws VoltAbortException {
        voltQueueSQL(dailyStats);
        VoltTable[] table = voltExecuteSQL();

        VoltTable resultRow = table[0];
        return resultRow;
    }
}