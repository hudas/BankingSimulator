package com.ignas.iot.simulation;

import com.google.common.collect.ImmutableMap;
import com.ignas.iot.DataAccessException;
import com.ignas.iot.IOTOperations;
import com.ignas.iot.OperationsFactory;
import org.jfairy.Fairy;
import org.jfairy.producer.person.Person;

import java.sql.SQLException;
import java.util.*;

/**
 * Created by ignas on 4/5/16.
 */
public class IOTSimulation {

    private IOTOperations operations;

    private static final ImmutableMap<PredefinedTask.TaskType, Integer> typeDeviation =
            ImmutableMap.<PredefinedTask.TaskType, Integer>builder()
                    .put(PredefinedTask.TaskType.ADD, 10)
                    .put(PredefinedTask.TaskType.WITHDRAW, 10)
                    .put(PredefinedTask.TaskType.TRANSFER, 80)
                    .build();

    public static final Integer PATIENTS = 1000;

    public static final Integer INSERT_CONDITION_FREQ = 10;
//    public static final Integer FIND_LATEST_FREQ = 10;
    public static final Integer FIND_LATEST_FREQ = 0;

    public static final int Threads = 10;

    public static final Integer WORK_ITERATIONS = 100000;

    public static final Integer MIN_HEART_RATE = 40;
    public static final Integer MAX_HEAR_RATE = 140;

    public static final Integer MIN_BLOOD_PRESSURE = 70;
    public static final Integer MAX_BLOOD_PRESSURE = 160;

    public static final Integer MIN_BODY_TEMP = 34;
    public static final Integer MAX_BODY_TEMP = 42;


    public static IOTSimulation ofVolt() {
        try {
            return new IOTSimulation(OperationsFactory.volt());
        } catch (DataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static IOTSimulation ofMongo() {
        try {
            return new IOTSimulation(OperationsFactory.mongo());
        } catch (DataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static IOTSimulation ofPostgres() {
        try {
            return new IOTSimulation(OperationsFactory.postgres());
        } catch (DataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    private IOTSimulation(IOTOperations operations) {
        this.operations = operations;
    }

    public void run() throws SQLException {
        Fairy fairy = Fairy.create(Locale.ENGLISH);

        long startTime = System.currentTimeMillis();
//
//        for (int index = 0; index < PATIENTS; index ++) {
//            Person person = fairy.person();
//            operations.insertPatient(person.firstName(), person.lastName(), new Long(index));
//        }

        long endTime = System.currentTimeMillis();
        System.out.println("Klientai sugeneruoti, užtruko: " + (endTime-startTime));

        startTime = System.currentTimeMillis();

        Long logCount = 0L;

        List<PredefinedAccount> accountList = new ArrayList<PredefinedAccount>();
        for (int i = 0; i < WORK_ITERATIONS; i++) {

            for(int j = 0; j < INSERT_CONDITION_FREQ; j++) {
                Integer selectedPatient = fairy.baseProducer().randomBetween(0, PATIENTS - 1);
                Integer pressure = fairy.baseProducer().randomBetween(MIN_BLOOD_PRESSURE, MAX_BLOOD_PRESSURE);
                Integer rate = fairy.baseProducer().randomBetween(MIN_HEART_RATE, MAX_HEAR_RATE);
                Integer temperature = fairy.baseProducer().randomBetween(MIN_BODY_TEMP, MAX_BODY_TEMP);

                operations.insertCondition(selectedPatient, logCount++, pressure, rate, temperature);
            }

            for(int j = 0; j < FIND_LATEST_FREQ; j++) {
                Integer selectedPatient = fairy.baseProducer().randomBetween(0, PATIENTS - 1);

                operations.getLatestCondition(selectedPatient);
            }
        }

        endTime = System.currentTimeMillis();
        System.out.println("Saskaitos sugeneruotos, uztruko: " + (endTime-startTime));
    }
}

