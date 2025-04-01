package org.example.model;

public class VacationPayResponse {
    private final double amount;

    public VacationPayResponse(double amount) {
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }
}