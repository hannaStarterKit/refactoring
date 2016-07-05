package com.starterkit.bank.core;

import java.util.HashMap;
import java.util.Map;

import com.starterkit.bank.methods.MoneyTransfer;

public class Bank {

	private Map<IBAN, Account> accountsByIBAN = new HashMap<IBAN, Account>();

	public Account getAccount(IBAN number) {
		return accountsByIBAN.get(number);
	}

	public void addAccount(Account account) {
		accountsByIBAN.put(account.getIBANNumber(), account);
	}

	public Money getMoneyInBank() {
		Money result = Money.noMoney("USD");

		for (Map.Entry<IBAN, Account> entry : accountsByIBAN.entrySet()) {
			result.addMoney(entry.getValue().getBalance());
		}
		return result;
	}

	public void transferMoney(String from, String to, Money amount) {
		transferMoney(new IBAN(from), new IBAN(to), amount);
	}

	public void transferMoney(IBAN from, IBAN to, Money amount) {
		Account fromAccount = accountsByIBAN.get(from);
		Account toAccount = accountsByIBAN.get(to);

		transferMoney(fromAccount, toAccount, amount);
	}

	private void transferMoney(Account fromAccount, Account toAccount, Money amount) {
		// XXX there are no validations below!
		MoneyTransfer fromTransfer = new MoneyTransfer();
		fromTransfer.setTransferDirection(MoneyTransferDirection.FROM_ACCOUNT);
		fromTransfer.setAccount(fromAccount);
		fromTransfer.setOtherIban(toAccount.getIBANNumber());
		fromTransfer.setAmount(amount);
		fromAccount.addMoneyTransfer(fromTransfer);

		MoneyTransfer toTransfer = new MoneyTransfer();
		toTransfer.setTransferDirection(MoneyTransferDirection.TO_ACCOUNT);
		toTransfer.setAccount(toAccount);
		toTransfer.setOtherIban(fromAccount.getIBANNumber());
		toTransfer.setAmount(amount);
		toAccount.addMoneyTransfer(toTransfer);
	}

}
