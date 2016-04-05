package com.ignas.banking;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

import java.math.BigDecimal;

/**
 * Created by ignas on 4/3/16.
 */
class MongoBanking implements BankingOperations {


    public MongoBanking() {
        MongoClient mongoClient = new MongoClient();
        MongoDatabase db = mongoClient.getDatabase("test");
    }

    public void addFunds(String accountNumber, BigDecimal amount) {

    }

    public void withdrawFunds(String accountNumber, BigDecimal amount) {

    }

    public void transferFunds(String debitAccountNumber, String creditAccountNumber, BigDecimal amount) {

    }

    public void checkBalance(String accountNumber) {

    }

    public void getTransactionsForPeriod() {

    }

    public void getTransactionsToBank() {

    }

    public void getTopExpenses() {

    }
}
