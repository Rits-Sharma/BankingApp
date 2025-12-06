package com.rits.BankingApp.dao;

import com.rits.BankingApp.Account;
import com.rits.BankingApp.exceptions.AccountNotFoundException;
import com.rits.BankingApp.exceptions.BankingException;
import com.rits.BankingApp.exceptions.InsufficientFundsException;
import com.rits.BankingApp.exceptions.UnauthorizedOperationException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AccountDao {
    public Account getUser(int acc_no, String password) throws UnauthorizedOperationException, SQLException {
        Connection conn = ConnectionProvider.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(
                String.format("select * from users where acc_no = %d and password ='%s';", acc_no, password));
        if (rs.next()) {
            String name = rs.getString("name");
            String pwd = rs.getString("password");
            int balance = rs.getInt("balance");
            return new Account(acc_no, name, pwd, balance);
        } else {
            throw new UnauthorizedOperationException("Invalid credentials");
        }
    }

    public int addUser(Account user) throws SQLException {
        Connection conn = ConnectionProvider.getConnection();
        String query = "insert into users (name, password, balance) values (?, ?, ?);";
        PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        pstmt.setString(1, user.getName());
        pstmt.setString(2, user.getPassword());
        pstmt.setInt(3, user.getBalance());
        int rows = pstmt.executeUpdate();
        if (rows == 0)
            throw new SQLException("Creating user failed, no rows affected.");
        ResultSet generatedKeys = pstmt.getGeneratedKeys();
        if (generatedKeys.next()) {
            return generatedKeys.getInt(1);
        }
        return 0;
    }

    public int withdraw(int acc_no, String password, int amount)
            throws SQLException, UnauthorizedOperationException, InsufficientFundsException {
        Connection conn = ConnectionProvider.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(
                String.format("select * from users where acc_no = %d and password ='%s';", acc_no, password));
        if (rs.next()) {
            int balance = rs.getInt("balance");
            if (balance < amount) {
                throw new InsufficientFundsException(balance, amount);
            }
            String updateQuery = String.format(
                    "update users set balance = balance - %d where acc_no = %d and password = '%s';", amount, acc_no, password);
            stmt.executeUpdate(updateQuery);
            return balance - amount;
        } else {
            throw new UnauthorizedOperationException("Invalid credentials");
        }
    }

    public void deposit(int acc_no, int amount) throws SQLException, AccountNotFoundException {
        Connection conn = ConnectionProvider.getConnection();
        Statement stmt = conn.createStatement();
        int rows = stmt.executeUpdate(
                String.format("update users set balance = balance + %d where acc_no = %d;", amount, acc_no));
        if (rows == 0) {
            throw new AccountNotFoundException(acc_no);
        }
    }

    public void transfer(int from, int to, String password, int amount) throws UnauthorizedOperationException, InsufficientFundsException, AccountNotFoundException, BankingException, SQLException {
        Connection conn = ConnectionProvider.getConnection();
        Statement stmt = conn.createStatement();
        var rs1 = stmt.executeQuery(
            String.format("Select balance from users where acc_no=%d and password='%s';", from, password));
        if (!rs1.next()) {
            throw new UnauthorizedOperationException("Invalid credentials");
        }
        int bal = rs1.getInt("balance");
        if (bal < amount) {
            throw new InsufficientFundsException(bal, amount);
        }
        var rs2 = stmt.executeQuery("Select acc_no from users where acc_no=" + to + ";");
        if (!rs2.next()) {
            throw new AccountNotFoundException(to);
        }
        String withdrawQuery = String.format(
            "update users set balance = balance - %d where acc_no = %d and password = '%s';", amount, from,
            password);
        int rows = stmt.executeUpdate(withdrawQuery);
        if (rows == 0) {
            throw new BankingException("Something went wrong during withdrawal");
        }
        String depositQuery = String.format(
            "update users set balance = balance + %d where acc_no = %d;", amount, to);
            stmt.executeUpdate(depositQuery);
            addTransactionLog(from, to, amount);
    }

    private void addTransactionLog(int from, int to, int amount) {
        Connection conn = ConnectionProvider.getConnection();
        String query = String.format(
            "Insert into transactions (from_acc_no, to_acc_no, amount, timestamp) values (%d, %d, %d, '%d');", from, to, amount, System.currentTimeMillis());
             
        try {
            Statement stmt = conn.createStatement();
            stmt.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
