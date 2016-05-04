package com.ignas.iot;

import java.sql.Connection;
import java.sql.DriverManager;


public class OperationsFactory {

    public static IOTOperations postgres() {
        Connection connection;

        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/simulation_data", "postgres", "postgres");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
           return null;
        }

        return new PostgresIOT(connection);
    }

    public static IOTOperations volt() {
        Connection connection;

        try {
            Class.forName("org.voltdb.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:voltdb://localhost:7002");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            return null;
        }

        return new VoltIOT(connection);
    }

    public static IOTOperations mongo() {
        return new MongoIOT();
    }

}
