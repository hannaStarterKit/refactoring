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
		assertEquals(BigDecimal.ZERO, moneyInBank.getAmount());
	}

	@Test
	public void shouldHaveSomeMoneyInBankWithOneNonEmptyAccount() {
		// given
		Bank bank = BankBuilder.emptyBank().withDummyEuroAccount("IBAN", "123", "13", "22", "10").build();

		// when
		Money moneyInBank = bank.getMoneyInBank();

		// then
		assertEquals(new BigDecimal("168"), moneyInBank.getAmount());
	}

	@Test
	public void shouldHaveMoneyInBankWithThreeNonEmptyAccounts() {
		// given
		Bank bank = BankBuilder.emptyBank().withDummyEuroAccount("IBAN1", "111")
				.withDummyEuroAccount("IBAN2", "222", "2").withDummyEuroAccount("IBAN3", "333", "3", "3").build();

		// when
		Money moneyInBank = bank.getMoneyInBank();

		// then
		assertEquals(new BigDecimal("674"), moneyInBank.getAmount());
	}

	@Test
	public void testTransferMoney() {
		// given
		String iban1 = "IBAN1";
		String iban2 = "IBAN2";
		Bank bank = BankBuilder.emptyBank().withDummyEuroAccount(iban1, "111").withDummyEuroAccount(iban2, "222")
				.build();
		assertEquals(new BigDecimal("111"), bank.getAccount(new IBAN(iban1)).getBalance().getAmount());
		assertEquals(new BigDecimal("222"), bank.getAccount(new IBAN(iban2)).getBalance().getAmount());

		// when
		bank.transferMoney(iban1, iban2, makeEuroMoney("11"));

		// then
		assertEquals(new BigDecimal("100"), bank.getAccount(new IBAN(iban1)).getBalance().getAmount());
		assertEquals(new BigDecimal("233"), bank.getAccount(new IBAN(iban2)).getBalance().getAmount());
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
			Account account = new Account(new IBAN(ibanNumber));

			Currency euroCurrency = Currency.getInstance("EUR");
			account.setActual(new Money(new BigDecimal(initialBalance), euroCurrency));

			for (String textualMonetaryAmount : monetaryAmounts) {
				PayInMoney payment = new PayInMoney(textualMonetaryAmount, euroCurrency);
				account.addMoneyTransfer(payment);
			}

			bank.addAccount(account);
			return this;
		}

		Bank build() {
			return bank;
		}

		static BankBuilder emptyBank() {
			return new BankBuilder(new Bank());
		}

		BankBuilder withEmptyAccount(String ibanNumber) {
			Account account = new Account(new IBAN(ibanNumber));
			bank.addAccount(account);
			return this;
		}

	}

}
