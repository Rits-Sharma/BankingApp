package com.rits.BankingApp;

import java.sql.SQLException;
import java.util.Scanner;

import com.rits.BankingApp.dao.AccountDao;
import com.rits.BankingApp.exceptions.AccountNotFoundException;
import com.rits.BankingApp.exceptions.BankingException;
import com.rits.BankingApp.exceptions.InsufficientFundsException;
import com.rits.BankingApp.exceptions.UnauthorizedOperationException;

public class BankingService {

    public Account loggedInAccount = null;
    private AccountDao accountDao;
    private static BankingService instance = null;
    Scanner scanner;

    private BankingService() {
        accountDao = new AccountDao();
        scanner = new Scanner(System.in);
    }

    public static BankingService getInstance() {
        if (instance == null) {
            instance = new BankingService();
        }
        return instance;
    }

    public void menu() {
        int choice;
        do {
            showMenu();
            choice = scanner.nextInt();
            scanner.nextLine();
            if (choice == 1) {
                viewBalance();
            }
            else if (choice == 2) {
                deposit();
            }
            else if (choice == 3) {
                withdraw();
            }
            else if (choice == 4) {
                transfer();
            }
            else if (choice == 5) {
                logout();
                BankingAppApplication.takeInitialInput();
                menu();
            } else {
                System.out.println("Invalid Choice!");
            }
        } while (true);
    }

    private void showMenu() {
        System.out.println("\n--- Banking Menu ---");
        System.out.println("1. View Balance");
        System.out.println("2. Deposit Amount");
        System.out.println("3. Withdraw Amount");
        System.out.println("4. Transfer Amount");
        System.out.println("5. Logout");
        System.out.print("\nEnter your choice: ");
    }

    public void viewBalance() {
        System.out.print("Enter your password : ");
        String pwd = scanner.nextLine();
        try {
            int balance = accountDao.getUser(loggedInAccount.getAccount_no(), pwd).getBalance();
            System.out.println("\nCurrent Account Balance : " + balance);
            System.out.println("Account Number : " + loggedInAccount.getAccount_no());
        } catch (UnauthorizedOperationException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            System.out.println("Something went wrong!\nTry Again");
        }
    }

    public void deposit() {
        System.out.print("Enter amount to deposit : ");
        int amount = scanner.nextInt(); scanner.nextLine();
        try {
            if(amount <= 0) {
                System.out.println("Deposit amount must be positive.");
                return;
            }
            accountDao.deposit(loggedInAccount.getAccount_no(), amount);
            System.out.println("Amount " + amount + " has been deposited successfully.");
        } catch (AccountNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void withdraw() {
        System.out.print("Enter amount to withdraw : ");
        int amount = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter your password : ");
        String pwd = scanner.nextLine();
        try {
            if(amount <= 0) {
                System.out.println("Withdraw amount must be positive.");
                return;
            }
            accountDao.withdraw(loggedInAccount.getAccount_no(), pwd, amount);
            System.out.println("Withdrawal of " + amount + " success!");
        } catch (UnauthorizedOperationException e) {
            System.out.println(e.getMessage());
        } catch (InsufficientFundsException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            System.out.println("Something went wrong");
        }
    }

    public void transfer() {
        System.out.print("Enter recipient account number : ");
        int toAcc = scanner.nextInt(); scanner.nextLine();
        System.out.print("Enter amount to transfer : ");
        int amount = scanner.nextInt(); scanner.nextLine();
        System.out.print("Enter your password : ");
        String pwd = scanner.nextLine();
        try {
            if(amount <= 0) {
                System.out.println("Deposit amount must be positive.");
                return;
            }
            accountDao.transfer(loggedInAccount.getAccount_no(), toAcc, pwd, amount);
            System.out.println("Transfer of " + amount + " to account " + toAcc + " success!");
        } catch (UnauthorizedOperationException e) {
            System.out.println(e.getMessage());
        } catch (InsufficientFundsException e) {
            System.out.println(e.getMessage());
        } catch (AccountNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (BankingException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            System.out.println("Something went wrong");
        }
    }

    public boolean login(int acc_no, String password) {
        try {
            Account acc = accountDao.getUser(acc_no, password);
            this.loggedInAccount = acc;
            System.out.println("\nLogin successful. Welcome, " + acc.getName() + "!");
            return true;
        } catch (UnauthorizedOperationException e) {
            System.out.println(e.getMessage());
            return false;
        } catch (SQLException e) {
            System.out.println("Something went wrong!\nTry Again");
            return false;
        }
    }

    public void logout() {
        this.loggedInAccount = null;
        System.out.println("Logged out successfully.");
    }

    public void signup(String name, String password, int initialDeposit) {
        Account acc = new Account(name, password, initialDeposit);
        try {
            int accountNo = accountDao.addUser(acc);
            System.out.println("Account created successfully. Your account number is: " + accountNo);
        } catch (SQLException e) {
            System.out.println("Something went wrong!\nTry Again");
        }
    }

}
