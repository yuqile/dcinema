package cn.leo.dcinema.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import cn.leo.dcinema.model.VodRecode;
import java.util.ArrayList;
import java.util.Observable;

public class VodDataHelper extends Observable {
	public static final int FAV = 1;
	public static final int RECODE = 3;
	public static final int ZHUI = 2;
	private static VodDataHelper mInstance = null;
	private LEOTVDBHelper dbHelper;

	private VodDataHelper(Context context) {
		dbHelper = LEOTVDBHelper.getInstance(context);
	}

	public static VodDataHelper getInstance(Context paramContext) {
		if (mInstance == null) {
			synchronized (VodDataHelper.class) {
				if (mInstance == null) {
					mInstance = new VodDataHelper(paramContext);
				}
			}
		}
		return mInstance;
	}

	public void deleteRecodes(int i) {
		SQLiteDatabase sqlitedatabase = dbHelper.getDB();
		String as[] = new String[1];
		as[0] = (new StringBuilder(String.valueOf(i))).toString();
		sqlitedatabase.delete("vod_info", "type=? ", as);
		////sqlitedatabase.close();
		setChanged();
		notifyObservers();
	}

	public void deleteRecodes(int i, int j) {
		SQLiteDatabase sqlitedatabase = dbHelper.getDB();
		String as[] = new String[2];
		as[0] = (new StringBuilder(String.valueOf(i))).toString();
		as[1] = (new StringBuilder(String.valueOf(j))).toString();
		sqlitedatabase.delete("vod_info", "id = ? and type = ? ", as);
		//sqlitedatabase.close();
		setChanged();
		notifyObservers();
	}

	public void insertRecode(VodRecode vodrecode) {
		if (queryRecode(vodrecode.id, vodrecode.type) != null)
			deleteRecodes(vodrecode.id, vodrecode.type);
		SQLiteDatabase sqlitedatabase = dbHelper.getDB();
		ContentValues contentvalues = new ContentValues();
		contentvalues.put("id", Integer.valueOf(vodrecode.id));
		contentvalues.put("title", vodrecode.title);
		contentvalues.put("image", vodrecode.imgUrl);
		contentvalues.put("banben", vodrecode.banben);
		contentvalues.put("updatetime",
				Long.valueOf(System.currentTimeMillis()));
		contentvalues.put("type", Integer.valueOf(vodrecode.type));
		contentvalues.put("setIndex", Integer.valueOf(vodrecode.setIndex));
		contentvalues
				.put("sourceIndex", Integer.valueOf(vodrecode.sourceIndex));
		contentvalues.put("position", Integer.valueOf(vodrecode.positon));
		sqlitedatabase.insert("vod_info", null, contentvalues);
		//sqlitedatabase.close();
		Log.d("", "insert recode ======================");
		setChanged();
		notifyObservers();
	}

	public boolean queryHasRecode(int i, int j) {
		SQLiteDatabase sqlitedatabase = dbHelper.getDB();
		String as[] = new String[2];
		as[0] = (new StringBuilder(String.valueOf(j))).toString();
		as[1] = (new StringBuilder(String.valueOf(i))).toString();
		Cursor cursor = sqlitedatabase.query("vod_info", null,
				"type=? and id=?", as, null, null, "updatetime desc ");
		boolean flag = false;
		if (cursor.getCount() > 0)
			flag = true;
		cursor.close();
		//sqlitedatabase.close();
		return flag;
	}

