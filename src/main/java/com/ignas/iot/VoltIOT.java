package com.ignas.iot;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by ignas on 4/3/16.
 */
public class VoltIOT implements IOTOperations {

    private Connection connection;

    public VoltIOT(Connection connection) {
        this.connection = connection;
    }

    public void insertCondition(long patientId, long conditionId, long bloodPressure, long heartRate, long bodyTemperature) {
        CallableStatement callable = null;
        try {
            callable = connection.prepareCall("{call InsertCondition(?, ?, ?, ?)}");
            callable.setLong(1, patientId);
            callable.setLong(2, bloodPressure);
            callable.setLong(3, heartRate);
            callable.setLong(4, bodyTemperature);
            callable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public void insertPatient(String name, String surname, Long patientId) {
        CallableStatement callable = null;
        try {
            callable = connection.prepareCall("{call InsertPatient(?, ?, ?)}");
            callable.setLong(1, patientId);
            callable.setString(2, name);
            callable.setString(3, surname);
            callable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public void getLatestCondition(long patientId) {
        CallableStatement callable = null;
        ResultSet resultSet = null;
        try {
            callable = connection.prepareCall("{call FindLatestCondition(?)}");
            callable.setLong(1, patientId);
            resultSet = callable.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}
