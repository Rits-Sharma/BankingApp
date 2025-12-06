package com.rits.BankingApp.dao;

public class DBInfo {
    public static String url = "jdbc:mysql://localhost:3306/bankapp";
    public static String user = "root";
    public static String password = "RitSql@629";

    private DBInfo() {
    }

    public static void setData(String url_, String username_, String password_) {
        if (url == null && user == null && password == null) {
            url = url_;
            user = username_;
            password = password_;
        }
    }
}
