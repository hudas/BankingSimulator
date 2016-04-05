package com.ignas.simulation;

/**
 * Created by ignas on 4/5/16.
 */
public class PredefinedTask {
    public enum TaskType {
        ADD,
        WITHDRAW,
        TRANSFER;
    }

    private Long taskId;
    private TaskType type;
    private String debitAccountNumber;
    private String creditAccountNumber;

    public PredefinedTask(Long taskId, TaskType type, String debitAccountNumber, String creditAccountNumber) {
        this.taskId = taskId;
        this.type = type;
        this.debitAccountNumber = debitAccountNumber;
        this.creditAccountNumber = creditAccountNumber;
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
}
