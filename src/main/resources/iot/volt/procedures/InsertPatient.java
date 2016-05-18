package com.company;

import org.voltdb.*;

public class InsertPatient extends VoltProcedure {

    public final SQLStmt insertPatient = new SQLStmt(
            "INSERT INTO hospital_patient(patient_id, name, surname)" +
                    " VALUES(?, ?, ?);");
    public final SQLStmt insertPatientStat = new SQLStmt(
            "INSERT INTO patient_stats(patient_id) VALUES(?);");

    public long run(long patientId, String name, String surname) throws VoltAbortException {
        voltQueueSQL(insertPatient, patientId, name, surname);
        voltQueueSQL(insertPatientStat, patientId);
        voltExecuteSQL();

        return 0;
    }
}

