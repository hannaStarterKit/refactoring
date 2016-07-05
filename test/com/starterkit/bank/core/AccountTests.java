package com.starterkit.bank.core;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.starterkit.bank.methods.InternalMoneyTransfer;
import com.starterkit.bank.methods.MoneyTransfer;
import com.starterkit.bank.methods.PayInMoney;
import com.starterkit.bank.methods.WithdrawMoney;

public class AccountTests {

	private static Currency PLN = Currency.getInstance("PLN");

	@Test
	public void shouldGetEmptyBalance() {
		// given
		Account account = new Account();

		// when
		Money balance = account.getBalance();

		// then
		assertEquals(BigDecimal.ZERO, balance.amount);
	}

	@Test
	public void shouldGiveBalanceForAccountWithOneBankTransfer() {
		// given
		Account account = new Account();
		MoneyTransfer transfer = new MoneyTransfer();
		transfer.amount = new Money(new BigDecimal("10"), PLN);
		account.moneyTransferHistory.add(transfer);

		// when
		Money balance = account.getBalance();

		// then
		assertEquals(BigDecimal.TEN, balance.amount);
		assertEquals(PLN, balance.currency);
	}

	@Test
	public void shouldGiveBalanceForAccountWithTwoBankTransfers() {
		// given
		Account account = new Account();
		MoneyTransfer transfer = new MoneyTransfer();
		transfer.amount = new Money(new BigDecimal("10"), PLN);
		account.moneyTransferHistory.add(transfer);

		MoneyTransfer transfer2 = new MoneyTransfer();
		transfer2.amount = new Money(new BigDecimal("12"), PLN);
		account.moneyTransferHistory.add(transfer2);

		// when
		Money balance = account.getBalance();

		// then
		assertEquals(new BigDecimal("22"), balance.amount);
		assertEquals(PLN, balance.currency);
	}

	@Test
	public void shouldGiveBalanceForAccountWithTwoDifferentBankTransfers() {
		// given
		Account account = new Account();
		MoneyTransfer transfer = new MoneyTransfer();
		transfer.amount = new Money(new BigDecimal("10"), PLN);
		account.moneyTransferHistory.add(transfer);

		InternalMoneyTransfer transfer2 = new InternalMoneyTransfer();
		transfer2.amount = new Money(new BigDecimal("13"), PLN);
		account.internalTransferHistory.add(transfer2);

		// when
		Money balance = account.getBalance();

		// then
		assertEquals(new BigDecimal("23"), balance.amount);
		assertEquals(PLN, balance.currency);
	}

	@Test
	public void shouldGiveBalanceForAccountWithFourDifferentBankTransfers() {
		// given
		Account account = AccountBuilder.emptyAccount() //
				.withPolishMoneyTransfer(1) //
				.withPolishInternalMoneyTransfer(2) //
				.withPolishPayInMoneyTransfer(4) //
				.withPolishWithdrawMoneyTransfer(7) //
				.build();

		// when
		Money balance = account.getBalance();

		// then
		assertEquals(BigDecimal.ZERO, balance.amount);
		assertEquals(PLN, balance.currency);
	}

	@Test
	public void test_Balance_With_Incomming_Outgoing_Transfers() {
		// given
		Account account = AccountBuilder.emptyAccount() //
				.withPolishMoneyTransfer(111, Direction.TO_ACCOUNT) //
				.withPolishMoneyTransfer(111, Direction.FROM_ACCOUNT) //
				.build();

		// when
		Money balance = account.getBalance();

		// then
		assertEquals(BigDecimal.ZERO, balance.amount);
		assertEquals(PLN, balance.currency);
	}

	@Test
	public void testGetHistory() {
		// given
		Account account = AccountBuilder.accountForIBAN("asdf") //
				.withPolishMoneyTransfer(10, Direction.TO_ACCOUNT, "1111") //
				.withPolishInternalMoneyTransfer(14, Direction.FROM_ACCOUNT, "2222") //
				.withPolishPayInMoneyTransfer(4).withPolishWithdrawMoneyTransfer(7).build();

		// TASK 0/9 - the 2 above rows do basically a similar thing, but for the
		// 2 different object perspectives. Placing a relation from Account to
		// Transfer and from Transfer to account. It is better to do it in one
		// place, as one can easy forget it, or corrupt by Copy Paste

		// when
		List<String> history = account.getHistory();

		// then
		assertEquals("FROM> 1111 TO> asdf AMOUNT> 10PLN", history.get(0));
		assertEquals("FROM> asdf TO> 2222 AMOUNT> 14PLN", history.get(1));
		assertEquals("PAID IN TO> asdf AMOUNT> 4PLN", history.get(2));
		assertEquals("FROM> asdf WITHDRAWN AMOUNT> 7PLN", history.get(3));
	}

