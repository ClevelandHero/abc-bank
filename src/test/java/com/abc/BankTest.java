package com.abc;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.Calendar;
import java.util.Date;

public class BankTest {
    private static final double DOUBLE_DELTA = 1e-15;
    
    private Account spyAccountMockTransaction(Account account, double amount) {
    	AbstractAccount spyAccount = spy((AbstractAccount)account);
    	Transaction trans = mock(Transaction.class);
    	
    	Calendar cal = Calendar.getInstance();
    	cal.add(Calendar.DATE, -1);
    	Date yesterday = cal.getTime();
    	
    	when(spyAccount.createTransaction(amount)).thenReturn(trans);
    	when(trans.getTransactionDate()).thenReturn(yesterday);
    	when(trans.getAmount()).thenReturn(amount);
    	
    	return spyAccount;
    }

    @Test
    public void testCustomerSummary() {
        Bank bank = new Bank();
        Customer john = new Customer("John");
        john.openAccount(new CheckingAccount());
        bank.addCustomer(john);

        assertEquals("Customer Summary\n - John (1 account)", bank.customerSummary());
    }

    @Test
    public void testCheckingAccount() {
        Bank bank = new Bank();
        final double amount = 100;
        Account checkingAccount = spyAccountMockTransaction(new CheckingAccount(), amount);
        Customer bill = new Customer("Bill").openAccount(checkingAccount);
        bank.addCustomer(bill);

        checkingAccount.deposit(amount);

		assertEquals(amount * (1 + CheckingAccount.ANNUAL_INTEREST_RATE / Utils.DAYS_OF_YEAR) - amount,
				bank.totalInterestPaid(), DOUBLE_DELTA);
    }

    @Test
    public void testSavingsAccount() {
        Bank bank = new Bank();
        final double amount = 1500;
        Account savingsAccount = spyAccountMockTransaction(new SavingsAccount(), amount);
        bank.addCustomer(new Customer("Bill").openAccount(savingsAccount));

        savingsAccount.deposit(amount);

		assertEquals(
				1000 * (1 + SavingsAccount.LOW_INTEREST_RATE / Utils.DAYS_OF_YEAR)
						+ (amount - 1000) * (1 + SavingsAccount.HIGH_INTEREST_RATE / Utils.DAYS_OF_YEAR)-amount,
				bank.totalInterestPaid(), DOUBLE_DELTA);
    }

    @Test
    public void testMaxiSavingsAccount() {
        Bank bank = new Bank();
        final double amount = 3000;
        Account checkingAccount = spyAccountMockTransaction(new MaxiSavingsAccount(), amount);
        bank.addCustomer(new Customer("Bill").openAccount(checkingAccount));

        checkingAccount.deposit(amount);

        assertEquals(amount*(1+MaxiSavingsAccount.HIGH_INTEREST_RATE/Utils.DAYS_OF_YEAR)-amount, bank.totalInterestPaid(), DOUBLE_DELTA);
    }

}
