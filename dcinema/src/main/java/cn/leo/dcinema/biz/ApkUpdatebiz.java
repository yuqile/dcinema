package cn.leo.dcinema.biz;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import org.codehaus.jackson.map.ObjectMapper;
import android.content.Context;
import android.util.Log;
import cn.leo.dcinema.Constants;
import cn.leo.dcinema.https.HttpUtils;
import cn.leo.dcinema.model.ApkUpdateInfo;
import cn.leo.dcinema.util.MD5Util;

public class ApkUpdatebiz {
	public static final String APKMD5 = "apkmd5";
	public static final String APKNAME = "apkname";
	public static final String APKPATH = "apkpath";
	public static final String APKURL = "apkurl";
	public static final String APPNAME = "appname";
	public static final String INSTRUCTION = "instruction";
	public static final String UPDATEINFO = "updateinfo";
	public static final String VERCODE = "verCode";
	public static final String VERNAME = "verName";
	private static final String TAG = "ApkUpdatebiz";

	public static String downLoadFile(Context context, String downloadUrl,
			String mdb5Str) {
		String s2 = null;
		File file = new File(context.getCacheDir(), Constants.UPDATE_APKNAME);
		try {
			Log.d(TAG, file.toString());
			if (file.exists()
					&& MD5Util.getFileMD5String(file).equalsIgnoreCase(mdb5Str)) {
				s2 = file.getAbsolutePath();
				return s2;
			}
			Log.d(TAG, downloadUrl);
			byte binaryBuffer[] = HttpUtils.getBinary(downloadUrl, null, null);
			if (binaryBuffer != null) {
				ByteArrayInputStream bytearrayinputstream = new ByteArrayInputStream(
						binaryBuffer);
				FileOutputStream fileoutputstream = new FileOutputStream(file);
				byte abyte1[] = new byte[30720];
				do {
					int i = bytearrayinputstream.read(abyte1);
					if (i == -1) {
						bytearrayinputstream.close();
						fileoutputstream.close();
						s2 = file.getAbsolutePath();
						break;
					}
					Log.d(TAG, "get APK");
					fileoutputstream.write(abyte1, 0, i);
				} while (true);
			}
		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}
		return s2;
	}

	public static ApkUpdateInfo parseUpdataInfo(Context context, String s) {
		String s1 = s;
		ApkUpdateInfo apkupdateinfo;
		if (!HttpUtils.isNetworkAvailable(context)) {
			apkupdateinfo = null;
		} else {
			String s2 = HttpUtils.getContent(s1, null, null);
			if (s2 == null)
				apkupdateinfo = null;
			else {
				Log.d(TAG, s2);
				try {
					apkupdateinfo = (ApkUpdateInfo) (new ObjectMapper())
							.readValue(s2, ApkUpdateInfo.class);
				} catch (Exception e) {
					Log.e(TAG, "parseUpdataInfo : " + e.toString());
					apkupdateinfo = null;
				}
			}
		}
		return apkupdateinfo;
	}
}
