package com.starterkit.bank.methods;

import java.util.Currency;

import com.starterkit.bank.core.MoneyTransferDirection;

public class WithdrawMoney extends AbstractTransfer {

	public WithdrawMoney(String monetaryAmount, Currency currency) {
		super(monetaryAmount, currency);
	}

	public WithdrawMoney() {
		super();
	}

	@Override
	public MoneyTransferDirection getTransferDirection() {
		return MoneyTransferDirection.FROM_ACCOUNT;
	}

	@Override
	public String asHistorical() {
		return String.format("FROM> %s WITHDRAWN AMOUNT> %s", account.getIBANNumber(), amount.toString());
	}
}
