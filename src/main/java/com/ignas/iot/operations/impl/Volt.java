package com.ignas.iot.operations.impl;

import com.ignas.iot.operations.Operations;

import java.sql.*;

/**
 * Created by ignas on 4/3/16.
 */
public class Volt implements Operations {

    private Connection connection;

    public Volt(Connection connection) {
        this.connection = connection;
    }

    public void insertRawCondition(long patientId, long conditionId, long bloodPressure, long heartRate, long bodyTemperature) {
        CallableStatement callable = null;
        try {
            callable = connection.prepareCall("{call InsertCondition(?, ?, ?, ?, ?)}");
            callable.setLong(1, patientId);
            callable.setLong(2, conditionId);
            callable.setLong(3, bloodPressure);
            callable.setLong(4, heartRate);
            callable.setLong(5, bodyTemperature);
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

    public void getLatestViewCondition(long patientId) {
        CallableStatement callable = null;
        ResultSet resultSet = null;
        try {
            callable = connection.prepareCall("{call FindLatestConditionView(?)}");
            callable.setLong(1, patientId);
            resultSet = callable.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public void insertConditionWithStats(long patientId, long conditionId, long bloodPressure, long heartRate, long bodyTemperature) {
        CallableStatement callable = null;
        try {
            callable = connection.prepareCall("{call InsertConditionWithStats(?, ?, ?, ?, ?)}");
            callable.setLong(1, patientId);
            callable.setLong(2, conditionId);
            callable.setLong(3, bloodPressure);
            callable.setLong(4, heartRate);
            callable.setLong(5, bodyTemperature);
            callable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public void getLatestConditionStats(long patientId) {
        CallableStatement callable = null;
        ResultSet resultSet = null;
        try {
            callable = connection.prepareCall("{call LatestConditionStats(?)}");
            callable.setLong(1, patientId);
            resultSet = callable.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public void fastDelete(long patientId) {
        CallableStatement callable = null;
        ResultSet resultSet = null;
        try {
            callable = connection.prepareCall("{call FastDelete(?)}");
            callable.setLong(1, patientId);
            resultSet = callable.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public void getDailyConditionStats() {
        CallableStatement callable = null;
        ResultSet resultSet = null;
        try {
            callable = connection.prepareCall("{call DailyConditionStats}");
            resultSet = callable.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public void removeOldData(long maxConditionId) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM condition_log WHERE log_id > ?");
            statement.setLong(1, maxConditionId);
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
            PreparedStatement other = connection.prepareStatement("DELETE FROM hospital_patient");
            other.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to Remove Old Data");
        }
    }
}
