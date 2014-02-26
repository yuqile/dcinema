package cn.leo.dcinema.biz;

import java.util.ArrayList;

import org.codehaus.jackson.map.ObjectMapper;

import android.util.Log;
import cn.leo.dcinema.https.HttpUtils;
import cn.leo.dcinema.model.VideoInfo;
import cn.leo.dcinema.model.VideoList;
import cn.leo.dcinema.model.ned.VodList;

public class VodListBiz {

	public static final String VodListGetUrl = "http://www.videozaixian.com/api/homepage/getItemListByCondition?";
	public static final String KEY_CATEGORYID = "categoryId=8";
	public static final String KEY_SUBCATEGORYID = "subCategoryId=0";
	public static final String KEY_START = "start=";
	public static final String KEY_PAGESIZE = "pageSize=";
	public static final String TOKEN_STR = "&";
	private static final String TAG = "VodListBiz";

	public static VideoList parseVodListData(int page, int pageSize) {
		VideoList videolist = null;
		int start = (page - 1) * pageSize;
		Log.d(TAG, "start = " + start + ",  page =" + page);
		VodList vodlist = parse(start, pageSize);
		if (vodlist != null) {
			videolist = new VideoList();
			videolist.video_count = vodlist.totalCount;
			videolist.zurpage = page;
			videolist.maxpage = (int) (vodlist.totalCount / pageSize);
			videolist.punpage = pageSize;
			videolist.video = new ArrayList<VideoInfo>();
			for (VodList.VideoInfo item : vodlist.items) {
				VideoInfo info = new VideoInfo();
				info.banben = "";
				info.id = item.id;
				info.img = item.bigImage;
				info.logo = item.smallImage;
				info.title = item.name;
				videolist.video.add(info);
			}
			vodlist.items.clear();
			vodlist.items = null;
			vodlist = null;
		}
		if (videolist != null)
			Log.d(TAG, videolist.toString());
		return videolist;
	}

	private static VodList parse(int start, int pageSize) {
		VodList vodlist = null;
		String url = new StringBuffer().append(VodListGetUrl)
				.append(KEY_CATEGORYID).append(TOKEN_STR)
				.append(KEY_SUBCATEGORYID).append(TOKEN_STR).append(KEY_START)
				.append(start).append(TOKEN_STR).append(KEY_PAGESIZE)
				.append(pageSize).toString();
		Log.d(TAG, (new StringBuffer("url=")).append(url).toString());
		String s1 = HttpUtils.getContent(url, null, null);
		if (s1 != null) {
			try {
				vodlist = (new ObjectMapper()).readValue(s1, VodList.class);
			} catch (Exception e) {
				Log.e(TAG, e.toString());
			}
		}
		return vodlist;
	}
}
