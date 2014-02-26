package cn.leo.dcinema.player;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import cn.leo.dcinema.R;
import cn.leo.dcinema.model.SharpnessEnum;
import cn.leo.dcinema.model.VideoPlayUrl;
import cn.leo.dcinema.util.BitmapWorkerTask;
import cn.leo.dcinema.util.StringUtil;
import java.util.ArrayList;

public class VodCtrBot extends PopupWindow {
	public VodCtrBot(Context context, VideoView videoview, Handler handler) {
		super(context);
		mDragging = false;
		hide = new _cls1();
		updateProgress = new _cls2();
//		rbFocus = new _cls3();
		mContetx = context;
		mVideoView = videoview;
		mHandler = handler;
		init();
	}

	private int setProgress() {
		int i = 0;
		if (mVideoView != null && !mDragging && mVideoView.isPlaying()) {
			i = mVideoView.getCurrentPosition();
			duration = mVideoView.getDuration();
			long l = (1000L * (long) i) / (long) duration;
			seekBar.setProgress((int) l);
			// setTipTimeLocation(l);
			int j = mVideoView.getBufferPercentage();
			seekBar.setSecondaryProgress(j * 10);
			mEndTime.setText(StringUtil.stringForTime(duration));
			mCurrentTime.setText(StringUtil.stringForTime(i));
			// tipTime.setText(StringUtil.stringForTime(i));
		}
		return i;
	}

	// private void setTipTimeLocation(long l) {
	// int ai[] = new int[2];
	// contentView.getLocationOnScreen(ai);
	// int i = ai[1];
	// seekBar.getLocationOnScreen(ai);
	// FrameLayout.LayoutParams layoutparams = new FrameLayout.LayoutParams(
	// -2, -2);
	// layoutparams
	// .setMargins(
	// (10 + ((int) ((l * (long) (-20 + seekBar.getWidth())) / 1000L) + ai[0]))
	// - tipTime.getWidth() / 2, -5
	// + (ai[1] - i - tipTime.getHeight()), 0, 0);
	// tipTime.setLayoutParams(layoutparams);
	// }

	private void updatePausePlay() {
		if (mVideoView.isPlaying())
			playOrPause.setImageResource(R.drawable.osd_pause_selector);
		else
			playOrPause.setImageResource(R.drawable.osd_play_selector);
	}

	public void dismiss() {
		super.dismiss();
		mDragging = false;
		mHandler.removeCallbacks(hide);
		mHandler.removeCallbacks(updateProgress);
	}

	public void doPauseResume() {
		if (this != null && isShowing()) {
			if (mVideoView.isPlaying()) {
				mVideoView.pause();
				mHandler.removeCallbacks(hide);
			} else {
				mVideoView.start();
				mHandler.postDelayed(hide, 5000L);
			}
			updatePausePlay();
		}
	}

	public void hide() {
	}

	@SuppressWarnings("deprecation")
	public void init() {
		setBackgroundDrawable(new BitmapDrawable());
		setFocusable(true);
		setWindowLayoutMode(-1, -2);
		contentView = ((LayoutInflater) mContetx
				.getSystemService("layout_inflater")).inflate(
				R.layout.vod_ctrbot_layout, null);
		// postiv = (ImageView)
		// contentView.findViewById(R.id.play_bottom_poster);
		name = (TextView) contentView.findViewById(R.id.play_bottom_videoName);
		sourceTag = (ImageView) contentView
				.findViewById(R.id.play_bottom_current_source);
		scaleTag = (ImageView) contentView
				.findViewById(R.id.play_bottom_screen_scale);
//		sharp = (ImageView) contentView
//				.findViewById(R.id.play_bottom_sharpness);
		seekBar = (SeekBar) contentView.findViewById(R.id.play_bottom_seekbar);
		seekBar.setOnFocusChangeListener(new _cls4());
		seekBar.setOnKeyListener(new _cls5());
		seekBar.setOnClickListener(null);
		mEndTime = (TextView) contentView
				.findViewById(R.id.play_bottom_duration);
		mCurrentTime = (TextView) contentView
				.findViewById(R.id.play_bottom_current_time);
		// tipTime = (TextView) contentView
		// .findViewById(R.id.play_bottom_tip_time);
//		rewind = (ImageView) contentView.findViewById(R.id.play_bottom_rewind);
//		playOrPause = (ImageView) contentView
//				.findViewById(R.id.play_bottom_play_pause);
//		forward = (ImageView) contentView
//				.findViewById(R.id.play_bottom_forward);
//		sharpness = (ImageView) contentView
//				.findViewById(R.id.play_bottom_sharpness_iv);
//		sharpnessRg = (RadioGroup) contentView
//				.findViewById(R.id.play_bottom_sharpness_choose);
//		subTitles = (ImageView) contentView
//				.findViewById(R.id.play_bottom_subtitles);
//		playOrPause.setOnClickListener(new _cls6());
//		sharpness.setOnFocusChangeListener(new _cls7());
//		sharpnessRg.setOnCheckedChangeListener(new _cls8());
		// setTipTimeLocation(1L);
		setContentView(contentView);
	}

