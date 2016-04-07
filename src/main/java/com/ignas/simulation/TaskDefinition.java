package com.ignas.simulation;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Range;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import org.jfairy.Fairy;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ignas on 4/6/16.
 */
public class TaskDefinition implements Runnable {

    private static final ImmutableMap<PredefinedTask.TaskType, Integer> typeDeviation =
            ImmutableMap.<PredefinedTask.TaskType, Integer>builder()
                    .put(PredefinedTask.TaskType.ADD, 10)
                    .put(PredefinedTask.TaskType.WITHDRAW, 10)
                    .put(PredefinedTask.TaskType.TRANSFER, 80)
                    .build();

    public static final Integer TASKS = 100000;


    ConnectionSource connectionSource;
    List<PredefinedClient> clientList = new ArrayList();
    List<PredefinedAccount> accountList = new ArrayList();
    private Map<PredefinedTask.TaskType, Range<Integer>> numbericRanges = new HashMap<PredefinedTask.TaskType, Range<Integer>>();
    Fairy fairy;

    public TaskDefinition(List<PredefinedClient> clientList, List<PredefinedAccount> accountList,
                          ConnectionSource connectionSource, Fairy fairy) {
        this.clientList = clientList;
        this.accountList = accountList;
        this.connectionSource = connectionSource;
        this.fairy = fairy;
    }

    public void run() {
        Dao<PredefinedTask, String> taskDao = null;
        try {
            taskDao = DaoManager.createDao(connectionSource, PredefinedTask.class);
        } catch (SQLException e) {
            System.out.println("Fucked up");
        }

        long startTime = System.currentTimeMillis();

        numbericRanges = new HashMap<PredefinedTask.TaskType, Range<Integer>>();
        Integer previousUpperBound = 0;
        for (PredefinedTask.TaskType type : typeDeviation.keySet()) {
            numbericRanges.put(type, Range.<Integer>closedOpen(previousUpperBound, previousUpperBound + typeDeviation.get(type)));
            previousUpperBound +=  typeDeviation.get(type);
        }


        for (int i = 0; i < TASKS; i++) {
            PredefinedTask.TaskType type = selectTaskType(fairy);

            String debitFrom = accountList.get(fairy.baseProducer().randomBetween(0, 1000 - 1)).getAccountNumber();
            String creditTo = accountList.get(fairy.baseProducer().randomBetween(0, 1000 - 1)).getAccountNumber();
            BigDecimal amount = BigDecimal.valueOf(fairy.baseProducer().randomBetween(0, 100));
            PredefinedTask task = new PredefinedTask(type, debitFrom, creditTo, amount);
            try {
                taskDao.create(task);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Uzduotys sugeneruotos, uztruko: " + (endTime-startTime));

    }

    private PredefinedTask.TaskType selectTaskType(Fairy fairy) {
        Integer typeSelector = fairy.baseProducer().randomBetween(0, 99);
        for(PredefinedTask.TaskType type : numbericRanges.keySet()) {
            if (numbericRanges.get(type).contains(typeSelector)) {
                return type;
            };
        }

        throw new IllegalArgumentException("Generated value not between [0; 100] " + typeSelector);
    }
}
