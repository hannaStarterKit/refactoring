package com.starterkit.bank.methods;

import com.starterkit.bank.core.Account;
import com.starterkit.bank.core.Direction;
import com.starterkit.bank.core.IBAN;
import com.starterkit.bank.core.Money;

public class InternalMoneyTransfer {
	
	public Direction transferDirection = Direction.TO_ACCOUNT;
	public Money amount;
	
	public Account account;
	public IBAN number;
	


	public String asHistorical() {
		if (transferDirection == Direction.TO_ACCOUNT) {
			return "FROM> " + number.number + " TO> " + account.number.number + " AMOUNT> "+ amount.toString();
		}
		else {
			return "FROM> " + account.number.number + " TO> " + number.number + " AMOUNT> "+ amount.toString();
		}
	}
	

}
