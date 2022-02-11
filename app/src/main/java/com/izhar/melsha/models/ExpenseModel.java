package com.izhar.melsha.models;

import java.io.Serializable;

public class ExpenseModel implements Serializable {
    String reason;
    int amount;

    public ExpenseModel(String reason, int amount) {
        this.reason = reason;
        this.amount = amount;
    }

    public ExpenseModel() {
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
