package cn.leo.dcinema.biz;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;

import cn.leo.dcinema.https.HttpUtils;
import cn.leo.dcinema.model.SharpnessEnum;
import cn.leo.dcinema.model.VideoPlayUrl;
import cn.leo.dcinema.model.ned.VodMoiveAddr;
import cn.leo.dcinema.util.XmlElement;

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
        HashMap<String, String> map = null;

        String s1 = HttpUtils.getContent(url, null, null);
        if (s1 != null) {
            try {
                vodMoiveAddr = new VodMoiveAddr();
                map = parseResult(s1);
                if (map != null) {
                    vodMoiveAddr.newUrl = map.get(KEY_NEWURL);
                    vodMoiveAddr.playUrl = map.get(KEY_URL);
                    map.clear();
                }
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        }

        return vodMoiveAddr;

    }


    private static final String KEY_MEDIAS = "medias";
    private static final String KEY_MEDIA = "media";
    private static final String KEY_MMSID = "mmsid";
    private static final String KEY_SEG = "seg";
    private static final String KEY_VID = "vid";
    private static final String KEY_NEWURL = "newurl";
    private static final String KEY_URL = "url";

    private static HashMap<String, String> parseResult(String in) {
        ByteArrayInputStream stream = new ByteArrayInputStream(in.getBytes());
        HashMap<String, String> item = null;
        String value = null;
        if (stream != null) {
            XmlElement xmlDocument = null;
            try {
                xmlDocument = XmlElement.parseXml(stream);
                List<XmlElement> recommends = xmlDocument.getAllChildren();
                item = new HashMap<String, String>();
                if (recommends != null) {
                    for (XmlElement element : recommends) {
                        String name = element.getName();
                        Log.d(TAG, "xml name :" + name);
                        if (KEY_MEDIAS.equals(name)) {
                            List<XmlElement> medias = element.getAllChildren();
                            for (XmlElement media : medias) {
                                name = media.getName();

                                Log.d(TAG, "xml name :" + name);
                                if (KEY_MEDIA.equals(name)) {
                                    List<XmlElement> segs = media.getAllChildren();
                                    for (XmlElement seg : segs) {
                                        name = seg.getName();
                                        Log.d(TAG, "xml name :" + name);
                                        if (KEY_SEG.equals(name)) {
                                            List<XmlElement> segitem = seg.getAllChildren();
                                            name = seg.getName();
                                            Log.d(TAG, "xml name :" + name);
                                            for (XmlElement url : segitem) {
                                                name = url.getName();
                                                value = url.getText();
                                                Log.d(TAG, "xml name :" + name);
                                                if (KEY_NEWURL.equals(name) || KEY_URL.equals(name)) {
                                                    item.put(name, value);
                                                    Log.d(TAG, "xml value :" + value);
                                                }
                                            }
                                            break;
                                        }
                                    }
                                    break;
                                }
                            }
                            break;
                        }
                    }
                }
            } catch (XmlPullParserException e) {
                // TODO Auto-generated catch block
                Log.e(TAG, e.toString());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                Log.e(TAG, e.toString());
            }
        }
        return item;
    }
}
