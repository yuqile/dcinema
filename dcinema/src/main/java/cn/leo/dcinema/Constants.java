package cn.leo.dcinema;

public interface Constants {
	public static final String updateDataJSONUrl = "http://115.28.17.15/3dcinemaupdate.json";
	public static final long updateCheckTime = 3600000L;
	public static final String ACTION = "cn.leo.dcinema.action.MY_ACTION";

	public static final String UPDATE_APKNAME = "3DCinema.apk";
	public static final String ApkUpdatebiz_ACTION = "cn.leo.dcinema.ACTION.ApkUpdate";
	public static final String APP_DB_CHANGE = "cn.leo.dcinema.app_db_change";

    public static final boolean DEBUG = false;

	public static int diaAppLayoutH = 600;
	public static int diaAppLayoutW = 800;

	public static final String SOUND_BACK = "back";
	public static final String SOUND_BACK_TO_TOP = "back_to_top";
	public static final String SOUND_COMFIRE = "comfire";
	public static final String SOUND_MOVE_BOTTOM = "move_bottom";
	public static final String SOUND_MOVE_LEFT = "move_left";
	public static final String SOUND_MOVE_RIGHT = "move_right";
	public static final String SOUND_NET_CONNECTED = "net_connected";
	public static final String SOUND_NET_FOUND = "net_found";
	public static final String SOUND_TOP_FLOAT_DISABLED = "top_float_disabled";
	public static final String SOUND_TOP_FLOAT = "top_float";
	public static final String SOUND_TOP_PAGE_MOVE = "top_page_move";

	// 配置相关键值字符串变量
	public static final String KEY_SETTING_SPF = "settingSPF";

	public static final String KEY_APK_RUNTIMES = "APKRunTime";
	public static final String KEY_USER_DEFAULT = "useUserDefined";
	public static final String KEY_PLAY_SOUND = "play_sound";
	public static final String KEY_SERVER = "server";
	public static final String KEY_SCALEMOD = "scalemod";
	public static final String KEY_SHARPNESS = "sharpness";
	public static final String KEY_LIVEADDR = "live_addr";
	public static final String KEY_AGGREADDR = "aggre_addr";
	public static final String KEY_VODADDR = "vod_addr";
	public static final String KEY_BROWSER_ADDR = "browser_addr";
	public static final String KEY_MOVIE = "电影";

	public static final String KEY_AGGRENON_OFF = "aggreonOff";
	public static final String KEY_VODON_OFF = "vodonOff";
	public static final String KEY_LIVEON_OFF = "liveonOff";
	public static final String KEY_LOCALON_OFF = "localonOff";
	public static final String KEY_BROWSERON_OFF = "browseronOff";

	//
	public static final String VST_SRVADD = "http://api.myvst.net/";

	// Hdfan
	public static final String KEY_FOOBAR = "foobar";
	public static final String KEY_NAME = "name";
	public static final String KEY_URL = "url";
	public static final String KEY_ID = "id";
	public static final String KEY_IMG = "img";
	public static final String KEY_LINK = "link";
	public static final String KEY_SITE = "site";
	public static final String KEY_DESC = "desc";
	public static final String KEY_DIRECTOR = "director";
	public static final String KEY_ACTOR = "actor";
	public static final String KEY_TOTAL_DUR = "total_dur";
	public static final String KEY_DUR = "dur";
	public static final String KEY_M3U8 = "m3u8";

	public static final String KEY_INOF = "INFO";
	public static final String KEY_ROWS = "ROWS";

	public static final String KEY_COUNT = "COUNT";
	public static final String KEY_PAGECOUNT = "PAGECOUNT";
	public static final String KEY_PAGE = "PAGE";
	public static final String KEY_PAGESIZE = "PAGESIZE";

	public static final String INTENT_KEY_VIDEOGATEGROY = "VIDEOGATEGROY";
	public static final String INTENT_KEY_VIDEODEAIL = "VIDEODEAIL";
}
