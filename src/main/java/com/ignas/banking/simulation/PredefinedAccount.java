package com.ignas.banking.simulation;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.math.BigDecimal;

import static com.j256.ormlite.field.DataType.BIG_DECIMAL_NUMERIC;


@DatabaseTable(tableName = "predefined_account")
public class PredefinedAccount {

    @DatabaseField(columnName = "account_id", generatedIdSequence = "account_id_seq")
    private Long accountId;

    @DatabaseField(columnName = "account_number")
    private String accountNumber;

    @DatabaseField(dataType = BIG_DECIMAL_NUMERIC)
    private BigDecimal balance;

    @DatabaseField(columnName = "client_id")
    private Long clientId;

    public PredefinedAccount() {
        // For OrmLite
    }

    public PredefinedAccount(Long clientId, String accountNumber, BigDecimal balance) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.clientId = clientId;
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

    public Long getClientId() {
        return clientId;
    }

    @Override
    public String toString() {
        return "PredefinedAccount{" +
                "accountId=" + accountId +
                ", accountNumber='" + accountNumber + '\'' +
                ", balance=" + balance +
                ", clientId=" + clientId +
                '}';
    }
}
