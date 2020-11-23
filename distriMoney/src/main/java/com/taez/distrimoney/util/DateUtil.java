package com.taez.distrimoney.util;

import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	/**
	 * 현재 시간을 리턴한다.
	 * @return
	 */
	public static long createCurrentTime() {
		Calendar cal = Calendar.getInstance();
		return cal.getTimeInMillis();
	}
	
	public long createAferTime(int afterMin) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, afterMin);
		return cal.getTimeInMillis();
	}

}
