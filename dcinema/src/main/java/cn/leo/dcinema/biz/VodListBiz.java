package cn.leo.dcinema.biz;

import java.util.ArrayList;

import org.codehaus.jackson.map.ObjectMapper;

import android.util.Log;

import cn.leo.dcinema.Constants;
import cn.leo.dcinema.https.HttpUtils;
import cn.leo.dcinema.model.VideoInfo;
import cn.leo.dcinema.model.VideoList;
import cn.leo.dcinema.model.ned.VodList;

public class VodListBiz {

    public static final String VodListGetUrl = "http://www.videozaixian.com/cmsapi/albums/91/subalbums/3d_indexed_old?";
    public static final String KEY_HASSUBALBUMNAMES = "hasSubAlbumNames=";
    public static final String KEY_AREA = "area=10000";
    public static final String KEY_CATEGORYID = "category=";
    public static final String KEY_PAGENO = "pageNo=";
    public static final String KEY_YEAR = "year=";
    public static final String KEY_ORDER = "order=2";
    public static final String KEY_SEARCHZZALBUM = "searchzzAlbum=1";
    public static final String KEY_PAGESIZE = "numPerPage=";
    public static final String TOKEN_STR = "&";
    private static final String TAG = "VodListBiz";

    public static VideoList parseVodListData(int page, int pageSize) {
        VideoList videolist = null;
        if (Constants.DEBUG)
            Log.d(TAG, "page =" + page);
        VodList vodlist = parse(page, pageSize);
        if (vodlist != null) {
            videolist = new VideoList();
            videolist.video_count = vodlist.total;
            videolist.zurpage = page;
            if (vodlist.total % pageSize == 0) {
                videolist.maxpage = (int) (vodlist.total / pageSize);
            } else {
                videolist.maxpage = (int) (vodlist.total / pageSize) + 1;
            }
            videolist.punpage = pageSize;
            videolist.video = new ArrayList<VideoInfo>();
            for (VodList.VideoInfo item : vodlist.albums) {
                VideoInfo info = new VideoInfo();
                info.banben = "";
                info.id = item.id;
                info.img = item.poster1;
                info.logo = item.poster2;
                info.title = item.name;
                videolist.video.add(info);
            }
            vodlist.albums.clear();
            vodlist.albums = null;
            vodlist = null;
        }
        if (videolist != null && Constants.DEBUG)
            Log.d(TAG, videolist.toString());
        return videolist;
    }

    private static VodList parse(int start, int pageSize) {
        VodList vodlist = null;
        String url = new StringBuffer().append(VodListGetUrl)
                .append(KEY_HASSUBALBUMNAMES).append(TOKEN_STR)
                .append(KEY_AREA).append(TOKEN_STR)
                .append(KEY_CATEGORYID).append(TOKEN_STR)
                .append(KEY_PAGENO).append(start).append(TOKEN_STR)
                .append(KEY_YEAR).append(TOKEN_STR)
                .append(KEY_ORDER).append(TOKEN_STR)
                .append(KEY_SEARCHZZALBUM).append(TOKEN_STR)
                .append(KEY_PAGESIZE).append(pageSize).toString();
        if (Constants.DEBUG)
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
