package com.abc;

import java.util.Date;
import java.util.List;

public class SavingsAccount extends AbstractAccount {

	static final double HIGH_INTEREST_RATE = 0.002;
	static final double LOW_INTEREST_RATE = 0.001;
	
	@Override
	public double interestEarned() {
		List<Transaction> trans = getTransactions();		
		if (trans.isEmpty()) return 0;
		
		Date today = DateProvider .getInstance().now();
		Date currentDate = trans.get(0).getTransactionDate();
		
		final double lowDailyRate = LOW_INTEREST_RATE/Utils.DAYS_OF_YEAR;
		final double highDailyRate = HIGH_INTEREST_RATE/Utils.DAYS_OF_YEAR;
		
		double balanceWithInterest = 0, balance = 0;
		for (Transaction t : trans) {
    		balance += t.getAmount();
    		if (Utils.isSameDay(currentDate, t.getTransactionDate())) {
    			balanceWithInterest += t.getAmount();
    		} else {
    			int days = Utils.daysBetween(t.getTransactionDate(), currentDate);
    			// balance with compound interest upto currentDate
    			if (balanceWithInterest <= 1000) {
	    			balanceWithInterest *= Math.pow(1+lowDailyRate, days);
    			} else {
					balanceWithInterest = 1000 * Math.pow(1 + lowDailyRate, days)
							+ (balanceWithInterest - 1000) * Math.pow(1 + highDailyRate, days);
    			}
    			// add transactions on currentDate
    			balanceWithInterest += t.getAmount();
    			currentDate = t.getTransactionDate();
    		}
    	}
		
		int days = Utils.daysBetween(today, currentDate);
    	// balance with compound interest from last transaction day to today
		if (balanceWithInterest <= 1000) {
			balanceWithInterest *= Math.pow(1+lowDailyRate, days);
		} else {
			balanceWithInterest = 1000 * Math.pow(1 + lowDailyRate, days)
					+ (balanceWithInterest - 1000) * Math.pow(1 + highDailyRate, days);
		}
    	
    	return balanceWithInterest - balance;

	}

	@Override
	public String getAccountType() {
		return "Savings Account\n";
	}

}
