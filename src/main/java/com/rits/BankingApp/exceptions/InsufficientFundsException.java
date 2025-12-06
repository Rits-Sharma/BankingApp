package com.rits.BankingApp.exceptions;

public class InsufficientFundsException extends BankingException {
    private final int balance;
    private final int amount;

    public InsufficientFundsException(int balance, int amount) {
        super("Insufficient funds. Balance: " + balance + ", Withdrawal attempt: " + amount);
        this.balance = balance;
        this.amount = amount;
    }
    
    public int getBalance() {
        return balance;
    }
    public int getAmount() {
        return amount;
    }
}