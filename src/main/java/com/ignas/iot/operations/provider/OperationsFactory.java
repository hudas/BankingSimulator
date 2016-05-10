package com.ignas.iot.operations.provider;

import com.ignas.iot.operations.impl.Volt;
import com.ignas.iot.operations.impl.Mongo;
import com.ignas.iot.operations.Operations;
import com.ignas.iot.operations.impl.Postgres;

import java.sql.Connection;
import java.sql.DriverManager;


public class OperationsFactory {

    private ProvidesOperation provider;

    private OperationsFactory(ProvidesOperation opsProvider) {
        provider = opsProvider;
    }

    public static OperationsFactory postgres() {
        return new OperationsFactory(() -> {
            Connection connection;

            try {
                Class.forName("org.postgresql.Driver");
                connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/simulation_data", "postgres", "postgres");
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println(e.getClass().getName()+": "+e.getMessage());
                return null;
            }

            return new Postgres(connection);
        });
    }

    public static OperationsFactory volt() {
        return new OperationsFactory(() -> {
            Connection connection;

            try {
                Class.forName("org.voltdb.jdbc.Driver");
                connection = DriverManager.getConnection("jdbc:voltdb://localhost:7002");
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println(e.getClass().getName()+": "+e.getMessage());
                return null;
            }

            return new Volt(connection);
        });
    }

    public static OperationsFactory mongo() {
        return new OperationsFactory(() -> new Mongo());
    }


    public Operations getOperations() {
        return provider.getOperations();
    }
}
