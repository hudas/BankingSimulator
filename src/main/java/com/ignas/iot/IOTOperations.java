package com.ignas.iot;

import java.math.BigDecimal;

/**
 * Basic interface which specifies all posible banking operations
 */
public interface IOTOperations {

    // OLTP Operations
    void insertCondition(long patientId, long conditionId, long bloodPressure, long heartRate, long bodyTemperature);
    void insertPatient(String name, String surname, Long patientId);
    void getLatestCondition(long patientId);
}
