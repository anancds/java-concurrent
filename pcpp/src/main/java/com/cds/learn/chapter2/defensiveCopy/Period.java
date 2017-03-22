package com.cds.learn.chapter2.defensiveCopy;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public final class Period {
	private final Date start;
	private final Date end;

	private Period(final Date start, final Date end) {
		if (start.compareTo(end) > 0) {
			throw new IllegalArgumentException(start + " after " + end);
		}
		this.start = start;
		this.end = end;
		//        this.start = new Date(start.getTime());
		//        this.end = new Date(end.getTime());
		if (this.start.compareTo(this.end) > 0) {
			throw new IllegalArgumentException(this.start + " after " + this.end);
		}
	}

	public static void main(String[] args) {
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");

		Calendar calendar = Calendar.getInstance();

		calendar.setTime(new Date());
		System.out.println(calendar.getTime());
		Date start = new Date();
		Date end = new Date();
		Period period = new Period(start, end);

		calendar.set(Calendar.YEAR, 2);
		end.setTime(calendar.getTimeInMillis());

		System.out.println(period.end());
	}

	public Date start() {
		return start;
	}
	//remainder omitted

	private Date end() {
		return end;
	}
}
