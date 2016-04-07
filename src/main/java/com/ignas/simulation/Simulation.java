package com.ignas.simulation;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Range;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.support.ConnectionSource;
import org.jfairy.Fairy;
import org.jfairy.producer.payment.IBAN;
import org.jfairy.producer.payment.IBANProvider;
import org.jfairy.producer.person.Person;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.Callable;

/**
 * Created by ignas on 4/5/16.
 */
public class Simulation {

    private static final ImmutableMap<PredefinedTask.TaskType, Integer> typeDeviation =
            ImmutableMap.<PredefinedTask.TaskType, Integer>builder()
                    .put(PredefinedTask.TaskType.ADD, 10)
                    .put(PredefinedTask.TaskType.WITHDRAW, 10)
                    .put(PredefinedTask.TaskType.TRANSFER, 80)
                    .build();

    private static Map<PredefinedTask.TaskType, Range<Integer>> numbericRanges = new HashMap<PredefinedTask.TaskType, Range<Integer>>();


    public static final Integer CLIENTS = 1000;
    public static final Integer ACCOUNTS = 5000;
    public static final Integer TASKS = 1000000;
    public static final Integer BACH_SIZE = 200000;

    public static final Integer MIN_BALANCE = 15;
    public static final Integer MAX_BALANCE = 100;

    public static void define() throws SQLException {
        Fairy fairy = Fairy.create(Locale.ENGLISH);
        ConnectionSource connectionSource = null;

        connectionSource = new JdbcConnectionSource("jdbc:postgresql://localhost:5432/simulation_data", "postgres", "postgres");

        Dao<PredefinedClient, String> clientDao = DaoManager.createDao(connectionSource, PredefinedClient.class);
        Dao<PredefinedAccount, String> accountDao = DaoManager.createDao(connectionSource, PredefinedAccount.class);
        final Dao<PredefinedTask, String> taskDao = DaoManager.createDao(connectionSource, PredefinedTask.class);

        List<PredefinedClient> clientList = new ArrayList();

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < CLIENTS; i ++) {
            Person person = fairy.person();
            PredefinedClient client = new PredefinedClient(person.firstName(), person.lastName(), person.nationalIdentificationNumber());
            clientDao.create(client);
            clientList.add(client);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Klientai sugeneruoti, uztruko: " + (endTime-startTime));

        startTime = System.currentTimeMillis();

        List<PredefinedAccount> accountList = new ArrayList<PredefinedAccount>();
        for (int i = 0; i < ACCOUNTS; i++) {
            IBAN iban = new IBANProvider(fairy.baseProducer()).get();
            Integer selectedClientIndex = fairy.baseProducer().randomBetween(0, CLIENTS - 1);

            PredefinedAccount account =
                    new PredefinedAccount(clientList.get(selectedClientIndex).getClientId(), iban.getIbanNumber(),
                            new BigDecimal(fairy.baseProducer().randomBetween(MIN_BALANCE, MAX_BALANCE)));

            accountDao.create(account);
            accountList.add(account);
        }

        endTime = System.currentTimeMillis();
        System.out.println("Saskaitos sugeneruotos, uztruko: " + (endTime-startTime));

        startTime = System.currentTimeMillis();

        numbericRanges = new HashMap<PredefinedTask.TaskType, Range<Integer>>();
        Integer previousUpperBound = 0;
        for (PredefinedTask.TaskType type : typeDeviation.keySet()) {
            numbericRanges.put(type, Range.<Integer>closedOpen(previousUpperBound, previousUpperBound + typeDeviation.get(type)));
            previousUpperBound +=  typeDeviation.get(type);
        }


        for (int j = 0; j < 10; j++ ) {
            final List<PredefinedTask> tasks = new ArrayList<PredefinedTask>();
            for (int i = 0; i < BACH_SIZE; i++) {
                PredefinedTask.TaskType type = selectTaskType(fairy);

                String debitFrom = accountList.get(fairy.baseProducer().randomBetween(0, ACCOUNTS - 1)).getAccountNumber();
                String creditTo = accountList.get(fairy.baseProducer().randomBetween(0, ACCOUNTS - 1)).getAccountNumber();
                BigDecimal amount = BigDecimal.valueOf(fairy.baseProducer().randomBetween(0, MAX_BALANCE));
                PredefinedTask task = new PredefinedTask(type, debitFrom, creditTo, amount);
                tasks.add(task);
            }
            TransactionManager.callInTransaction(connectionSource,
                    new Callable<Void>() {
                        public Void call() throws Exception {
                            for (PredefinedTask task : tasks) {
                                taskDao.create(task);
                            }
                            return null;
                        }
                    });
        }


        endTime = System.currentTimeMillis();
        System.out.println("Uzduotys sugeneruotos, uztruko: " + (endTime-startTime));

        connectionSource.close();
    }

    private static PredefinedTask.TaskType selectTaskType(Fairy fairy) {
        Integer typeSelector = fairy.baseProducer().randomBetween(0, 99);
        for(PredefinedTask.TaskType type : numbericRanges.keySet()) {
            if (numbericRanges.get(type).contains(typeSelector)) {
                return type;
            };
        }

        throw new IllegalArgumentException("Generated value not between [0; 100] " + typeSelector);
    }
}

