package com.starterkit.bank.methods;

import com.starterkit.bank.core.Account;
import com.starterkit.bank.core.Money;

public class WithdrawMoney {

	public Money money;
	public Account account;


	public String asHistorical() {
		// TASK 1/11 - Try to replace the return statement below with the String.format execution
		return "FROM> " + account.number.number + " WITHDRAWN AMOUNT> "+ money.toString();
	}
}
