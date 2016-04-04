package com.ignas.banking;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Created by ignas on 4/3/16.
 */
public class OperationsFactory {

    public static BankingOperations postgres() throws DataAccessException {
        Connection connection;

        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/testdb", "postgres", "postgres");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            throw new DataAccessException();
        }

        return new JDBCBanking(connection);
    }

    public static BankingOperations volt() throws DataAccessException {
        Connection connection;

        try {
            Class.forName("org.voltdb.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:voltdb://localhost:7002");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            throw new DataAccessException();
        }

        return new JDBCBanking(connection);
    }

    public static BankingOperations mongo() throws DataAccessException {
        return new MongoBanking();
    }

}
