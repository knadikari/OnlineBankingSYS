package org.assigment.onlinebanking.bank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.assigment.onlinebanking.bank.Account.AccountType;
import org.assigment.onlinebanking.bank.Transaction.TransactionType;

public class Account {

	private static int accountId;
	private static AccountType accountType;
	private double balance;
	private TransactionType transactionType;
	private List<Transaction> transactions =new ArrayList<Transaction>();

	public enum AccountType {
		SAVINGS, CHECKING;
	};

	public Account(AccountType accountType, double initialAmount, String customerID) {
		try {
			Connection newConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank", "root", "admin");
			Statement newState = newConn.createStatement();
			newState.executeUpdate(
					"INSERT INTO account (account_customer_ID, account_Type, account_Balance) VALUES('" + customerID + "' , '" + accountType + "' , " + initialAmount + ");");
			
			ResultSet newResult = newState.executeQuery("SELECT account_Num FROM account WHERE account_customer_ID= '"+ customerID + "' ;");
			newResult.last();
			setAccountId(newResult.getInt("account_Num"));
			setAccountType(accountType);
			setBalance(initialAmount);
			setTransactions();
			System.out.println("Account Created Succesful");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Account create was Unsuccesful");
		}

	}

	public Account(int acccount_id, AccountType account_type, double account_balance) {
		setAccountId(acccount_id);
		setAccountType(account_type);
		setBalance(account_balance);
		setTransactions();
	}

	public Account(int accountId, String customerID) {

	}

	private void setAccountId(int account_id) {
		accountId = account_id;
	}

	private void setAccountType(AccountType value) {
		accountType = value;
	}

	private void setBalance(double initialAmount) {
		balance = initialAmount;

	}
	
	private void setTransactions(){
		try {
			Connection newConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank", "root", "admin");
			Statement newState = newConn.createStatement();
			ResultSet newResult = newState.executeQuery("SELECT *FROM transaction WHERE transaction_AccountID= '" + accountId
					+ "';");
			while (newResult.next()) {
				int transactionId = newResult.getInt("transaction_ID");
				double ammount = newResult.getInt("transaction_Ammount");
				TransactionType transactionType = TransactionType.valueOf(newResult.getString("transaction_Type"));
				
				Transaction newAccount = new Transaction(transactionId, ammount, transactionType);
				transactions.add(newAccount);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Adding account Unsucsessful");
		}
	}

	public int getAccountId() {
		return (accountId);
	}

	public double getBalance() {
		return (balance);
	}
	
	public String getaccountType() {
		return (accountType.toString());
	}
	
	public void getTransactions(){
		for(Transaction tr : transactions){
			System.out.println("Transaction Num: "+ tr.gettransactionID());
			System.out.println("Transaction Type: "+ tr.gettransactionType());
			System.out.println("Transaction Amount: "+ tr.getammount());
		}
		
	}

	public boolean verifyUser(String userName) {
		try {
			Connection newConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank", "root", "admin");
			Statement newState = newConn.createStatement();
			ResultSet newResult = newState
					.executeQuery("SELECT *FROM account WHERE account_Num=" + accountId + "AND customer_ID" + userName);
			if (!newResult.wasNull()) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	public void transfer(int anotherAccountId, double ammount) {
		try {
			Connection newConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank", "root", "admin");
			Statement newState = newConn.createStatement();
			ResultSet newResult = newState
					.executeQuery("SELECT *FROM account WHERE account_Num = '" + anotherAccountId + ";");
			if (newResult.next()) {
				int accountNum = newResult.getInt("account_Num");
				AccountType accountType = AccountType.valueOf(newResult.getString("account_Type"));
				int balance = newResult.getInt("account_Balance");
				Account anotherAccount = new Account(accountNum, accountType, balance);
				if (debit(ammount)) {
					anotherAccount.credit(ammount);
					System.out.println("Transaction was sucssesful");
				} else {
					System.out.println("Transaction was unsucssesful");
				}
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public boolean debit(double ammount) {
		if (balance > ammount) {
			Transaction newTansaction = new Transaction(ammount, transactionType.DEBIT, accountId);
			balance = balance - ammount;
			try {
				Connection newConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank", "root", "admin");
				Statement newState = newConn.createStatement();
				newState.executeUpdate("UPDATE account SET account_Balance = "+ balance +" WHERE account_Num = '" + accountId + ";");
				return true;

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return (false);
			}
		} else
			return false;
	}

	public boolean credit(double ammount) {
		Transaction newTansaction = new Transaction(ammount, transactionType.CREDIT, accountId);
		balance = balance + ammount;
		try {
			Connection newConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank", "root", "admin");
			Statement newState = newConn.createStatement();
			newState.executeUpdate("UPDATE account SET account_Balance = "+ balance +" WHERE account_Num = '" + accountId + ";");
			return true;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return (false);
		}
	}
}
