package cn.leo.dcinema.player;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import cn.leo.dcinema.R;

public class VodSeek extends PopupWindow {
	public VodSeek(Context context, VideoView videoview, Handler handler) {
		super(context);
		mDragging = false;
		hide = new _cls1();
		UPDATE_PROGRESS = new _cls2();
		mContext = context;
		mVideoView = videoview;
		mHandler = handler;
		init();
	}

	private String stringForTime(int i) {
		int j = i / 1000;
		int k = j % 60;
		int l = (j / 60) % 60;
		int i1 = j / 3600;
		String s;
		if (i1 > 0) {
			Object aobj1[] = new Object[3];
			aobj1[0] = Integer.valueOf(i1);
			aobj1[1] = Integer.valueOf(l);
			aobj1[2] = Integer.valueOf(k);
			s = String.format("%d:%02d:%02d", aobj1);
		} else {
			Object aobj[] = new Object[2];
			aobj[0] = Integer.valueOf(l);
			aobj[1] = Integer.valueOf(k);
			s = String.format("%02d:%02d", aobj);
		}
		return s;
	}

	public void dismiss() {
		mHandler.removeCallbacks(UPDATE_PROGRESS);
		mHandler.removeCallbacks(hide);
		mDragging = false;
		super.dismiss();
	}

	@SuppressWarnings("deprecation")
	public void init() {
		inflater = (LayoutInflater) mContext
				.getSystemService("layout_inflater");
		setFocusable(true);
		setBackgroundDrawable(new BitmapDrawable());
		setWindowLayoutMode(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		contentView = inflater.inflate(R.layout.seekbar, null);
		FrameLayout.LayoutParams layoutparams = new FrameLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		layoutparams.gravity = Gravity.BOTTOM;
		contentView.setLayoutParams(layoutparams);
		contentView.setFocusable(true);
		((ViewGroup) contentView)
				.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
		contentView.setOnKeyListener(new _cls3());
		seekBar = (SeekBar) contentView.findViewById(R.id.seekbar);
		mEndTime = (TextView) contentView.findViewById(R.id.mEndTime);
		mCurrentTime = (TextView) contentView.findViewById(R.id.mCurrentTime);
		tv_name = (TextView) contentView.findViewById(R.id.news_name);
		setContentView(contentView);
	}

	protected void setDraggingProgress(int i) {
		mHandler.removeCallbacks(hide);
		mHandler.postDelayed(hide, TIMEOUT);
		if (!mDragging && mVideoView.isPlaying()) {
			mDragging = true;
			System.out.println(mDragging);
			draggingPosition = mVideoView.getCurrentPosition();
		}
		if (duration > 0) {
			switch (i) {
			case FFWD_FLAG:
				draggingPosition = draggingPosition + incrementMs;
				break;
			case REW_FLAG:
				draggingPosition = draggingPosition - incrementMs;
				break;

			default:
				return;
			}
			
			if (draggingPosition < 0)
				draggingPosition = 0;
			else if (draggingPosition > duration)
				draggingPosition = duration;
			long l = (1000L * (long) draggingPosition) / (long) duration;
			seekBar.setProgress((int) l);
		}
		if (mEndTime != null)
			mEndTime.setText(stringForTime(duration));
		if (mCurrentTime != null)
			mCurrentTime.setText(stringForTime(draggingPosition));
		return;
	}

	public void setDuration() {
		if (mVideoView.isPlaying())
			duration = mVideoView.getDuration();
	}
	
	public void setTvName(String name){
		tv_name.setText(name);
	}

	public int setProgress() {
		int i = 0;
		Log.d(TAG, (new StringBuilder("mDragging = ")).append(mDragging)
				.toString());
		if (mVideoView != null && !mDragging && mVideoView.getDuration() >= 0) {
			i = mVideoView.getCurrentPosition();
			setDuration();
			if (duration > 0) {
				long l = (1000L * (long) i) / (long) duration;
				seekBar.setProgress((int) l);
			}
			int j = mVideoView.getBufferPercentage();
			seekBar.setSecondaryProgress(j * 10);
			if (mEndTime != null)
				mEndTime.setText(stringForTime(duration));
			if (mCurrentTime != null)
				mCurrentTime.setText(stringForTime(i));
		} else {
			i = 0;
		}
		return i;
	}

	public void showAtLocation(View view, int i, int j, int k) {
		mHandler.post(UPDATE_PROGRESS);
		mHandler.removeCallbacks(hide);
		mHandler.postDelayed(hide, TIMEOUT);
		contentView.requestFocus();
		super.showAtLocation(view, i, j, k);
	}

	private static final int FFWD_FLAG = 100;
	private static final int REW_FLAG = 101;
	private static final String TAG = "vodseek";
	private static final int TIMEOUT = 10000;
	private static int incrementMs = 30000;
	private Runnable UPDATE_PROGRESS;
	View contentView;
	int draggingPosition;
	int duration;
	Runnable hide;
	private LayoutInflater inflater;
	private Context mContext;
	private TextView mCurrentTime;
	private TextView tv_name;
	private boolean mDragging;
	private TextView mEndTime;
	private Handler mHandler;
	private VideoView mVideoView;
	private SeekBar seekBar;

	private class _cls1 implements Runnable {

		public void run() {
			dismiss();
		}
	}

	private class _cls2 implements Runnable {

		public void run() {
			int i = setProgress();
			if (!mDragging && isShowing() && mVideoView.isPlaying())
				mHandler.postDelayed(UPDATE_PROGRESS, 1000 - i % 1000);
		}
	}

	private class _cls3 implements View.OnKeyListener {

		public boolean onKey(View view, int i, KeyEvent keyevent) {
			boolean flag = true;
			if (keyevent.getAction() != KeyEvent.ACTION_DOWN
					|| i != KeyEvent.KEYCODE_DPAD_LEFT) {
				if (keyevent.getAction() == KeyEvent.ACTION_DOWN
						&& i == KeyEvent.KEYCODE_DPAD_RIGHT)
					setDraggingProgress(FFWD_FLAG);
				else if ((i == KeyEvent.KEYCODE_DPAD_LEFT || i == KeyEvent.KEYCODE_DPAD_RIGHT)
						&& (keyevent.getAction() == KeyEvent.ACTION_UP)) {
					if (mDragging && mVideoView.isPlaying()) {
						Log.d(TAG, "up.." + draggingPosition);
						mVideoView.seekTo(draggingPosition);
						mDragging = false;
					}
				} else {
					flag = false;
				}
			} else {
				setDraggingProgress(REW_FLAG);
			}
			return flag;
		}

	}

}
