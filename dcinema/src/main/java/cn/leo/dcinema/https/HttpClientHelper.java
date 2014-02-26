package cn.leo.dcinema.https;

import android.util.Log;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultRedirectHandler;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HttpContext;

public class HttpClientHelper {
	private static final int CONNECT_TIMEOUT = 5000;
	private static final int SO_TIMEOUT = 30000;
	private static String TAG = "HttpClientHelper";
	private static DefaultHttpClient client = null;

	static class DynamicRedirectHandler extends DefaultRedirectHandler {

		public boolean isRedirectRequested(HttpResponse httpresponse,
				HttpContext httpcontext) {
			return false;
		}
	}

	public HttpClientHelper() {
	}

	public static HttpResult get(String s) {
		return get(s, null, null, null, SO_TIMEOUT);
	}

	public static HttpResult get(String s, Header aheader[]) {
		return get(s, aheader, null, null, SO_TIMEOUT);
	}

	public static HttpResult get(String s, Header aheader[],
			NameValuePair anamevaluepair[]) {
		return get(s, aheader, anamevaluepair, null, SO_TIMEOUT);
	}

	public static HttpResult get(String s, Header aheader[],
			NameValuePair anamevaluepair[], Cookie acookie[], int i) {
		HttpGet httpget;
		client = initHttpClient();
		client.getParams().setIntParameter("http.socket.timeout", i);
		httpget = new HttpGet();
		client.setRedirectHandler(new DefaultRedirectHandler());
		if (anamevaluepair != null) {
			StringBuilder stringbuilder = new StringBuilder("?");
			for (int j = 0; j < anamevaluepair.length; j++) {
				stringbuilder.append("&");
				Object aobj[] = new Object[2];
				aobj[0] = anamevaluepair[j].getName();
				aobj[1] = anamevaluepair[j].getValue();
				stringbuilder.append(String.format("%s=%s", aobj));
			}
			s = (new StringBuilder(String.valueOf(s))).append(
					stringbuilder.toString()).toString();
		}
		Log.d(TAG, (new StringBuilder("get url=")).append(s).toString());
		try {
			httpget.setURI(new URI(s));
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			Log.e(TAG, e.toString());
		}
		if (aheader != null && aheader.length > 0)
			httpget.setHeaders(aheader);
		if (acookie == null || acookie.length <= 0) {
			client.getCookieStore().clear();
		} else {
			BasicCookieStore basiccookiestore = new BasicCookieStore();
			basiccookiestore.addCookies(acookie);
			client.setCookieStore(basiccookiestore);
		}
		HttpResult httpresult = null;
		try {
			httpresult = new HttpResult(client.execute(httpget),
					client.getCookieStore());
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			Log.e(TAG, e.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.e(TAG, e.toString());
		}
		httpget.abort();
		return httpresult;
	}

	public static HttpResult get(String s, NameValuePair anamevaluepair[]) {
		return get(s, null, anamevaluepair, null, SO_TIMEOUT);
	}

	@SuppressWarnings("unused")
	private static String getResultRedirecUrl(String s, Header aheader[],
			NameValuePair anamevaluepair[]) {
		HttpURLConnection httpurlconnection = null;
		try {
			httpurlconnection = (HttpURLConnection) (new URL(s))
					.openConnection();
			if (!(aheader == null || aheader.length <= 0)) {
				for (int j = 0; j < aheader.length; j++) {
					Header header = aheader[j];
					httpurlconnection.setRequestProperty(header.getName(),
							header.getValue());
				}
			}
			String s1;
			Log.d(TAG,
					(new StringBuilder("返回码: ")).append(
							httpurlconnection.getResponseCode()).toString());
			s1 = httpurlconnection.getURL().toString();
			s = s1;
		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}
		if (httpurlconnection != null)
			httpurlconnection.disconnect();
		return s;
	}

	public static DefaultHttpClient initHttpClient() {
		if (client == null) {
			BasicHttpParams basichttpparams = new BasicHttpParams();
			HttpProtocolParams.setHttpElementCharset(basichttpparams, "UTF-8");
			HttpProtocolParams
					.setVersion(basichttpparams, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(basichttpparams, "UTF-8");
			HttpProtocolParams.setUseExpectContinue(basichttpparams, true);
			HttpProtocolParams
					.setUserAgent(
							basichttpparams,
							"Mozilla/5.0(Linux;U;Android 2.2.1;en-us;Nexus One Build.FRG83) AppleWebKit/553.1(KHTML,like Gecko) Version/4.0 Mobile Safari/533.1");
			ConnManagerParams.setTimeout(basichttpparams, 2000L);
			HttpConnectionParams.setConnectionTimeout(basichttpparams,
					CONNECT_TIMEOUT);
			HttpConnectionParams.setSoTimeout(basichttpparams, SO_TIMEOUT);
			SchemeRegistry schemeregistry = new SchemeRegistry();
			schemeregistry.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 80));
			schemeregistry.register(new Scheme("https", SSLSocketFactory
					.getSocketFactory(), 443));
			client = new DefaultHttpClient(new ThreadSafeClientConnManager(
					basichttpparams, schemeregistry), basichttpparams);
		}
		return client;
	}

