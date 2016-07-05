package com.starterkit.bank.methods;

import java.util.Currency;

import com.starterkit.bank.core.MoneyTransferDirection;

public class PayInMoney extends AbstractTransfer {

	public PayInMoney(String monetaryAmount, Currency currency) {
		super(monetaryAmount, currency);
	}

	public PayInMoney() {
		super();
	}

	@Override
	public MoneyTransferDirection getTransferDirection() {
		return MoneyTransferDirection.TO_ACCOUNT;
	}

	@Override
	public String asHistorical() {
		return "PAID IN TO> " + account.getIBANNumber() + " AMOUNT> " + amount.toString();
	}

}
