package cn.leo.dcinema.player;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import cn.leo.dcinema.R;
import cn.leo.dcinema.model.VideoSource;
import cn.leo.dcinema.util.StringUtil;
import java.util.ArrayList;

public class VodCtrTop extends PopupWindow {
	private static final int HIDE_DELAY = 8000;

	public VodCtrTop(Context context, Handler handler) {
		this(context, handler, HIDE_DELAY);
	}

	public VodCtrTop(Context context, Handler handler, int delayMS) {
		super(context);
		autoHide = new AutoHideRunable();
		mContetx = context;
		mHandler = handler;
		autoHideDelay = delayMS;
		init();
	}

	private void createChildViews(ArrayList<VideoSource> arraylist) {
		for (int i = 0; i < arraylist.size(); i++) {
			VideoSource videosource = arraylist.get(i);
			ImageView imageview = new ImageView(mContetx);
			imageview.setFocusable(true);
			imageview.setFocusableInTouchMode(true);
			imageview
					.setBackgroundResource(R.drawable.vod_ctrtop_list_item_source_bg);
			imageview.setImageResource(StringUtil
					.getSourceTag(videosource.sourceName));
			imageview.setTag(Integer.valueOf(i));
			imageview
					.setOnFocusChangeListener(new VodCtrTopOnFocusChangeListener());
			imageview.setOnClickListener(new VodCtrTopOnClickListener());
			LinearLayout.LayoutParams layoutparams = new LinearLayout.LayoutParams(
					-2, -2);
			layoutparams.setMargins(15, 0, 15, 0);
			listSource.addView(imageview, i, layoutparams);
		}
	}

	public void dismiss() {
		mHandler.removeCallbacks(autoHide);
		super.dismiss();
	}

	public void init() {
		setBackgroundDrawable(mContetx.getResources().getDrawable(
				android.R.color.transparent));
		setFocusable(true);
		setWindowLayoutMode(-2, -2);
		View view = ((LayoutInflater) mContetx
				.getSystemService("layout_inflater")).inflate(
				R.layout.vod_ctrtop_layout, null);
		txtName = (TextView) view.findViewById(R.id.vod_ctrtop_layout_name_txt);
		listSource = (LinearLayout) view
				.findViewById(R.id.vod_ctrtop_layout_source_horlist);
		setContentView(view);
	}

	public void setSelectionAndFocus(int i) {
		for (int j = 0; j < sources.size(); j++) {
			listSource.getChildAt(j).setSelected(false);
		}
		listSource.getChildAt(i).setSelected(true);
		listSource.getChildAt(i).requestFocus();
	}

	public void setVideoName(String s) {
		txtName.setText(s);
	}

	public void setVideoSources(ArrayList<VideoSource> arraylist) {
		if (arraylist == null)
			sources = new ArrayList<VideoSource>();
		else
			sources = arraylist;
		createChildViews(sources);
	}

	public void showAtLocation(View view, int i, int j, int k) {
		mHandler.removeCallbacks(autoHide);
		mHandler.postDelayed(autoHide, autoHideDelay);
		super.showAtLocation(view, i, j, k);
	}

	Runnable autoHide;
	private int autoHideDelay;
	private LinearLayout listSource;
	private Context mContetx;
	private Handler mHandler;
	private ArrayList<VideoSource> sources;
	private TextView txtName;

	private class AutoHideRunable implements Runnable {

		public void run() {
			dismiss();
		}
	}

	private class VodCtrTopOnFocusChangeListener implements
			View.OnFocusChangeListener {

		public void onFocusChange(View view, boolean flag) {
			if (flag) {
				mHandler.removeCallbacks(autoHide);
				mHandler.postDelayed(autoHide, autoHideDelay);
			}
		}
	}

	private class VodCtrTopOnClickListener implements View.OnClickListener {

		public void onClick(View view) {
			int i = ((Integer) view.getTag()).intValue();
			mHandler.sendMessage(mHandler.obtainMessage(10, i, 0));
			dismiss();
		}
	}

}