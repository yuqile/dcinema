package cn.leo.dcinema.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnKeyListener;
import android.util.Log;
import android.view.KeyEvent;

public class UtilDate {
	private static final String TAG = "UtilDate";

	/**
	 * 测试版本有效时间控制
	 */
	public static void setPeriodOfValidityTo(final Activity activity, int year,
			int month, int day) {
		// 测试版本限制
		Log.d(TAG,
				"isPeriodOfValidityTo="
						+ isPeriodOfValidityTo(year, month, day));
		if (!isPeriodOfValidityTo(year, month, day)) {
			AlertDialog.Builder builder = new Builder(activity);
			builder.setMessage("Test version has expired!");
			builder.setPositiveButton("Confirm", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					activity.finish();
				}
			});
			AlertDialog dialog = builder.create();
			dialog.setCancelable(false);
			dialog.setOnKeyListener(new OnKeyListener() {
				@Override
				public boolean onKey(DialogInterface dialog, int keyCode,
						KeyEvent event) {
					if (keyCode == KeyEvent.KEYCODE_SEARCH) {
						return true;
					}
					return false;
				}
			});
		}
	}

	/**
	 * 判断当前的时间跟传入的时间的大小
	 */
	public static boolean isPeriodOfValidityTo(int year, int month, int day) {
		Calendar c = Calendar.getInstance();
		int currentYear = c.get(Calendar.YEAR);
		int currentMonth = c.get(Calendar.MONTH) + 1;
		int currentDay = c.get(Calendar.DATE);

		// Log.d(TAG, "year=" + currentYear + "|month=" + currentMonth +
		// "|currentDay=" + currentDay);

		if (currentYear < year) {
			return true;
		} else if (currentYear > year) {
			return false;
		} else if (currentMonth < month) {
			return true;
		} else if (currentMonth > month) {
			return false;
		} else if (currentDay < day) {
			return true;
		} else if (currentDay > day) {
			return false;
		}
		return true;
	}

	private static final long DAY_TIME_MILLIS = 24 * 3600 * 1000L;

	/**
	 * 时间字符串操作： 当时间为个位数时，在前面补0 比如：时间为5:1 -- > 05:01: 5 -- >05 , 1 -- >01
	 */
	public static String fillZeroForTime(int time) {
		String strReturn = time + "";
		if (time / 10 == 0) {
			strReturn = "0" + time;
		}
		return strReturn;
	}

	/**
	 * 时间字符串操作： 根据长整型时间戳，获取时间 如果时间为当天的时间，返回小时：分钟 ,否则，返回日期
	 */
	@SuppressWarnings("deprecation")
	public static String getTimeByTimeMilli(long timeMilli) {
		String time = "";
		Date date = new Date(timeMilli);
		long currentTimeMilli = System.currentTimeMillis();

		if (currentTimeMilli / DAY_TIME_MILLIS == timeMilli / DAY_TIME_MILLIS) {
			time = fillZeroForTime(date.getHours()) + " : "
					+ fillZeroForTime(date.getMinutes());
		} else {
			time = date.getMonth() + 1 + "��" + fillZeroForTime(date.getDate())
					+ "��";
		}
		return time;
	}

	/**
	 * 时间字符串操作：根据传入的时间秒数，计算持续时间 时间格式为：超过1小时，显示为hour：minute：second
	 * 不超过，显示为minute：second
	 */
	public static String getDurationTime(int durationTime) {
		String time = "";
		int minute = durationTime % 3600 / 60;
		int second = durationTime % 60;
		if (durationTime < 3600) {
			time = fillZeroForTime(minute) + " : " + fillZeroForTime(second);
		} else {
			int hour = durationTime / 3600;
			time = fillZeroForTime(hour) + " : " + fillZeroForTime(minute)
					+ " : " + fillZeroForTime(second);
		}
		return time;
	}

	/**
	 * 时间字符串操作： 根据长整型时间戳，获取完整日期 日期格式为：yyyy年MM月dd日 HH:mm
	 */
	public static String getFulltDateByTimeMilli(long timeMilli) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm",
				Locale.getDefault());
		String fullDate = sdf.format(new Date(timeMilli));
		return fullDate;
	}

	/**
	 * 时间字符串操作： 根据长整型时间戳，获取完整日期 日期格式为：yyyy年MM月dd日
	 */
	public static String getDateByTimeMilli(long timeMilli) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",
				Locale.getDefault());
		String fullDate = sdf.format(new Date(timeMilli));
		return fullDate;
	}

	/**
	 * 时间字符串操作： 根据长整型时间戳，获取日期 日期格式由传入的SimpleDateFormat决定
	 */
	public static String getDate(long timeMilli, SimpleDateFormat sdf) {
		String date = sdf.format(new Date(timeMilli));
		return date;
	}

	/**
	 * 时间字符串操作： 根据长整型时间戳，获取日期 日期格式由传入的pattern决定
	 */
	public static String getDate(long timeMilli, String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern,
				Locale.getDefault());
		String date = sdf.format(new Date(timeMilli));
		return date;
	}

}
