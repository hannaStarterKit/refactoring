package com.starterkit.bank.methods;

import java.math.BigDecimal;
import java.util.Currency;

import com.starterkit.bank.core.Account;
import com.starterkit.bank.core.Money;

public class PayInMoney {
	
	// TASK 1/20.0 - the transferring methods have many things in common, take a look at them and try to reduce the code behind it, remove some classes if needed, ... Think about abstraction, or Type forwarding

	public Money money;
	public Account account;

	


	public PayInMoney(String monetaryAmount, Currency currency) {
		money = new Money(new BigDecimal(monetaryAmount), currency);
	}




	public PayInMoney() {
		// TODO Auto-generated constructor stub
	}




	public String asHistorical() {
		return "PAID IN TO> " + account.number.number + " AMOUNT> "+ money.toString();
	}
	
}
