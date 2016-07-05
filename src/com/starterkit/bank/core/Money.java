package com.starterkit.bank.core;

import java.math.BigDecimal;
import java.util.Currency;

public class Money {

	private BigDecimal amount;
	private Currency currency;

	public Money(BigDecimal amount, Currency instance) {
		this.amount = amount;
		currency = instance;
	}

	public Money(Money actual) {
		this(actual.amount, actual.currency);
	}

	public static Money noMoney(String currencyCode) {
		return new Money(BigDecimal.ZERO, Currency.getInstance(currencyCode));
	}

	public void addMoney(Money money) {
		amount = amount.add(money.amount);
	}
	
	public void subtractMoney(Money money) {
		amount = amount.subtract(money.amount);
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public Currency getCurrency() {
		return currency;
	}

	@Override
	public String toString() {
		return amount + currency.toString();
	}

}
