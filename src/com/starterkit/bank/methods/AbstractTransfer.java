package com.starterkit.bank.methods;

import java.math.BigDecimal;
import java.util.Currency;

import com.starterkit.bank.core.Account;
import com.starterkit.bank.core.MoneyTransferDirection;
import com.starterkit.bank.core.Money;

public abstract class AbstractTransfer {

	protected MoneyTransferDirection transferDirection;
	protected Money amount;
	protected Account account;

	public AbstractTransfer(String monetaryAmount, Currency currency) {
		amount = new Money(new BigDecimal(monetaryAmount), currency);
	}

	public AbstractTransfer() {
	}

	public abstract String asHistorical();

	public void setTransferDirection(MoneyTransferDirection transferDirection) {
		this.transferDirection = transferDirection;
	}

	public void setAmount(Money amount) {
		this.amount = amount;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public MoneyTransferDirection getTransferDirection() {
		return transferDirection;
	}

	public Money getAmount() {
		return amount;
	}

}
