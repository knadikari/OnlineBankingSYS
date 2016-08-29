package org.assigment.onlinebanking.aspect;

import org.aspectj.lang.JoinPoint;

public aspect VerificationAspect {
	
	pointcut VerifyInt(JoinPoint joinPoint) : call(* *.Customer.*(int*)) && args(*);
	
	before(JoinPoint joinPoint):VerifyInt(joinPoint){
		Object[] args = joinPoint.getArgs();
	    for (int i = 0; i < args.length; i++) {
	        if (args[i] instanceof Integer && (Integer)args[i] < 0) {
	        	System.exit(0);
	        }
	    }
	}

}