	public VodRecode queryLastRecode(int i) {
		SQLiteDatabase sqlitedatabase = dbHelper.getDB();
		String as[] = new String[1];
		as[0] = (new StringBuilder(String.valueOf(i))).toString();
		Cursor cursor = sqlitedatabase.query("vod_info", null, "type=? ", as,
				null, null, "updatetime desc ");
		VodRecode vodrecode = null;
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			vodrecode = new VodRecode();
			vodrecode.id = cursor.getInt(cursor.getColumnIndex("id"));
			vodrecode.title = cursor.getString(cursor.getColumnIndex("title"));
			vodrecode.banben = cursor
					.getString(cursor.getColumnIndex("banben"));
			vodrecode.imgUrl = cursor.getString(cursor.getColumnIndex("image"));
			vodrecode.type = cursor.getInt(cursor.getColumnIndex("type"));
			vodrecode.lastTime = cursor.getLong(cursor
					.getColumnIndex("updatetime"));
			vodrecode.sourceIndex = cursor.getInt(cursor
					.getColumnIndex("sourceIndex"));
			vodrecode.setIndex = cursor.getInt(cursor
					.getColumnIndex("setIndex"));
			vodrecode.positon = cursor
					.getInt(cursor.getColumnIndex("position"));
		}
		cursor.close();
		//sqlitedatabase.close();
		return vodrecode;
	}

	public VodRecode queryRecode(int i, int j) {
		SQLiteDatabase sqlitedatabase = dbHelper.getDB();
		String as[] = new String[2];
		as[0] = (new StringBuilder(String.valueOf(i))).toString();
		as[1] = (new StringBuilder(String.valueOf(j))).toString();
		Cursor cursor = sqlitedatabase.query("vod_info", null,
				"id=? and type=? ", as, null, null, "updatetime desc ");
		VodRecode vodrecode = null;
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			vodrecode = new VodRecode();
			vodrecode.id = cursor.getInt(cursor.getColumnIndex("id"));
			vodrecode.title = cursor.getString(cursor.getColumnIndex("title"));
			vodrecode.banben = cursor
					.getString(cursor.getColumnIndex("banben"));
			vodrecode.imgUrl = cursor.getString(cursor.getColumnIndex("image"));
			vodrecode.type = cursor.getInt(cursor.getColumnIndex("type"));
			vodrecode.lastTime = cursor.getLong(cursor
					.getColumnIndex("updatetime"));
			vodrecode.sourceIndex = cursor.getInt(cursor
					.getColumnIndex("sourceIndex"));
			vodrecode.setIndex = cursor.getInt(cursor
					.getColumnIndex("setIndex"));
			vodrecode.positon = cursor
					.getInt(cursor.getColumnIndex("position"));
		}
		cursor.close();
		//sqlitedatabase.close();
		return vodrecode;
	}

	public int queryRecodeCount(int i) {
		SQLiteDatabase sqlitedatabase = dbHelper.getDB();
		String as[] = new String[1];
		as[0] = (new StringBuilder(String.valueOf(i))).toString();
		Cursor cursor = sqlitedatabase.query("vod_info", null, "type=? ", as,
				null, null, "updatetime desc ");
		int j = cursor.getCount();
		cursor.close();
		//sqlitedatabase.close();
		return j;
	}

	public ArrayList<VodRecode> queryRecodes(int i) {
		SQLiteDatabase sqlitedatabase;
		Cursor cursor;
		ArrayList<VodRecode> arraylist;
		sqlitedatabase = dbHelper.getDB();
		String as[] = new String[1];
		as[0] = (new StringBuilder(String.valueOf(i))).toString();
		cursor = sqlitedatabase.query("vod_info", null, "type=? ", as, null,
				null, "updatetime desc ");
		arraylist = null;
		if (!(cursor.getCount() <= 0)) {
			arraylist = new ArrayList<VodRecode>();
			while (cursor.moveToNext()) {
				VodRecode vodrecode = new VodRecode();
				vodrecode.id = cursor.getInt(cursor.getColumnIndex("id"));
				vodrecode.title = cursor.getString(cursor
						.getColumnIndex("title"));
				vodrecode.banben = cursor.getString(cursor
						.getColumnIndex("banben"));
				vodrecode.imgUrl = cursor.getString(cursor
						.getColumnIndex("image"));
				vodrecode.type = cursor.getInt(cursor.getColumnIndex("type"));
				vodrecode.lastTime = cursor.getLong(cursor
						.getColumnIndex("updatetime"));
				vodrecode.sourceIndex = cursor.getInt(cursor
						.getColumnIndex("sourceIndex"));
				vodrecode.setIndex = cursor.getInt(cursor
						.getColumnIndex("setIndex"));
				vodrecode.positon = cursor.getInt(cursor
						.getColumnIndex("position"));
				arraylist.add(vodrecode);
			}
		}
		cursor.close();
		//sqlitedatabase.close();
		return arraylist;
	}
}
