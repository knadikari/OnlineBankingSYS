package org.assigment.onlinebanking;

import org.assigment.onlinebanking.bank.Account.AccountType;

import static org.junit.Assert.assertEquals;

import org.assigment.onlinebanking.bank.Customer;
import org.junit.Assert;
import org.junit.Test;

public class Test1 {
	@Test
	public void test(){
		AccountType hello =AccountType.SAVINGS;
		int account;
		Customer cus = new Customer("930080101V", "123");
		account = cus.createAccount(hello, 4500);
		cus.checkBalance(account);
		//cus.logout()
		assertEquals("",true, true);
	}

}
