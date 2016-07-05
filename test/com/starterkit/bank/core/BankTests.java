package com.starterkit.bank.core;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.Currency;

import org.junit.Test;

import com.starterkit.bank.methods.PayInMoney;

public class BankTests {

	@Test
	public void shouldHaveNoMoneyInAlmostEmptyBank() {
		// given
		Bank bank = BankBuilder.emptyBank().withEmptyAccount("1243").build();
		
		// when
		Money moneyInBank = bank.getMoneyInBank();
		
		// then
		assertEquals(BigDecimal.ZERO, moneyInBank.amount);
	}
	

	@Test
	public void shouldHaveSomeMoneyInBankWithOneNonEmptyAccount() {
		// given
		Bank bank = BankBuilder.emptyBank().withDummyEuroAccount("IBAN", "123", "13", "22", "10").build();
		
		// when
		Money moneyInBank = bank.getMoneyInBank();
		
		// then
		assertEquals(new BigDecimal("168"), moneyInBank.amount);
	}
	
	@Test
	public void shouldHaveMoneyInBankWithThreeNonEmptyAccounts() {
		// given
		Bank bank = BankBuilder.emptyBank()
				.withDummyEuroAccount("IBAN1", "111")
				.withDummyEuroAccount("IBAN2", "222", "2")
				.withDummyEuroAccount("IBAN3", "333", "3", "3")
				.build();
		
		// when
		Money moneyInBank = bank.getMoneyInBank();
		
		// then
		assertEquals(new BigDecimal("674"), moneyInBank.amount);
	}

	@Test
	public void testTransferMoney() {
		// given
		Bank bank = BankBuilder.emptyBank()
				.withDummyEuroAccount("IBAN1", "111")
				.withDummyEuroAccount("IBAN2", "222")
				.build();
		assertEquals(new BigDecimal("111"), ((Account) bank.accounts.get(0)).getBalance().amount);
		assertEquals(new BigDecimal("222"), ((Account) bank.accounts.get(1)).getBalance().amount);
		// TASK 1/10.2 - Introduce a Type

		
		// when
		bank.giveMoney("IBAN1", "IBAN2", makeEuroMoney("11"));
		
		// then
		assertEquals(new BigDecimal("100"), ((Account) bank.accounts.get(0)).getBalance().amount);
		assertEquals(new BigDecimal("233"), ((Account) bank.accounts.get(1)).getBalance().amount);
	}
	
	private Money makeEuroMoney(String value) {
		return new Money(new BigDecimal(value), Currency.getInstance("EUR"));
	}

	private static class BankBuilder {
		
		private Bank bank;

		private BankBuilder(Bank bank) {
			this.bank = bank;
		}

		public BankBuilder withDummyEuroAccount(String ibanNumber, String initialBalance, String... monetaryAmounts) {
			Account account = new Account();
			account.number = new IBAN(ibanNumber);
			
			Currency euroCurrency = Currency.getInstance("EUR");
			account.actual = new Money(new BigDecimal(initialBalance), euroCurrency);
			
			for (String textualMonetaryAmount : monetaryAmounts) {
				PayInMoney payment = new PayInMoney(textualMonetaryAmount, euroCurrency);
				payment.account= account;
				account.payTransferHistory.add(payment);
			}
			
			
			bank.accounts.add(account);
			return this;
		}

		Bank build() {
			return bank;
		}

		static BankBuilder emptyBank() {
			return new BankBuilder(new Bank());
		}
		
		BankBuilder withEmptyAccount(String ibanNumber) {
			Account account = new Account();
			account.number = new IBAN(ibanNumber);
			bank.accounts.add(account);
			return this;
		}
		
	}

}