	public void playOrPauseRequesFocus() {
		playOrPause.requestFocus();
	}

	public void seekBarRequestFocus() {
		seekBar.requestFocus();
	}

	protected void setDraggingProgress(int i) {
		if (!mDragging && mVideoView.isPlaying())
			draggingDura = mVideoView.getCurrentPosition();
		mDragging = true;
		if (duration > 0) {
			switch (i) {
			case 100:
				draggingDura = INCREMENTMS + draggingDura;
				break;
			case 101:
				draggingDura = -INCREMENTMS + draggingDura;
				break;
			default:
				break;
			}
		}
		long l;
		if (draggingDura < 0)
			draggingDura = 0;
		else if (draggingDura > duration)
			draggingDura = duration;
		l = (1000L * (long) draggingDura) / (long) duration;
		seekBar.setProgress((int) l);
		// setTipTimeLocation(l);
		mCurrentTime.setText(StringUtil.stringForTime(draggingDura));
		// tipTime.setText(StringUtil.stringForTime(draggingDura));
	}

//	public void setHdSdTag(int i) {
//		if (i >= 1080)
//			sharp.setImageResource(R.drawable.osd_1080p);
//		else if (1080 > i && i >= 720)
//			sharp.setImageResource(R.drawable.osd_720p);
//		else
//			sharp.setImageResource(R.drawable.osd_sdp);
//	}

	// public void setPost(String s) {
	// BitmapWorkerTask bitmapworkertask = new BitmapWorkerTask(mContetx,
	// postiv, false, false);
	// String as[] = new String[1];
	// as[0] = s;
	// bitmapworkertask.execute(as);
	// }

	public void setScaleTag(int i) {
		if (scaleTag != null) {
			switch (i) {
			case 0:
				scaleTag.setImageResource(R.drawable.osd_16_9);
				break;
			case 1:
				scaleTag.setImageResource(R.drawable.osd_4_3);
				break;
			case 2:
				scaleTag.setImageResource(R.drawable.osd_default);
				break;
			case 4:
				scaleTag.setImageResource(R.drawable.osd_raw);
				break;
			default:
				break;
			}
		}
	}

//	@SuppressWarnings("deprecation")
//	public void setSharpness(ArrayList<?> arraylist, SharpnessEnum sharpnessenum) {
//		urls = arraylist;
//		sharpness.setImageResource(StringUtil.getSharpByName(sharpnessenum
//				.getName()));
//		sharpnessRg.removeAllViews();
//		for (int i = 0; i < arraylist.size(); i++) {
//			if (((VideoPlayUrl) arraylist.get(i)).sharp != sharpnessenum) {
//				RadioButton radiobutton = new RadioButton(mContetx);
//				radiobutton.setButtonDrawable(new BitmapDrawable());
//				radiobutton.setBackgroundResource(StringUtil
//						.getSharpByName(((VideoPlayUrl) arraylist.get(i)).sharp
//								.getName()));
//				radiobutton.setTag(arraylist.get(i));
//				radiobutton.setOnFocusChangeListener(rbFocus);
//				sharpnessRg.addView(radiobutton);
//				LinearLayout.LayoutParams layoutparams = new LinearLayout.LayoutParams(
//						-2, -2);
//				layoutparams.leftMargin = 10;
//				radiobutton.setLayoutParams(layoutparams);
//			}
//		}
//	}

	public void setSourceTag(int i) {
		if (sourceTag != null)
			sourceTag.setImageResource(i);
	}

	public void setVideoName(String s) {
		name.setText(s);
	}

	public void show() {
//		updatePausePlay();
		setProgress();
		showAtLocation(mVideoView, 80, 0, 0);
		mHandler.post(updateProgress);
		mHandler.removeCallbacks(hide);
		mHandler.postDelayed(hide, TIMEOUT);
	}

	protected static final int FFWD_FLAG = 100;
	private static final int INCREMENTMS = 30000;
	protected static final int REW_FLAG = 101;
	@SuppressWarnings("unused")
	private static final String TAG = "vodbot";
	private static final int TIMEOUT = 5000;
	@SuppressWarnings("unused")
	View contentView;
	private int draggingDura;
	private int duration;
	@SuppressWarnings("unused")
	private ImageView forward;
	private Runnable hide;
	private Context mContetx;
	private TextView mCurrentTime;
	private boolean mDragging;
	private TextView mEndTime;
	private Handler mHandler;
	private VideoView mVideoView;
	private TextView name;
	private ImageView playOrPause;
	// private ImageView postiv;
	private View.OnFocusChangeListener rbFocus;
	@SuppressWarnings("unused")
	private ImageView rewind;
	private ImageView scaleTag;
	private SeekBar seekBar;
//	private ImageView sharp;
//	private ImageView sharpness;
	private RadioGroup sharpnessRg;
	private ImageView sourceTag;
	@SuppressWarnings("unused")
	private ImageView subTitles;
	// private TextView tipTime;
	private Runnable updateProgress;
	private ArrayList<?> urls;

