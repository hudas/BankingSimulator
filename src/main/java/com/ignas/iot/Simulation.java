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
    private SimulationConfig config;

    private volatile long endTime;
    private long start;
    private Integer threadNum;


    public Simulation(OperationsFactory operations, SimulationConfig config, Integer threadNum) {
        this.connectionProvider = operations;
        this.config = config;
        this.threadNum = threadNum;
    }

    public void define() throws SQLException {
        OperationService service = new OperationService(connectionProvider.getOperations(), config.getPatientCount(), new Long(config.getPredefinedConditions()));
        long startTime = System.currentTimeMillis();

        for (int index = 0; index < config.getPatientCount(); index ++) {
            service.insertPatient(new Long(index));
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Klientai sugeneruoti, užtruko: " + (endTime-startTime));

        startTime = System.currentTimeMillis();

        for(int j = 0; j < config.getPredefinedConditions(); j++) {
            service.insertRawCondition(j);
        }

        endTime = System.currentTimeMillis();
        System.out.println("DB Paruošta, uztruko: " + (endTime-startTime));
    }


    public void run() throws SQLException {
        //prepareForRun();

        start = System.currentTimeMillis();
        System.out.println("Starting NEW Simulation on Threads: " + config.getThreadCount() + " ON: " + start);
        System.out.println("Iterations: " + config.getWorkIterations() + " ON: " + start);
        for (int i = 0; i < config.getThreadCount(); i++) {
            new Thread(
                    new ThreadedSimulation(
                            i,
                            new OperationService(
                                    connectionProvider.getOperations(),
                                    config.getPatientCount(),
                                    new Long(config.getPredefinedConditions())
                            ),
                            config.getIntervalMilis()
                    )
            ).start();
        }
    }

    private void prepareForRun() {
        Long start = System.currentTimeMillis();
        Operations ops = connectionProvider.getOperations();

        System.out.println("Removing last run Data");
        ops.removeOldData(config.getPredefinedConditions());
        Long endTime = System.currentTimeMillis();
        System.out.println("Data removed, took: " + (endTime - start));
    }

    public class ThreadedSimulation implements Runnable {

        private OperationService service;
        private Integer threadNum;
        private Long simulationTime;

        public ThreadedSimulation(Integer threadNum, OperationService service, Long simulationTime) {
            this.threadNum = threadNum;
            this.service = service;
            this.simulationTime = simulationTime;
        }

        public void run() {
            Long count = 0L;

            for (int i = 0; i < config.getWorkIterations(); i++) {

                for(int j = 0; j < config.getInsertCondFreq(); j++) {
                    service.insertRawCondition(config.getWorkIterations() * config.getInsertCondFreq() * threadNum + count++);
                }

                for(int j = 0; j < config.getFindLatestFreq(); j++) {
                    service.getLatestCondition();
                }

                for(int j = 0; j < config.getFindLatestFromViewFreq(); j++) {
                    service.getLatestViewCondition();
                }

                for(int j = 0; j < config.getInsertCondWithStatsFreq(); j++) {
                    service.insertConditionWithStats(config.getWorkIterations() * config.getInsertCondWithStatsFreq() * threadNum + count++);
                }

                for(int j = 0; j < config.getFindLatestStatFreq(); j++) {
                    service.getLatestConditionStats();
                }

                for(int j = 0; j < config.getDailyCondStatsFreq(); j++) {
                    service.getDailyConditionStats();
                }

                long currentTime = System.currentTimeMillis();
                if (simulationTime > 0 && currentTime - start > simulationTime) {
                    System.out.println("Breaking time!");
                    break;
                }
            }

            endTime = System.currentTimeMillis();
            System.out.println("Gija : " + Thread.currentThread().getName() + " Baigė darbą: " + endTime);
            System.out.println("Užtruko: " + (endTime - start));
        }
    }
}

