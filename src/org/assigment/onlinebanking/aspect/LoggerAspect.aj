package org.assigment.onlinebanking.aspect;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

import org.assigment.onlinebanking.bank.Customer;

public aspect LoggerAspect {
	
	pointcut ExceptionRecord() : call(* *.*.*(..));

	after() throwing(Exception t) : ExceptionRecord(){
	    System.err.println(" Log "+t.getMessage());
	    WriteFile(t.getMessage(),"exceptionLog.txt");
	}
	
	
	pointcut LoginRecord(Customer customer) : call(* Customer.login(String , String)) && (target(Customer));
	
	after(Customer customer) : LoginRecord(customer){
		String msg = "User Name: "+customer.getUserName();
		WriteFile(msg, "loginLog.txt");
	}
	
	public void WriteFile(String Log, String fileName){
		  try {
			    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fileName, true)));
			    out.println(Log);
			    out.close();
			} catch (IOException e) {
			    //exception handling left as an exercise for the reader
			}
	}

}
