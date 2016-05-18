package com.company;

import org.voltdb.SQLStmt;
import org.voltdb.VoltProcedure;

import java.util.Date;

public class InsertCondition extends VoltProcedure {

    public final SQLStmt insertCondition = new SQLStmt(
            "INSERT INTO condition_log(patient_id, log_id, measurement_time, blood_pressure, heart_rate, body_temperature)" +
                    " VALUES(?, ?, ?, ?, ?, ?);");

    public long run(long patientId, long logId, long bloodPressure, long hearRate, long bodyTemperature) throws VoltAbortException {
        voltQueueSQL(insertCondition, patientId, logId, getTransactionTime(), bloodPressure, hearRate, bodyTemperature);
        voltExecuteSQL();

        return 0;
    }
}
