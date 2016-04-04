package com.ignas.banking;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by ignas on 4/3/16.
 */
public class JDBCBanking implements BankingOperations {

    private Connection connection;

    public JDBCBanking(Connection connection) {
        this.connection = connection;
    }

    public void addFunds(String accountNumber, BigDecimal amount) {
        CallableStatement callable = null;
        try {
            callable = connection.prepareCall("call add_funds(?, ?)");
            callable.setString(1, accountNumber);
            callable.setBigDecimal(2, amount);
            callable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void withdrawFunds(String accountNumber, BigDecimal amount) {
        CallableStatement callable = null;
        try {
            callable = connection.prepareCall("call withdraw_funds(?, ?)");
            callable.setString(1, accountNumber);
            callable.setBigDecimal(2, amount);
            callable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void transferFunds(String debitAccountNumber, String creditAccountNumber, BigDecimal amount) {
        CallableStatement callable = null;
        try {
            callable = connection.prepareCall("call transfer_funds(?, ?, ?)");
            callable.setString(1, debitAccountNumber);
            callable.setString(2, creditAccountNumber);
            callable.setBigDecimal(3, amount);
            callable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void checkBalance(String accountNumber) {
        CallableStatement callable = null;
        try {
            callable = connection.prepareCall("call check_balance(?, ?, ?)");
            callable.setString(1, accountNumber);
            ResultSet resultSet = callable.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void getTransactionsForPeriod() {

    }

    public void getTransactionsToBank() {

    }

    public void getTopExpenses() {

    }
}
