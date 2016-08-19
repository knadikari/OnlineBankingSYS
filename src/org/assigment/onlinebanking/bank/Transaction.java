package org.assigment.onlinebanking.bank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Transaction {

	private static int transactionID;
	private static TransactionType transactionType;
	private static double ammount;

	public enum TransactionType {
		CREDIT, DEBIT;
	}

	private void settransactionID(int value){
		transactionID = value;
	}
	private void setAmmount(double value) {
		ammount = value;
	}

	private void settransactionType(TransactionType value) {
		transactionType = value;
	}

	public Transaction(double amount, TransactionType transactionType, int accountID){
		try {
			Connection newConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank", "root", "admin");
			Statement newState = newConn.createStatement();
			newState
					.executeQuery("INSERT INTO account (transaction_Ammount, transaction_Type, transaction_AccountID) VALUES(" + amount
							+ " , '" + transactionType + "' , " + accountID + " );");
			ResultSet newResult = newState.executeQuery("SELECT transactionID FROM transaction WHERE transaction_AccountID= "+ accountID + " ;");
			newResult.last();
			settransactionID(newResult.getInt("transaction_ID"));
			setAmmount(amount);
			settransactionType(transactionType);
			System.out.println("Transaction was Succesful");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Transaction was Unsuccesful");
		}
		setAmmount(amount);
		settransactionType(transactionType);
	}

	public Transaction(int transactionId, double amount, TransactionType transactionType) {
		setAmmount(amount);
		settransactionType(transactionType);
	}

}
