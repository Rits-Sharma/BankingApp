package com.rits.BankingApp.dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class DBInfo {
    static Properties properties = new Properties();
    static{
        try {
        properties.load(new FileInputStream("credential.properties"));
        } catch (IOException e) {
        e.printStackTrace();
        }
    }
    public static String url = properties.getProperty("DATABASE_URL");
    public static String user = properties.getProperty("DATABASE_USERNAME");
    public static String password = properties.getProperty("DATABASE_PASSWORD");
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
