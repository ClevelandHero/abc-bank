package com.abc;

import java.util.Date;
import java.util.List;
import java.util.ListIterator;

public class MaxiSavingsAccount extends AbstractAccount {
	
	static final double HIGH_INTEREST_RATE = 0.05;
	static final double LOW_INTEREST_RATE = 0.001;

	@Override
	public double interestEarned() {
		return compoundInterestEarned(getDailyInterestRate());
	}

	@Override
	public String getAccountType() {
		return "Maxi Savings Account\n";
	}
	
	@Override
	public double getDailyInterestRate() {
		if (withdrawInPastTenDays()) {
			return LOW_INTEREST_RATE/Utils.DAYS_OF_YEAR;
		} else {
			return HIGH_INTEREST_RATE/Utils.DAYS_OF_YEAR;
		}
	}
	
	private boolean withdrawInPastTenDays() {
		List<Transaction> trans = getTransactions();		
		if (trans.isEmpty()) return false;
		
		ListIterator<Transaction> iter = trans.listIterator(trans.size());
		Date today = DateProvider.getInstance().now();
		while (iter.hasPrevious()) {
			Transaction tran = iter.previous();
			if (Utils.daysBetween(today,tran.getTransactionDate())>10) {
				return false;
			}
			if (tran.getAmount()<0) {
				return true;
			}
		}
		return false;
	}

}
