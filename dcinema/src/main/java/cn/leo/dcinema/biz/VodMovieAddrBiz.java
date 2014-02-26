package cn.leo.dcinema.biz;

import java.util.ArrayList;

import org.codehaus.jackson.map.ObjectMapper;

import android.util.Log;
import cn.leo.dcinema.https.HttpUtils;
import cn.leo.dcinema.model.SharpnessEnum;
import cn.leo.dcinema.model.VideoPlayUrl;
import cn.leo.dcinema.model.ned.VodMoiveAddr;

public class VodMovieAddrBiz {
	public static final String VodMovieAddrGetUrl = "http://www.videozaixian.com/api/albums/playForApi?";
	public static final String KEY_IPTV_ALBUMID = "iptvAlbumId=";
	public static final String KEY_STREAM = "stream=";
	public static final String KEY_SERIES = "series=";
	public static final String TOKEN_STR = "&";
	public static final String KEY_3D720P = "3d720p";
	public static final String KEY_3D1080P = "3d1080p6m";

	private static final String TAG = "VodMovieAddrBiz";

	public static ArrayList<VideoPlayUrl> parseVodMovieAddr(String url) {
		ArrayList<VideoPlayUrl> playUrllist = null;
		VodMoiveAddr vodAddr = parseVod(url);
		if (vodAddr != null) {
			playUrllist = new ArrayList<VideoPlayUrl>();
			VideoPlayUrl playUrl = new VideoPlayUrl();
			playUrl.playurl = vodAddr.playUrl;
			playUrl.sharp = SharpnessEnum._3D;
			playUrllist.add(playUrl);
		}
		return playUrllist;
	}

	private static VodMoiveAddr parseVod(String url) {
		VodMoiveAddr vodMoiveAddr = null;

		Log.d(TAG, (new StringBuffer("url=")).append(url).toString());

		String s1 = HttpUtils.getContent(url, null, null);
		if (s1 != null) {
			try {
				vodMoiveAddr = (new ObjectMapper()).readValue(s1,
						VodMoiveAddr.class);
			} catch (Exception e) {
				Log.e(TAG, e.toString());
			}
		}

		return vodMoiveAddr;

	}
}
