package com.ignas.simulation;

import java.math.BigDecimal;

/**
 * Created by ignas on 4/5/16.
 */
public class PredefinedAccount {

    private Long accountId;
    private String accountNumber;
    private BigDecimal balance;

    public PredefinedAccount(Long accountId, String accountNumber, BigDecimal balance) {
        this.accountId = accountId;
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    public Long getAccountId() {
        return accountId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }
}
