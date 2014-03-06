package cn.leo.dcinema.biz;

import java.util.ArrayList;

import org.codehaus.jackson.map.ObjectMapper;

import android.util.Log;

import cn.leo.dcinema.Constants;
import cn.leo.dcinema.https.HttpUtils;
import cn.leo.dcinema.model.VideoDetailInfo;
import cn.leo.dcinema.model.VideoInfo;
import cn.leo.dcinema.model.VideoSet;
import cn.leo.dcinema.model.VideoSource;
import cn.leo.dcinema.model.ned.VodMoiveDetails;

public class VodMovieDetailsBiz {
    public static final String VodMovieDetailsGetUrl = "http://www.videozaixian.com/cmsapi/albums/";
    public static final String KEY_STR_QUESTION = "?";
    public static final String KEY_DETAILS = "withVideoDetails=false"; //为true时可以获得相关的影片
    public static final String KEY_ISCHECK = "isChecked=1";
    public static final String KEY_TONKEN = "&";


    private static final String TAG = "VodListBiz";
    private static String series = "1";

    public static VideoDetailInfo parseVodListData(int iptvAlbumId) {
        VideoDetailInfo videoDetailInfo = null;
        VodMoiveDetails vodinfo = parseVod(iptvAlbumId);
        if (vodinfo != null) {
            videoDetailInfo = new VideoDetailInfo();
            videoDetailInfo.actor = vodinfo.actors;
            videoDetailInfo.area = vodinfo.area;
            videoDetailInfo.director = vodinfo.directors;
            videoDetailInfo.id = vodinfo.id;
            videoDetailInfo.img = vodinfo.poster1;
            videoDetailInfo.info = vodinfo.plot;
            videoDetailInfo.recommends = new ArrayList<VideoInfo>();
            videoDetailInfo.type = Constants.KEY_MOVIE;
            if (vodinfo.videos != null) {
                for (VodMoiveDetails.Video item : vodinfo.videos) {
                    VideoInfo info = new VideoInfo();
                    info.id = item.id;
                    info.img = item.poster1;
                    info.logo = item.poster3;
                    videoDetailInfo.recommends.add(info);
                }
            }

            /*
            videoDetailInfo.playlist = new ArrayList<VideoSource>();
            if (vodinfo.data.streams.d720p != null) {
                VideoSource source = new VideoSource();
                source.sourceName = VodMovieAddrBiz.KEY_3D720P;

                source.sets = new ArrayList<VideoSet>();
                VideoSet set = new VideoSet();
                String url = new StringBuffer()
                        .append(VodMovieAddrBiz.VodMovieAddrGetUrl)
                        .append(VodMovieAddrBiz.KEY_IPTV_ALBUMID)
                        .append(iptvAlbumId).append(VodMovieAddrBiz.TOKEN_STR)
                        .append(VodMovieAddrBiz.KEY_STREAM)
                        .append(source.sourceName)
                        .append(VodMovieAddrBiz.TOKEN_STR)
                        .append(VodMovieAddrBiz.KEY_SERIES).append(series)
                        .toString();
                set.link = url;
                source.sets.add(set);
                videoDetailInfo.playlist.add(source);
            }
            if (vodinfo.data.streams.d1080p6m != null) {
                VideoSource source = new VideoSource();
                source.sourceName = VodMovieAddrBiz.KEY_3D1080P;
                source.sets = new ArrayList<VideoSet>();
                VideoSet set = new VideoSet();
                String url = new StringBuffer()
                        .append(VodMovieAddrBiz.VodMovieAddrGetUrl)
                        .append(VodMovieAddrBiz.KEY_IPTV_ALBUMID)
                        .append(iptvAlbumId).append(VodMovieAddrBiz.TOKEN_STR)
                        .append(VodMovieAddrBiz.KEY_STREAM)
                        .append(source.sourceName)
                        .append(VodMovieAddrBiz.TOKEN_STR)
                        .append(VodMovieAddrBiz.KEY_SERIES).append(series)
                        .toString();
                set.link = url;
                source.sets.add(set);
                videoDetailInfo.playlist.add(source);
            }
            */
        }
        return videoDetailInfo;
    }

    private static VodMoiveDetails parseVod(int iptvAlbumId) {
        VodMoiveDetails vodMovie = null;
        String url = new StringBuffer().append(VodMovieDetailsGetUrl)
                .append(iptvAlbumId).append(KEY_STR_QUESTION)
                .append(KEY_DETAILS).append(KEY_TONKEN)
                .append(KEY_ISCHECK)
                .toString();
        Log.d(TAG, (new StringBuffer("url=")).append(url).toString());
        String s1 = HttpUtils.getContent(url, null, null);
        if (s1 != null) {
            try {
                vodMovie = (new ObjectMapper()).readValue(s1,
                        VodMoiveDetails.class);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        }
        return vodMovie;
    }
}
