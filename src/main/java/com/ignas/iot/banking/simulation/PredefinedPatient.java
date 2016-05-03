package com.ignas.iot.banking.simulation;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by ignas on 4/5/16.
 */
@DatabaseTable(tableName = "predefined_client")
public class PredefinedPatient {

    @DatabaseField(columnName = "client_id", generatedIdSequence = "client_id_seq")
    private Long patientId;

    @DatabaseField(columnName = "client_name")
    private String name;

    @DatabaseField(columnName = "client_surname")
    private String surname;

    public PredefinedPatient() {
    }

    public PredefinedPatient(Long id, String clientName, String clientSurname) {
        patientId = id;
        this.name = clientName;
        this.surname = clientSurname;
    }


    public Long getPatientId() {
        return patientId;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    @Override
    public String toString() {
        return "PredefinedPatient{name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                '}';
    }
}
