package org.assigment.onlinebanking.bank;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.assigment.onlinebanking.bank.Transaction.TransactionType;
import org.assigment.onlinebanking.db.DbConnection;

public class Account {

	private int accountId;
	private AccountType accountType;
	private double balance;
	private ArrayList<Transaction> transactions =new ArrayList<Transaction>();
	private DbConnection dbConnection = DbConnection.getConnection();

	public enum AccountType {
		SAVINGS, CHECKING;
	};

	public Account(AccountType accountType, double initialAmount, String customerID) {
		String query = "INSERT INTO account (account_customer_ID, account_Type, account_Balance) VALUES('" + customerID + "' , '" + accountType + "' , " + initialAmount + ");";
		try {
			dbConnection.runQuery(query);
			query = "SELECT account_Num FROM account WHERE account_customer_ID= '"+ customerID + "' ;";
			ResultSet newResult = dbConnection.getDbResult(query);
			newResult.last();
			setAccountId(newResult.getInt("account_Num"));
			setAccountType(accountType);
			setBalance(initialAmount);
			setTransactions();
			System.out.println("Account Created Succesful");

		} catch (SQLException e) {
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
		String query = "SELECT *FROM transaction WHERE transaction_AccountID= '" + accountId + "';";
		try {
			ResultSet transactionResult = dbConnection.getDbResult(query);
			while (transactionResult.next()) {
				int transactionId = transactionResult.getInt("transaction_ID");
				double ammount = transactionResult.getDouble("transaction_Ammount");
				TransactionType transactionType = TransactionType.valueOf(transactionResult.getString("transaction_Type"));
				Transaction newTransaction = new Transaction(transactionId, ammount, transactionType);
				transactions.add(newTransaction);
			}
			System.out.println("Adding transaction Sucsessful");
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Adding transaction Unsucsessful");
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
		String query = "SELECT *FROM account WHERE account_Num=" + accountId + "AND customer_ID ='" + userName + "';";
		try {
			ResultSet newResult = dbConnection.getDbResult(query);
			if (!newResult.wasNull()) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public void transfer(int anotherAccountId, double ammount) {
		String query = "SELECT *FROM account WHERE account_Num = " + anotherAccountId + ";";
		try {
			ResultSet newResult = dbConnection.getDbResult(query);
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
			Transaction newTansaction = new Transaction(ammount, TransactionType.DEBIT, accountId);
			balance = balance - ammount;
			String query = "UPDATE account SET account_Balance = "+ balance +" WHERE account_Num = " + accountId + ";";
			try {
				dbConnection.runQuery(query);
				transactions.add(newTansaction);
				return true;
				

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return (false);
			}
		} else
			return false;
	}

	public boolean credit(double ammount) {
		Transaction newTansaction = new Transaction(ammount, TransactionType.CREDIT, accountId);
		balance = balance + ammount;
		String query = "UPDATE account SET account_Balance = "+ balance +" WHERE account_Num = " + accountId + ";";
		try {
			dbConnection.runQuery(query);
			transactions.add(newTansaction);
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return (false);
		}
	}
}
