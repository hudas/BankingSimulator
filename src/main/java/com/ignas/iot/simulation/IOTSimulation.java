package com.ignas.iot.simulation;

import com.google.common.collect.ImmutableMap;
import com.ignas.iot.IOTOperations;
import org.jfairy.Fairy;
import org.jfairy.producer.person.Person;

import java.sql.SQLException;
import java.util.*;

/**
 * Created by ignas on 4/5/16.
 */
public class IOTSimulation {

    private IOTOperations operations;

    // Simulation Definition
    public static final Integer PATIENTS = 1000;
    public static final Integer PREPARED_CONDITIONS = 10000;


    public static final Integer THREAD_COUNT = 50;
    public static final Integer WORK_ITERATIONS = 100;

    public static final Integer INSERT_CONDITION_FREQ = 100;
    public static final Integer FIND_LATEST_FREQ = 0;
    public static final Integer INSERT_COND_STAT_FREQ = 0;
    public static final Integer FIND_LATEST_STAT_FREQ = 0;
    public static final Integer DAILY_COND_STAT_FREQ = 0;


    // Business Rules

    public static final Integer MIN_HEART_RATE = 40;
    public static final Integer MAX_HEAR_RATE = 140;

    public static final Integer MIN_BLOOD_PRESSURE = 70;
    public static final Integer MAX_BLOOD_PRESSURE = 160;

    public static final Integer MIN_BODY_TEMP = 34;
    public static final Integer MAX_BODY_TEMP = 42;

    private volatile Integer logCount = PREPARED_CONDITIONS;
    private volatile long endTime;
    private volatile int finished = 0;
    private long start;


    public IOTSimulation(IOTOperations operations) {
        this.operations = operations;
    }

    public void define() throws SQLException {
        operations.removeAllData();
        Fairy fairy = Fairy.create(Locale.ENGLISH);

        long startTime = System.currentTimeMillis();

        for (int index = 0; index < PATIENTS; index ++) {
            Person person = fairy.person();
            operations.insertPatient(person.firstName(), person.lastName(), new Long(index));
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Klientai sugeneruoti, užtruko: " + (endTime-startTime));

        startTime = System.currentTimeMillis();

        for(int j = 0; j < PREPARED_CONDITIONS; j++) {
            insertRandomCondition(fairy, j);
        }

        endTime = System.currentTimeMillis();
        System.out.println("DB Paruošta, uztruko: " + (endTime-startTime));
    }


    public void run() throws SQLException {
//        System.out.println("Removing last run Data");
//        operations.removeOldData(PREPARED_CONDITIONS);
//        System.out.println("Data removed");

        Fairy fairy = Fairy.create(Locale.ENGLISH);


        start = System.currentTimeMillis();
        System.out.println("Starting Simulation on Threads: " + THREAD_COUNT + " ON: " + start);
        for (int i = 0; i < THREAD_COUNT; i++) {
            new Thread(new ThreadedSimulation(fairy, i)).start();
        }
    }

    public class ThreadedSimulation implements Runnable {

        private Fairy fairy;
        private Integer threadNum;

        public ThreadedSimulation(Fairy fairy, Integer threadNum) {
            this.fairy = fairy;
            this.threadNum = threadNum;
        }

        public void run() {
            int count = 0;
            for (int i = 0; i < WORK_ITERATIONS; i++) {

                for(int j = 0; j < INSERT_CONDITION_FREQ; j++) {
                    insertRandomCondition(fairy, 280000000 + WORK_ITERATIONS*INSERT_CONDITION_FREQ*threadNum + count++);
                }

                for(int j = 0; j < FIND_LATEST_FREQ; j++) {
                    operations.getLatestCondition(fairy.baseProducer().randomBetween(0, PATIENTS - 1));
                }

                for(int j = 0; j < INSERT_COND_STAT_FREQ; j++) {
                    insertRandomConditionStat(fairy, logCount);
                }

                for(int j = 0; j < FIND_LATEST_STAT_FREQ; j++) {
                    operations.getLatestConditionStats(fairy.baseProducer().randomBetween(0, PATIENTS - 1));
                }

                for(int j = 0; j < DAILY_COND_STAT_FREQ; j++) {
                    operations.getDailyConditionStats();
                }
            }

            endTime = System.currentTimeMillis();
            System.out.println("Gija : " + Thread.currentThread().getName() + " Baigė darbą: " + endTime);
            System.out.println("Užtruko: " + (endTime - start));
        }
    }

    private void insertRandomCondition(Fairy fairy, Integer conditionId) {
        Integer selectedPatient = fairy.baseProducer().randomBetween(0, PATIENTS - 1);
        Integer pressure = fairy.baseProducer().randomBetween(MIN_BLOOD_PRESSURE, MAX_BLOOD_PRESSURE);
        Integer rate = fairy.baseProducer().randomBetween(MIN_HEART_RATE, MAX_HEAR_RATE);
        Integer temperature = fairy.baseProducer().randomBetween(MIN_BODY_TEMP, MAX_BODY_TEMP);

        operations.insertRawCondition(selectedPatient, conditionId, pressure, rate, temperature);
    }

    private void insertRandomConditionStat(Fairy fairy, Integer conditionId) {
        Integer selectedPatient = fairy.baseProducer().randomBetween(0, PATIENTS - 1);
        Integer pressure = fairy.baseProducer().randomBetween(MIN_BLOOD_PRESSURE, MAX_BLOOD_PRESSURE);
        Integer rate = fairy.baseProducer().randomBetween(MIN_HEART_RATE, MAX_HEAR_RATE);
        Integer temperature = fairy.baseProducer().randomBetween(MIN_BODY_TEMP, MAX_BODY_TEMP);

        operations.insertConditionWithStats(selectedPatient, conditionId, pressure, rate, temperature);
    }
}

