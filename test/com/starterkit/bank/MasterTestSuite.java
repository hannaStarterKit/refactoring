package com.starterkit.bank;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.starterkit.bank.core.AccountTests;
import com.starterkit.bank.core.BankTests;

@RunWith(Suite.class)
@Suite.SuiteClasses({ AccountTests.class, BankTests.class })
public class MasterTestSuite {
}