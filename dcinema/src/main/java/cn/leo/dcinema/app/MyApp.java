package cn.leo.dcinema.app;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import cn.leo.dcinema.Constants;
import cn.leo.dcinema.R;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

public class MyApp extends Application {

	private static final String TAG = "MyApp";


	private void initSound() {
		soundMap = new HashMap<String, Integer>();
		soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
		soundPool
				.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {

					@Override
					public void onLoadComplete(SoundPool arg0, int arg1,
							int arg2) {
						MyApp.loaded = true;
					}

				});
		soundMap.put(Constants.SOUND_BACK,
				Integer.valueOf(soundPool.load(this, R.raw.back, 1)));
		soundMap.put(Constants.SOUND_BACK_TO_TOP,
				Integer.valueOf(soundPool.load(this, R.raw.back_to_top, 1)));
		soundMap.put(Constants.SOUND_COMFIRE,
				Integer.valueOf(soundPool.load(this, R.raw.comfire, 1)));
		soundMap.put(Constants.SOUND_MOVE_BOTTOM,
				Integer.valueOf(soundPool.load(this, R.raw.move_bottom, 1)));
		soundMap.put(Constants.SOUND_MOVE_LEFT,
				Integer.valueOf(soundPool.load(this, R.raw.move_left, 1)));
		soundMap.put(Constants.SOUND_MOVE_RIGHT,
				Integer.valueOf(soundPool.load(this, R.raw.move_right, 1)));
		soundMap.put(Constants.SOUND_NET_CONNECTED,
				Integer.valueOf(soundPool.load(this, R.raw.net_connected, 1)));
		soundMap.put(Constants.SOUND_NET_FOUND,
				Integer.valueOf(soundPool.load(this, R.raw.net_found, 1)));
		soundMap.put(Constants.SOUND_TOP_FLOAT_DISABLED, Integer
				.valueOf(soundPool.load(this, R.raw.top_float_disabled, 1)));
		soundMap.put(Constants.SOUND_TOP_FLOAT,
				Integer.valueOf(soundPool.load(this, R.raw.top_float, 1)));
		soundMap.put(Constants.SOUND_TOP_PAGE_MOVE,
				Integer.valueOf(soundPool.load(this, R.raw.page_change, 1)));
	}

	public static void playSound(String s) {
		AudioManager audiomanager = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE);
		float f = (float) audiomanager.getStreamVolume(3)
				/ (float) audiomanager.getStreamMaxVolume(3);
		if (loaded && sp.getBoolean(Constants.KEY_PLAY_SOUND, true))
			soundPool.play(((Integer) soundMap.get(s)).intValue(), f, f, 1, 0,
					1.0F);
	}

	public static void setApkRunTime(int i) {
		android.content.SharedPreferences.Editor editor = sp.edit();
		editor.putInt(Constants.KEY_APK_RUNTIMES, i);
		editor.commit();
	}

	public static void setBase(String s) {
		baseServer = s;
		android.content.SharedPreferences.Editor editor = sp.edit();
		editor.putString(Constants.KEY_SERVER, s);
		editor.commit();
	}

	public static void setChanState(boolean flag) {
		android.content.SharedPreferences.Editor editor = sp.edit();
		editor.putBoolean(Constants.KEY_USER_DEFAULT, flag);
		editor.commit();
	}

	public static void setScaleMod(int i) {
		scaleMod = i;
		android.content.SharedPreferences.Editor editor = sp.edit();
		editor.putInt(Constants.KEY_SCALEMOD, i);
		editor.commit();
	}

	public static void setSharpness(int i) {
		sharpness = i;
		android.content.SharedPreferences.Editor editor = sp.edit();
		editor.putInt(Constants.KEY_SHARPNESS, i);
		editor.commit();
	}

	public static String getLiveSrvAddr() {
		return sp.getString(Constants.KEY_LIVEADDR, null);
	}

	public static void setLiveSrvAddr(String addr) {
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(Constants.KEY_LIVEADDR, addr);
		editor.commit();
	}

	public static String getAggreSrvAddr() {
		return sp.getString(Constants.KEY_AGGREADDR, null);
	}

	public static void setAggreSrvAddr(String addr) {
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(Constants.KEY_AGGREADDR, addr);
		editor.commit();
	}

	public static String getVodSrvAddr() {
		return sp.getString(Constants.KEY_VODADDR, null);
	}

	public static void setVodSrvAddr(String addr) {
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(Constants.KEY_VODADDR, addr);
		editor.commit();
	}

	public static String getBroswerSrvAddr() {
		return sp.getString(Constants.KEY_BROWSER_ADDR, null);
	}

	public static void setBroswerSrvAddr(String addr) {
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(Constants.KEY_BROWSER_ADDR, addr);
		editor.commit();
	}

	public void onCreate() {

		super.onCreate();
		context = this;
		pool = Executors.newFixedThreadPool(2);
		sp = getApplicationContext().getSharedPreferences(
				Constants.KEY_SETTING_SPF, MODE_PRIVATE);
		baseServer = sp.getString(Constants.KEY_SERVER, Constants.VST_SRVADD);

		scaleMod = sp.getInt(Constants.KEY_SCALEMOD, 2);
		sharpness = sp.getInt(Constants.KEY_SHARPNESS, 0);

		liveSrvAddr = sp.getString(Constants.KEY_LIVEADDR, null);
		// 读出预设值，如果没有就保存
		setdefalut();
		Log.d(TAG, "liveSrvAddr[" + liveSrvAddr);
		Log.d(TAG, "baseSerber[" + baseServer);
		initSound();
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		pool.isTerminated();
	}

	public static String baseServer;
	public static String liveSrvAddr;
	private static Context context;
	public static SQLiteOpenHelper dbHeper;
	private static boolean loaded = false;
	public static ExecutorService pool = null;
	public static int scaleMod;
	public static int sharpness;
	private static HashMap<String, Integer> soundMap;
	private static SoundPool soundPool;
	public static SharedPreferences sp;

	//
	public static boolean aggreonOff = true;
	public static boolean vodonOff = true;
	public static boolean liveonOff = true;
	public static boolean localonOff = true;
	public static boolean browseronOff = false;

	private void setdefalut() {
		if (sp != null) {
			aggreonOff = sp.getBoolean(Constants.KEY_AGGRENON_OFF, true);
			vodonOff = sp.getBoolean(Constants.KEY_VODON_OFF, true);
			liveonOff = sp.getBoolean(Constants.KEY_LIVEON_OFF, true);
			localonOff = sp.getBoolean(Constants.KEY_LOCALON_OFF, true);
			browseronOff = sp.getBoolean(Constants.KEY_BROWSERON_OFF, false);
		}
		setAggreonOff(aggreonOff);
		setVodonOff(vodonOff);
		setLiveOff(liveonOff);
		setLocalOff(localonOff);
		setBrowserOff(browseronOff);

		if (getAggreSrvAddr() == null) {
			setAggreSrvAddr("http://tv1.hdpfans.com/~rss.get.channel/site/hdp/rss/1/xml/1/ajax/1/key/bc54f4d60f1cec0f9a6cb70e13f2127a");
		}
	}

	public static void setAggreonOff(boolean onOff) {
		SharedPreferences.Editor editor = sp.edit();
		editor.putBoolean(Constants.KEY_AGGRENON_OFF, onOff);
		editor.commit();
	}

	public static boolean getAggreonOff() {
		return sp.getBoolean(Constants.KEY_AGGRENON_OFF, false);
	}

	public static void setVodonOff(boolean onOff) {
		SharedPreferences.Editor editor = sp.edit();
		editor.putBoolean(Constants.KEY_VODON_OFF, onOff);
		editor.commit();
	}

	public static boolean getVodonOff() {
		return sp.getBoolean(Constants.KEY_VODON_OFF, false);
	}

	public static void setLiveOff(boolean onOff) {
		SharedPreferences.Editor editor = sp.edit();
		editor.putBoolean(Constants.KEY_LIVEON_OFF, onOff);
		editor.commit();
	}

	public static boolean getLiveOff() {
		return sp.getBoolean(Constants.KEY_LIVEON_OFF, false);
	}

	public static void setBrowserOff(boolean onOff) {
		SharedPreferences.Editor editor = sp.edit();
		editor.putBoolean(Constants.KEY_BROWSERON_OFF, onOff);
		editor.commit();
	}

	public static boolean getBrowserOff() {
		return sp.getBoolean(Constants.KEY_BROWSERON_OFF, false);
	}

	public static void setLocalOff(boolean onOff) {
		SharedPreferences.Editor editor = sp.edit();
		editor.putBoolean(Constants.KEY_LOCALON_OFF, onOff);
		editor.commit();
	}

	public static boolean getLocalOff() {
		return sp.getBoolean(Constants.KEY_LOCALON_OFF, false);
	}

}