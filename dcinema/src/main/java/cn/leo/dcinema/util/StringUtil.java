package cn.leo.dcinema.util;

import java.util.Locale;
import android.util.Log;
import cn.leo.dcinema.R;
import cn.leo.dcinema.app.MyApp;

public class StringUtil {

	public StringUtil() {
	}

	public static String getCateUrl() {
		String s = MyApp.baseServer;
		if (s != null)
			System.out.println(s);
		else
			s = null;
		return s;
	}

	public static String getDetailUrl(int i) {
		String s = MyApp.baseServer;
		String s1;
		if (s != null) {
			System.out.println(s);
			s1 = (new StringBuilder(String.valueOf(s)))
					.append("?data=info&id=").append(i).toString();
		} else {
			s1 = null;
		}
		return s1;
	}

	public static String getNewsUrl() {
		String s = MyApp.baseServer;
		String s1;
		if (s != null) {
			System.out.println(s);
			s1 = (new StringBuilder(String.valueOf(s))).append(
					"?data=play_newslist").toString();
		} else {
			s1 = null;
		}
		return s1;
	}

	public static String getSearchUrl(String s) {
		String s1 = MyApp.baseServer;
		String s2;
		if (s1 != null) {
			System.out.println(s1);
			s2 = (new StringBuilder(String.valueOf(s1))).append("?data=so&wd=")
					.append(s).toString();
		} else {
			s2 = null;
		}
		return s2;
	}

	public static int getSharpByName(String s) {
		int i = R.drawable.osd_sd_selector;
		if (!s.equals("超清")) {
			if (s.equals("标清"))
				i = R.drawable.osd_biaohd_selector;
			else if (!s.equals("流畅"))
				if (s.equals("高清"))
					i = R.drawable.osd_hd_selector;
				else if (s.equals("蓝光"))
					i = R.drawable.osd_blue_selector;
		} else {
			i = R.drawable.osd_superhd_selector;
		}
		return i;
	}

	public static int getSourceTag(String s) {
		int i;
		if (s.contains("迅雷离线") || s.contains("lixian"))
			i = R.drawable.source_xunleilx_focus;
		else if (s.contains("优酷") || s.contains("youku"))
			i = R.drawable.source_youku_focus;
		else if (s.contains("风行") || s.contains("funshion"))
			i = R.drawable.source_funshion_focus;
		else if (s.contains("凤凰") || s.contains("ifeng"))
			i = R.drawable.source_ifeng_focus;
		else if (s.contains("酷6") || s.contains("ku6"))
			i = R.drawable.source_ku6_focus;
		else if (s.contains("乐视") || s.contains("letv"))
			i = R.drawable.source_letv_focus;
		else if (s.contains("电影网") || s.contains("dianying")
				|| s.contains("m1905"))
			i = R.drawable.source_dianying_focus;
		else if (s.contains("网易") || s.contains("163") || s.contains("wangyi"))
			i = R.drawable.source_163_focus;
		else if (s.contains("PPS") || s.contains("pps"))
			i = R.drawable.source_pps_focus;
		else if (s.contains("PPTV") || s.contains("pptv"))
			i = R.drawable.source_pptv_focus;
		else if (s.contains("奇艺") || s.contains("qiyi"))
			i = R.drawable.source_iqiyi_focus;
		else if (s.contains("新浪") || s.contains("sina"))
			i = R.drawable.source_sina_focus;
		else if (s.contains("搜狐") || s.contains("sohu"))
			i = R.drawable.source_sohu_focus;
		else if (s.contains("56"))
			i = R.drawable.source_56_focus;
		else if (s.contains("腾讯") || s.contains("QQ") || s.contains("qq"))
			i = R.drawable.source_qq_focus;
		else if (s.contains("土豆") || s.contains("tudou"))
			i = R.drawable.source_tudou_focus;
		else if (s.contains("电驴") || s.contains("dianlv"))
			i = R.drawable.source_dianlv_focus;
		else if (s.contains("TV189") || s.contains("天翼"))
			i = R.drawable.source_tv189_focus;
		else if (s.contains("优米") || s.contains("umi"))
			i = R.drawable.source_umi_focus;
		else if (s.contains("迅雷") || s.contains("xunlei")
				|| s.contains("kankan") || s.contains("\u770B\u770B"))
			i = R.drawable.source_xunlei_focus;
		else if (s.contains("音乐台") || s.contains("yinyuetai"))
			i = R.drawable.source_yinyuetai_focus;
		else if (s.contains("CNTV") || s.contains("cntv"))
			i = R.drawable.source_cntv_focus;
		else
			i = R.drawable.source_other_focus;
		return i;
	}

	public static String getTypeUrl(String s) {
		String s1 = MyApp.baseServer;
		String s2;
		if (s1 != null) {
			System.out.println(s1);
			s2 = (new StringBuilder(String.valueOf(s1))).append("?data=so&wd=")
					.append(s).toString();
		} else {
			s2 = null;
		}
		return s2;
	}

