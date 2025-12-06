package com.rits.BankingApp.exceptions;

public class AccountNotFoundException extends BankingException {
    public AccountNotFoundException(int accountId) {
        super("Account not found: " + accountId);
    }
}