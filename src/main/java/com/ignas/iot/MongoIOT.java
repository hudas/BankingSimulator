package com.ignas.iot;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.Date;

/**
 * Created by ignas on 4/3/16.
 */
class MongoIOT implements IOTOperations {

    private MongoDatabase db;

    public MongoIOT() {
        MongoClient mongoClient = new MongoClient();
        db = mongoClient.getDatabase("iot_simulation");
    }

    public void insertCondition(long patientId, long conditionId, long bloodPressure, long heartRate, long bodyTemperature) {
        MongoCollection collection = db.getCollection("condition_log");
        Document document = new Document();
        document.put("patient_id", patientId);
        document.put("condition_id", 30);
        document.put("measurement_time", new Date());
        document.put("blood_pressure", bloodPressure);
        document.put("heart_rate", heartRate);
        document.put("body_temperature", bodyTemperature);
        collection.insertOne(document);
    }

    public void insertPatient(String name, String surname, Long patientId) {
        MongoCollection table = db.getCollection("hospital_patient");
        Document document = new Document();
        document.put("id", patientId);
        document.put("name", name);
        document.put("surname", surname);
        table.insertOne(document);
    }

    public void getLatestCondition(long patientId) {
        MongoCollection table = db.getCollection("condition_log");

        FindIterable<Document> result = table.find(new Document("patient_id", patientId))
                                             .sort(new Document("measurement_time", -1))
                                             .limit(1);
    }

}