	public static int[] getWeaResByWeather(String s) {
		String as[] = s.split("转|到");
		int ai[] = new int[as.length];
		int i = 0;
		do {
			if (i >= as.length) {
				if (ai.length == 3) {
					if (ai[0] == 0) {
						int ai2[] = new int[2];
						ai2[0] = ai[1];
						ai2[1] = ai[2];
						ai = ai2;
					}
				} else if (ai.length == 1) {
					int ai1[] = new int[2];
					ai1[0] = ai[0];
					ai1[1] = 0;
					ai = ai1;
				}
				return ai;
			}
			ai[i] = getWeaResByWeather1(as[i]);
			Log.i("info", (new StringBuilder("拆分后的天气：")).append(as[i])
					.toString());
			i++;
		} while (true);
	}

	public static int getWeaResByWeather1(String s) {
		int i = R.drawable.ic_weather_heavy_rain_l;
		if (!s.equals("阴")) {
			if (s.equals("多云"))
				i = R.drawable.ic_weather_partly_cloudy_l;
			else if (s.equals("晴"))
				i = R.drawable.ic_weather_clear_day_l;
			else if (s.equals("小雨"))
				i = R.drawable.ic_weather_chance_of_rain_l;
			else if (s.equals("中雨"))
				i = R.drawable.ic_weather_rain_xl;
			else if (!s.equals("大雨") && !s.equals("暴雨") && !s.equals("大暴雨")
					&& !s.equals("特大暴雨"))
				if (s.equals("阵雨"))
					i = R.drawable.ic_weather_chance_storm_l;
				else if (s.equals("雷阵雨"))
					i = R.drawable.ic_weather_thunderstorm_l;
				else if (s.equals("小雪"))
					i = R.drawable.ic_weather_chance_snow_l;
				else if (s.equals("中雪"))
					i = R.drawable.ic_weather_flurries_l;
				else if (s.equals("大雪"))
					i = R.drawable.ic_weather_snow_l;
				else if (s.equals("暴雪"))
					i = R.drawable.ic_weather_snow_l;
				else if (s.equals("冰雹"))
					i = R.drawable.ic_weather_icy_sleet_l;
				else if (s.equals("雨夹雪"))
					i = R.drawable.ic_weather_icy_sleet_l;
				else if (s.equals("风"))
					i = R.drawable.ic_weather_windy_l;
				else if (s.equals("龙卷风"))
					i = R.drawable.ic_weather_windy_l;
				else if (s.equals("雾"))
					i = R.drawable.ic_weather_fog_l;
				else
					i = 0;
		} else {
			i = R.drawable.ic_weather_cloudy_l;
		}
		return i;
	}

	public static int sourceStringToResourceID(String s) {
		int i;
		if (s.contains("迅雷离线") || s.contains("lixian"))
			i = 2130838011;
		else if (s.contains("优酷") || s.contains("youku"))
			i = 2130838017;
		else if (s.contains("风行") || s.contains("funshion"))
			i = 2130837966;
		else if (s.contains("凤凰") || s.contains("ifeng"))
			i = 2130837969;
		else if (s.contains("酷6") || s.contains("ku6"))
			i = 2130837975;
		else if (s.contains("乐视") || s.contains("letv"))
			i = 2130837978;
		else if (s.contains("电影网") || s.contains("dianying")
				|| s.contains("m1905"))
			i = 2130837963;
		else if (s.contains("网易") || s.contains("163") || s.contains("wangyi"))
			i = 2130837949;
		else if (s.contains("PPS") || s.contains("pps"))
			i = 2130837984;
		else if (s.contains("PPTV") || s.contains("pptv"))
			i = 2130837987;
		else if (s.contains("奇艺") || s.contains("qiyi"))
			i = 2130837972;
		else if (s.contains("新浪") || s.contains("sina"))
			i = 2130837993;
		else if (s.contains("搜狐") || s.contains("sohu"))
			i = 2130837996;
		else if (s.contains("56"))
			i = 2130837952;
		else if (s.contains("腾讯") || s.contains("QQ") || s.contains("qq"))
			i = 2130837990;
		else if (s.contains("土豆") || s.contains("tudou"))
			i = 2130837999;
		else if (s.contains("电驴") || s.contains("dianlv"))
			i = 2130837960;
		else if (s.contains("TV189") || s.contains("天翼"))
			i = 2130838002;
		else if (s.contains("优米") || s.contains("umi"))
			i = 2130838005;
		else if (s.contains("迅雷") || s.contains("xunlei")
				|| s.contains("kankan") || s.contains("看看"))
			i = 2130838008;
		else if (s.contains("音乐台") || s.contains("yinyuetai"))
			i = 2130838014;
		else if (s.contains("CNTV") || s.contains("cntv"))
			i = 2130837957;
		else
			i = 2130837981;
		return i;
	}

	public static String stringForTime(int i) {
		int j = i / 1000;
		int k = j % 60;
		int l = (j / 60) % 60;
		int i1 = j / 3600;
		Object aobj[] = new Object[3];
		aobj[0] = Integer.valueOf(i1);
		aobj[1] = Integer.valueOf(l);
		aobj[2] = Integer.valueOf(k);
		return String.format(Locale.getDefault(), "%02d:%02d:%02d", aobj);
	}
}
