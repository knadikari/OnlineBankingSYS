package org.assigment.onlinebanking.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DbConnection {
	
	private static final String ROOT_NAME = "root";
	private static final String PASSWORD = "admin";
	private static final String PORT_NUMBER = "3306";
	private static final String DB_NAME = "bank";
	private static final String DB_CONNECTION = "jdbc:mysql://localhost:" + PORT_NUMBER + "/" + DB_NAME; 
	private static DbConnection dbConnection = new DbConnection();
	private Connection newConn;
	
	
	
	private DbConnection(){
		try {
		newConn = DriverManager.getConnection(DB_CONNECTION, ROOT_NAME, PASSWORD);	
		} catch (SQLException e){
			e.printStackTrace();
		}
	}
	
	public static DbConnection getConnection(){
		return dbConnection;
	}
	public boolean runQuery(String sqlQuery){
		
		try {
			Statement newState = newConn.createStatement();	
			newState.executeUpdate(sqlQuery);
			return true;

		} catch (SQLException e) {
			e.printStackTrace();
			return (false);
		}
	}
	
	public ResultSet getDbResult(String sqlQuery){
		
		try {
			Statement newState = newConn.createStatement();	
			ResultSet newResult = newState.executeQuery(sqlQuery);
			return newResult;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	public boolean isConnected(){
		try {
			return newConn.isClosed();
				
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public void reconnect(){
		try {
		newConn = DriverManager.getConnection(DB_CONNECTION, ROOT_NAME, PASSWORD);	
		} catch (SQLException e){
			e.printStackTrace();
		}
	}

}
