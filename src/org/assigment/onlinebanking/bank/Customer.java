package org.assigment.onlinebanking.bank;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.assigment.onlinebanking.bank.Account.AccountType;
import org.assigment.onlinebanking.db.DbConnection;

public class Customer {

	private static String userName;
	private ArrayList<Account> accounts = new ArrayList<Account>();
	private DbConnection dbConnection = DbConnection.getConnection();

	public Customer(String userName, String password) {
		login(userName, password);

	}

	public Customer(String userName, String password, String customerName) {
		register(userName, password, customerName);
	}

	private void setUserName(String value) {
		userName = value;
	}

	public String getUserName() {
		return (userName);
	}

	private void setBankAccounts() {
		String query = "SELECT *FROM account WHERE account_customer_ID= '" + userName + "';";
		
		try {
			ResultSet accountResult = dbConnection.getDbResult(query);
			while (accountResult.next()) {
				int accountNum = accountResult.getInt("account_Num");
				AccountType accountType = AccountType.valueOf(accountResult.getString("account_Type"));
				double balance = accountResult.getDouble("account_Balance");
				Account newAccount = new Account(accountNum, accountType, balance);
				accounts.add(newAccount);

			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Adding account Unsucsessful");
		}
	}

	public Account getAccount(int accountId) {
		Account account = null;
		for (int i=0; i<accounts.size();i++ ) {
			if (accounts.get(i).getAccountId() == accountId) {
				account = accounts.get(i);
				return (account);
			}
		}
		return (account);
	}

	public void getAccounts() {
		for (Account ac : accounts) {
			System.out.println("Account Num: " + ac.getAccountId());
			System.out.println("Account Type: " + ac.getaccountType());
			System.out.println("Account Balance: " + ac.getBalance());
		}
	}

	public void login(String userName, String password) {
		String query = "SELECT *FROM customer WHERE customer_ID= '" + userName + "' AND customer_password= '" + password
				+ "';";
		try {
			ResultSet newResult = dbConnection.getDbResult(query);
			if (newResult.next()) {
				query = "UPDATE login SET status= 'YES' WHERE customer_ID= '" + userName + "';";
				dbConnection.runQuery(query);
				System.out.println("Login succesful");
				setUserName(userName);

			} else {
				System.out.println("Login was Unsuccesful");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Login was Unsuccesful");
		}
		setBankAccounts();
	}

	public void logout() {
		String query = "UPDATE login SET status= 'NO' WHERE customer_ID= '" + userName + "';";
		try {
			dbConnection.runQuery(query);
			System.out.println("Log out sucsessful");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Log out Unsucsessful");
		}
	}

	private void register(String userName, String password, String customerName) {
		String query = "SELECT *FROM customer WHERE customer_ID= '" + userName + "');";
		try {
			ResultSet newResult = dbConnection.getDbResult(query);
			if (newResult.wasNull()) {
				query = "INSERT INTO customer VALUES( '" + userName + "' , '" + customerName + "' , '" + password + "');";
				dbConnection.runQuery(query);
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
		String query = "SELECT * FROM login WHERE customer_ID= '" + userName + "';";
		try {
			ResultSet newResult = dbConnection.getDbResult(query);
			newResult.next();
			if (newResult.getString("status").equals("YES")) {
				return (true);
			} else {
				return (false);
			}
		} catch (SQLException e) {
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

	public void checkTransfers(int accountId) {
		Account account = getAccount(accountId);
		account.getTransactions();
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
		String query = "INSERT INTO bank VALUES('" + userName + "' , '" + message + "');";
		try {
			dbConnection.runQuery(query);
			System.out.println("Message send succesful");

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Message send Unsuccesful");
		}

	}

	public void changeName(String newName) {
		
		String query = "UPDATE customer SET customer_Name = '" + newName + "' WHERE customer_ID= '" + userName + "';";

		try {
			dbConnection.runQuery(query);
			System.out.println("Change name Succesful");

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Change name Unsuccesful");
		}

	}

}