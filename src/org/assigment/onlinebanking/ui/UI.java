package org.assigment.onlinebanking.ui;

import java.util.Scanner;

import org.assigment.onlinebanking.bank.Customer;

public class UI {

	public static void main(String[] args) {
		Customer customer = null;
		Scanner input = new Scanner(System.in);
		int selection;
		System.out.println("1)Login\n2)Register\n3)Exit");
		System.out.print("Select your number: ");
		selection = input.nextInt();

		if (selection == 1) {
			System.out.print("Enter your ID number: ");
			String id = input.next();
			System.out.print("Enter your Password: ");
			String pass = input.next();
			customer = new Customer(id, pass);
		}

		else if (selection == 2) {
			System.out.print("Enter your ID number: ");
			String id = input.next();
			System.out.print("Enter your Password: ");
			String pass = input.next();
			System.out.print("Enter your Name: ");
			String name = input.next();
			customer = new Customer(id, pass, name);
		} else {
			System.exit(0);
			input.close();
		}

		while (true) {
			System.out.println(
					"1)Check balance\n2)Credit the account\n3)Withdraw from the account\n4)Transfer money to a account\n5)History of Inquiries\n6)Send a messsage to the bank\n7)Details of accounts\n8)Manage the profile\n9)Log out");
			System.out.print("Select your number: ");
			selection = input.nextInt();

			if (selection == 1) {
				System.out.print("Enter the account number: ");
				customer.checkBalance(input.nextInt());
			}

			else if (selection == 2) {
				System.out.print("Enter the account number: ");
				int accountNum = input.nextInt();
				System.out.print("Enter the Ammount you want to credit: ");
				double ammount = input.nextDouble();
				customer.credit(accountNum, ammount);
			}

			else if (selection == 3) {
				System.out.print("Enter the account number: ");
				int accountNum = input.nextInt();
				System.out.print("Enter the Ammount you want to withdraw: ");
				double ammount = input.nextDouble();
				customer.withdraw(accountNum, ammount);
			}

			else if (selection == 4) {
				System.out.print("Enter your account number: ");
				int accountNum = input.nextInt();
				System.out.print("Enter account number to transfer: ");
				int otherAccountNum = input.nextInt();
				System.out.print("Enter the Ammount you want to transfer: ");
				double ammount = input.nextDouble();
				customer.transfer(accountNum, otherAccountNum, ammount);
			}

			else if (selection == 5) {
				System.out.print("Enter your account number: ");
				int accountNum = input.nextInt();
				customer.checkTransfers(accountNum);
			}

			else if (selection == 6) {
				System.out.println("Enter your message in  one line: ");
				String message = input.next();
				customer.sendAMessage(message);
			}

			else if (selection == 7) {
				customer.getAccounts();
			}

			else if (selection == 8) {
				System.out.println("Edit 1)Name");
				selection = input.nextInt();
				if (selection == 1) {
					System.out.print("Enter the new name: ");
					customer.changeName(input.next());
				}
			}

			else {
				customer.logout();
				System.exit(0);
				input.close();
			}
		}
	}

}
