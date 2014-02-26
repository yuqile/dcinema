package cn.leo.dcinema.https;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import java.net.HttpURLConnection;
import java.net.URL;
import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicHeader;

import cn.leo.dcinema.util.MD5Util;

public class HttpUtils {
	private static final String TAG = "HttpUtils";

	public static byte[] getBinary(String s, Header aheader[],
			NameValuePair anamevaluepair[]) {
		byte abyte0[] = null;
		HttpResult httpresult = HttpClientHelper.get(s, aheader,
				anamevaluepair, null, 0);
		if (httpresult != null
				&& httpresult.getStatusCode() == HttpResult.HTTP_STATUS_CODE_OK)
			abyte0 = httpresult.getResponse();
		return abyte0;
	}

	public static String getContent(String s, Header aheader[],
			NameValuePair anamevaluepair[]) {
		String s1;
		HttpResult httpresult;
		if (!TextUtils.isEmpty(s)
				&& (s.contains("52itv.cn") || s.contains("hdplay.cn") || s
						.contains("myvst.net"))) {
			Header aheader1[] = new Header[3];
			aheader1[0] = new BasicHeader("User-Mac", "");
			aheader1[1] = new BasicHeader("User-Agent",
					"GGwlPlayer/QQ243944493");
			aheader1[2] = new BasicHeader("User-Key", MD5Util.getMD5String(
					(new StringBuilder("time-"))
							.append(String.valueOf(System.currentTimeMillis())
									.substring(0, 8)).append("/key-52itvlive")
							.toString()).substring(0, 16));
			if (aheader == null) {
				aheader = aheader1;
			} else {
				Header aheader2[] = new Header[aheader.length + aheader1.length];
				System.arraycopy(aheader, 0, aheader2, 0, aheader.length);
				System.arraycopy(aheader1, 0, aheader2, aheader1.length,
						aheader1.length);
				aheader = aheader2;
			}
		}
		s1 = null;
		httpresult = HttpClientHelper.get(s, aheader, anamevaluepair);
		if (httpresult != null && httpresult.getStatusCode() == 200)
			s1 = httpresult.getHtml();
		return s1;
	}

	public static String getResultRedirecUrl(String s, Header aheader[],
			NameValuePair anamevaluepair[]) {
		HttpURLConnection httpurlconnection = null;
		String s1 = null;
		httpurlconnection = null;
		if (!TextUtils.isEmpty(s)) {
			Header aheader1[] = new Header[3];
			aheader1[0] = new BasicHeader("User-Mac", "");
			aheader1[1] = new BasicHeader("User-Agent",
					"GGwlPlayer/QQ243944493");
			aheader1[2] = new BasicHeader("User-Key", MD5Util.getMD5String(
					(new StringBuilder("time-"))
							.append(String.valueOf(System.currentTimeMillis())
									.substring(0, 8)).append("/key-52itvlive")
							.toString()).substring(0, 16));
			if (aheader == null) {
				aheader = aheader1;
			} else {
				Header aheader2[] = new Header[aheader.length + aheader1.length];
				System.arraycopy(aheader, 0, aheader2, 0, aheader.length);
				System.arraycopy(aheader1, 0, aheader2, aheader1.length,
						aheader1.length);
				aheader = aheader2;
			}
		}
		try {
			httpurlconnection = (HttpURLConnection) (new URL(s))
					.openConnection();
			if (aheader == null || aheader.length <= 0) {
				Log.d(TAG,
						((new StringBuilder("返回码: ")).append(httpurlconnection
								.getResponseCode()).toString()));
				s1 = httpurlconnection.getURL().toString();
				Log.d(TAG, "URL:" + s1);
				if (httpurlconnection != null)
					httpurlconnection.disconnect();
			} else {
				for (int j = 0; j < aheader.length; j++) {
					Header header = aheader[j];
					httpurlconnection.setRequestProperty(header.getName(),
							header.getValue());
				}
				Log.d(TAG,
						((new StringBuilder("返回码: ")).append(httpurlconnection
								.getResponseCode()).toString()));
				s1 = httpurlconnection.getURL().toString();
				s = s1;
				if (httpurlconnection != null)
					httpurlconnection.disconnect();
			}
		} catch (Exception e) {
			Log.e(TAG + "getResultRedirecUrl", e.toString());
		}
		return s1;
	}

	public static boolean isEthernetDataEnable(Context context) {
		boolean flag = false;
		try {
			flag = NetWorkHelper.isEthernetDataEnable(context);
		} catch (Exception e) {
			flag = false;
			Log.e(TAG, e.toString());
		}
		return flag;
	}

	public static boolean isNetworkAvailable(Context context) {
		return NetWorkHelper.isNetworkAvailable(context);
	}

	public static boolean isWifiDataEnable(Context context) {
		boolean flag = false;
		try {
			flag = NetWorkHelper.isWifiDataEnable(context);
		} catch (Exception e) {
			flag = false;
			Log.e("httpUtils.isWifiDataEnable()", e.toString());
		}
		return flag;
	}
}
