package com.abc;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class Utils {
	
	public static final int DAYS_OF_YEAR = 365;
	
	private static final AtomicInteger ACCOUNT_COUNTER = new AtomicInteger(0);
	
	private Utils(){}

	public static int daysBetween(Date d1, Date d2) {
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(d1);
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(d2);
		
		int year1 = cal1.get(Calendar.YEAR), year2 = cal2.get(Calendar.YEAR);
		int day1 = cal1.get(Calendar.DAY_OF_YEAR), day2 = cal2.get(Calendar.DAY_OF_YEAR);
		if (year1 == year2) {
			return day1>day2 ? day1-day2 : day2-day1;
		} else {
			if (year1 < year2) {
				// swap them
				int temp = year1;
				year1 = year2;
				year2 = temp;
			}
			
			int days = 0;
			while(year1 > year2) {
				cal1.add(Calendar.YEAR, -1);
				days += cal1.getActualMaximum(Calendar.DAY_OF_YEAR);
				year1 = cal1.get(Calendar.YEAR);
			}
			return days - day2 + cal1.get(Calendar.DAY_OF_YEAR);
		}
	}
	
	public static boolean isSameDay(Date d1, Date d2) {
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(d1);
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(d2);
		return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
				&& cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
	}
	
	public static boolean isOwnAccount(Customer customer, Account account) {
		for (Account acct : customer.getAccounts()) {
			if (acct == account) {
				return true;
			}
		}
		return false;
	}
	
	public static int generateAccountNumber() {
		return ACCOUNT_COUNTER.incrementAndGet();
	}
	
}
