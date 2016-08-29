package org.assigment.onlinebanking.bank;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.assigment.onlinebanking.db.DbConnection;

public class Transaction {

	private static int transactionID;
	private TransactionType transactionType;
	private static double ammount;
	private DbConnection dbConnection = DbConnection.getConnection();

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
	
	public int gettransactionID(){
		return(transactionID);
	}
	
	public String gettransactionType(){
		return(transactionType.toString());
	}
	
	public double getammount(){
		return(ammount);
	}

	public Transaction(double amount, TransactionType transactionType, int accountID){
		String query = "INSERT INTO account (transaction_Ammount, transaction_Type, transaction_AccountID) VALUES(" + amount
				+ " , '" + transactionType + "' , " + accountID + " );";
		try {
			dbConnection.runQuery(query);
			query = "SELECT transactionID FROM transaction WHERE transaction_AccountID= "+ accountID + " ;";
			ResultSet newResult = dbConnection.getDbResult(query);
			newResult.last();
			settransactionID(newResult.getInt("transaction_ID"));
			setAmmount(amount);
			settransactionType(transactionType);
			System.out.println("Transaction was Succesful");
			
		} catch (SQLException e) {
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
