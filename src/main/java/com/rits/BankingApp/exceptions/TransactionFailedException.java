package com.rits.BankingApp.exceptions;

public class TransactionFailedException extends BankingException {
    public TransactionFailedException(String message) {
        super(message);
    }
}