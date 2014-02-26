package cn.leo.dcinema.biz;

import android.content.Context;
import android.util.Log;
import cn.leo.dcinema.https.HttpUtils;
import cn.leo.dcinema.model.VideoInfo;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.codehaus.jackson.*;
import org.codehaus.jackson.map.ObjectMapper;

public class VideoTJBiz {
	private static final String TAG = "VideoTJBiz";

	public static ArrayList<VideoInfo> parseSubject(String s, String s1) {
		String s2;
		NameValuePair anamevaluepair[] = new NameValuePair[1];
		anamevaluepair[0] = new BasicNameValuePair("zid", s1);
		s2 = HttpUtils.getContent(s, null, anamevaluepair);
		if (s2 == null) {
			return null;
		}
		ArrayList<VideoInfo> arraylist = null;
		try {
			arraylist = new ArrayList<VideoInfo>();
			ObjectMapper objectmapper = new ObjectMapper();
			for (Iterator<JsonNode> iterator = objectmapper.readTree(s2)
					.path("video").iterator(); iterator.hasNext(); arraylist
					.add((VideoInfo) objectmapper.treeToValue(
							(JsonNode) iterator.next(), VideoInfo.class))) {
			}

		} catch (JsonProcessingException e) {
			Log.e(TAG, e.toString());
			arraylist = null;
		} catch (IOException e) {
			Log.e(TAG, e.toString());
			arraylist = null;
		}
		return arraylist;
	}

	public static ArrayList<VideoInfo> parseTJ(Context context, String s,
			boolean flag) {
		String s1;
		File file;
		s1 = (new StringBuilder(String.valueOf(s))).append("recommend")
				.toString();
		file = new File(context.getCacheDir(), "tjFile");
		Log.d(TAG, s1);
		Log.d(TAG, file.toString());

		ArrayList<VideoInfo> arraylist = null;

		if (!flag || !file.exists()) {
			String s2 = HttpUtils.getContent(s1, null, null);
			if (s2 == null) {
				arraylist = null;
				return arraylist;
			} else {
				Log.d(TAG, s2);
				FileOutputStream fileoutputstream;
				ByteArrayInputStream bytearrayinputstream;
				byte abyte0[];
				try {
					fileoutputstream = new FileOutputStream(file);
					bytearrayinputstream = new ByteArrayInputStream(
							s2.getBytes());
					abyte0 = new byte[2048];
					int i = 0;
					while ((i = bytearrayinputstream.read(abyte0)) != -1) {
						fileoutputstream.write(abyte0, 0, i);
					}
					fileoutputstream.close();
					bytearrayinputstream.close();
					arraylist = new ArrayList<VideoInfo>();
					ObjectMapper objectmapper;
					Iterator<JsonNode> iterator;
					objectmapper = new ObjectMapper();
					iterator = objectmapper.readTree(s2).path("video")
							.iterator();
					while (iterator.hasNext()) {
						arraylist.add((VideoInfo) objectmapper.treeToValue(
								(JsonNode) iterator.next(), VideoInfo.class));
					}

				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					Log.e(TAG, e.toString());
				} catch (IOException e) {
					Log.e(TAG, e.toString());
				}
			}
		} else {
			arraylist = new ArrayList<VideoInfo>();
			ObjectMapper objectmapper1 = new ObjectMapper();
			try {
				// 从文件中cache文件中读出VideoInfo
				for (Iterator<JsonNode> iterator1 = objectmapper1
						.readTree(file).path("video").iterator(); iterator1
						.hasNext();) {
					arraylist.add((VideoInfo) objectmapper1.treeToValue(
							(JsonNode) iterator1.next(), VideoInfo.class));
				}

			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				Log.e(TAG, e.toString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.e(TAG, e.toString());
			}
		}
		for (Iterator<VideoInfo> it = arraylist.iterator(); it.hasNext();) {
			Log.d(TAG, it.next().toString());
		}
		return arraylist;
	}
}
