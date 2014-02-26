package cn.leo.dcinema.https;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class NetWorkHelper {
	private static String LOG_TAG = "NetWorkHelper";

	public static boolean isEthernetDataEnable(Context paramContext)
			throws Exception {
		return ((ConnectivityManager) paramContext
				.getSystemService("connectivity")).getNetworkInfo(9)
				.isConnectedOrConnecting();
	}

	public static boolean isNetworkAvailable(Context paramContext) {
		ConnectivityManager localConnectivityManager = (ConnectivityManager) paramContext
				.getSystemService("connectivity");
		boolean bool = false;
		if (localConnectivityManager == null) {
			Log.w(LOG_TAG, "couldn't get connectivity manager");
			return bool;
		}
		while (true) {
			Log.d(LOG_TAG, "network is not available");
			NetworkInfo[] arrayOfNetworkInfo = localConnectivityManager
					.getAllNetworkInfo();
			if (arrayOfNetworkInfo != null) {
				for (int i = 0;; i++) {
					if (i >= arrayOfNetworkInfo.length)
						return bool;
					if ((arrayOfNetworkInfo[i].isAvailable())
							&& (arrayOfNetworkInfo[i].isConnected())) {
						Log.d(LOG_TAG, "network is available");
						bool = true;
						return bool;
					}
				}
			}else{
				Log.d(LOG_TAG,"arrayOfNetworkInfo is null");
			}
		}
	}

	public static boolean isWifiDataEnable(Context paramContext)
			throws Exception {
		return ((ConnectivityManager) paramContext
				.getSystemService("connectivity")).getNetworkInfo(1)
				.isConnectedOrConnecting();
	}
}
