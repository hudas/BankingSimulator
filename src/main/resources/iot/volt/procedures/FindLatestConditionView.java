package com.company;

import org.voltdb.SQLStmt;
import org.voltdb.VoltProcedure;
import org.voltdb.VoltTable;

public class FindLatestConditionView extends VoltProcedure {

    public final SQLStmt findLatestCondition = new SQLStmt(
            "SELECT log.* from latest_measurement mes\n" +
                    "  JOIN condition_log log ON \n" +
                    "mes.patient_id = log.patient_id and log.log_id = mes.log_id\n" +
                    "WHERE mes.patient_id = ?");

    public VoltTable run(long patientId) throws VoltAbortException {
        voltQueueSQL(findLatestCondition, patientId);
        VoltTable[] table = voltExecuteSQL();

        VoltTable resultRow = table[0];
        return resultRow;
    }
}
