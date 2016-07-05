package com.starterkit.bank.core;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import com.starterkit.bank.methods.InternalMoneyTransfer;
import com.starterkit.bank.methods.MoneyTransfer;
import com.starterkit.bank.methods.PayInMoney;
import com.starterkit.bank.methods.WithdrawMoney;

public class Account {

	public IBAN number;
	Customer owner;

	Money actual = new Money(new BigDecimal("0"), Currency.getInstance("PLN"));

	List<InternalMoneyTransfer> internalTransferHistory = new ArrayList<>();
	List<MoneyTransfer> moneyTransferHistory = new ArrayList<>();
	List<PayInMoney> payTransferHistory = new ArrayList<>();
	List<WithdrawMoney> withdrawTransferHistory = new ArrayList<>();

	public Account() {

	}

	public Account(IBAN iban) {
		number = iban;
		// TODO Auto-generated constructor stub
	}

	Money getBalance() {
		Money actualPlusHistory = new Money(actual.amount, actual.currency);

		for (int i = 0; i < moneyTransferHistory.size(); i++) {
			if (Direction.TO_ACCOUNT == moneyTransferHistory.get(i).transferDirection) {
				actualPlusHistory.amount = actualPlusHistory.amount.add(moneyTransferHistory.get(i).amount.amount);
			} else {
				actualPlusHistory.amount = actualPlusHistory.amount.subtract(moneyTransferHistory.get(i).amount.amount);
			}
		}

		for (int i = 0; i < internalTransferHistory.size(); i++) {
			if (Direction.TO_ACCOUNT == internalTransferHistory.get(i).transferDirection) {
				actualPlusHistory.amount = actualPlusHistory.amount.add(internalTransferHistory.get(i).amount.amount);
			} else {
				actualPlusHistory.amount = actualPlusHistory.amount.subtract(internalTransferHistory.get(i).amount.amount);
			}
		}

		for (int i = 0; i < payTransferHistory.size(); i++) {
			actualPlusHistory.amount = actualPlusHistory.amount.add(payTransferHistory.get(i).money.amount);
		}

		for (int i = 0; i < withdrawTransferHistory.size(); i++) {
			actualPlusHistory.amount = actualPlusHistory.amount.subtract(withdrawTransferHistory.get(i).money.amount);
		}

		return actualPlusHistory;
	}

	List<String> getHistory() {
		List<String> history = new ArrayList<>();
		// TASK 1/20.1 - will the conceptual changes have any impact on these places here? 

		for (int i = 0; i < moneyTransferHistory.size(); i++) {
			history.add(moneyTransferHistory.get(i).asHistorical());
		}

		for (int i = 0; i < internalTransferHistory.size(); i++) {
			history.add(internalTransferHistory.get(i).asHistorical());
		}

		for (int i = 0; i < payTransferHistory.size(); i++) {
			history.add(payTransferHistory.get(i).asHistorical());
		}

		for (int i = 0; i < withdrawTransferHistory.size(); i++) {
			history.add(withdrawTransferHistory.get(i).asHistorical());
		}

		return history;
	}

	Money getAverageTransaction() {
		BigDecimal value = BigDecimal.ZERO;
		int count = 0;

		for (int i = 0; i < moneyTransferHistory.size(); i++) {
			value = value.add(moneyTransferHistory.get(i).amount.amount);
			count++;
		}

		for (int i = 0; i < internalTransferHistory.size(); i++) {
			value = value.add(internalTransferHistory.get(i).amount.amount);
			count++;
		}

		for (int i = 0; i < payTransferHistory.size(); i++) {
			value = value.add(payTransferHistory.get(i).money.amount);
			count++;
		}

		for (int i = 0; i < withdrawTransferHistory.size(); i++) {
			value = value.add(withdrawTransferHistory.get(i).money.amount);
			count++;
		}

		return new Money(value.divide(new BigDecimal(count + "")), actual.currency);
	}
}
