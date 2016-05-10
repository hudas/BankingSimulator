package com.ignas.iot;

import com.ignas.iot.operations.Operations;
import com.j256.ormlite.stmt.query.In;
import org.jfairy.Fairy;
import org.jfairy.producer.person.Person;

import java.util.Locale;

/**
 * Integration point of business rules and persistance layer
 */
public class OperationService {
    public static final Integer MIN_HEART_RATE = 40;
    public static final Integer MAX_HEAR_RATE = 140;

    public static final Integer MIN_BLOOD_PRESSURE = 70;
    public static final Integer MAX_BLOOD_PRESSURE = 160;

    public static final Integer MIN_BODY_TEMP = 34;
    public static final Integer MAX_BODY_TEMP = 42;

    private static volatile Long createdConditions = 0L;

    private Integer patients;
    private Operations operations;
    private Fairy dataProvider;




    public OperationService(Operations ops, Integer patients) {
        operations = ops;
        dataProvider = Fairy.create(Locale.ENGLISH);
        this.patients = patients;
    }

    public void insertPatient(Long patientId) {
        Person person = dataProvider.person();
        operations.insertPatient(person.firstName(), person.lastName(), patientId);
    }


    public void insertRawCondition() {
        Integer pressure = dataProvider.baseProducer().randomBetween(MIN_BLOOD_PRESSURE, MAX_BLOOD_PRESSURE);
        Integer rate = dataProvider.baseProducer().randomBetween(MIN_HEART_RATE, MAX_HEAR_RATE);
        Integer temperature = dataProvider.baseProducer().randomBetween(MIN_BODY_TEMP, MAX_BODY_TEMP);

        operations.insertRawCondition(getRandomPatientId(), ++createdConditions, pressure, rate, temperature);
    }

    public void getLatestCondition() {
        operations.getLatestCondition(getRandomPatientId());
    }

    public void getLatestViewCondition() {
        operations.getLatestViewCondition(getRandomPatientId());
    }


    public void insertConditionWithStats() {
        Integer pressure = dataProvider.baseProducer().randomBetween(MIN_BLOOD_PRESSURE, MAX_BLOOD_PRESSURE);
        Integer rate = dataProvider.baseProducer().randomBetween(MIN_HEART_RATE, MAX_HEAR_RATE);
        Integer temperature = dataProvider.baseProducer().randomBetween(MIN_BODY_TEMP, MAX_BODY_TEMP);

        operations.insertConditionWithStats(getRandomPatientId(), ++createdConditions, pressure, rate, temperature);
    }

    public void getLatestConditionStats() {
        operations.getLatestConditionStats(getRandomPatientId());
    }

    public void getDailyConditionStats() {
        operations.getDailyConditionStats();
    }

    public void removeOldData() {
        operations.removeOldData(0);
    }

    public void removeAllData() {
        operations.removeAllData();
    }

    private Integer getRandomPatientId() {
        return dataProvider.baseProducer().randomBetween(0, patients - 1);
    }
}
