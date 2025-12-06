package com.rits.BankingApp.exceptions;

public class UnauthorizedOperationException extends BankingException {
    public UnauthorizedOperationException(String operation) {
        super("Unauthorized operation: " + operation);
    }
}