package com.ignas.banking;

import java.math.BigDecimal;

/**
 * Basic interface which specifies all posible banking operations
 */
public interface BankingOperations {

    // OLTP Operations
    void addFunds(String accountNumber, BigDecimal amount);
    void withdrawFunds(String accountNumber, BigDecimal amount);
    void transferFunds(String debitAccountNumber, String creditAccountNumber, BigDecimal amount);
    void checkBalance(String accountNumber);


    // OLAP Operations
    void getTransactionsForPeriod();


    /**
     *
     *
     */
    void getTransactionsToBank();

    /**
     * Method that retrieves Top money receivers
     *
     */
    void getTopExpenses();
}
