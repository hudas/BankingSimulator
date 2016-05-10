package com.ignas.iot;

import com.ignas.iot.configuration.SimulationConfig;
import com.ignas.iot.operations.Operations;
import com.ignas.iot.operations.provider.OperationsFactory;
import org.jfairy.Fairy;
import org.jfairy.producer.person.Person;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by ignas on 4/5/16.
 */
public class Simulation {


    private OperationsFactory connectionProvider;

    // Simulation Definition
    public static Integer PATIENTS = 1000;
    public static Integer PREPARED_CONDITIONS = 0;

    private SimulationConfig config;

    // Business Rules+



    private volatile Integer logCount = PREPARED_CONDITIONS;
    private volatile long endTime;
    private long start;


    public Simulation(OperationsFactory operations, SimulationConfig config) {
        this.connectionProvider = operations;
        this.config = config;
    }

    public void define() throws SQLException {
        //operations.removeAllData();
        OperationService service = new OperationService(connectionProvider.getOperations(), PATIENTS);
        long startTime = System.currentTimeMillis();

        for (int index = 0; index < PATIENTS; index ++) {
            service.insertPatient(new Long(index));
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Klientai sugeneruoti, užtruko: " + (endTime-startTime));

        startTime = System.currentTimeMillis();

        for(int j = 0; j < PREPARED_CONDITIONS; j++) {
            service.insertRawCondition();
        }

        endTime = System.currentTimeMillis();
        System.out.println("DB Paruošta, uztruko: " + (endTime-startTime));
    }


    public void run() throws SQLException {
        prepareForRun();

        start = System.currentTimeMillis();
        System.out.println("Starting Simulation on Threads: " + config.getThreadCount() + " ON: " + start);
        for (int i = 0; i < config.getThreadCount(); i++) {
            new Thread(new ThreadedSimulation(i, new OperationService(connectionProvider.getOperations(), PATIENTS))).start();
        }
    }

    private void prepareForRun() {
        Long start = System.currentTimeMillis();
        Operations ops = connectionProvider.getOperations();

        System.out.println("Removing last run Data");
        ops.removeOldData(PREPARED_CONDITIONS);
        Long endTime = System.currentTimeMillis();
        System.out.println("Data removed, took: " + (endTime - start));
    }

    public class ThreadedSimulation implements Runnable {

        private OperationService service;
        private Integer threadNum;

        public ThreadedSimulation(Integer threadNum, OperationService service) {
            this.threadNum = threadNum;
            this.service = service;
        }

        public void run() {
            for (int i = 0; i < config.getWorkIterations(); i++) {

                for(int j = 0; j < config.getInsertCondFreq(); j++) {
                    service.insertRawCondition();
                }

                for(int j = 0; j < config.getFindLatestFreq(); j++) {
                    service.getLatestCondition();
                }

                for(int j = 0; j < config.getFindLatestFromViewFreq(); j++) {
                    service.getLatestViewCondition();
                }

                for(int j = 0; j < config.getInsertCondWithStatsFreq(); j++) {
                    service.insertConditionWithStats();
                }

                for(int j = 0; j < config.getFindLatestStatFreq(); j++) {
                    service.getLatestConditionStats();
                }

                for(int j = 0; j < config.getDailyCondStatsFreq(); j++) {
                    service.getDailyConditionStats();
                }
            }

            endTime = System.currentTimeMillis();
            System.out.println("Gija : " + Thread.currentThread().getName() + " Baigė darbą: " + endTime);
            System.out.println("Užtruko: " + (endTime - start));
        }
    }
}

