package cn.leo.dcinema.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import java.io.IOException;

public class BoxInfoFetcher {
	private static final String TAG = "BoxInfoFetcher";
	private Context context;

	public BoxInfoFetcher(Context context) {
		this.context = context;
	}

	public String fetchBoxModel() {
		return "盒子型号：" + Build.MODEL;
	}

	private String fetchVersionAndroid() {
		return "安卓版本：Android " + Build.VERSION.RELEASE;
	}

	private String fetchVersionCore() {
		Object localObject = null;
		CMDExecute localCMDExecute = new CMDExecute();
		try {
			String[] arrayOfString = new String[2];
			arrayOfString[0] = "/system/bin/cat";
			arrayOfString[1] = "/proc/version";
			localObject = localCMDExecute.run(arrayOfString, "system/bin/");
			String str = ((String) localObject).substring(0,
					((String) localObject).indexOf("\n"));
			localObject = str;
			return "Linux内核：" + (String) localObject;
		} catch (IOException e) {
			Log.d(TAG, e.toString());
			return null;
		}

	}

	private String fetch_apk_version() {
		String str = null;
		PackageManager localPackageManager = this.context.getPackageManager();
		try {
			PackageInfo localPackageInfo = localPackageManager.getPackageInfo(
					this.context.getPackageName(), 0);
			str = localPackageInfo.versionName;

		} catch (PackageManager.NameNotFoundException localNameNotFoundException) {
			localNameNotFoundException.printStackTrace();
		}
		return "VST版本：" + str;
	}

	private String fetch_cpu_info() {
		Object localObject = null;
		CMDExecute localCMDExecute = new CMDExecute();
		try {
			String[] arrayOfString = new String[2];
			arrayOfString[0] = "/system/bin/cat";
			arrayOfString[1] = "/proc/cpuinfo";
			localObject = localCMDExecute.run(arrayOfString, "/system/bin/");
			String str = ((String) localObject).substring(0,
					((String) localObject).lastIndexOf("\n"));
			localObject = str;
			return "CPU信息：" + (String) localObject;
		} catch (IOException localIOException) {
			while (true)
				localIOException.printStackTrace();
		}
	}


	private String fetch_mac_eth() {
		Object localObject = null;
		CMDExecute localCMDExecute = new CMDExecute();
		try {
			String[] arrayOfString = new String[2];
			arrayOfString[0] = "/system/bin/cat";
			arrayOfString[1] = "/sys/class/net/eth0/address";
			localObject = localCMDExecute.run(arrayOfString, "system/bin/");
			localObject = ((String) localObject).substring(0,
					((String) localObject).indexOf("\n"));
			if (((String) localObject).length() > 27) {
				String str = ((String) localObject).substring(0, 27);
				localObject = str;
			}
			return "有线MAC：" + (String) localObject;
		} catch (IOException localIOException) {
			while (true)
				localIOException.printStackTrace();
		}
	}

	private String fetch_mac_wlan() {
		Object localObject = null;
		CMDExecute localCMDExecute = new CMDExecute();
		try {
			String[] arrayOfString = new String[2];
			arrayOfString[0] = "/system/bin/cat";
			arrayOfString[1] = "/sys/class/net/wlan0/address";
			localObject = localCMDExecute.run(arrayOfString, "system/bin/");
			localObject = ((String) localObject).substring(0,
					((String) localObject).indexOf("\n"));
			if (((String) localObject).length() > 28) {
				String str = ((String) localObject).substring(0, 28);
				localObject = str;
			}
			return "无线MAC：" + (String) localObject;
		} catch (IOException localIOException) {
			while (true)
				localIOException.printStackTrace();
		}
	}

	public String fetchBoxInfo() {
		return fetch_mac_eth() + "\n" + fetch_mac_wlan() + "|"
				+ fetchVersionAndroid() + "|" + fetchBoxModel() + "|"
				+ fetch_cpu_info() + "\n" + fetchVersionCore() + "\n"
				+ fetch_apk_version();
	}

	public String fetch_mac_eth1() {
		String localObject = null;
		CMDExecute localCMDExecute = new CMDExecute();
		try {
			String[] arrayOfString = new String[2];
			arrayOfString[0] = "/system/bin/cat";
			arrayOfString[1] = "/sys/class/net/eth0/address";
			localObject = localCMDExecute.run(arrayOfString, "system/bin/");
			localObject = ((String) localObject).substring(0,
					((String) localObject).indexOf("\n"));
			if (((String) localObject).length() > 27) {
				String str = ((String) localObject).substring(0, 27);
				localObject = str;
			}

		} catch (IOException localIOException) {
			localIOException.printStackTrace();
		}
		return localObject;
	}
}