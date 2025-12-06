package com.rits.BankingApp;

import java.util.Scanner;

public class BankingAppApplication {

	private static boolean isLoggedIn = false;
	private static Scanner scan;

	public static void main(String[] args) {
		System.out.println("Welcome to Banking Application");
		scan = new Scanner(System.in);
		takeInitialInput();
		BankingService.getInstance().menu();

	}

	public static void takeInitialInput() {
		int choice;
		do {
			System.out.println("\n1. Login");
			System.out.println("2. Signup");
			System.out.println("0. Exit");
			System.out.print("\nEnter your choice: ");
			choice = scan.nextInt();
			scan.nextLine();
			if (choice == 1) {
				System.out.print("Enter Account Number: ");
				int acc_no = scan.nextInt();
				scan.nextLine();
				System.out.print("Enter Password: ");
				String password = scan.nextLine();
				isLoggedIn = BankingService.getInstance().login(acc_no, password);	
			}
			else if (choice == 2) {
				System.out.print("Enter Name: ");
				String name = scan.nextLine();
				System.out.print("Set Password: ");
				String password = scan.nextLine();
				System.out.print("Enter Initial Deposit: ");
				int initialDeposit = scan.nextInt();
				scan.nextLine();
				BankingService.getInstance().signup(name, password, initialDeposit);
			} else if (choice == 0) {
				System.out.println("Exiting... Thank you for using Banking Application.");
				isLoggedIn = false;
				System.exit(0);
			}
			else {
				System.out.println("Invalid Choice! Try Again.");
			}
		} while(choice != 0 && !isLoggedIn);
	}

}
