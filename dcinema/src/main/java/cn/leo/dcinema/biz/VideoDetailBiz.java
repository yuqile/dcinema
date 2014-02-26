package cn.leo.dcinema.biz;

import android.util.Log;
import cn.leo.dcinema.https.HttpUtils;
import cn.leo.dcinema.model.VideoDetailInfo;
import cn.leo.dcinema.model.VideoInfo;
import cn.leo.dcinema.model.VideoSet;
import cn.leo.dcinema.model.VideoSource;
import java.io.IOException;
import java.util.Iterator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;

public class VideoDetailBiz {
	private static final String TAG = "VideoDetailBiz";

	public static VideoDetailInfo parseDetailInfo(String s, int i) {
		Log.d(TAG, new StringBuffer().append("parseDetailInfo[").append(s).append(i)
				.append("]").toString());
		String s1 = HttpUtils.getContent((new StringBuilder(String.valueOf(s)))
				.append(i).append(".json").toString(), null, null);
		VideoDetailInfo videodetailinfo = null;
		if (s1 != null) {
			ObjectMapper objectmapper;
			JsonNode jsonnode;
			Iterator<JsonNode> iterator = null;
			objectmapper = new ObjectMapper();
			try {
				jsonnode = objectmapper.readTree(s1);
				iterator = jsonnode.path("playlist").iterator();
				while (iterator.hasNext()) {
					JsonNode jsonnode1;
					Iterator<JsonNode> iterator1;
					jsonnode1 = (JsonNode) iterator.next();
					iterator1 = jsonnode1.path("list").iterator();
					while (iterator1.hasNext()) {
						objectmapper.treeToValue((JsonNode) iterator1.next(),
								VideoSet.class);
					}
					objectmapper.treeToValue(jsonnode1, VideoSource.class);
				}
				Iterator<JsonNode> iterator2 = jsonnode.path("new_top")
						.iterator();
				while (iterator2.hasNext()) {
					objectmapper.treeToValue((JsonNode) iterator2.next(),
							VideoInfo.class);
				}
				videodetailinfo = (VideoDetailInfo) objectmapper.treeToValue(
						jsonnode, VideoDetailInfo.class);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				Log.e(TAG, e.toString());
				videodetailinfo = null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.e(TAG, e.toString());
				videodetailinfo = null;
			}
		}
		return videodetailinfo;
	}

}