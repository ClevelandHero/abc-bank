package com.abc;

public class CheckingAccount extends AbstractAccount {
	
	static final double ANNUAL_INTEREST_RATE = 0.001;

	@Override
	public double interestEarned() {
		return compoundInterestEarned(getDailyInterestRate());
	}

	@Override
	public String getAccountType() {
		return "Checking Account\n";
	}

	@Override
	public double getDailyInterestRate() {
		return ANNUAL_INTEREST_RATE/Utils.DAYS_OF_YEAR;
	}

}
