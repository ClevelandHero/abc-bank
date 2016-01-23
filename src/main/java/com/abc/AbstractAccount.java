package com.abc;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class AbstractAccount implements Account {

    private final List<Transaction> transactions;
    private final int accountNumber;

    public AbstractAccount() {
        this.transactions = new CopyOnWriteArrayList<Transaction>();
        accountNumber = Utils.generateAccountNumber();
    }
    
    public List<Transaction> getTransactions() {
    	return transactions;
    }
    
    public int getAccountNumber() {
    	return accountNumber;
    }

    public void deposit(final double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("amount must be greater than zero");
        } else {
            transactions.add(createTransaction(amount));
        }
    }

	public void withdraw(final double amount) {
	    if (amount <= 0) {
	        throw new IllegalArgumentException("amount must be greater than zero");
	    } else {
	    	double currentBalance = sumTransactions();
	    	if (amount > currentBalance) {
	    		throw new IllegalArgumentException("cannot withdraw more than current balance: " + currentBalance);
	    	}
	        transactions.add(createTransaction(-amount));
	    }
	}

    public abstract double interestEarned();

    public double sumTransactions() {
       return checkIfTransactionsExist(true);
    }

    private double checkIfTransactionsExist(boolean checkAll) {
        double amount = 0.0;
        for (Transaction t: transactions)
            amount += t.getAmount();
        return amount;
    }

    public abstract String getAccountType();
    
    public double getDailyInterestRate() {
    	// by default no interest in this economy 
    	return 0;
    }
    
    protected double compoundInterestEarned(final double dailyRate) {
    	if (transactions.isEmpty()) return 0;
    	
    	Date today = DateProvider.getInstance().now();
    	Date currentDate = transactions.get(0).getTransactionDate();
    	
    	double balanceWithInterest = 0, balance = 0;
    	for (Transaction t : transactions) {
    		balance += t.getAmount();
    		if (Utils.isSameDay(currentDate, t.getTransactionDate())) {
    			balanceWithInterest += t.getAmount();
    		} else {
    			int days = Utils.daysBetween(t.getTransactionDate(), currentDate);
    			// balance with compound interest upto currentDate
    			balanceWithInterest *= Math.pow(1+dailyRate, days);
    			// add transactions on currentDate
    			balanceWithInterest += t.getAmount();
    			currentDate = t.getTransactionDate();
    		}
    	}

    	// balance with compound interest from last transaction day to today
    	balanceWithInterest *= Math.pow(1+dailyRate, Utils.daysBetween(today, currentDate));
    	
    	return balanceWithInterest - balance;
    }
    
    public void transfer(final Account toAccount, final double amount) {
    	if (this == toAccount) {
    		throw new IllegalArgumentException("cannot transfer within the same account");
    	}
    	Account first, second;
    	if (getAccountNumber() < toAccount.getAccountNumber()) {
    		first = this;
    		second = toAccount;
    	} else {
    		first = toAccount;
    		second = this;
    	}
    	// always acquire lock of account with smaller
    	// account number to avoid deadlock
    	synchronized(first) {
    		synchronized(second) {
    			withdraw(amount);
    			toAccount.deposit(amount);
    		}
    	}
    }
    
    // easy to mock for unit test
    Transaction createTransaction(double amount) {
    	return new Transaction(amount);
    }

}
