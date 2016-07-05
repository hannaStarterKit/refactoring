package com.starterkit.bank.core;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

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
		assertEquals(BigDecimal.ZERO, balance.getAmount());
	}

	@Test
	public void shouldGiveBalanceForAccountWithOneBankTransfer() {
		// given
		Account account = new Account();
		MoneyTransfer transfer = new MoneyTransfer("10", PLN);
		account.addMoneyTransfer(transfer);

		// when
		Money balance = account.getBalance();

		// then
		assertEquals(BigDecimal.TEN, balance.getAmount());
		assertEquals(PLN, balance.getCurrency());
	}

	@Test
	public void shouldGiveBalanceForAccountWithTwoBankTransfers() {
		// given
		Account account = new Account();
		MoneyTransfer transfer = new MoneyTransfer("10", PLN);
		account.addMoneyTransfer(transfer);

		MoneyTransfer transfer2 = new MoneyTransfer("12", PLN);
		account.addMoneyTransfer(transfer2);

		// when
		Money balance = account.getBalance();

		// then
		assertEquals(new BigDecimal("22"), balance.getAmount());
		assertEquals(PLN, balance.getCurrency());
	}

	@Test
	public void shouldGiveBalanceForAccountWithTwoDifferentBankTransfers() {
		// given
		Account account = new Account();
		MoneyTransfer transfer = new MoneyTransfer("10", PLN);
		account.addMoneyTransfer(transfer);

		InternalMoneyTransfer transfer2 = new InternalMoneyTransfer("13", PLN);
		account.addMoneyTransfer(transfer2);

		// when
		Money balance = account.getBalance();

		// then
		assertEquals(new BigDecimal("23"), balance.getAmount());
		assertEquals(PLN, balance.getCurrency());
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
		assertEquals(BigDecimal.ZERO, balance.getAmount());
		assertEquals(PLN, balance.getCurrency());
	}

	@Test
	public void test_Balance_With_Incomming_Outgoing_Transfers() {
		// given
		Account account = AccountBuilder.emptyAccount() //
				.withPolishMoneyTransfer(111, MoneyTransferDirection.TO_ACCOUNT) //
				.withPolishMoneyTransfer(111, MoneyTransferDirection.FROM_ACCOUNT) //
				.build();

		// when
		Money balance = account.getBalance();

		// then
		assertEquals(BigDecimal.ZERO, balance.getAmount());
		assertEquals(PLN, balance.getCurrency());
	}

	@Test
	public void testGetHistory() {
		// given
		Account account = AccountBuilder.accountForIBAN("asdf") //
				.withPolishMoneyTransfer(10, MoneyTransferDirection.TO_ACCOUNT, "1111") //
				.withPolishInternalMoneyTransfer(14, MoneyTransferDirection.FROM_ACCOUNT, "2222") //
				.withPolishPayInMoneyTransfer(4).withPolishWithdrawMoneyTransfer(7).build();

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
				.withPolishMoneyTransfer(10, MoneyTransferDirection.TO_ACCOUNT, "1111") //
				.withPolishInternalMoneyTransfer(14, MoneyTransferDirection.TO_ACCOUNT, "2222") //
				.build();

		// when
		Money average = account.getAverageTransaction();

		// then
		assertEquals(new BigDecimal("12"), average.getAmount());
	}

	@Test
	public void testGetAverageTransactionConsiderOutgoingTransactions() {
		// given
		Account account = new Account(new IBAN("asdf"));

		MoneyTransfer transfer = new MoneyTransfer("10", PLN);
		transfer.setOtherIban(new IBAN("asdf"));
		account.addMoneyTransfer(transfer);

		WithdrawMoney transfer4 = new WithdrawMoney("6", PLN);
		account.addMoneyTransfer(transfer4);

		// when
		Money average = account.getAverageTransaction();

		// then
		assertEquals(new BigDecimal("2"), average.getAmount());
	}

	private static class AccountBuilder {

		private Account account;

		private AccountBuilder() {
		}

		private AccountBuilder(Account account) {
			this.account = account;
		}

		public AccountBuilder withPolishInternalMoneyTransfer(int amount) {
			return withPolishInternalMoneyTransfer(amount, MoneyTransferDirection.TO_ACCOUNT, null);
		}

		public AccountBuilder withPolishMoneyTransfer(int amount) {
			return withPolishMoneyTransfer(amount, MoneyTransferDirection.TO_ACCOUNT, null);
		}

		public AccountBuilder withPolishMoneyTransfer(int amount, MoneyTransferDirection direction) {
			return withPolishMoneyTransfer(amount, direction, null);
		}

		public AccountBuilder withPolishInternalMoneyTransfer(int amount, MoneyTransferDirection direction, String otherIBANNumber) {
			InternalMoneyTransfer transfer = new InternalMoneyTransfer(amount + "", PLN);
			transfer.setTransferDirection(direction);
			transfer.otherIBANNumber = new IBAN(otherIBANNumber);
			account.addMoneyTransfer(transfer);
			return this;
		}

		public AccountBuilder withPolishWithdrawMoneyTransfer(int amount) {
			WithdrawMoney transfer = new WithdrawMoney();
			transfer.setAmount(new Money(toBD(amount), PLN));
			account.addMoneyTransfer(transfer);
			return this;
		}

		public AccountBuilder withPolishPayInMoneyTransfer(int amount) {
			PayInMoney transfer = new PayInMoney();
			transfer.setAmount(new Money(toBD(amount), PLN));
			account.addMoneyTransfer(transfer);
			return this;
		}

		AccountBuilder withPolishMoneyTransfer(int amount, MoneyTransferDirection direction, String otherIBANNumber) {
			MoneyTransfer transfer = new MoneyTransfer();
			transfer.setAmount(new Money(toBD(amount), PLN));
			transfer.setTransferDirection(direction);
			transfer.setOtherIban(new IBAN(otherIBANNumber));
			account.addMoneyTransfer(transfer);
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
