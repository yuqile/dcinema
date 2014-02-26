package cn.leo.dcinema.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import android.util.Log;

public class ConfigUtil {
	public static final String VER = "VER";
	private static Properties props = new Properties();

	public static String getValue(String s) {
		return props.getProperty(s);
	}

	public static void updateProperties(String s, String s1) {
		props.setProperty(s, s1);
	}

	static {
		props = new Properties();
		try {
			props.load(Thread.currentThread().getContextClassLoader()
					.getResourceAsStream("config.properties"));
		} catch (FileNotFoundException e) {
			Log.e(VER, e.toString());
		} catch (IOException e) {
			// TODO: handle exception
			Log.e(VER, e.toString());
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(VER, e.toString());
		}

	}
}
