package com.rits.BankingApp.dao;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionProvider {
    private static Connection connection;

    private ConnectionProvider() {}

    public static Connection getConnection() {
        if (connection==null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(
                        DBInfo.url,
                        DBInfo.user,
                        DBInfo.password
                );
                return connection;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return connection;
        }

    }
}
