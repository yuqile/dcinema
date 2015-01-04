package cn.leo.dcinema.biz;

import android.content.Context;
import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn.leo.dcinema.https.HttpClientHelper;
import cn.leo.dcinema.https.HttpUtils;
import cn.leo.dcinema.model.VideoInfo;
import cn.leo.dcinema.model.VideoList;
import cn.leo.dcinema.model.VideoTypeInfo;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;


public class VideoCateBiz {
	private static final String TAG = "VideoCateBiz";

	public static Map<String, ArrayList<String>> parseCateList(String s,
			String s1) {
		Log.d(TAG, "parseCateList");
		String s2;
		NameValuePair anamevaluepair[] = new NameValuePair[2];
		BasicNameValuePair basicnamevaluepair = new BasicNameValuePair("tid",
				s1);
		anamevaluepair[0] = basicnamevaluepair;
		anamevaluepair[1] = new BasicNameValuePair("data", "item");
		s2 = HttpUtils.getContent(s, null, anamevaluepair);
		HashMap<String, ArrayList<String>> hashmap = null;
		JsonNode jsonnode;
		ArrayList<String> arraylist;
		Iterator<JsonNode> iterator;
		try {
			Log.d(TAG, s);
			Log.d(TAG, s2);
			hashmap = new HashMap<String, ArrayList<String>>();
			jsonnode = (new ObjectMapper()).readTree(s2);
			JsonNode jsonnode1 = jsonnode.path("item");
			arraylist = new ArrayList<String>();
			if (jsonnode1 != null) {
				Log.d(TAG, "has item");
				iterator = jsonnode1.iterator();

				while (iterator.hasNext()) {
					arraylist.add(((JsonNode) iterator.next()).path("name")
							.asText());
				}
				hashmap.put("item", arraylist);
			}
			ArrayList<String> arraylist1;
			Iterator<JsonNode> iterator1;
			JsonNode jsonnode2 = jsonnode.path("area");
			if (jsonnode2 != null) {
				Log.d(TAG, "has area");
				arraylist1 = new ArrayList<String>();
				iterator1 = jsonnode2.iterator();
				while (iterator1.hasNext()) {
					arraylist1.add(((JsonNode) iterator1.next()).path("name")
							.asText());
				}
				hashmap.put("area", arraylist1);
			}
			ArrayList<String> arraylist2;
			Iterator<JsonNode> iterator2;
			JsonNode jsonnode3 = jsonnode.path("year");
			if (jsonnode3 != null) {
				Log.d(TAG, "has year");
				arraylist2 = new ArrayList<String>();
				iterator2 = jsonnode3.iterator();
				while (iterator2.hasNext()) {
					arraylist2.add(((JsonNode) iterator2.next()).path("name")
							.asText());
				}
				hashmap.put("year", arraylist2);
			}
		} catch (Exception e) {
			Log.e(TAG, e.toString());
			hashmap = null;
		}
		return hashmap;
	}

	public static ArrayList<VideoTypeInfo> parseTopCate(Context context,
			String s, boolean flag) {
		Log.d(TAG, "parseTopCate");
		String s1;
		File file;
		s1 = (new StringBuilder(String.valueOf(s))).append("item").toString();
		file = new File(context.getCacheDir(), "cateFile");
		ArrayList<VideoTypeInfo> arraylist = null;
		if (!(!flag || !file.exists())) {
			/* 从Cache文件中读取推荐数据 */
			ObjectMapper objectmapper1 = new ObjectMapper();
			try {
				Iterator<JsonNode> iterator1 = objectmapper1.readTree(file)
						.path("type").iterator();
				arraylist = new ArrayList<VideoTypeInfo>();
				while (iterator1.hasNext()) {
					VideoTypeInfo videotypeinfo1 = (VideoTypeInfo) objectmapper1
							.treeToValue((JsonNode) iterator1.next(),
									VideoTypeInfo.class);
					System.out.println(videotypeinfo1);
					arraylist.add(videotypeinfo1);
				}
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				Log.e(TAG, e.toString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.e(TAG, e.toString());
			}
		} else {
			String s2 = HttpUtils.getContent(s1, null, null);
			if (s2 != null) {
				FileOutputStream fileoutputstream;
				ByteArrayInputStream bytearrayinputstream;
				byte abyte0[];
				try {
					fileoutputstream = new FileOutputStream(file);
					bytearrayinputstream = new ByteArrayInputStream(
							s2.getBytes());
					abyte0 = new byte[2048];
					while (true) {
						int i = bytearrayinputstream.read(abyte0);
						if (i != -1) {
							fileoutputstream.write(abyte0, 0, i);
						} else {
							break;
						}
					}
					fileoutputstream.close();
					bytearrayinputstream.close();
					arraylist = new ArrayList<VideoTypeInfo>();
					ObjectMapper objectmapper;
					Iterator<JsonNode> iterator;
					objectmapper = new ObjectMapper();
					iterator = objectmapper.readTree(s2).path("type")
							.iterator();
					while (iterator.hasNext()) {
						VideoTypeInfo videotypeinfo = (VideoTypeInfo) objectmapper
								.treeToValue((JsonNode) iterator.next(),
										VideoTypeInfo.class);
						System.out.println(videotypeinfo);
						arraylist.add(videotypeinfo);
					}
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					Log.e(TAG, e.toString());
					arraylist = null;
				} catch (IOException e) {
					// TODO: handle exception
					Log.e(TAG, e.toString());
					arraylist = null;
				}

			}
		}
		return arraylist;
	}

	public static VideoList parseVideoList(String s, Map<String, String> map) {
		Log.d(TAG, "parseVideoList");
		String s1;
		VideoList videolist = null;
		if (map.get("item") != null
				&& ((String) map.get("item")).equalsIgnoreCase("全部"))
			map.remove("item");
		if (map.get("area") != null
				&& ((String) map.get("area")).equalsIgnoreCase("不限"))
			map.remove("area");
		s1 = HttpUtils.getContent(s, null, HttpClientHelper.mapToPairs(map));
		Log.d(TAG, "get list from " + s);
		if (s1 != null) {
			Log.d(TAG, "get list :" + s1);
			try {
				ObjectMapper objectmapper = new ObjectMapper();
				JsonNode jsonnode = objectmapper.readTree(s1);
				for (Iterator<JsonNode> iterator = jsonnode.path("video")
						.iterator(); iterator.hasNext(); objectmapper
						.treeToValue((JsonNode) iterator.next(),
								VideoInfo.class)) {
					objectmapper = new ObjectMapper();
					jsonnode = objectmapper.readTree(s1);
				}
				videolist = (VideoList) objectmapper.treeToValue(jsonnode,
						VideoList.class);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				Log.e(TAG, e.toString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.e(TAG, e.toString());
			}
		}
		return videolist;
	}
}
