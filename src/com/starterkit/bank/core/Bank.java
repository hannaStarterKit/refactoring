package com.starterkit.bank.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.starterkit.bank.methods.MoneyTransfer;

public class Bank {

	// TASK 1/08 - such members being public / protected are in fact implementation details leaking into API. They must be repaired / made private with public accessor methods.
	List accounts = new ArrayList();
	// TASK 1/10.0 - As the Checkstyles state, the collection above is of a RAW type. The benefits are obvious, the costs are little, introduce a Type
	ExchangeRates exchangeRates;
	
	Money getMoneyInBank() {
		Money result = Money.noMoney("USD");
		
		// TASK 1/04 - Iterating with this convention provides many risks, and takes place, repair to foreach
		Iterator iterator = accounts.iterator();
		while (iterator.hasNext()) {
			// TASK 1/05 - Everytime we are trying to do something with money, we need to take the inner parts of it and do something with them, would it be better to have the Money class to do it for us?
			result.amount = result.amount.add(((Account) iterator.next()).getBalance().amount);
		}
		return result;
	}
	
	// TASK 1/03 - method naming needs to be corrected, inconsistencies are imense
	void giveMoney(String from, String to, Money amount) {
		pushMoney(new IBAN(from), new IBAN(to), amount);
	}
	
	void pushMoney(IBAN from, IBAN to, Money amount) {
		
		Account fromAccount = null;
		Account toAccount = null;
		
		for (Account a : (List<Account>)accounts) {
			// TASK 1/10.1 - Introduce a Type
			if (a.number.number.equals(from.number)) {
				fromAccount = a;
			}
			if (a.number.number.equals(to.number)) {
				toAccount = a;
			}
		}
		
		// TASK 1/09 - this code below would look good as a method replaceMoney(Account, Account, Money). This would propose a good interface, 3 methods differentiating by the types only, and reusing themselves
		// XXX there are no validations below!
		
		MoneyTransfer fromTransfer = new MoneyTransfer();
		fromTransfer.transferDirection = Direction.FROM_ACCOUNT;
		fromTransfer.account = fromAccount;
		fromTransfer.number = toAccount.number;
		fromTransfer.amount = amount;
		fromAccount.moneyTransferHistory.add(fromTransfer);
		
		MoneyTransfer toTransfer = new MoneyTransfer();
		toTransfer.transferDirection = Direction.TO_ACCOUNT;
		toTransfer.account = toAccount;
		toTransfer.number = fromAccount.number;
		toTransfer.amount = amount;
		toAccount.moneyTransferHistory.add(toTransfer);
	}
	
}