	public static NameValuePair[] mapToPairs(Map<String, String> map) {
		Set<String> set = map.keySet();
		NameValuePair anamevaluepair[];
		if (set == null || set.size() == 0) {
			anamevaluepair = null;
		} else {
			String as[] = (String[]) set.toArray(new String[0]);
			anamevaluepair = new NameValuePair[as.length];

			for (int i = 0; i < as.length; i++) {
				anamevaluepair[i] = new BasicNameValuePair(as[i],
						(String) map.get(as[i]));
			}

		}
		return anamevaluepair;
	}

	public static HttpResult post(String s, Header aheader[]) {
		return post(s, aheader, null, null, SO_TIMEOUT);
	}

	public static HttpResult post(String s, Header aheader[],
			NameValuePair anamevaluepair[]) {
		return post(s, aheader, anamevaluepair, null, SO_TIMEOUT);
	}

	public static HttpResult post(String s, Header aheader[],
			NameValuePair anamevaluepair[], Cookie acookie[]) {
		return post(s, aheader, anamevaluepair, acookie, SO_TIMEOUT);
	}

	public static HttpResult post(String s, Header aheader[],
			NameValuePair anamevaluepair[], Cookie acookie[], int i) {
		HttpPost httppost;
		ArrayList<NameValuePair> arraylist = new ArrayList<NameValuePair>();
		Log.d(TAG, (new StringBuilder(" post url=")).append(s).toString());
		client = initHttpClient();
		client.getParams().setIntParameter("http.socket.timeout", i);
		httppost = new HttpPost(s);
		if (anamevaluepair != null) {
			if (anamevaluepair.length > 0) {
				for (int k = 0; k < anamevaluepair.length; k++) {
					arraylist.add(anamevaluepair[k]);
				}
			}
			try {
				httppost.setEntity(new UrlEncodedFormEntity(arraylist, "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				Log.e(TAG, e.toString());
			}
		}
		if (aheader != null && aheader.length > 0)
			httppost.setHeaders(aheader);
		if (acookie == null || acookie.length <= 0) {
			client.getCookieStore().clear();
		} else {
			BasicCookieStore basiccookiestore = new BasicCookieStore();
			basiccookiestore.addCookies(acookie);
			client.setCookieStore(basiccookiestore);
		}
		HttpResult httpresult = null;
		try {
			httpresult = new HttpResult(client.execute(httppost),
					client.getCookieStore());
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.e(TAG, e.toString());
		}
		httppost.abort();
		return httpresult;
	}

	public static HttpResult post(String s, NameValuePair anamevaluepair[]) {
		return post(s, null, anamevaluepair, null, SO_TIMEOUT);
	}
}
