package org.assigment.onlinebanking.aspect;

import org.assigment.onlinebanking.bank.Customer;


public aspect VerificationAspect {
	
	pointcut VerifyInt() : call(* Customer.*(int*));
	
	before():VerifyInt(){
		
		Object[] args = thisJoinPoint.getArgs();
	    for (int i = 0; i < args.length; i++) {
	        if (args[i] instanceof Integer && (Integer)args[i] < 0) {
	        	System.exit(0);
	        }
	    }
	}

}
