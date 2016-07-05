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

	@Test
	public void shouldGetEmptyBalance() {
		Account account = new Account();
		Money balance = account.getBalance();
		assertEquals(balance.amount, new BigDecimal("0"));
		// TASK 0/1 - Is this above a correct way of triggering the assertion? Try to make it fail and see in the console output.
	}

	@Test
	public void test_Balance_With_Non_Empty_Money_Transfer() {
		// TASK 0/2 - the method name does not comply with the convention (proposed is the should instead of test and CamelCase)
		Account account = new Account();
		MoneyTransfer transfer = new MoneyTransfer();
		transfer.amount = new Money(new BigDecimal("10"), Currency.getInstance("PLN"));
		account.moneyTransferHistory.add(transfer);
		Money balance = account.getBalance();
		assertEquals(balance.amount, new BigDecimal("10"));
		assertEquals(balance.currency, Currency.getInstance("PLN"));
		// TASK 0/3 - There is a special convention in which the tests are being structured:
		
		// given
		// when
		// then
		
		// use it here
	}
	

	@Test
	public void test_Balance_With_Non_Empty_Money_Transfer2() {
		// TASK 0/4 - the method name to be repaired here too
		Account account = new Account();
		MoneyTransfer transfer = new MoneyTransfer();
		transfer.amount = new Money(new BigDecimal("10"), Currency.getInstance("PLN"));
		account.moneyTransferHistory.add(transfer);
		transfer.amount = new Money(new BigDecimal("10"), Currency.getInstance("PLN"));
		account.moneyTransferHistory.add(transfer);
		Money balance = account.getBalance();
		assertEquals(balance.amount, new BigDecimal("20"));
		assertEquals(balance.currency, Currency.getInstance("PLN"));
		// TASK 0/5 - try to change the amounts in one of the 2 money transfers, see what happens
	}

	@Test
	public void test_Balance_With_Non_Empty_Money_Transfer3() {
		Account account = new Account();
		MoneyTransfer transfer = new MoneyTransfer();
		transfer.amount = new Money(new BigDecimal("10"), Currency.getInstance("PLN"));
		account.moneyTransferHistory.add(transfer);
		InternalMoneyTransfer transfer2 = new InternalMoneyTransfer();
		transfer2.amount = new Money(new BigDecimal("13"), Currency.getInstance("PLN"));
		account.internalTransferHistory.add(transfer2);
		Money balance = account.getBalance();
		assertEquals(balance.amount, new BigDecimal("23"));
		assertEquals(balance.currency, Currency.getInstance("PLN"));
	}
	

	@Test
	public void test_Balance_With_Non_Empty_Money_Transfer4() {
		Account account = new Account();
		MoneyTransfer transfer = new MoneyTransfer();
		transfer.amount = new Money(new BigDecimal("1"), Currency.getInstance("PLN"));
		account.moneyTransferHistory.add(transfer);
		InternalMoneyTransfer transfer2 = new InternalMoneyTransfer();
		transfer2.amount = new Money(new BigDecimal("2"), Currency.getInstance("PLN"));
		account.internalTransferHistory.add(transfer2);
		PayInMoney transfer3 = new PayInMoney();
		transfer3.money = new Money(new BigDecimal("4"), Currency.getInstance("PLN"));
		account.payTransferHistory.add(transfer3);
		WithdrawMoney transfer4 = new WithdrawMoney();
		transfer4.money = new Money(new BigDecimal("7"), Currency.getInstance("PLN"));
		account.withdrawTransferHistory.add(transfer4);
		// TASK 0/6 - What a mess, try to introduce the Builder pattern to repair the instantiation of the preconditions 
		Money balance = account.getBalance();
		assertEquals(new BigDecimal("0"), balance.amount);
		assertEquals(balance.currency, Currency.getInstance("PLN"));
	}
	

	@Test
	public void test_Balance_With_Incomming_Outgoing_Transfers() {
		Account account = new Account();
		MoneyTransfer transfer = new MoneyTransfer();
		transfer.amount = new Money(new BigDecimal("111"), Currency.getInstance("PLN"));
		transfer.transferDirection = Direction.TO_ACCOUNT;
		account.moneyTransferHistory.add(transfer);
		// TASK 0/7 - would it also make sense to check the balance here too? but then the unit test would not comply with the given/when/then standard
		MoneyTransfer transfer2 = new MoneyTransfer();
		transfer2.amount = new Money(new BigDecimal("111"), Currency.getInstance("PLN"));
		transfer2.transferDirection = Direction.FROM_ACCOUNT;
		account.moneyTransferHistory.add(transfer2);
		Money balance = account.getBalance();
		assertEquals(new BigDecimal("0"), balance.amount);
		assertEquals(balance.currency, Currency.getInstance("PLN"));
	}

	@Test
	public void testGetHistory() {
		Account account = new Account(new IBAN("asdf"));
		MoneyTransfer transfer = new MoneyTransfer();
		transfer.amount = new Money(new BigDecimal("10"), Currency.getInstance("PLN"));
		transfer.number = new IBAN("1111");
		transfer.transferDirection = Direction.TO_ACCOUNT;
		transfer.account = account;
		account.moneyTransferHistory.add(transfer);
		InternalMoneyTransfer transfer2 = new InternalMoneyTransfer();
		transfer2.amount = new Money(new BigDecimal("14"), Currency.getInstance("PLN"));
		transfer2.number = new IBAN("2222");
		transfer2.transferDirection = Direction.FROM_ACCOUNT;
		transfer2.account = account;
		account.internalTransferHistory.add(transfer2);
		PayInMoney transfer3 = new PayInMoney();
		transfer3.money = new Money(new BigDecimal("4"), Currency.getInstance("PLN"));
		transfer3.account = account;
		account.payTransferHistory.add(transfer3);
		// TASK 0/9 - the 2 above rows do basically a similar thing, but for the 2 different object perspectives. Placing a relation from Account to Transfer and from Transfer to account. It is better to do it in one place, as one can easy forget it, or corrupt by Copy Paste
		WithdrawMoney transfer4 = new WithdrawMoney();
		transfer4.money = new Money(new BigDecimal("7"), Currency.getInstance("PLN"));
		transfer4.account = account;
		account.withdrawTransferHistory.add(transfer4);
		List<String> history= account.getHistory();
		assertEquals("FROM> 1111 TO> asdf AMOUNT> 10PLN", history.get(0));
		assertEquals("FROM> asdf TO> 2222 AMOUNT> 14PLN", history.get(1));
		assertEquals("PAID IN TO> asdf AMOUNT> 4PLN", history.get(2));
		assertEquals("FROM> asdf WITHDRAWN AMOUNT> 7PLN", history.get(3));
	}

	@Test
	public void testGetAverageTransaction() {
		Account account = new Account(new IBAN("asdf"));
		MoneyTransfer transfer = new MoneyTransfer();
		transfer.amount = new Money(new BigDecimal("10"), Currency.getInstance("PLN"));
		transfer.number = new IBAN("1111");
		transfer.transferDirection = Direction.TO_ACCOUNT;
		account.moneyTransferHistory.add(transfer);
		InternalMoneyTransfer transfer2 = new InternalMoneyTransfer();
		transfer2.amount = new Money(new BigDecimal("14"), Currency.getInstance("PLN"));
		transfer2.number = new IBAN("2222");
		transfer2.transferDirection = Direction.FROM_ACCOUNT;
		account.internalTransferHistory.add(transfer2);
		Money average = account.getAverageTransaction();
		assertEquals(new BigDecimal("12"), average.amount);
	}

	@Test
	@Ignore
	public void testGetAverageTransaction2() {
		// TASK 0/8 - This test does not work - something is no-yes
		Account account = new Account(new IBAN("asdf"));
		MoneyTransfer transfer = new MoneyTransfer();
		transfer.amount = new Money(new BigDecimal("10"), Currency.getInstance("PLN"));
		transfer.number = new IBAN("asdf");
		account.moneyTransferHistory.add(transfer);
		WithdrawMoney transfer4 = new WithdrawMoney();
		transfer4.money = new Money(new BigDecimal("6"), Currency.getInstance("PLN"));
		transfer4.account = account;
		account.withdrawTransferHistory.add(transfer4);
		Money average = account.getAverageTransaction();
		assertEquals(new BigDecimal("2"), average.amount);
	}

}
