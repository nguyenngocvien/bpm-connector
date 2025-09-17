package com.bpm.core.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

	public static String convertDateToString(Date date, String format) {

		String result = null;

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		result = simpleDateFormat.format(date);

		return result;
	}
}
