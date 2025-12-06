package com.rits.BankingApp;

public class Account {
    private int account_no;
    private String name;
    private String password;
    private int balance;

    public Account(int account_no, String name, String password, int balance) {
        this.account_no = account_no;
        this.name = name;
        this.password = password;
        this.balance = balance;
    }

    public Account(String name, String password, int balance) {
        this.name = name;
        this.password = password;
        this.balance = balance;
    }

    public int getAccount_no() {
        return account_no;
    }

    public void setAccount_no(int account_no) {
        this.account_no = account_no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "User [account_no=" + account_no + ", name=" + name + ", balance=" + balance
                + "]";
    }
}
