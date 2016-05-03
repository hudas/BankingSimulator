package com.ignas.banking.simulation;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Range;
import org.jfairy.Fairy;
import org.postgresql.copy.CopyIn;
import org.postgresql.copy.CopyManager;
import org.postgresql.core.BaseConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
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
    public static final Integer BACHES = 1000;
    public static final Integer BACH_SIZE = 10000;
    public static final Integer CLIENTS = 1000;
    public static final Integer ACCOUNTS = 5000;


    List<PredefinedClient> clientList = new ArrayList();
    List<PredefinedAccount> accountList = new ArrayList();

    private Map<PredefinedTask.TaskType, Range<Integer>> numbericRanges = new HashMap<PredefinedTask.TaskType, Range<Integer>>();
    Fairy fairy;

    public TaskDefinition(List<PredefinedClient> clientList, List<PredefinedAccount> accountList, Fairy fairy) {
        this.clientList = clientList;
        this.accountList = accountList;
        this.fairy = fairy;
    }

    public void run() {
        numbericRanges = new HashMap<PredefinedTask.TaskType, Range<Integer>>();
        Integer previousUpperBound = 0;
        for (PredefinedTask.TaskType type : typeDeviation.keySet()) {
            numbericRanges.put(type, Range.<Integer>closedOpen(previousUpperBound, previousUpperBound + typeDeviation.get(type)));
            previousUpperBound +=  typeDeviation.get(type);
        }

        Connection connection = null;
        System.out.println("Starting Thread:");
        long startTime = System.currentTimeMillis();

        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/simulation_data", "postgres", "postgres");
            connection.setAutoCommit(false);

            for (int j = 0; j < BACHES; j++) {
                Statement stmt = connection.createStatement();
                StringBuilder builder = new StringBuilder();
                CopyManager cm = new CopyManager((BaseConnection) connection);
                CopyIn cpIN = cm.copyIn("COPY predefined_task_2(debit_from, credit_to, amount, type) FROM STDIN WITH DELIMITER '|'");

                for (int i = 0; i < BACH_SIZE; i++) {
                    PredefinedTask.TaskType type = selectTaskType(fairy);

                    String debitFrom = accountList.get(fairy.baseProducer().randomBetween(0, ACCOUNTS - 1)).getAccountNumber();
                    String creditTo = accountList.get(fairy.baseProducer().randomBetween(0, ACCOUNTS - 1)).getAccountNumber();
                    BigDecimal amount = BigDecimal.valueOf(fairy.baseProducer().randomBetween(0, 100));



                    builder.append(debitFrom);
                    builder.append("|");
                    builder.append(creditTo);
                    builder.append("|");
                    builder.append(amount);
                    builder.append("|\n");





//                    String SQL = "INSERT INTO predefined_task_2(debit_from, credit_to, amount, type)" +
//                            " VALUES ('" + debitFrom + "', '" + creditTo + "', " + amount + ", '" + type.name() + "')";
//                    // Create statement object
//
//                    stmt.addBatch(SQL);
                }

                cpIN.writeToCopy(builder.toString().getBytes(), 0, builder.length());
                cpIN.endCopy();


//                stmt.executeBatch();
                connection.commit();
            }


        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Uzduotys sugeneruotos, gija uztruko: " + (endTime-startTime));

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
