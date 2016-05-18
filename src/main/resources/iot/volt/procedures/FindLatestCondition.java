package com.company;

import javafx.scene.control.TableRow;
import org.voltdb.SQLStmt;
import org.voltdb.VoltProcedure;
import org.voltdb.VoltTable;
import org.voltdb.VoltTableRow;

import java.util.Date;

public class FindLatestCondition extends VoltProcedure {

    public final SQLStmt findLatestCondition = new SQLStmt(
            "SELECT * FROM condition_log WHERE patient_id = ? ORDER BY measurement_time DESC LIMIT 1;");

    public VoltTable run(long patientId) throws VoltAbortException {
        voltQueueSQL(findLatestCondition, patientId);
        VoltTable[] table = voltExecuteSQL();

        VoltTable resultRow = table[0];
        return resultRow;
    }
}
