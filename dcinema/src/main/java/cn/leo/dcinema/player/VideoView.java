package cn.leo.dcinema.player;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.*;
import android.widget.MediaController;
import android.widget.MediaController.MediaPlayerControl;
import android.widget.ProgressBar;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import cn.leo.dcinema.util.MD5Util;

public class VideoView extends SurfaceView implements MediaPlayerControl {
	private int mSurfaceHeight;
	private SurfaceHolder mSurfaceHolder;
	private int mSurfaceWidth;
	private int mTargetState;
	private Uri mUri;
	private Uri mUris[];
	private int mVideoHeight;
	private int mVideoWidth;
	WindowManager wm;

	public static interface OnChangScaleListener {
		public abstract void changeScale(int i);
	}

	public VideoView(Context context) {
		super(context);
		mCurrentState = STATE_IDLE;
		mTargetState = STATE_IDLE;
		mSurfaceHolder = null;
		mMediaPlayer = null;
		mOnInfoListener = null;
		mOnCompletionListener = null;
		mOnPreparedListener = null;
		mOnBufferingUpdateListener = null;
		mOnSeekCompleteListener = null;
		mOnErrorListener = null;
		isList = false;
		handler = new Handler(Looper.getMainLooper());
		defaultScale = 0;
		isResultSeek = true;
		mContext = context;
		initVideoView();
	}

	public VideoView(Context context, AttributeSet attributeset) {
		this(context, attributeset, 0);
	}

	public VideoView(Context context, AttributeSet attributeset, int i) {
		super(context, attributeset, i);
		mCurrentState = STATE_IDLE;
		mTargetState = STATE_IDLE;
		mSurfaceHolder = null;
		mMediaPlayer = null;
		mOnInfoListener = null;
		mOnCompletionListener = null;
		mOnPreparedListener = null;
		mOnBufferingUpdateListener = null;
		mOnSeekCompleteListener = null;
		mOnErrorListener = null;
		isList = false;
		handler = new Handler(Looper.getMainLooper());
		defaultScale = 0;
		isResultSeek = true;
		mContext = context;
		initVideoView();
	}

	private void attachMediaController() {
		if (mMediaPlayer != null && mMediaController != null) {
			mMediaController.setMediaPlayer(this);
			View parentView;
			if (getParent() instanceof View)
				parentView = (View) getParent();
			else
				parentView = this;
			mMediaController.setAnchorView(parentView);
			mMediaController.setEnabled(isInPlaybackState());
		}
	}

	@SuppressWarnings("deprecation")
	private void initVideoView() {
		mVideoWidth = 0;
		mVideoHeight = 0;
		getHolder().addCallback(mSHCallback);
		getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		setFocusable(true);
		setFocusableInTouchMode(true);
		requestFocus();
		mCurrentState = STATE_IDLE;
		mTargetState = STATE_IDLE;
		bar = new ProgressBar(mContext);
		wm = (WindowManager) mContext.getSystemService("window");
	}

	private boolean isInPlaybackState() {
		boolean flag = true;
		if (mMediaPlayer == null || mCurrentState == STATE_ERROR
				|| mCurrentState == STATE_IDLE
				|| mCurrentState == STATE_PREPARING)
			flag = false;
		return flag;
	}

