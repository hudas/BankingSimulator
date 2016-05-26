package com.company;

import org.voltdb.SQLStmt;
import org.voltdb.VoltProcedure;
import org.voltdb.VoltTable;

public class LatestConditionStats extends VoltProcedure {

    public final SQLStmt findLatestCondition = new SQLStmt(
            "SELECT log.* " +
                    "FROM condition_log log " +
                    "WHERE log.patient_id = ? " +
                    "      and log.measurement_time > TO_TIMESTAMP(second, SINCE_EPOCH(second, NOW) - 15*60) " +
                    "ORDER BY log.measurement_time DESC;");

    public VoltTable run(long patientId) throws VoltProcedure.VoltAbortException {
        voltQueueSQL(findLatestCondition, patientId);
        VoltTable[] table = voltExecuteSQL();

        VoltTable resultRow = table[0];
        return resultRow;
    }
}
