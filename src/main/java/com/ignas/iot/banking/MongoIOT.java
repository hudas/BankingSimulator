package com.ignas.iot.banking;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

import java.math.BigDecimal;

/**
 * Created by ignas on 4/3/16.
 */
class MongoIOT implements IOTOperations {


    public MongoIOT() {
        MongoClient mongoClient = new MongoClient();
        MongoDatabase db = mongoClient.getDatabase("test");
    }

    public void insertCondition(long patientId, long conditionId, long bloodPressure, long heartRate, long bodyTemperature) {

    }

    public void insertPatient(String name, String surname, Long patientId) {

    }

    public void getLatestCondition(long patientId) {

    }

}