	private class _cls1 implements Runnable {

		public void run() {
			if (isShowing())
				dismiss();
		}
	}

	private class _cls2 implements Runnable {

		public void run() {
			int i = 0;
			if (isShowing() && mVideoView.isPlaying()) {
				i = mVideoView.getCurrentPosition();
				if (!mDragging)
					setProgress();
				else
					mCurrentTime.setText(StringUtil.stringForTime(i));
			}
			mHandler.postDelayed(updateProgress, 1000 - i % 1000);
		}
	}

//	private class _cls3 implements View.OnFocusChangeListener {
//
//		public void onFocusChange(View view, boolean flag) {
//			if (flag || sharpness.hasFocus()) {
//
//			} else {
//				boolean flag1;
//
//				flag1 = false;
//
//				for (int i = 0; i < sharpnessRg.getChildCount(); i++) {
//					flag1 |= sharpnessRg.getChildAt(i).hasFocus();
//				}
//
//				if (!flag1)
//					sharpnessRg.setVisibility(View.INVISIBLE);
//			}
//		}
//	}

	private class _cls4 implements android.view.View.OnFocusChangeListener {

		public void onFocusChange(View view, boolean flag) {
			if (flag) {
				mHandler.removeCallbacks(hide);
				mHandler.postDelayed(hide, TIMEOUT);
				// tipTime.setVisibility(0);
			} else {
				// tipTime.setVisibility(4);
			}
		}
	}

	private class _cls5 implements View.OnKeyListener {

		public boolean onKey(View view, int i, KeyEvent keyevent) {
			boolean flag = true;
			mHandler.removeCallbacks(hide);
			mHandler.postDelayed(hide, TIMEOUT);
			if (keyevent.getAction() != 0 || i != 21) {
				if (keyevent.getAction() == 0 && i == 22)
					setDraggingProgress(100);
				else if (keyevent.getAction() == 1 && (i == 22 || i == 21)) {
					if (mDragging && mVideoView.isPlaying()) {
						mVideoView.seekTo(draggingDura);
						mDragging = false;
					}
				} else {
					flag = false;
				}
			} else {
				setDraggingProgress(101);
			}
			return flag;
		}
	}

	private class _cls6 implements View.OnClickListener {

		public void onClick(View view) {
			mHandler.removeCallbacks(hide);
			mHandler.postDelayed(hide, TIMEOUT);
			doPauseResume();
		}
	}

	private class _cls7 implements android.view.View.OnFocusChangeListener {

		public void onFocusChange(View view, boolean flag) {
			if (!flag) {
				if (!flag && !sharpnessRg.hasFocus())
					sharpnessRg.setVisibility(View.INVISIBLE);
			} else {
				mHandler.removeCallbacks(hide);
				mHandler.postDelayed(hide, TIMEOUT);
				sharpnessRg.setVisibility(View.VISIBLE);
			}
		}

	}

	private class _cls8 implements
			android.widget.RadioGroup.OnCheckedChangeListener {

		@SuppressWarnings("deprecation")
		public void onCheckedChanged(RadioGroup radiogroup, int i) {
			mHandler.removeCallbacks(hide);
			mHandler.postDelayed(hide, TIMEOUT);
			RadioButton radiobutton = (RadioButton) radiogroup.findViewById(i);
			VideoPlayUrl videoplayurl = (VideoPlayUrl) radiobutton.getTag();
			int j = urls.indexOf(videoplayurl);
			VideoPlayUrl videoplayurl1 = (VideoPlayUrl) urls.get(j);
//			sharpness.setImageResource(StringUtil
//					.getSharpByName(videoplayurl1.sharp.getName()));
			sharpnessRg.removeAllViews();
			for (int k = 0; k < urls.size(); k++) {
				if (k != j) {
					RadioButton radiobutton1 = new RadioButton(mContetx);
					radiobutton1.setButtonDrawable(new BitmapDrawable());
					radiobutton1.setBackgroundResource(StringUtil
							.getSharpByName(((VideoPlayUrl) urls.get(k)).sharp
									.getName()));
					radiobutton1.setTag(urls.get(k));
					radiobutton.setOnFocusChangeListener(rbFocus);
					sharpnessRg.addView(radiobutton1);
					LinearLayout.LayoutParams layoutparams = new LinearLayout.LayoutParams(
							-2, -2);
					layoutparams.leftMargin = 10;
					radiobutton1.setLayoutParams(layoutparams);
				}
			}
			sharpnessRg.setVisibility(View.INVISIBLE);
			mHandler.sendMessage(mHandler.obtainMessage(12,
					mVideoView.getCurrentPosition(), 0, videoplayurl1.playurl));
		}
	}
}
