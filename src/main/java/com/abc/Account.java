package com.abc;

import java.util.List;

public interface Account {
	
	public List<Transaction> getTransactions();
	
	public int getAccountNumber();
	
	public void deposit(final double amount);
	
	public void withdraw(final double amount);
	
    public double interestEarned();

    public double sumTransactions();

    public String getAccountType();
    
    public double getDailyInterestRate();
    
    public void transfer(final Account toAccount, final double amount);

}
