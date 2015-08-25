package com.zhongcw.java.base.calendar;

import javax.swing.JOptionPane;

public class MyCalendar {
	private int year;
	private int month;

	public MyCalendar() {
		String yearString = JOptionPane.showInputDialog("Enter fuul year:");
		this.year = Integer.parseInt(yearString);

		String monthString = JOptionPane
				.showInputDialog("Enter month n number between 1 and 12:");
		this.month = Integer.parseInt(monthString);

		PrintMonth(year, month);
	}

	private void PrintMonth(int year, int month) {
		PrintMonthTitle(year, month);

		PrintMonthBody(year, month);
	}

	private void PrintMonthTitle(int year, int month) {
		System.out.println(" " + getMonthName(month) + " " + year);
		System.out.println("----------------------------");
		System.out.println(" Sun Mon Tue Wed Thu Fri Sat");
	}

	static String getMonthName(int month) {
		String monthName = null;
		switch (month) {
		case 1:
			monthName = "January";
			break;
		case 2:
			monthName = "February";
			break;
		case 3:
			monthName = "March";
			break;
		case 4:
			monthName = "April";
			break;
		case 5:
			monthName = "May";
			break;
		case 6:
			monthName = "June";
			break;
		case 7:
			monthName = "July";
			break;
		case 8:
			monthName = "August";
			break;
		case 9:
			monthName = "September";
			break;
		case 10:
			monthName = "October";
			break;
		case 11:
			monthName = "November";
			break;
		case 12:
			monthName = "December";
		}
		return monthName;
	}

	private void PrintMonthBody(int year, int month) {
		int startDay = getStartDay(year, month);

		int numberOfDaysInMonth = getNumberOfDaysInMonth(year, month);

		int i = 0;
		for (i = 0; i < startDay; i++){
			System.out.print("    ");
		}
		for (i = 1; i <= numberOfDaysInMonth; i++) {
			if (i < 10){
				System.out.print("   " + i);
			}else{
				System.out.print("  " + i);
			}
			
			if ((i + startDay) % 7 == 0){
				System.out.println(); //7 day
			}
			
		}
		System.out.println();
	}

	static int getStartDay(int year, int month) {
		int startDay1800 = 1;// 这里假设1800年1月1日是星期一
		int totalNumberOfDays = getTotalNumberOfDays(year, month);

		return (totalNumberOfDays + startDay1800) % 7;
	}

	static int getTotalNumberOfDays(int year, int month) {
		int total = 0;
		for (int i = 1800; i < year; i++)
			if (isLeapYear(i))
				total += 366;
			else
				total += 365;

		for (int i = 1; i < month; i++)
			total += getNumberOfDaysInMonth(year, i);

		return total;
	}

	static int getNumberOfDaysInMonth(int year, int month) {
		switch (month) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			return 31;
		case 4:
		case 6:
		case 9:
		case 11:
			return 30;
		case 2:
			return isLeapYear(year) ? 29 : 28;
		default:
			return 0;

		}
	}

	static boolean isLeapYear(int year) {
		return year % 400 == 0 || (year % 4 == 0 && year % 400 != 0);
	}

	public static void main(String[] args) {
		MyCalendar myCalendar = new MyCalendar();
	}

}
