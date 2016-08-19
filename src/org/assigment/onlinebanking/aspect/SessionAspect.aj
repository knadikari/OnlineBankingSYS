package org.assigment.onlinebanking.aspect;

import org.assigment.onlinebanking.bank.Customer;

public aspect SessionAspect {
	
	pointcut LogingCheck(Customer customer): call (* Customer.transfer(..))  || call (* Customer.credit(..)) || call(* Customer.withdraw(..)) || call (* Customer.checkBalance(..));
	
	before(Customer customer): LogingCheck(customer){
		if(customer.isLogin() != true){
			System.exit(0);
		}
	}

}
