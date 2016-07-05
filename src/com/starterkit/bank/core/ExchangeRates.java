package com.starterkit.bank.core;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.HashMap;
import java.util.Map;

public class ExchangeRates {
	
	Map<Currency, Map<Currency, BigDecimal>> currencies;
	
	void addExchangeRate(Currency from, Currency to, BigDecimal exchangeRate) {
		HashMap<Currency, BigDecimal> hashMap = new HashMap<>();
		hashMap.put(to, exchangeRate);
		currencies.put(from, hashMap);
	}
	
	BigDecimal getExchangeRate(Currency from, Currency to) {
		return currencies.get(from).get(to);
	}

}
