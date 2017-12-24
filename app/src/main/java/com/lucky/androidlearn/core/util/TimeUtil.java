package com.lucky.androidlearn.core.util;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@SuppressLint("SimpleDateFormat")
public class TimeUtil {

	public static String[] WEEK = { "周日", "周一", "周二", "周三", "周四", "周五", "周六" };

	public static final long weekTime = 7 * 24 * 60 * 60 * 1000l;
	public static final long oneDayTime = 1 * 24 * 60 * 60 * 1000l;

	/**
	 * 将毫秒转化为为yyyy年M月dd日
	 * 
	 * @param c
	 * @return
	 */
	public static String FormatTodayString(long c) {
		// final DateFormat format = new SimpleDateFormat("yyyy-MM-dd"); 月份前面有0
		final DateFormat format = new SimpleDateFormat("yyyy年M月dd日");
		return format.format(new Date(c));
	}

	/**
	 * yyyy-MM-dd
	 * 
	 * @param c
	 * @return
	 */
	public static String FormatToday(long c) {
		final DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		return format1.format(new Date(c));
	}

	/**
	 * yyyy-MM-dd
	 * 
	 * @param c
	 * @return
	 */
	public static String FormatTodToday(long c) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date dBefore = new Date();
		Calendar calendar = Calendar.getInstance(); // 得到日历
		try {
			calendar.setTime(new Date(c));// 把当前时间赋给日历
		} catch (Exception e) {
			e.printStackTrace();
		}
		calendar.add(Calendar.DAY_OF_MONTH, 1); // 设置为后一天
		dBefore = calendar.getTime(); // 得到前一天的时间
		return df.format(dBefore);
	}

	/**
	 * yyyy-MM-dd
	 * 
	 * @param SleepYesDate
	 * @return
	 */
	public static String FormatYesToday(long c) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date dBefore = new Date();
		Calendar calendar = Calendar.getInstance(); // 得到日历
		try {
			calendar.setTime(new Date(c));// 把当前时间赋给日历
		} catch (Exception e) {
			e.printStackTrace();
		}
		calendar.add(Calendar.DAY_OF_MONTH, -1); // 设置为前一天
		dBefore = calendar.getTime(); // 得到前一天的时间
		return df.format(dBefore);
	}

	/**
	 * 将格式为yyyy年M月dd日的时间转化为毫秒数
	 * 
	 * @param date
	 * @return
	 */
	public static long FormatDateToLong(String date) {
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(new SimpleDateFormat("yyyy年M月dd日").parse(date));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return c.getTimeInMillis();
	}

	/**
	 * 将格式为yyyy-MM-dd hh:mm的时间转化为毫秒数
	 * 
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static long FormatDateForHourToLong(String date)
			throws ParseException {
		Calendar c = Calendar.getInstance();
		c.setTime(new SimpleDateFormat("yyyy-M-dd hh:mm").parse(date));
		return c.getTimeInMillis();
	}

	public static String FormatDateToString(long c, String formatstr) {
		final DateFormat format1 = new SimpleDateFormat(formatstr);
		return format1.format(new Date(c));
	}

	public static String FormatDateToYear(long c) {
		final DateFormat format1 = new SimpleDateFormat("yyyy-M-dd");
		return format1.format(new Date(c));
	}

	public static long FormatDateToLong(String sdt, String formatstr)
			throws ParseException {
		final DateFormat format1 = new SimpleDateFormat(formatstr);
		return format1.parse(sdt).getTime();
	}

	public static String DataStringToString(String time) {
		Date date = new Date(Long.parseLong(time));
		SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd hh:mm");
		return formater.format(date);
	}

	public static String DataStringToString(long time) {
		Date date = new Date(time);
		SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd hh:mm");
		return formater.format(date);
	}

	/**
	 * 格式化时间MM-dd hh:mm
	 * 
	 * @param time
	 * @return
	 */
	public static String FomartTimeForXX(long time) {
		Date date = new Date(time);
		SimpleDateFormat formater = new SimpleDateFormat("MM-dd hh:mm");
		return formater.format(date);
	}

	/**
	 * 获取年月日数组
	 * 
	 * @param temp
	 *            yyyy年mm月dd日
	 * @return string[]
	 */
	public static String[] getTimeArray(String temp) {
		if (temp.contains("年") && temp.contains("月") && temp.contains("日")) {
			temp = temp.replace("年", "#").replace("月", "#").replace("日", "#");
		}
		if (temp.contains("#")) {
			String[] time = temp.split("#");
			return time;
		}
		return null;
	}

	/**
	 * 获取当前时间的大写时间[三月十二日]
	 * 
	 * @param time
	 * @return
	 */
	public static String getTimeForCapital(long time) {
		String strTime = null;
		try {
			String str = FormatTodayString(time);
			String[] strTimeArr = getTimeArray(str);
			String month = getCapitalTime(Integer.valueOf(strTimeArr[1]));
			String day = getCapitalTime(Integer.valueOf(strTimeArr[2]));
			strTime = month + "月" + day + "日";
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return strTime;
	}

	/**
	 * 根据小写数字时间获取大写数字时间
	 * 
	 * @param time
	 * @return
	 */
	private static String getCapitalTime(Integer time) {
		// TODO Auto-generated method stub
		String timeStr = null;
		switch (time) {
		case 0:
			timeStr = "零";
			break;
		case 1:
			timeStr = "一";
			break;
		case 2:
			timeStr = "二";
			break;
		case 3:
			timeStr = "三";
			break;
		case 4:
			timeStr = "四";
			break;
		case 5:
			timeStr = "五";
			break;
		case 6:
			timeStr = "六";
			break;
		case 7:
			timeStr = "七";
			break;
		case 8:
			timeStr = "八";
			break;
		case 9:
			timeStr = "九";
			break;
		case 10:
			timeStr = "十";
			break;
		case 11:
			timeStr = "十一";
			break;
		case 12:
			timeStr = "十二";
			break;
		case 13:
			timeStr = "十三";
			break;
		case 14:
			timeStr = "十四";
			break;
		case 15:
			timeStr = "十五";
			break;
		case 16:
			timeStr = "十六";
			break;
		case 17:
			timeStr = "十七";
			break;
		case 18:
			timeStr = "十八";
			break;
		case 19:
			timeStr = "十九";
			break;
		case 20:
			timeStr = "二十";
			break;
		case 21:
			timeStr = "二十一";
			break;
		case 22:
			timeStr = "二十二";
			break;
		case 23:
			timeStr = "二十三";
			break;
		case 24:
			timeStr = "二十四";
			break;
		case 25:
			timeStr = "二十五";
			break;
		case 26:
			timeStr = "二十六";
			break;
		case 27:
			timeStr = "二十七";
			break;
		case 28:
			timeStr = "二十八";
			break;
		case 29:
			timeStr = "二十九";
			break;
		case 30:
			timeStr = "三十";
			break;
		case 31:
			timeStr = "三十一";
			break;
		}
		return timeStr;
	}

	public static String getTimeForHourMin(long time) {
		String currentTimeStr = TimeUtil.FormatDateToString(time, "HH:mm");
		return currentTimeStr;
	}

	public static int getTimeForHour(long time) {
		String currentTimeStr = TimeUtil.FormatDateToString(time, "HH");
		String[] str = currentTimeStr.split(":");
		return Integer.valueOf(str[0]);
	}

	/**
	 * 将系统当前时间转化为星期
	 * 
	 * @param c
	 * @return
	 */
	public static String FormatTodayToWeek(long c) {
		Date date = new Date(c);
		return WEEK[date.getDay()];
	}

	/**
	 * 判断时间是否是一周的开始(星期天为一周的开始)
	 * 
	 * @param longTime
	 *            需要判断的时间
	 * @return [true:是;false:不是]
	 */
	public static boolean checkIsStartWeek(long longTime) {
		boolean isStartWeek = false;
		String startTime = null;
		try {
			startTime = FormatTodayToWeek(longTime);
			if (startTime != null && startTime.equals(WEEK[0])) {
				isStartWeek = true;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return isStartWeek;
	}

	/**
	 * 将系统当前时间转化为星期
	 * 
	 * @param c
	 * @return
	 */
	public static String FormatTodTodayToWeek(long c) {
		Date dBefore = new Date();
		Calendar calendar = Calendar.getInstance(); // 得到日历
		try {
			calendar.setTime(new Date(c));// 把当前时间赋给日历
		} catch (Exception e) {
			e.printStackTrace();
		}
		calendar.add(Calendar.DAY_OF_MONTH, 1); // 设置为前一天
		dBefore = calendar.getTime(); // 得到前一天的时间
		// return df.format(dBefore);

		return WEEK[dBefore.getDay()];
	}

	/**
	 * 将系统当前时间转化为星期前一天
	 * 
	 * @param c
	 * @return
	 */
	public static String FormatYesTodayToWeek(long c) {
		// Date date = new Date(c);
		Date dBefore = new Date();
		Calendar calendar = Calendar.getInstance(); // 得到日历
		try {
			calendar.setTime(new Date(c));// 把当前时间赋给日历
		} catch (Exception e) {
			e.printStackTrace();
		}
		calendar.add(Calendar.DAY_OF_MONTH, -1); // 设置为前一天
		dBefore = calendar.getTime(); // 得到前一天的时间
		// return df.format(dBefore);

		return WEEK[dBefore.getDay()];
	}

	/**
	 * 获得当天0点时间
	 * 
	 * @return
	 */
	public static int getTimesmorning() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return (int) (cal.getTimeInMillis() / 1000);
	}

	/**
	 * 判断时间是否在一周以内(以今天为临界点)
	 * 
	 * @param dbTime
	 * @param isContainToday
	 *            是否包含今天 [false:不包含今天,true:包含今天]
	 * @return
	 */
	public static boolean checkTimeContainWeek(long dbTime,
			boolean isContainToday) {
		boolean isPass = true;
		long currentTime = System.currentTimeMillis();
		long timePoor = currentTime - dbTime;
		long timeValue = 0l;
		timeValue = weekTime;
		if (isContainToday
				&& FormatToday(currentTime).equals(FormatToday(dbTime))) {
			// 时间是今天
			return isPass;
		}
		if (!isContainToday) {
			// 如果是不按包含今天的一周数据
			timeValue += oneDayTime;
		}
		if (timePoor < 0) {
			// 时间超过今天
			isPass = false;
		} else if (timePoor > timeValue) {
			isPass = false;
		}
		return isPass;
	}

	/**
	 * 将小时数转化为：mm月dd天hh小时
	 * 
	 * @param totalHour
	 * @return
	 */
	public static String formatTimeToMonth(double totalHour) {
		String strTime = "";
		int month = 30 * 24;
		int day = 24;
		double curentMonth = 0;
		double currentDay = 0;
		double currenthour = 0;
		if (totalHour >= month) {
			curentMonth = totalHour / month;
			double poorTime = totalHour % month;
			if (poorTime >= day) {
				currentDay = poorTime / day;
				currenthour = poorTime % day;
			} else {
				currenthour = poorTime;
			}
			strTime = (int) curentMonth + "月" + (int) currentDay + "天"
					+ (int) currenthour + "小时";
		} else if (totalHour >= day && totalHour < month) {
			String dayStr = "";
			String hourStr = "";
			currentDay = totalHour / day;
			currenthour = totalHour % day;
			dayStr = (int) currentDay + "天";
			hourStr = (int) currenthour + "小时";
			if (currentDay < 10) {
				// dayStr = "0" + (int) currentDay + "天";
			}
			if (currenthour < 10) {
				// hourStr = "0" + (int) currenthour + "小时";
			}
			strTime = dayStr + hourStr;
		} else {
			currenthour = totalHour;
			strTime = (int) currenthour + "小时";
		}
		return strTime;
	}
	
	public static String getDateAndTime() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(
				System.currentTimeMillis()));
	}
}