	@Test
	public void testGetAverageTransaction() {
		// given
		Account account = AccountBuilder.accountForIBAN("asdf") //
				.withPolishMoneyTransfer(10, Direction.TO_ACCOUNT, "1111") //
				.withPolishInternalMoneyTransfer(14, Direction.FROM_ACCOUNT, "2222") //
				.build();

		// when
		Money average = account.getAverageTransaction();

		// then
		assertEquals(new BigDecimal("12"), average.amount);
	}

	@Test
	@Ignore
	public void testGetAverageTransaction2() {
		// TASK 0/8 - This test does not work - something is no-yes
		Account account = new Account(new IBAN("asdf"));
		MoneyTransfer transfer = new MoneyTransfer();
		transfer.amount = new Money(new BigDecimal("10"), PLN);
		transfer.number = new IBAN("asdf");
		account.moneyTransferHistory.add(transfer);
		WithdrawMoney transfer4 = new WithdrawMoney();
		transfer4.money = new Money(new BigDecimal("6"), PLN);
		transfer4.account = account;
		account.withdrawTransferHistory.add(transfer4);
		Money average = account.getAverageTransaction();
		assertEquals(new BigDecimal("2"), average.amount);
	}

	private static class AccountBuilder {

		private Account account;

		private AccountBuilder() {
		}

		private AccountBuilder(Account account) {
			this.account = account;
		}

		public AccountBuilder withPolishInternalMoneyTransfer(int amount) {
			return withPolishInternalMoneyTransfer(amount, Direction.TO_ACCOUNT, null);
		}

		public AccountBuilder withPolishMoneyTransfer(int amount) {
			return withPolishMoneyTransfer(amount, Direction.TO_ACCOUNT, null);
		}

		public AccountBuilder withPolishMoneyTransfer(int amount, Direction direction) {
			return withPolishMoneyTransfer(amount, direction, null);
		}

		public AccountBuilder withPolishInternalMoneyTransfer(int amount, Direction direction, String otherIBANNumber) {
			InternalMoneyTransfer transfer = new InternalMoneyTransfer();
			transfer.amount = new Money(toBD(amount), PLN);
			transfer.transferDirection = direction;
			transfer.number = new IBAN(otherIBANNumber);
			transfer.account = account;
			account.internalTransferHistory.add(transfer);
			return this;
		}

		public AccountBuilder withPolishWithdrawMoneyTransfer(int amount) {
			WithdrawMoney transfer = new WithdrawMoney();
			transfer.money = new Money(toBD(amount), PLN);
			transfer.account = account;
			account.withdrawTransferHistory.add(transfer);
			return this;
		}

		public AccountBuilder withPolishPayInMoneyTransfer(int amount) {
			PayInMoney transfer = new PayInMoney();
			transfer.money = new Money(toBD(amount), PLN);
			transfer.account = account;
			account.payTransferHistory.add(transfer);
			return this;
		}

		AccountBuilder withPolishMoneyTransfer(int amount, Direction direction, String otherIBANNumber) {
			MoneyTransfer transfer = new MoneyTransfer();
			transfer.amount = new Money(toBD(amount), PLN);
			transfer.transferDirection = direction;
			transfer.number = new IBAN(otherIBANNumber);
			transfer.account = account;
			account.moneyTransferHistory.add(transfer);
			return this;
		}

		private BigDecimal toBD(int amount) {
			return new BigDecimal("" + amount);
		}

		static AccountBuilder emptyAccount() {
			return new AccountBuilder(new Account());
		}

		static AccountBuilder accountForIBAN(String iban) {
			return new AccountBuilder(new Account(new IBAN(iban)));
		}

		Account build() {
			return account;
		}
	}

}
