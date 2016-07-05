package com.starterkit.bank.methods;

import java.util.Currency;

import com.starterkit.bank.core.MoneyTransferDirection;
import com.starterkit.bank.core.IBAN;

public class MoneyTransfer extends AbstractTransfer {

	public IBAN otherIBANNumber;

	public MoneyTransfer(String monetaryAmount, Currency currency) {
		super(monetaryAmount, currency);
		setTransferDirection(MoneyTransferDirection.TO_ACCOUNT);
	}

	public MoneyTransfer() {
		super();
	}

	@Override
	public String asHistorical() {
		if (transferDirection == MoneyTransferDirection.TO_ACCOUNT) {
			return "FROM> " + otherIBANNumber + " TO> " + account.getIBANNumber() + " AMOUNT> " + amount.toString();
		} else {
			return "FROM> " + account.getIBANNumber() + " TO> " + otherIBANNumber + " AMOUNT> " + amount.toString();
		}
	}

	public void setOtherIban(IBAN iban) {
		otherIBANNumber = iban;
	}

}
