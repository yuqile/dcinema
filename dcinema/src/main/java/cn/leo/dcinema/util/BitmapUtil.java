package cn.leo.dcinema.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.util.LruCache;
import com.example.android.bitmapfun.util.DiskLruCache;

import cn.leo.dcinema.https.HttpUtils;

import java.io.File;
import java.net.URI;
import org.apache.http.*;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;

public class BitmapUtil {
	private static final long DISK_CACHE_SIZE = 83886080L;
	private static final String DISK_CACHE_SUBDIR = "diskCache";
	private static final int MEMORY_CACHE_SIZE = 10485760*2;
	private static final String TAG = "BitmapUtil";
	private static BitmapUtil instance;
	private static Context mContext;
	private static DiskLruCache mDiskCache;
	private static LruCache<String, Bitmap> memoryCache;

	private BitmapUtil(Context context) {
		mContext = context;
		memoryCache = new LruCache<String, Bitmap>(MEMORY_CACHE_SIZE);
		File file = new File(mContext.getCacheDir(), DISK_CACHE_SUBDIR);
		mDiskCache = DiskLruCache.openCache(mContext, file, DISK_CACHE_SIZE);
	}

	private int calculateInSampleSize(
			android.graphics.BitmapFactory.Options options, int i, int j) {
		int k = options.outHeight;
		int l = options.outWidth;
		int i1 = 1;
		int j1;
		if (i == 0 || j == 0) {
			j1 = i1;
		} else {
			if (k > j || l > i)
				if (l > k)
					i1 = Math.round((float) k / (float) j);
				else
					i1 = Math.round((float) l / (float) i);
			Log.d(TAG, (new StringBuilder("原图尺寸：")).append(l).append("x")
					.append(k).append(",实际尺寸：").append(i).append("x").append(j)
					.append(",inSampleSize = ").append(i1).toString());
			j1 = i1;
		}
		return j1;
	}

	public static Bitmap getBitmap(Context context, String s, boolean flag) {
		getInstance(context);
		Bitmap bitmap = instance.getBitmapFromDisk(s);
		Bitmap bitmap2;
		if (bitmap != null) {
			Log.d(TAG, (new StringBuilder("sp:from disk bitmap"))
					.append(bitmap).toString());
			instance.addToCache(s, bitmap, flag, true);
			bitmap2 = bitmap;
		} else {
			Bitmap bitmap1 = instance.getBitmapFromNet(s, 0, 0);
			if (bitmap1 != null)
				instance.addToCache(s, bitmap1, flag, true);
			bitmap2 = bitmap1;
		}
		return bitmap2;
	}

	public static Bitmap getBitmap(String s) {
		Bitmap bitmap = null;
		byte abyte0[] = HttpUtils.getBinary(s, null, null);
		if (abyte0 != null)
			bitmap = BitmapFactory.decodeByteArray(abyte0, 0, abyte0.length);
		return bitmap;
	}

	public static BitmapUtil getInstance(Context context) {
		if (instance == null) {
			synchronized (BitmapUtil.class) {
				if (instance == null) {
					instance = new BitmapUtil(context);
				}
			}
		}
		return instance;
	}

	public void addToCache(String s, Bitmap bitmap, boolean isLruCache,
			boolean isDiskLruCache) {
		if (s != null && bitmap != null) {
			String s1 = MD5Util.getMD5String(s);
			if (isLruCache)
				memoryCache.put(s1, bitmap);
			if (isDiskLruCache)
				mDiskCache.put(
						(new StringBuilder(String.valueOf(s1))).append(".png")
								.toString(), bitmap);
		}
	}

	public Bitmap getBitmap(byte abyte0[], int i, int j) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		options.inSampleSize = calculateInSampleSize(options, i, j);
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeByteArray(abyte0, 0, abyte0.length, options);
	}

	public Bitmap getBitmapFromDisk(String s) {
		String s1 = (new StringBuilder(String.valueOf(MD5Util.getMD5String(s))))
				.append(".png").toString();
		return mDiskCache.get(s1);
	}

	public Bitmap getBitmapFromMemory(String s) {
		String s1 = MD5Util.getMD5String(s);
		return (Bitmap) memoryCache.get(s1);
	}

	public Bitmap getBitmapFromNet(String s, int i, int j) {
		Log.d(TAG, "url [" + s + "]" + "i=" + i + "; j=" + j);
		Bitmap bitmap;
		DefaultHttpClient defaulthttpclient;
		HttpGet httpget = null;
		bitmap = null;
		defaulthttpclient = new DefaultHttpClient();
		defaulthttpclient.getParams().setIntParameter("http.socket.timeout",
				60000);
		defaulthttpclient
				.setHttpRequestRetryHandler(new DefaultHttpRequestRetryHandler(
						5, false));
		try {
			httpget = new HttpGet();
			httpget.setURI(new URI(s));
			HttpResponse httpresponse = defaulthttpclient.execute(httpget);
			if (httpresponse.getStatusLine().getStatusCode() == 200)
				bitmap = BitmapFactory.decodeStream(httpresponse.getEntity()
						.getContent());
			if (bitmap == null)
				Log.e(TAG, (new StringBuilder("getBitmapFromNet 下载失败 "))
						.append(0).append(" 次, url = ").append(s).toString());
			httpget.abort();
			defaulthttpclient.getConnectionManager().shutdown();
		} catch (Exception e) {
			Log.e(TAG, e.toString());
			httpget.abort();
			defaulthttpclient.getConnectionManager().shutdown();
		}
		return bitmap;
	}

}
