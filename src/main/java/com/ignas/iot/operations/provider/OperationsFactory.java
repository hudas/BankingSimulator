package com.ignas.iot.operations.provider;

import com.ignas.iot.operations.impl.Volt;
import com.ignas.iot.operations.impl.Mongo;
import com.ignas.iot.operations.Operations;
import com.ignas.iot.operations.impl.Postgres;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.*;



public class OperationsFactory {

    private ProvidesOperation provider;
    private static Operations voltConnection;
    private static Operations voltConnection1;
    private static Operations voltConnection2;
    private static volatile int issuedConnections;


    private OperationsFactory(ProvidesOperation opsProvider) {
        provider = opsProvider;
    }

    public static OperationsFactory postgres(String host) {
        return new OperationsFactory(() -> {
            Connection connection;

            try {
                Class.forName("org.postgresql.Driver");
                connection = DriverManager.getConnection("jdbc:postgresql://" + host + ":5432/simulation_data", "postgres", "postgres");
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println(e.getClass().getName()+": "+e.getMessage());
                return null;
            }

            return new Postgres(connection);
        });
    }

    public static OperationsFactory volt(String host) {
        Connection connection;

        return new OperationsFactory(() -> {
            Random random = new Random();
            random.setSeed(System.currentTimeMillis());

            Integer server = random.nextInt();
            if (server % 3 == 0) {
                return getColtCon(host, "7002");
            } else if (server % 3 == 1) {
                return getColtCon(host, "8002");
            } else {
                return getColtCon(host, "9002");
            }
        });
    }

    private static Operations getColtCon(String host, String port) {
        Connection connection;
        try {
            Class.forName("org.voltdb.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:voltdb://" + host + ":" + port);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            return null;
        }


        return new Volt(connection);
    }

    public static OperationsFactory mongo(String host) {
        return new OperationsFactory(() -> new Mongo());
    }


    public Operations getOperations() {
        return provider.getOperations();
    }
}
