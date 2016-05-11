package com.ignas.iot.operations.impl;

import com.google.common.collect.Iterables;
import com.ignas.iot.operations.Operations;
import com.mongodb.MongoClient;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.joda.time.LocalDateTime;

import java.util.Arrays;
import java.util.Date;

/**
 * Created by ignas on 4/3/16.
 */
public class Mongo implements Operations {

    private MongoDatabase db;

    public Mongo() {
        MongoClient mongoClient = new MongoClient();
        db = mongoClient.getDatabase("iot_simulation");
    }

    public void insertRawCondition(long patientId, long conditionId, long bloodPressure, long heartRate, long bodyTemperature) {
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
        document.put("patient_id", patientId);
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

    public void getLatestViewCondition(long patientId) {

    }

    @Override
    public void fastDelete(long patientId) {

    }

    public void insertConditionWithStats(long patientId, long conditionId, long bloodPressure, long heartRate, long bodyTemperature) {
        MongoCollection collection = db.getCollection("condition_log");
        Document document = new Document();
        document.put("patient_id", patientId);
        document.put("condition_id", 30);
        document.put("measurement_time", new Date());
        document.put("blood_pressure", bloodPressure);
        document.put("heart_rate", heartRate);
        document.put("body_temperature", bodyTemperature);
        collection.insertOne(document);

        FindIterable<Document> docs = db.getCollection("hospital_patient").find(new Document("patient_id", patientId));
        Document doc = Iterables.getFirst(docs, null);

        Document stats = (Document) doc.get("patient_stat");

        Long pressureSum = 0L;
        Long rateSum = 0L;
        Long tempSum = 0L;

        Long pressureMin = 0L;
        Long pressureMax = 0L;

        Long rateMin = 0L;
        Long rateMax = 0L;

        Long temperatureMin = 0L;
        Long temperatureMax = 0L;

        if (stats != null) {
            pressureSum = stats.getLong("tmp_pressure_sum");
            rateSum = stats.getLong("tmp_rate_sum");
            tempSum = stats.getLong("tmp_temp_sum");

            pressureMin = stats.getLong("pressure_min");
            pressureMax = stats.getLong("pressure_max");

            rateMin = stats.getLong("rate_min");
            rateMax = stats.getLong("rate_max");

            temperatureMin = stats.getLong("temperature_min");
            temperatureMax = stats.getLong("temperature_max");

            if (bloodPressure < pressureMin) {
                pressureMin = bloodPressure;
            }

            if (bloodPressure > pressureMax) {
                pressureMax = bloodPressure;
            }

            if (heartRate < rateMin) {
                rateMin = heartRate;
            }

            if (heartRate > rateMax) {
                rateMax = heartRate;
            }

            if (bodyTemperature < temperatureMin) {
                temperatureMin = bodyTemperature;
            }

            if (bloodPressure > temperatureMax) {
                temperatureMax = bodyTemperature;
            }
        } else {
            pressureMin = bloodPressure;
            pressureMax = bloodPressure;

            rateMin = heartRate;
            rateMax = heartRate;

            temperatureMin = bodyTemperature;
            temperatureMax = bodyTemperature;
        }

        db.getCollection("hospital_patient")
                .updateOne(
                        new Document("patient_id", patientId),
                        new Document("$set",
                                new Document("patient_stat",
                                    new Document("tmp_pressure_sum", pressureSum + bloodPressure)
                                            .append("tmp_rate_sum", rateSum + heartRate)
                                            .append("tmp_temp_sum", tempSum + bodyTemperature)
                                            .append("pressure_min", pressureMin)
                                            .append("pressure_max", pressureMax)
                                            .append("rate_min", rateMin)
                                            .append("rate_max", rateMax)
                                            .append("temperature_min", temperatureMin)
                                            .append("temperature_max", temperatureMax)
                                )
                        )
        );
    }

    public void getLatestConditionStats(long patientId) {
        MongoCollection table = db.getCollection("condition_log");

        FindIterable<Document> result = table.find()
                .filter(
                        Filters.and(
                                Filters.gt("measurement_time", LocalDateTime.now().minusMinutes(15).toDate()),
                                Filters.eq("patient_id", patientId)
                        )
                )
                .sort(new Document("measurement_time", -1));
    }

    public void getDailyConditionStats() {
        MongoCollection table = db.getCollection("condition_log");

        AggregateIterable<Document> result = table
                .aggregate(
                        Arrays.asList(
                                new Document("$group",
                                        new Document("_id", "$patient_id")
                                                .append("avg_blood_pressure", new Document("$avg", "$blood_pressure"))
                                                .append("min_blood_pressure", new Document("$min", "$blood_pressure"))
                                                .append("max_blood_pressure", new Document("$max", "$blood_pressure"))
                                                .append("avg_heart_rate", new Document("$avg", "$heart_rate"))
                                                .append("min_heart_rate", new Document("$min", "$heart_rate"))
                                                .append("max_heart_rate", new Document("$max", "$heart_rate"))
                                                .append("avg_body_temperature", new Document("$avg", "$body_temperature"))
                                                .append("min_body_temperature", new Document("$min", "$body_temperature"))
                                                .append("max_body_temperature", new Document("$max", "$body_temperature"))
                                )
                        )
                );
    }

    public void removeOldData(long maxConditionId) {
        db.getCollection("condition_log").deleteMany(new Document());
    }

    public void removeAllData() {
        db.getCollection("hospital_patient").deleteMany(new Document());
        db.getCollection("condition_log").deleteMany(new Document());
    }

}
