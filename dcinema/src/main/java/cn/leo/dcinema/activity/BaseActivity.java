package cn.leo.dcinema.activity;

import java.lang.ref.WeakReference;
import com.umeng.analytics.MobclickAgent;
import cn.leo.dcinema.Constants;
import cn.leo.dcinema.app.MyApp;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;

public class BaseActivity extends Activity {

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	public static interface NumKeyClickListener {
		public abstract void multeKeyDown(int i);

		public abstract void singleKeyDown(int i);
	}

	private static final String TAG = "BaseActivity";

	public BaseActivity() {
		isRunning = false;
		listener = null;
		numWaiting = false;
		sb = new StringBuilder();
		handler = new MyHandler(this);
	}

	private static class MyHandler extends Handler {
		private final WeakReference<Activity> mActivity;

		public MyHandler(Activity activity) {
			mActivity = new WeakReference<Activity>(activity);
		}

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			Log.d(TAG, msg.toString());
			if (mActivity.get() == null) {
				return;
			}
			super.handleMessage(msg);
			if (msg.what == MSG_NUM_END) {
				if (!TextUtils.isEmpty(sb.toString())) {
					int i = Integer.parseInt(sb.toString());
					listener.multeKeyDown(i);
				}
				sb = new StringBuilder();
				numWaiting = false;
			}
		}

	}

	protected static boolean isRunning() {
		return isRunning;
	}

	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		wm = (WindowManager) getSystemService("window");
		requestWindowFeature(1);
		getWindow().setFlags(1024, 1024);
		isRunning = true;
		myApp = (MyApp) getApplication();
		Log.d(TAG, myApp.getPackageName());
		progressDialog = new ProgressDialog(this);
		progressDialog.setProgressStyle(0);
		progressDialog.setMessage("加载中...");
		progressDialog.setIndeterminate(false);
	}

	protected void onDestroy() {
		super.onDestroy();
		isRunning = false;
	}

	public boolean onKeyDown(int keyCode, KeyEvent keyevent) {
		boolean flag;
		boolean flag1;
		flag = false;
		System.out
				.println((new StringBuilder("----------------------keyCode = "))
						.append(keyCode).toString());
		if (keyCode != KeyEvent.KEYCODE_0 && keyCode != KeyEvent.KEYCODE_1
				&& keyCode != KeyEvent.KEYCODE_2
				&& keyCode != KeyEvent.KEYCODE_3
				&& keyCode != KeyEvent.KEYCODE_4
				&& keyCode != KeyEvent.KEYCODE_5
				&& keyCode != KeyEvent.KEYCODE_6
				&& keyCode != KeyEvent.KEYCODE_7
				&& keyCode != KeyEvent.KEYCODE_8
				&& keyCode != KeyEvent.KEYCODE_9)
			flag1 = false;
		else
			flag1 = true;
		if (sb.length() <= 4) {
			if (!flag1 || listener == null) {
				if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
					MyApp.playSound(Constants.SOUND_COMFIRE);
					flag = super.onKeyDown(keyCode, keyevent);
				} else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
					MyApp.playSound(Constants.SOUND_MOVE_BOTTOM);
					flag = super.onKeyDown(keyCode, keyevent);
				} else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
					MyApp.playSound(Constants.SOUND_MOVE_LEFT);
					flag = super.onKeyDown(keyCode, keyevent);
				} else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
					MyApp.playSound(Constants.SOUND_MOVE_RIGHT);
					flag = super.onKeyDown(keyCode, keyevent);
				} else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
					MyApp.playSound(Constants.SOUND_MOVE_BOTTOM);
					flag = super.onKeyDown(keyCode, keyevent);
				} else if (keyCode == KeyEvent.KEYCODE_BACK) {
					MyApp.playSound(Constants.SOUND_BACK);
					flag = super.onKeyDown(keyCode, keyevent);
				} else {
					flag = super.onKeyDown(keyCode, keyevent);
				}
			} else {
				numWaiting = true;
				switch (keyCode) {
				case KeyEvent.KEYCODE_0:
					sb.append(0);
					break;
				case KeyEvent.KEYCODE_1:
					sb.append(1);
					break;
				case KeyEvent.KEYCODE_2:
					sb.append(2);
					break;
				case KeyEvent.KEYCODE_3:
					sb.append(3);
					break;
				case KeyEvent.KEYCODE_4:
					sb.append(4);
					break;
				case KeyEvent.KEYCODE_5:
					sb.append(5);
					break;
				case KeyEvent.KEYCODE_6:
					sb.append(6);
					break;
				case KeyEvent.KEYCODE_7:
					sb.append(7);
					break;
				case KeyEvent.KEYCODE_8:
					sb.append(8);
					break;
				case KeyEvent.KEYCODE_9:
					sb.append(9);
					if (!TextUtils.isEmpty(sb.toString())) {
						int j = Integer.parseInt(sb.toString());
						listener.singleKeyDown(j);
					}
					if (numWaiting) {
						handler.removeMessages(MSG_NUM_END);
						handler.sendEmptyMessageDelayed(MSG_NUM_END, 2000L);
					}
					flag = true;
					break;
				default:
					if (!TextUtils.isEmpty(sb.toString())) {
						int j = Integer.parseInt(sb.toString());
						listener.singleKeyDown(j);
					}
					if (numWaiting) {
						handler.removeMessages(MSG_NUM_END);
						handler.sendEmptyMessageDelayed(MSG_NUM_END, 2000L);
					}
					flag = true;
					break;
				}
			}
		}
		return flag;
	}

	public static void progressDismiss() {
		if (progressDialog.isShowing())
			progressDialog.dismiss();
	}

	public void progressShow() {
		if (!progressDialog.isShowing())
			progressDialog.show();
	}

	public void setOnNumKeyClickListener(NumKeyClickListener numkeyclicklistener) {
		listener = numkeyclicklistener;
	}

	private static int MSG_NUM_END = 0;
	private static Handler handler;
	public static boolean isRunning;
	private static NumKeyClickListener listener;
	private MyApp myApp;
	private static boolean numWaiting;
	private static ProgressDialog progressDialog;
	static StringBuilder sb;
	public WindowManager wm;

}
