package com.ignas.banking;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

/**
 * Created by ignas on 4/3/16.
 */
class MongoBanking implements BankingOperations {


    public MongoBanking() {
        MongoClient mongoClient = new MongoClient();
        MongoDatabase db = mongoClient.getDatabase("test");
    }

    public void addFunds() {

    }

    public void withdrawFunds() {

    }

    public void transferFunds() {

    }

    public void checkBalance() {

    }

    public void getTransactionsForPeriod() {

    }

    public void getTransactionsToBank() {

    }

    public void getTopExpenses() {

    }
}
