package org.assigment.onlinebanking.bank;

import java.nio.charset.MalformedInputException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.assigment.onlinebanking.bank.Account.AccountType;

public class Customer {

	private static String userName;
	private String password;
	private List<Account> accounts = new ArrayList<Account>();

	public Customer(String userName, String password) {
		login(userName, password);

	}

	public Customer(String userName, String password, String customerName) {
		register(userName, password, customerName);
	}

	private void setPassword(String value) {
		password = value;
	}

	private void setUserName(String value) {
		userName = value;
	}

	public String getUserName() {
		return (userName);
	}

	private void setBankAccounts() {
		try {
			Connection newConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank", "root", "admin");
			Statement newState = newConn.createStatement();
			ResultSet newResult = newState
					.executeQuery("SELECT *FROM account WHERE account_customer_ID= '" + userName + "';");
			while (newResult.next()) {
				int accountNum = newResult.getInt("account_Num");
				AccountType accountType = AccountType.valueOf(newResult.getString("account_Type"));
				double balance = newResult.getDouble("account_Balance");
				Account newAccount = new Account(accountNum, accountType, balance);
				accounts.add(newAccount);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Adding account Unsucsessful");
		}
	}

	public Account getAccount(int accountId) {
		Account account = null;
		for (int i = 0; i < accounts.size() - 1; i++) {
			if (accounts.get(i).getAccountId() == accountId) {
				account = accounts.get(i);
				break;
			}
		}
		return (account);
	}

	public void login(String userName, String password) {
		try {
			Connection newConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank", "root", "admin");
			Statement newState = newConn.createStatement();
			ResultSet newResult = newState.executeQuery("SELECT *FROM customer WHERE customer_ID= '" + userName
					+ "' AND customer_password= '" + password + "';");
			if (newResult.next()) {
				newState.executeUpdate("UPDATE login SET status= 'YES' WHERE customer_ID= '" + userName + "';");
				System.out.println("Login succesful");
				setUserName(userName);
				setPassword(password);

			} else {
				System.out.println("Login was Unsuccesful");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Login was Unsuccesful");
		}

		setBankAccounts();
	}

	public void logout() {
		try {
			Connection newConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank", "root", "admin");
			Statement newState = newConn.createStatement();
			newState.executeUpdate("UPDATE login SET status= 'NO' WHERE customer_ID= '" + userName + "';");
			System.out.println("Log out sucsessful");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Log out Unsucsessful");
		}
	}

	private void register(String userName, String password, String customerName) {
		try {
			Connection newConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank", "root", "admin");
			Statement newState = newConn.createStatement();
			ResultSet newResult = newState.executeQuery("SELECT *FROM customer WHERE customer_ID=" + userName);
			if (newResult.wasNull()) {
				newState.executeUpdate("INSERT INTO customer VALUES( '" + userName + "' , '" + customerName + "' , '"
						+ password + "');");
				System.out.println("Registration succesful");
				login(userName, password);
			} else {
				System.out.println("Registration was Unsuccesful");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Registration was Unsuccesful");
		}
	}

	public int createAccount(AccountType accountType, double initialAmount) {
		Account newAccount = new Account(accountType, initialAmount, userName);
		accounts.add(newAccount);
		return (newAccount.getAccountId());
	}

	public boolean isLogin() {
		try {
			Connection newConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank", "root", "admin");
			Statement newState = newConn.createStatement();
			ResultSet newResult = newState.executeQuery("SELECT status login WHERE customer_ID=" + userName);
			if (newResult.getString("status") == "YES") {
				return (true);
			} else {
				return (false);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return (false);
		}

	}

	public void checkBalance(int accountId) {
		Account account = getAccount(accountId);
		if (account != null) {
			System.out.println("Account number: " + account.getAccountId());
			System.out.println("Balance: " + account.getBalance());
		}

	}

	public void transfer(int myAcountId, int anotherAccount, double ammount) {
		Account myAccount = getAccount(myAcountId);

		if (myAccount != null) {
			myAccount.transfer(anotherAccount, ammount);
		}

		else {
			System.out.println("This user cannot acssese this account");
		}
	}

	public void credit(int accountId, double ammount) {
		Account account = getAccount(accountId);
		account.credit(ammount);

	}

	public void withdraw(int accountId, double ammount) {
		Account account = getAccount(accountId);
		account.debit(ammount);

	}

	public void sendAMessage(String message) {
		try {
			Connection newConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank", "root", "admin");
			Statement newState = newConn.createStatement();
			ResultSet newResult = newState
					.executeQuery("INSERT INTO bank VALUES('" + userName + "' , '" + message + "');");
			System.out.println("Message send succesful");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}