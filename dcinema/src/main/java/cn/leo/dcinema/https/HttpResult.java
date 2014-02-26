package cn.leo.dcinema.https;

import android.text.TextUtils;
import android.util.Log;
import java.util.Arrays;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.apache.http.util.EntityUtils;

public class HttpResult {
	private static final String TAG = "HttpResult";
	private Cookie[] cookies;
	private Header[] headers;
	private byte[] response;
	private int statusCode = -1;
	public static final int HTTP_STATUS_CODE_OK = 200;

	public HttpResult(HttpResponse httpresponse) {
		statusCode = -1;
		new HttpResult(httpresponse, null);
	}

	public HttpResult(HttpResponse httpresponse, CookieStore cookiestore) {
		statusCode = -1;
		try {
			if (cookiestore != null)
				cookies = (Cookie[]) cookiestore.getCookies().toArray(
						new Cookie[0]);
			if (httpresponse != null) {
				headers = httpresponse.getAllHeaders();
				statusCode = httpresponse.getStatusLine().getStatusCode();
				Log.d(TAG, new StringBuffer().append(statusCode).toString());
				response = EntityUtils.toByteArray(httpresponse.getEntity());
			}
		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}
	}

	public Cookie getCookie(String s) {
		Cookie cookie = null;
		if (cookies != null && cookies.length != 0) {
			for (int i = 0; i < cookies.length; i++) {
				cookie = cookies[i];
				if (cookie.getName().equalsIgnoreCase(s)) {
					return cookie;
				}
			}
		}
		return cookie;
	}

	public Cookie[] getCookies() {
		return cookies;
	}

	public Header getHeader(String s) {
		Header header = null;
		if (headers != null && headers.length != 0) {
			for (int i = 0; i < headers.length; i++) {
				if (headers[i].getName().equalsIgnoreCase(s)) {
					header = headers[i];
				}
			}
		}
		return header;
	}

	public Header[] getHeaders() {
		return headers;
	}

	public String getHtml() {
		return getText("UTF-8");
	}

	public String getHtml(String s) {
		return getText(s);
	}

	public byte[] getResponse() {
		byte abyte0[];
		if (response == null)
			abyte0 = null;
		else
			abyte0 = Arrays.copyOf(response, response.length);
		return abyte0;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public String getText(String s) {
		String s1 = null;
		if (response != null) {
			try {
				if (TextUtils.isEmpty(s))
					s = "utf-8";
				String s2 = new String(response, s);
				s1 = s2;
			} catch (Exception e) {
				Log.e(TAG, e.toString());
			}
		}
		return s1;
	}

	public String toString() {
		return (new StringBuilder("HttpResult [cookies="))
				.append(Arrays.toString(cookies)).append(", headers=")
				.append(Arrays.toString(headers)).append(", response=")
				.append(getText("utf-8")).append(", statuCode=")
				.append(statusCode).append("]").toString();
	}

}
