package com.ignas.iot;

import java.math.BigDecimal;

/**
 * Basic interface which specifies all posible banking operations
 */
public interface IOTOperations {

    // management
    void removeOldData(long maxConditionId);
    void removeAllData();
    void insertPatient(String name, String surname, Long patientId);

    // First
    void insertRawCondition(long patientId, long conditionId, long bloodPressure, long heartRate, long bodyTemperature);
    void getLatestCondition(long patientId);
    void getLatestViewCondition(long patientId);

    // Second
    void insertConditionWithStats(long patientId, long conditionId, long bloodPressure, long heartRate, long bodyTemperature);
    void getLatestConditionStats(long patientId);
    void getDailyConditionStats();
}
