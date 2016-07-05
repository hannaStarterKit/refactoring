package com.starterkit.bank.methods;

import java.util.Currency;

import com.starterkit.bank.core.MoneyTransferDirection;
import com.starterkit.bank.core.IBAN;

public class InternalMoneyTransfer extends AbstractTransfer {

	public IBAN otherIBANNumber;

	public InternalMoneyTransfer(String monetaryAmount, Currency currency) {
		super(monetaryAmount, currency);
		setTransferDirection(MoneyTransferDirection.TO_ACCOUNT);
	}

	@Override
	public String asHistorical() {
		if (transferDirection == MoneyTransferDirection.TO_ACCOUNT) {
			return "FROM> " + otherIBANNumber + " TO> " + account.getIBANNumber() + " AMOUNT> " + amount.toString();
		} else {
			return "FROM> " + account.getIBANNumber() + " TO> " + otherIBANNumber + " AMOUNT> " + amount.toString();
		}
	}

}
