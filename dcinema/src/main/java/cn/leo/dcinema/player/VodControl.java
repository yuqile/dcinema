package cn.leo.dcinema.player;

import cn.leo.dcinema.R;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupWindow;

public class VodControl extends PopupWindow {

	public VodControl(Context context, VideoView videoview, Handler handler) {
		super(context);
		hide = new Runnable() {

			public void run() {
				hide();
			}
		};
		mContetx = context;
		mVideoView = videoview;
		mHandler = handler;
		init();
	}

	private void doPauseResume() {
		if (this != null && isShowing()) {
			mHandler.removeCallbacks(hide);
			mHandler.postDelayed(hide, TIMEOUT);
			if (mVideoView.isPlaying())
				mVideoView.pause();
			else
				mVideoView.start();
			updatePausePlay();
		}
	}

	private void updatePausePlay() {
		if (pause != null)
			if (mVideoView.isPlaying())
				pause.setImageResource(R.drawable.pause);
			else
				pause.setImageResource(R.drawable.play);
	}

	public void hide() {
		mHandler.removeCallbacks(hide);
		dismiss();
	}

	@SuppressWarnings("deprecation")
	public void init() {
		setBackgroundDrawable(new BitmapDrawable());
		setFocusable(true);
		setWindowLayoutMode(-1, -1);
		View view = ((LayoutInflater) mContetx
				.getSystemService("layout_inflater")).inflate(
				R.layout.pause_play, null);
		view.setFocusable(true);
		view.requestFocus();
		pause = (ImageButton) view.findViewById(R.id.pause_or_paly);
		view.setOnKeyListener(new View.OnKeyListener() {

			public boolean onKey(View view, int i, KeyEvent keyevent) {
				boolean flag = true;
				if (i == KeyEvent.KEYCODE_DPAD_CENTER
						&& keyevent.getAction() == KeyEvent.ACTION_DOWN)
					doPauseResume();
				else if (i == KeyEvent.KEYCODE_DPAD_UP
						&& keyevent.getAction() == KeyEvent.ACTION_DOWN)
					hide();
				else if (i == KeyEvent.KEYCODE_DPAD_DOWN
						&& keyevent.getAction() == KeyEvent.ACTION_DOWN)
					hide();
				else if (i == KeyEvent.KEYCODE_DPAD_LEFT
						&& keyevent.getAction() == KeyEvent.ACTION_DOWN)
					hide();
				else if (i == KeyEvent.KEYCODE_DPAD_RIGHT
						&& keyevent.getAction() == KeyEvent.ACTION_DOWN)
					hide();
				else
					flag = false;
				return flag;
			}
		});
		setContentView(view);
	}

	public void show() {
		updatePausePlay();
		showAtLocation(mVideoView, 48, 0, 0);
		mHandler.removeCallbacks(hide);
		mHandler.postDelayed(hide, TIMEOUT);
	}

	private static final int TIMEOUT = 10000;
	Runnable hide;
	private Context mContetx;
	private Handler mHandler;
	private VideoView mVideoView;
	private ImageButton pause;

}