package com.ignas.simulation;


import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.math.BigDecimal;

@DatabaseTable(tableName = "predefined_task")
public class PredefinedTask {

    public enum TaskType {
        ADD,
        WITHDRAW,
        TRANSFER;
    }

    @DatabaseField(columnName = "task_id", generatedIdSequence = "task_id_seq")
    private Long taskId;

    @DatabaseField(dataType = DataType.ENUM_STRING)
    private TaskType type;

    @DatabaseField(columnName = "debit_from")
    private String debitAccountNumber;

    @DatabaseField(columnName = "credit_to")
    private String creditAccountNumber;

    @DatabaseField(dataType = DataType.BIG_DECIMAL_NUMERIC)
    private BigDecimal amount;

    public PredefinedTask() {
        // For OrmLite
    }

    public PredefinedTask(TaskType type, String debitAccountNumber, String creditAccountNumber, BigDecimal amount) {
        this.type = type;
        this.debitAccountNumber = debitAccountNumber;
        this.creditAccountNumber = creditAccountNumber;
        this.amount = amount;
    }

    public TaskType getType() {
        return type;
    }

    public String getDebitAccountNumber() {
        return debitAccountNumber;
    }

    public String getCreditAccountNumber() {
        return creditAccountNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
