package com.ignas.banking.simulation;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by ignas on 4/5/16.
 */
@DatabaseTable(tableName = "predefined_client")
public class PredefinedClient {

    @DatabaseField(columnName = "client_id", generatedIdSequence = "client_id_seq")
    private Long clientId;

    @DatabaseField(columnName = "client_name")
    private String clientName;

    @DatabaseField(columnName = "client_surname")
    private String clientSurname;

    @DatabaseField(columnName = "client_code")
    private String clientCode;

    public PredefinedClient() {
    }

    public PredefinedClient(String clientName, String clientSurname, String clientCode) {
        this.clientName = clientName;
        this.clientSurname = clientSurname;
        this.clientCode = clientCode;
    }


    public Long getClientId() {
        return clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public String getClientSurname() {
        return clientSurname;
    }

    public String getClientCode() {
        return clientCode;
    }

    @Override
    public String toString() {
        return "PredefinedPatient{clientName='" + clientName + '\'' +
                ", clientSurname='" + clientSurname + '\'' +
                ", clientCode='" + clientCode + '\'' +
                '}';
    }
}
