package com.ignas.iot;

import java.sql.*;

/**
 * Created by ignas on 4/3/16.
 */
public class PostgresIOT implements IOTOperations {

    private Connection connection;

    public PostgresIOT(Connection connection) {
        this.connection = connection;
    }

    public void insertRawCondition(long patientId, long conditionId, long bloodPressure, long heartRate, long bodyTemperature) {
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
            throw new RuntimeException("Failed to Log patient condition");
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
            throw new RuntimeException("Failed to insert new Patient");
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
            throw new RuntimeException("Failed to get Latest Condition: " + patientId);
        }
    }

    public void getLatestViewCondition(long patientId) {

    }

    public void insertConditionWithStats(long patientId, long conditionId, long bloodPressure, long heartRate, long bodyTemperature) {
        CallableStatement callable = null;
        try {
            callable = connection.prepareCall("{call InsertConditionStats(?, ?, ?, ?)}");
            callable.setLong(1, patientId);
            callable.setLong(2, bloodPressure);
            callable.setLong(3, heartRate);
            callable.setLong(4, bodyTemperature);
            callable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to Log patient condition");
        }
    }

    public void getLatestConditionStats(long patientId) {
        CallableStatement callable = null;
        ResultSet resultSet = null;
        try {
            callable = connection.prepareCall("{call FindLatestConditionStats(?)}");
            callable.setLong(1, patientId);
            resultSet = callable.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to get Latest Condition: " + patientId);
        }
    }

    public void getDailyConditionStats() {
        CallableStatement callable = null;
        ResultSet resultSet = null;
        try {
            callable = connection.prepareCall("{call DailyConditionStats()}");
            resultSet = callable.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to get Daily stats:");
        }
    }

    public void removeOldData(long maxConditionId) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM condition_log WHERE log_id > 120482561");
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to Remove Old Data");
        }
    }

    public void removeAllData() {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM condition_log");
            statement.executeUpdate();
            statement = connection.prepareStatement("DELETE FROM hospital_patient");
            statement.executeUpdate();
            statement = connection.prepareStatement("DELETE FROM patient_stats");
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to Remove Old Data");
        }
    }
}
