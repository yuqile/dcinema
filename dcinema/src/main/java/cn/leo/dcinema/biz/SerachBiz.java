package cn.leo.dcinema.biz;

import android.util.Log;
import cn.leo.dcinema.https.HttpUtils;
import cn.leo.dcinema.model.VideoInfo;
import cn.leo.dcinema.model.VideoList;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Iterator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;

@SuppressWarnings("unused")
public class SerachBiz {
	private static final String TAG = "SerachBiz";

	public static VideoList parseSerachResult(String s, String s1, int i) {
		String str = HttpUtils.getContent(
				(new StringBuilder(String.valueOf(s))).append("?data=so&wd=")
						.append(s1).append("&page=").append(i)
						.append("&num=20").toString(), null, null);
		Log.d(TAG, str);
		VideoList videolist;
		try {
			ObjectMapper objectmapper = new ObjectMapper();
			JsonNode jsonnode = objectmapper.readTree(str);
			Iterator<JsonNode> iterator = jsonnode.path("video").iterator();
			while (true) {
				if (!iterator.hasNext()) {
					videolist = (VideoList) objectmapper.treeToValue(jsonnode,
							VideoList.class);
					break;
				}
				objectmapper.treeToValue((JsonNode) iterator.next(),
						VideoInfo.class);
			}
		} catch (JsonProcessingException e) {
			Log.e(TAG, e.toString());
			videolist = null;
		} catch (IOException e) {
			Log.e(TAG, e.toString());
			videolist = null;
		}
		return videolist;
	}
}
