package cn.leo.dcinema.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LEOTVDBHelper extends SQLiteOpenHelper {
	private SQLiteDatabase db = null;

	private LEOTVDBHelper(Context context1) {
		super(context1, DBNAME, null, VERSION);
		db = this.getWritableDatabase();
	}

	public SQLiteDatabase getDB() {
		return db;
	}

	public static LEOTVDBHelper getInstance(Context context1) {
		if (mInstance == null)
			synchronized (LEOTVDBHelper.class) {
				if (mInstance == null)
					mInstance = new LEOTVDBHelper(context1);
			}
		return mInstance;
	}

	public void onCreate(SQLiteDatabase sqlitedatabase) {
		sqlitedatabase.execSQL(CREATE_VOD_TABLE);
		sqlitedatabase.execSQL(CREATE_CHANNEL_INFO);
		sqlitedatabase.execSQL(CREATE_TYPE_INFO);
		sqlitedatabase.execSQL(CREATE_LIVE_RECODE);
		sqlitedatabase.execSQL(CREATE_VIEW_CHANNEL_INFO);
	}

	public void onUpgrade(SQLiteDatabase sqlitedatabase, int i, int j) {
		sqlitedatabase.execSQL(DROP_VOD_TABLE);
		sqlitedatabase.execSQL(DROP_CHANNEL_INFO);
		sqlitedatabase.execSQL(DROP_TYPE_INFO);
		sqlitedatabase.execSQL(DROP_LIVE_RECODE);
		sqlitedatabase.execSQL(DROP_VIEW_CHANNEL_INFO);
		onCreate(sqlitedatabase);
	}

	public static final String AREA = "area";
	private static final String CREATE_CHANNEL_INFO = "CREATE TABLE IF NOT EXISTS channel_info(  vid integer NOT NULL,  num integer ,vname text NOT\u3000NULL ,tid text , sourcetext text , epgid text , huibo text , quality text , pinyin text ) ";
	private static final String CREATE_LIVE_RECODE = "CREATE TABLE IF NOT EXISTS live_recode( vid integer NOT NULL, duration integer default 0 ,lastsource  integer default 0 ,favorit integer DEFAULT 0 ) ";
	private static final String CREATE_TYPE_INFO = "CREATE TABLE IF NOT EXISTS type_info ( tid text NOT NULL , tname text );";
	private static final String CREATE_VIEW_CHANNEL_INFO = "CREATE VIEW IF NOT EXISTS channel_info_view AS select * from channel_info LEFT JOIN  live_recode ON channel_info.vid=live_recode.vid";
	private static final String CREATE_VOD_TABLE = "create table if not exists vod_info ( id  Integer  not null ,title  text  not null ,banben  text not null ,image text  not null ,type Integer  not null ,updatetime  long not null , sourceIndex  Integer  ,setIndex  Integer  ,position   Integer  ) ";
	public static final String CUSTOM_TID = "custom_tid";
	public static final String CUSTOM_TNAME = "自定义";
	public static final String DBNAME = "LeoTV.db3";
	private static final String DROP_CHANNEL_INFO = "DROP TABLE IF EXISTS channel_info";
	private static final String DROP_LIVE_RECODE = "DROP TABLE IF EXISTS live_recode";
	private static final String DROP_TYPE_INFO = "DROP TABLE IF EXISTS type_info";
	private static final String DROP_VIEW_CHANNEL_INFO = "DROP VIEW IF EXISTS channel_info_view";
	private static final String DROP_VOD_TABLE = "DROP TABLE IF EXISTS vod_info";
	public static final String DURATION = "duration";
	public static final String EPGID = "epgid";
	public static final String FAVORIT = "favorit";
	public static final String FAVORITE_TID = "favorite_tid";
	public static final String FAVORITE_TNAME = "我的收藏";
	public static final String HUIBO = "huibo";
	public static final String LASTSOURCE = "lastsource";
	public static final String NUM = "num";
	public static final String PINYIN = "pinyin";
	public static final String POSITION = "position";
	public static final String QUALITY = "quality";
	public static final String SETINDEX = "setIndex";
	public static final String SOURCEINDEX = "sourceIndex";
	public static final String SOURCETEXT = "sourcetext";
//	private static final String TAG = "LEODBHelper";
	public static final String TID = "tid";
	public static final String TNAME = "tname";
	public static final String TNAME_CHANNEL_INFO = "channel_info";
	public static final String TNAME_LIVE_RECODE = "live_recode";
	public static final String TNAME_TYPE_INFO = "type_info";
	public static final String UPDATETIME = "updatetime";
	public static final int VERSION = 1;
	public static final String VID = "vid";
	public static final String VNAME = "vname";
	public static final String VNAME_CHANNEL_INFO = "channel_info_view";
	public static final String VOD_BANBEN = "banben";
	public static final String VOD_FILM_ID = "id";
	public static final String VOD_FILM_TITLE = "title";
	public static final String VOD_IMAGE_URL = "image";
	public static final String VOD_RECODE_TYPE = "type";
	public static final String VOD_TABLE = "vod_info";
	public static final String VOD_UPDATE_TIME = "updatetime";
	private static LEOTVDBHelper mInstance = null;
//	private Context context;

}