package com.starterkit.bank.core;

import java.util.ArrayList;
import java.math.BigDecimal;
import java.util.List;
import java.util.Currency;
import java.util.Iterator;

public class Money {
	// TASK 1/02 - This code has been written by someone who does not care. To repair it do the following:
	// reformat
	// organize imports
	// remove stupid comments

	public Money(BigDecimal amount, Currency instance) {
this.amount = amount;
currency = instance;
	}
	BigDecimal amount;
	Currency currency;

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return amount + currency.toString(); 
	}

	public static Money noMoney(String string) {
		// TASK 1/01 - string is not a good argument name, pay attention in the next tasks too
		return new Money(BigDecimal.ZERO, Currency.getInstance(string));
	}
	
}