	private void openVideo() {
		if (mUri != null && mSurfaceHolder != null) {
			Log.d(TAG,
					(new StringBuilder("current uri = ")).append(
							mUri.toString()).toString());
			Intent intent = new Intent("com.android.music.musicservicecommand");
			intent.putExtra("command", "pause");
			mContext.sendBroadcast(intent);
			release(false);
			(new Thread(new Runnable() {
				final String video_url = mUri.toString();

				public void run() {
					final String rurl = getLive_Location(video_url);
					mUri = Uri.parse(rurl);
					mMediaPlayer = new MediaPlayer();
					mMediaPlayer.setOnPreparedListener(mPreparedListener);
					mMediaPlayer
							.setOnVideoSizeChangedListener(mSizeChangedListener);
					mMediaPlayer
							.setOnSeekCompleteListener(mSeekCompleteListener);
					mMediaPlayer.setOnInfoListener(mInfoListener);
					mDuration = -1;
					mMediaPlayer.setOnCompletionListener(mCompletionListener);
					mMediaPlayer.setOnErrorListener(mErrorListener);
					mMediaPlayer
							.setOnBufferingUpdateListener(mBufferingUpdateListener);
					mCurrentBufferPercentage = 0;
					Log.d(TAG,
							(new StringBuilder("当前视频加载地址  =")).append(
									mUri.toString()).toString());
					try {
						mMediaPlayer.setDataSource(mContext, mUri, mHeaders);
						mMediaPlayer.setDisplay(mSurfaceHolder);
						mMediaPlayer.setAudioStreamType(3);
						mMediaPlayer.setScreenOnWhilePlaying(true);
						mMediaPlayer.prepareAsync();
						handler.postDelayed(TimeOutError, TIMEOUTDEFAULT);
						mCurrentState = STATE_PREPARING;
						attachMediaController();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						Log.w(TAG,
								(new StringBuilder(
										"ille .. Unable to open content: "))
										.append(mUri).toString(), e);
						mCurrentState = STATE_ERROR;
						mTargetState = STATE_ERROR;
						mErrorListener.onError(mMediaPlayer, 1, 0);
					} catch (SecurityException e) {
						// TODO Auto-generated catch block
						Log.e(TAG, "SecurityException" + e.toString());
					} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						Log.e(TAG, "IllegalStateException" + e.toString());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						Log.e(TAG, (new StringBuilder(
								"ioe .. Unable to open content: "))
								.append(mUri).toString(), e);
						mCurrentState = STATE_ERROR;
						mTargetState = STATE_ERROR;
						mErrorListener.onError(mMediaPlayer, 1, 0);
					} catch (Exception e) {
						Log.d(TAG, "exception." + e.toString());
					}
				}
			})).start();

		}
	}

	private void release(boolean flag) {
		if (mMediaPlayer != null) {
			mMediaPlayer.reset();
			mMediaPlayer.release();
			mMediaPlayer = null;
			mCurrentState = STATE_IDLE;
			if (flag)
				mTargetState = STATE_IDLE;
		}
		handler.removeCallbacks(TimeOutError);
		if (bar.getParent() != null)
			wm.removeView(bar);
	}

	private void toggleMediaControlsVisiblity() {
		if (mMediaController.isShowing())
			mMediaController.hide();
		else
			mMediaController.show();
	}

	public boolean canPause() {
		return mCanPause;
	}

	public boolean canSeekBackward() {
		return mCanSeekBack;
	}

	public boolean canSeekForward() {
		return mCanSeekForward;
	}

	public int getBufferPercentage() {
		int i;
		if (mMediaPlayer != null)
			i = mCurrentBufferPercentage;
		else
			i = 0;
		return i;
	}

	public int getCurrentPosition() {
		int i = 0;
		if (isInPlaybackState()) {
			if (!isList) {
				i = mMediaPlayer.getCurrentPosition();
			} else {
				int j = 0;
				for (int k = 0; k < index; k++) {
					j += mDurations[k];
				}
				i = j + mMediaPlayer.getCurrentPosition();
			}
		}
		return i;
	}

	public int getDuration() {
		int i = 0;
		if (isInPlaybackState()) {
			if (mDuration <= 0) {
				if (isList) {
					mDuration = mMediaPlayer.getDuration();
					for (int j = 0; j < mDurations.length; j++) {
						mDuration = mDuration + mDurations[j];
					}
					i = mDuration;
				} else {
					mDuration = mMediaPlayer.getDuration();
				}
			}
			i = mDuration;
		} else {
			mDuration = -1;
			i = mDuration;
		}
		Log.d(TAG, "getDuration [" + i + "]");
		return i;
	}

	public String getLive_Location(String s) {
		if (s.startsWith("http://")) {
			String s1;
			s1 = s;
			if (!(!s.contains(".52itv.cn/") && !s.contains(".myvst.net/")
					&& !s.contains(".hdplay.cn/") && !s
						.contains("ku6.com/broadcast/sub"))) {
				try {
					conn = (HttpURLConnection) (new URL(s)).openConnection();
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					Log.e(TAG, e.toString());
					if (conn != null)
						conn.disconnect();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					Log.e(TAG, e.toString());
					if (conn != null)
						conn.disconnect();
				}
				conn.setInstanceFollowRedirects(false);
				conn.setRequestProperty("User-Key", get_livekey());
				conn.setRequestProperty("User-Agent", "GGwlPlayer/QQ243944493");
				conn.setRequestProperty("User-Ver", "VSTlive/3.0.0");
				try {
					if (conn.getResponseCode() == 301
							|| conn.getResponseCode() == 302) {
						s1 = conn.getHeaderField("Location");
						Log.d(TAG,
								(new StringBuilder("状态: "))
										.append(conn.getResponseCode())
										.append(" -> 取重定向URL:: ").append(s1)
										.toString());
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					Log.e(TAG, e.toString());
					if (conn != null)
						conn.disconnect();
				}
				if (conn != null)
					conn.disconnect();

				if (s1.contains("gslb.tv.sohu.com/live?cid=")
						&& !s1.contains("&type=hls"))
					s1 = (new StringBuilder(String.valueOf(s1))).append(
							"&type=hls").toString();
				else if (s1.contains("zb.v.qq.com:1863/?progid=")
						&& !s1.contains("&ostype=ios"))
					s1 = (new StringBuilder(String.valueOf(s1))).append(
							"&ostype=ios").toString();
				else if (s1.contains("totiptv.com/live/")
						&& !s1.contains(".m3u8?bitrate=800"))
					s1 = (new StringBuilder(String.valueOf(s1))).append(
							"?bitrate=800").toString();
				s = s1;
			}
		}
		return s;
	}

	public String get_livekey() {
		return MD5Util.getMD5String(
				(new StringBuilder("time-"))
						.append(String.valueOf(System.currentTimeMillis())
								.substring(0, 8)).append("/key-52itvlive")
						.toString()).substring(0, 16);
	}

	public boolean isPlaying() {
		boolean flag;
		if (isInPlaybackState() && mMediaPlayer.isPlaying())
			flag = true;
		else
			flag = false;
		return flag;
	}

	public boolean onKeyDown(int i, KeyEvent keyevent) {
		boolean flag;
		flag = true;
		boolean flag1;
		if (i != KeyEvent.KEYCODE_BACK && i != KeyEvent.KEYCODE_VOLUME_UP
				&& i != KeyEvent.KEYCODE_VOLUME_DOWN
				&& i != KeyEvent.KEYCODE_VOLUME_MUTE
				&& i != KeyEvent.KEYCODE_MENU && i != KeyEvent.KEYCODE_CALL
				&& i != KeyEvent.KEYCODE_ENDCALL)
			flag1 = flag;
		else
			flag1 = false;
		if (!isInPlaybackState() || !flag1 || mMediaController == null) {
			flag = super.onKeyDown(i, keyevent);
			return flag;
		} else {
			if (i != KeyEvent.KEYCODE_HEADSETHOOK
					&& i != KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE) {

				if (i == KeyEvent.KEYCODE_MEDIA_PLAY) {
					if (!mMediaPlayer.isPlaying()) {
						start();
						mMediaController.hide();
					}
					if (i == KeyEvent.KEYCODE_MEDIA_STOP
							|| i == KeyEvent.KEYCODE_MEDIA_PAUSE) {
						if (mMediaPlayer.isPlaying()) {
							pause();
							mMediaController.show();
						}
					}
					if (i == KeyEvent.KEYCODE_PROG_YELLOW) {
						defaultScale = (1 + defaultScale) % 3;
						selectScales(defaultScale);
						if (mOnChangScaleListener != null)
							mOnChangScaleListener.changeScale(defaultScale);
					}
					toggleMediaControlsVisiblity();
				}
			}
			if (mMediaPlayer.isPlaying()) {
				pause();
				mMediaController.show();
			} else {
				start();
				mMediaController.hide();
			}
			return flag;
		}
	}

	protected void onMeasure(int i, int j) {
		int k = getDefaultSize(mVideoWidth, i);
		int l = getDefaultSize(mVideoHeight, j);
		setMeasuredDimension(k, l);
	}

	public boolean onTouchEvent(MotionEvent motionevent) {
		if (isInPlaybackState() && mMediaController != null)
			toggleMediaControlsVisiblity();
		return false;
	}

	public boolean onTrackballEvent(MotionEvent motionevent) {
		if (isInPlaybackState() && mMediaController != null)
			toggleMediaControlsVisiblity();
		return false;
	}

	public void pause() {
		if (isInPlaybackState() && mMediaPlayer.isPlaying()) {
			mMediaPlayer.pause();
			mCurrentState = STATE_PAUSED;
		}
		mTargetState = STATE_PAUSED;
	}

	public int resolveAdjustedSize(int i, int j) {
		int k = i;
		int l = MeasureSpec.getMode(j);
		int i1 = MeasureSpec.getSize(j);
		switch (l) {
		case MeasureSpec.AT_MOST:
			k = Math.min(i, i1);
			break;
		case MeasureSpec.UNSPECIFIED:
			k = i;
			break;
		case MeasureSpec.EXACTLY:
			k = i1;
			break;

		default:
			break;
		}
		return k;
	}

	public void resume() {
		openVideo();
	}

	public void seekTo(int i) {
		if (i > 0) {
			Log.d(TAG, (new StringBuilder("msec = ")).append(i).toString());
//			if (isResultSeek) {
				if (isInPlaybackState()) {
					mMediaPlayer.seekTo(i);
					mSeekWhenPrepared = 0;
				} else {
					mSeekWhenPrepared = i;
					Log.d(TAG, (new StringBuilder("mSeekWhenPrepared = "))
							.append(mSeekWhenPrepared).toString());
				}
//			}
			if (mDurations != null) {
				for (int j = 0; j < mDurations.length; j++) {
					i -= mDurations[j];
					if (i >= 0)
						break;
					int k = i + mDurations[j];
					isResultSeek = true;
					if (index == j) {
						mMediaPlayer.seekTo(k);
						mSeekWhenPrepared = 0;
					} else {
						index = j;
						mUri = mUris[j];
						mSeekWhenPrepared = k;
						Log.d(TAG, (new StringBuilder("mSeekWhenPrepared = "))
								.append(mSeekWhenPrepared).toString());
						handler.post(new Runnable() {

							public void run() {
								openVideo();
							}
						});
					}
				}
			}
		}
	}

	public void selectScales(int scaleMod) {
		if (getWindowToken() == null) {
			return;
		} else {
			double d;
			double d1;
			Rect rect = new Rect();
			getWindowVisibleDisplayFrame(rect);
			Log.d(TAG,
					(new StringBuilder("Rect = ")).append(rect.top).append(":")
							.append(rect.bottom).append(":").append(rect.left)
							.append(":").append(rect.right).toString());
			d = rect.bottom - rect.top;
			d1 = rect.right - rect.left;
			Log.d(TAG, (new StringBuilder("diplay = ")).append(d1).append(":")
					.append(d).toString());
			if (d > 0.0D && d1 > 0.0D && (double) mVideoHeight > 0.0D
					&& (double) mVideoWidth > 0.0D) {
				ViewGroup.LayoutParams layoutparams = getLayoutParams();
				switch (scaleMod) {
				case A_DEFALT: // '\0'
					if (d1 / d >= (double) (mVideoWidth / mVideoHeight)) {
						layoutparams.height = (int) d;
						layoutparams.width = (int) ((d * (double) mVideoWidth) / (double) mVideoHeight);
					} else {
						layoutparams.width = (int) d1;
						layoutparams.height = (int) ((d1 * (double) mVideoHeight) / (double) mVideoWidth);
					}
					Log.d(TAG,
							(new StringBuilder("A_DEFALT === "))
									.append(layoutparams.width).append(":")
									.append(layoutparams.height).toString());
					setLayoutParams(layoutparams);
					break;

				case A_4X3: // '\001'
					if (d1 / d >= 1.3333333333333333D) {
						layoutparams.height = (int) d;
						layoutparams.width = (int) ((4D * d) / 3D);
					} else {
						layoutparams.width = (int) d1;
						layoutparams.height = (int) ((3D * d1) / 4D);
					}
					Log.d(TAG,
							(new StringBuilder("A_4X3 === "))
									.append(layoutparams.width).append(":")
									.append(layoutparams.height).toString());
					setLayoutParams(layoutparams);
					break;

				case A_16X9: // '\002'
					if (d1 / d >= 1.7777777777777777D) {
						layoutparams.height = (int) d;
						layoutparams.width = (int) ((16D * d) / 9D);
					} else {
						layoutparams.width = (int) d1;
						layoutparams.height = (int) ((9D * d1) / 16D);
					}
					Log.d(TAG,
							(new StringBuilder("A_16X9 === "))
									.append(layoutparams.width).append(":")
									.append(layoutparams.height).toString());
					setLayoutParams(layoutparams);
					break;
				}
			}
		}
	}

	public void setDefaultScale(int i) {
		defaultScale = i;
	}

	public void setMediaController(MediaController mediacontroller) {
		if (mMediaController != null)
			mMediaController.hide();
		mMediaController = mediacontroller;
		attachMediaController();
	}

	public void setOnBufferingUpdateListener(
			MediaPlayer.OnBufferingUpdateListener onbufferingupdatelistener) {
		mOnBufferingUpdateListener = onbufferingupdatelistener;
	}

	public void setOnChangScaleListener(
			OnChangScaleListener onchangscalelistener) {
		mOnChangScaleListener = onchangscalelistener;
	}

	public void setOnCompletionListener(
			MediaPlayer.OnCompletionListener oncompletionlistener) {
		mOnCompletionListener = oncompletionlistener;
	}

	public void setOnErrorListener(MediaPlayer.OnErrorListener onerrorlistener) {
		mOnErrorListener = onerrorlistener;
	}

	public void setOnInfoListener(MediaPlayer.OnInfoListener oninfolistener) {
		mOnInfoListener = oninfolistener;
	}

	public void setOnPreparedListener(
			MediaPlayer.OnPreparedListener onpreparedlistener) {
		mOnPreparedListener = onpreparedlistener;
	}

	public void setOnSeekCompleteListener(
			MediaPlayer.OnSeekCompleteListener onseekcompletelistener) {
		mOnSeekCompleteListener = onseekcompletelistener;
	}

	public void setVideoPath(String s) {
		setVideoURI(Uri.parse(s));
	}

	public void setVideoURI(Uri uri) {
		setVideoURI(uri, null);
	}

	public void setVideoURI(Uri uri, Map<String, String> map) {
		mUri = uri;
		mHeaders = map;
		mSeekWhenPrepared = 0;
		isList = false;
		openVideo();
		requestLayout();
		invalidate();
	}

	public void setVideoURI(Uri auri[], Map<String, String> map, int ai[]) {
		if (auri == null || ai == null || auri.length != ai.length) {
			throw new IllegalArgumentException(
					"uris must not null , durations must nuo null and uris.length must =durations.length");
		} else {
			isResultSeek = false;
			mUris = auri;
			index = 0;
			isList = true;
			mDurations = ai;
			mUri = auri[index];
			mHeaders = map;
			mSeekWhenPrepared = 0;
			openVideo();
			requestLayout();
			invalidate();
			return;
		}
	}

	public void start() {
		if (isInPlaybackState()) {
			mMediaPlayer.start();
			mCurrentState = STATE_PLAYING;
		}
		mTargetState = STATE_PLAYING;
	}

	public void stopPlayback() {
		if (mMediaPlayer != null) {
			mMediaPlayer.stop();
			mMediaPlayer.release();
			if (bar.getParent() != null)
				wm.removeView(bar);
			mMediaPlayer = null;
			mCurrentState = STATE_IDLE;
			mTargetState = STATE_IDLE;
		}
	}

	public void suspend() {
		release(false);
	}

	public static final int A_16X9 = 2;
	public static final int A_4X3 = 1;
	public static final int A_DEFALT = 0;
	public static final int A_RAW = 4;
	@SuppressWarnings("unused")
	private static final int MEDIA_ERROR_TIMED_OUT = -110;
	private static final int STATE_ERROR = -1;
	private static final int STATE_IDLE = 0;
	private static final int STATE_PAUSED = 4;
	private static final int STATE_PLAYBACK_COMPLETED = 5;
	private static final int STATE_PLAYING = 3;
	private static final int STATE_PREPARED = 2;
	private static final int STATE_PREPARING = 1;
	private static final String TAG = "VideoView";
	private static final long TIMEOUTDEFAULT = 30000L;
	private static HttpURLConnection conn;
	private Runnable TimeOutError = new Runnable() {

		public void run() {
			Log.e(TAG, (new StringBuilder(
					"time .. open video time out : Uri = ")).append(mUri)
					.toString());
			mCurrentState = STATE_ERROR;
			mTargetState = STATE_ERROR;
			mErrorListener.onError(mMediaPlayer, 1, -100);
			release(false);
		}
	};
	private ProgressBar bar;
	private int defaultScale;
	Handler handler;
	private int index;
	private boolean isList;
	boolean isResultSeek;

	private OnBufferingUpdateListener mBufferingUpdateListener = new OnBufferingUpdateListener() {

		public void onBufferingUpdate(MediaPlayer mediaplayer, int i) {
			mCurrentBufferPercentage = i;
		}

	};

	private boolean mCanPause;
	private boolean mCanSeekBack;
	private boolean mCanSeekForward;
	private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {

		@Override
		public void onCompletion(MediaPlayer mediaplayer) {
			// TODO Auto-generated method stub
			if (!isList || index >= -1 + mUris.length) {
				mCurrentState = STATE_PLAYBACK_COMPLETED;
				mTargetState = STATE_PLAYBACK_COMPLETED;
				if (mMediaController != null)
					mMediaController.hide();
				if (mOnCompletionListener != null)
					mOnCompletionListener.onCompletion(mMediaPlayer);
			} else {
				VideoView videoview = VideoView.this;
				videoview.index = 1 + videoview.index;
				mUri = mUris[index];
				Log.d(TAG, (new StringBuilder("index = ")).append(index)
						.append(",uri = ").append(mUri).toString());
				openVideo();
			}
		}
	};

	private Context mContext;
	private int mCurrentBufferPercentage;
	private int mCurrentState;
	private int mDuration;
	private int mDurations[];
	private MediaPlayer.OnErrorListener mErrorListener = new MediaPlayer.OnErrorListener() {

		@Override
		public boolean onError(MediaPlayer mediaplayer, int what, int extra) {
			// TODO Auto-generated method stub
			Log.d(TAG, (new StringBuilder("Error: ")).append(what).append(",")
					.append(extra).toString());
			mCurrentState = STATE_ERROR;
			mTargetState = STATE_ERROR;
			if (mMediaController != null)
				mMediaController.hide();

			if ((mOnErrorListener == null || !mOnErrorListener.onError(
					mMediaPlayer, what, extra)) && getWindowToken() != null) {
				mContext.getResources();
				class VideoPlayerErrorDialog implements
						DialogInterface.OnClickListener {

					public void onClick(DialogInterface dialoginterface, int k) {
						if (mOnCompletionListener != null)
							mOnCompletionListener.onCompletion(mMediaPlayer);
					}
				}

				String s;
				switch (what) {
				case MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
					s = "playback error";
					break;
				default:
					s = "unknown error ";
					break;
				}

				(new AlertDialog.Builder(mContext)).setTitle("Sorry")
						.setMessage(s)
						.setPositiveButton("确认", new VideoPlayerErrorDialog())
						.setCancelable(false).show();
			}
			return true;
		}
	};
	private Map<String, String> mHeaders;
	MediaPlayer.OnInfoListener mInfoListener = new MediaPlayer.OnInfoListener() {

		@Override
		public boolean onInfo(MediaPlayer mediaplayer, int what, int extra) {
			Log.i(TAG, (new StringBuilder("OnInfoListener-------->what:"))
					.append(what).append(",  extra :").append(extra).toString());
			if (mOnInfoListener == null) {
				if (getWindowToken() != null) {
					WindowManager.LayoutParams layoutparams = new WindowManager.LayoutParams();
					layoutparams.format = -2;
					layoutparams.flags = 8;
					layoutparams.width = -2;
					layoutparams.height = -2;
					layoutparams.gravity = 17;
					switch (what) {
					default:
						break;

					case MediaPlayer.MEDIA_INFO_BUFFERING_START:
						if (bar.getParent() == null)
							wm.addView(bar, layoutparams);
						break;

					case MediaPlayer.MEDIA_INFO_BUFFERING_END:
						if (bar.getParent() != null)
							wm.removeView(bar);
						break;
					}
				}
			} else {
				mOnInfoListener.onInfo(mediaplayer, what, extra);
			}
			return true;
		}
	};
	private MediaController mMediaController;
	private MediaPlayer mMediaPlayer;
	@SuppressWarnings("unused")
	private MediaPlayer.OnBufferingUpdateListener mOnBufferingUpdateListener;
	private OnChangScaleListener mOnChangScaleListener;
	private MediaPlayer.OnCompletionListener mOnCompletionListener;
	private MediaPlayer.OnErrorListener mOnErrorListener;
	private MediaPlayer.OnInfoListener mOnInfoListener;
	private MediaPlayer.OnPreparedListener mOnPreparedListener;
	private MediaPlayer.OnSeekCompleteListener mOnSeekCompleteListener;
	MediaPlayer.OnPreparedListener mPreparedListener = new MediaPlayer.OnPreparedListener() {

		@Override
		public void onPrepared(MediaPlayer mediaplayer) {
			// TODO Auto-generated method stub
			int i;
			mCurrentState = STATE_PREPARED;
			VideoView videoview = VideoView.this;
			VideoView videoview1 = VideoView.this;
			mCanSeekForward = true;
			videoview1.mCanSeekBack = true;
			videoview.mCanPause = true;
			handler.removeCallbacks(TimeOutError);
			if (mMediaController != null)
				mMediaController.setEnabled(true);
			mVideoWidth = mediaplayer.getVideoWidth();
			mVideoHeight = mediaplayer.getVideoHeight();
			if (mOnPreparedListener != null)
				mOnPreparedListener.onPrepared(mMediaPlayer);
			i = mSeekWhenPrepared;
			if (i != 0) {
				Log.d(TAG, (new StringBuilder("seekToPosition =")).append(i)
						.toString());
				seekTo(i);
			}
			if (mVideoWidth == 0 || mVideoHeight == 0) {
				if (mTargetState == STATE_PLAYING)
					start();
			} else {
				getHolder().setFixedSize(mVideoWidth, mVideoHeight);
				if (!(mSurfaceWidth != mVideoWidth || mSurfaceHeight != mVideoHeight)) {
					if (mTargetState != STATE_PLAYING) {
						if (!isPlaying()
								&& (i != 0 || getCurrentPosition() > 0)
								&& mMediaController != null)
							mMediaController.show(0);
					} else {
						start();
						if (mMediaController != null)
							mMediaController.show();
					}
				}
			}
		}
	};
	SurfaceHolder.Callback mSHCallback = new SurfaceHolder.Callback() {

		@Override
		public void surfaceChanged(SurfaceHolder surfaceholder, int i, int j,
				int k) {
			// TODO Auto-generated method stub
			Log.d(TAG, "surfaceChanged...");
			mSurfaceWidth = j;
			mSurfaceHeight = k;
			boolean flag;
			boolean flag1;
			if (mTargetState == STATE_PLAYING)
				flag = true;
			else
				flag = false;
			if (mVideoWidth == j && mVideoHeight == k)
				flag1 = true;
			else
				flag1 = false;
			if (mMediaPlayer != null && flag && flag1) {
				if (mSeekWhenPrepared != 0)
					seekTo(mSeekWhenPrepared);
				start();
			}
		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			Log.d(TAG, "surfaceCreated..");
			mSurfaceHolder = holder;
			openVideo();
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			Log.d(TAG, "surfaceDestroyed..");
			mSurfaceHolder = null;
			mUri = null;
			if (mMediaController != null)
				mMediaController.hide();
			release(true);
		}
	};
	private MediaPlayer.OnSeekCompleteListener mSeekCompleteListener = new MediaPlayer.OnSeekCompleteListener() {

		@Override
		public void onSeekComplete(MediaPlayer mediaplayer) {
			// TODO Auto-generated method stub
			if (isList)
				isResultSeek = false;
			if (mOnSeekCompleteListener != null)
				mOnSeekCompleteListener.onSeekComplete(mediaplayer);
		}
	};
	private int mSeekWhenPrepared;
	MediaPlayer.OnVideoSizeChangedListener mSizeChangedListener = new MediaPlayer.OnVideoSizeChangedListener() {

		@Override
		public void onVideoSizeChanged(MediaPlayer mediaplayer, int width,
				int height) {
			// TODO Auto-generated method stub
			mVideoWidth = mediaplayer.getVideoWidth();
			mVideoHeight = mediaplayer.getVideoHeight();
			if (mVideoWidth != 0 && mVideoHeight != 0)
				getHolder().setFixedSize(mVideoWidth, mVideoHeight);
		}
	};
	@Override
	public int getAudioSessionId() {
		// TODO Auto-generated method stub
		return 0;
	}

}
