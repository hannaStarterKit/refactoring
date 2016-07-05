package com.starterkit.bank.core;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import com.starterkit.bank.methods.AbstractTransfer;

public class Account {

	private IBAN number;

	private Money actual = new Money(new BigDecimal("0"), Currency.getInstance("PLN"));

	private List<AbstractTransfer> transfers = new ArrayList<>();

	public Account() {

	}

	public Account(IBAN iban) {
		number = iban;
	}

	public Money getBalance() {
		Money actualPlusHistory = new Money(actual);

		for (AbstractTransfer transfer : transfers) {
			if (MoneyTransferDirection.TO_ACCOUNT == transfer.getTransferDirection()) {
				actualPlusHistory.addMoney(transfer.getAmount());
			} else {
				actualPlusHistory.subtractMoney(transfer.getAmount());
			}
		}

		return actualPlusHistory;
	}

	public List<String> getHistory() {
		List<String> history = new ArrayList<>(transfers.size());

		for (AbstractTransfer transfer : transfers) {
			history.add(transfer.asHistorical());
		}

		return history;
	}

	public Money getAverageTransaction() {
		BigDecimal overallBalance = getBalance().getAmount();
		BigDecimal transactionCount = new BigDecimal(transfers.size() + "");

		return new Money(overallBalance.divide(transactionCount), actual.getCurrency());
	}

	public IBAN getIBANNumber() {
		return number;
	}

	public void addMoneyTransfer(AbstractTransfer transfer) {
		transfers.add(transfer);
		transfer.setAccount(this);
	}

	public void setActual(Money money) {
		this.actual = money;
	}
}
