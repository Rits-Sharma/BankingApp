package com.rits.BankingApp.exceptions;

public class InvalidAccountStateException extends BankingException {
    public InvalidAccountStateException(String message) {
        super(message);
    }
}