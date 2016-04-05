package com.ignas.simulation;

import com.google.common.collect.ImmutableMap;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import org.jfairy.Fairy;
import org.jfairy.producer.payment.CreditCard;
import org.jfairy.producer.payment.IBAN;
import org.jfairy.producer.person.Person;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Locale;

/**
 * Created by ignas on 4/5/16.
 */
public class Simulation {

    ImmutableMap<PredefinedTask.TaskType, Integer> typeDeviation =
            ImmutableMap.<PredefinedTask.TaskType, Integer>builder()
                    .put(PredefinedTask.TaskType.ADD, 10)
                    .put(PredefinedTask.TaskType.WITHDRAW, 10)
                    .put(PredefinedTask.TaskType.TRANSFER, 80)
                    .build();

    public static Long CLIENTS = 1000L;
    public static Long ACCOUNTS = 1000L;

    public static void define() throws SQLException {
        Fairy fairy = Fairy.create(Locale.ENGLISH);
        ConnectionSource connectionSource = null;
        Dao<PredefinedClient, String> clientDao = null;

        connectionSource = new JdbcConnectionSource("jdbc:postgresql://localhost:5432/simulation_data", "postgres", "postgres");
        clientDao = DaoManager.createDao(connectionSource, PredefinedClient.class);

        for (int i = 0; i < CLIENTS; i ++) {
            Person person = fairy.person();
            PredefinedClient client = new PredefinedClient(person.firstName(), person.lastName(), person.nationalIdentificationNumber());
            System.out.println("Fake client: " + client.toString());

            clientDao.create(client);
        }

        connectionSource.close();
    }
}
