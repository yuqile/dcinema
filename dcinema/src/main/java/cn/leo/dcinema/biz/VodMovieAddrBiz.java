package cn.leo.dcinema.biz;

import java.util.ArrayList;

import org.codehaus.jackson.map.ObjectMapper;

import android.util.Log;

import cn.leo.dcinema.https.HttpUtils;
import cn.leo.dcinema.model.SharpnessEnum;
import cn.leo.dcinema.model.VideoPlayUrl;
import cn.leo.dcinema.model.ned.VodMoiveAddr;

public class VodMovieAddrBiz {
    public static final String VodMovieAddrGetUrl = "http://www.videozaixian.com/cmsapi/videos/";
    public static final String KEY_TON = "/";
    public static final String KEY_TON_QUESTION = "?";
    public static final String KEY_SERIES = "series=1";
    public static final String TOKEN_STR = "&";
    public static final String KEY_TOTAL = "total=1";
    public static final String KEY_3D720P = "3d720P";
    public static final String KEY_3D1080P = "3d1080P";
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
