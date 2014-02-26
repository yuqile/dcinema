package cn.leo.dcinema.player;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.media.AudioManager;
import android.os.*;
import android.util.Log;
import android.view.*;
import android.widget.*;
import cn.leo.dcinema.R;
import cn.leo.dcinema.model.VideoSource;
import java.util.ArrayList;

public class PlayerMenuContrl extends PopupWindow implements
		View.OnKeyListener, View.OnClickListener {
	private static final String TAG = "PlayerMenuContrl";

	public PlayerMenuContrl(Context context1, Handler handler1, int i) {
		voiceLeve = new ImageView[10];
		sourceIndex = 0;
		autoDismiss = 5000;
		autoHide = new Runnable() {

			public void run() {
				dismiss();
			}
		};
		context = context1;
		handler = handler1;
		menuType = i;
		audioManager = (AudioManager) context1.getSystemService("audio");
		init();
	}

	private void init() {
		View view;
		setBackgroundDrawable(context.getResources().getDrawable(
				android.R.color.transparent));
		setFocusable(true);
		setWindowLayoutMode(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		view = ((LayoutInflater) context.getSystemService("layout_inflater"))
				.inflate(R.layout.player_menu_contrl, null);
		voice = (LinearLayout) view.findViewById(R.id.player_menu_voice);
		voiceLeft = (ImageView) voice.findViewById(R.id.player_menu_voice_left);
		voiceRight = (ImageView) voice
				.findViewById(R.id.player_menu_voice_right);
		scalor = (LinearLayout) view.findViewById(R.id.player_menu_sclar);
		scalorTv = (TextView) scalor.findViewById(R.id.player_menu_sclar_tv);
		scalorLeft = (ImageView) scalor
				.findViewById(R.id.player_menu_sclar_left);
		scalorRight = (ImageView) scalor
				.findViewById(R.id.player_menu_sclar_right);
		if (menuType != 2) {
			if (menuType == 1) {
				source = (LinearLayout) view
						.findViewById(R.id.player_menu_source);
				sourveTv = (TextView) source
						.findViewById(R.id.player_menu_source_tv);
				sourceLeft = (ImageView) source
						.findViewById(R.id.player_menu_source_left);
				sourceRight = (ImageView) source
						.findViewById(R.id.player_menu_source_right);
				source.setVisibility(View.VISIBLE);
				source.setOnKeyListener(this);
				sourceLeft.setOnClickListener(this);
				sourceRight.setOnClickListener(this);
				choose = (LinearLayout) view
						.findViewById(R.id.player_menu_chooseSet);
				choose.setOnClickListener(this);
				if (((VodPlayer) context).responseType().contains("电影")
						&& ((VodPlayer) context).responseSetSize() < 2)
					choose.setVisibility(View.GONE);
				else
					choose.setVisibility(View.VISIBLE);
			}
		} else {

		}
		voiceLeve[0] = (ImageView) voice.findViewById(R.id.menu_voice_1);
		voiceLeve[1] = (ImageView) voice.findViewById(R.id.menu_voice_2);
		voiceLeve[2] = (ImageView) voice.findViewById(R.id.menu_voice_3);
		voiceLeve[3] = (ImageView) voice.findViewById(R.id.menu_voice_4);
		voiceLeve[4] = (ImageView) voice.findViewById(R.id.menu_voice_5);
		voiceLeve[5] = (ImageView) voice.findViewById(R.id.menu_voice_6);
		voiceLeve[6] = (ImageView) voice.findViewById(R.id.menu_voice_7);
		voiceLeve[7] = (ImageView) voice.findViewById(R.id.menu_voice_8);
		voiceLeve[8] = (ImageView) voice.findViewById(R.id.menu_voice_9);
		voiceLeve[9] = (ImageView) voice.findViewById(R.id.menu_voice_10);
		voice.setOnKeyListener(this);
		scalor.setOnKeyListener(this);
		voiceLeft.setOnClickListener(this);
		voiceRight.setOnClickListener(this);
		scalorLeft.setOnClickListener(this);
		scalorRight.setOnClickListener(this);
		setContentView(view);
		return;
	}

	private void setVoice(int i) {
		if (i != -1) {
			if (i == 1) {
				audioManager.adjustStreamVolume(3, i, 8);
				showVoiceLevel(getVoice());
			}
		} else {
			audioManager.adjustStreamVolume(3, i, 8);
			showVoiceLevel(getVoice());
		}
	}

	public void dismiss() {
		handler.removeCallbacks(autoHide);
		super.dismiss();
	}

	public int getVoice() {
		int i = audioManager.getStreamVolume(3);
		int j = audioManager.getStreamMaxVolume(3);
		return (int) (10F * ((float) i / (float) j));
	}

	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.player_menu_source_left: // L2
			if (menuType == 1) {
				if (sourceIndex > 0) {
					sourceIndex = -1 + sourceIndex;
					setVideoSources(sourceIndex);
					handler.sendMessage(handler.obtainMessage(10, sourceIndex,
							0));
				}
			} else if (menuType == 2)
				;
			// ((LivePlayer) context).previousSource();
			break;
		case R.id.player_menu_source_right: // L3
			if (menuType == 1) {
				if (sourceIndex < -1 + videoSources.size()) {
					sourceIndex = 1 + sourceIndex;
					setVideoSources(sourceIndex);
					handler.sendMessage(handler.obtainMessage(10, sourceIndex,
							0));
				}
			} else if (menuType == 2)
				;// ((LivePlayer) context).nextSource();
			break;
		case R.id.player_menu_voice_left: // L4
			setVoice(-1);
			break;
		case R.id.player_menu_voice_right: // L5
			setVoice(1);
			return;

		case R.id.player_menu_sclar_left: // L6
			if (menuType == 1)
				scalorTv.setText(((VodPlayer) context).changScale(2));
			else if (menuType == 2)
				;// scalorTv.setText(((LivePlayer) context).changScale(2));
			break;

		case R.id.player_menu_sclar_right: // L7
			if (menuType == 1)
				scalorTv.setText(((VodPlayer) context).changScale(1));
			else if (menuType == 2)
				;// scalorTv.setText(((LivePlayer) context).changScale(1));
			break;

		case R.id.player_menu_list: // L8
			Message message = handler.obtainMessage(2);
			Bundle bundle = new Bundle();
			bundle.putString("tid", null);
			bundle.putInt("index", -1);
			message.setData(bundle);
			handler.sendMessage(message);
			dismiss();
			break;
		case R.id.player_menu_fav: // L9
			// ((LivePlayer) context).favCurrentChannel();
			dismiss();
			break;
		case R.id.player_menu_chooseSet: // L10
			((VodPlayer) context).showChooseSetDialog(true);
			dismiss();
			handler.removeCallbacks(autoHide);
			handler.postDelayed(autoHide, autoDismiss);
			return;

		default:
			handler.removeCallbacks(autoHide);
			handler.postDelayed(autoHide, autoDismiss);
			return;
		}
	}

	public boolean onKey(View view, int i, KeyEvent keyevent) {
		if (i == 21 && keyevent.getAction() == 0)
			switch (view.getId()) {
			case R.id.player_menu_source:
				if (menuType == 1) {
					if (sourceIndex > 0) {
						sourceIndex = -1 + sourceIndex;
						setVideoSources(sourceIndex);
						handler.sendMessage(handler.obtainMessage(10,
								sourceIndex, 0));
					}
				} else if (menuType == 2)
					;// ((LivePlayer) context).previousSource();
				break;

			case R.id.player_menu_voice:
				setVoice(-1);
				break;

			case R.id.player_menu_sclar:
				switch (menuType) {
				case 1: // '\001'
					scalorTv.setText(((VodPlayer) context).changScale(2));
					break;

				case 2: // '\002'
					// scalorTv.setText(((LivePlayer) context).changScale(2));
					break;

				case 3: // '\003'
					// scalorTv.setText(((TVBackActivity) context)
					// .changeScalType(2));
					break;

				case 4: // '\004'
					// scalorTv.setText(((NewsInformation) context)
					// .changeScalType(2));
					break;

				case 5: // '\005'
					// ((XLLXPlayer) context).changeScale(2);
					break;
				}
				break;
			}
		else if (i == 22 && keyevent.getAction() == 0)
			switch (view.getId()) {
			default:
				break;

			case R.id.player_menu_source:
				if (menuType == 1) {
					if (sourceIndex < -1 + videoSources.size()) {
						sourceIndex = 1 + sourceIndex;
						setVideoSources(sourceIndex);
						handler.sendMessage(handler.obtainMessage(10,
								sourceIndex, 0));
					}
					break;
				}
				if (menuType == 2)
					;// ((LivePlayer) context).nextSource();
				break;

			case R.id.player_menu_voice:
				setVoice(1);
				break;

			case R.id.player_menu_sclar:
				switch (menuType) {
				case 1: // '\001'
					scalorTv.setText(((VodPlayer) context).changScale(1));
					break;

				case 2: // '\002'
					// scalorTv.setText(((LivePlayer) context).changScale(1));
					break;

				case 3: // '\003'
					// scalorTv.setText(((TVBackActivity) context)
					// .changeScalType(1));
					break;

				case 4: // '\004'
					// scalorTv.setText(((NewsInformation) context)
					// .changeScalType(1));
					break;

				case 5: // '\005'
					// ((XLLXPlayer) context).changeScale(1);
					break;
				}
				break;
			}
		handler.removeCallbacks(autoHide);
		handler.postDelayed(autoHide, autoDismiss);
		return false;
	}

	public void setScalor(int i) {
		if (scalorTv != null) {
			switch (i % 3) {
			case 0:
				scalorTv.setText("16:9");
				break;
			case 1:
				scalorTv.setText("4:3");
				break;
			case 2:
				scalorTv.setText("原始比例");
				break;

			default:
				break;
			}
		}
	}

	public void setVideoSources(int i) {
		if (i >= 0 && i < videoSources.size()) {
			sourceIndex = i;
			sourveTv.setText(((VideoSource) videoSources.get(sourceIndex)).sourceName);
		}
	}

	public void setVideoSources(ArrayList<VideoSource> arraylist, int i) {
		if (arraylist != null) {
			videoSources = arraylist;
			setVideoSources(i);
		} else {
			videoSources = new ArrayList<VideoSource>();
			sourveTv.setText("无视频源");
		}
		if (videoSources.size() <= 1)
			source.setVisibility(View.GONE);
	}

	public void showAtLocation(View view, int i, int j, int k) {
		handler.removeCallbacks(autoHide);
		handler.postDelayed(autoHide, autoDismiss);
		super.showAtLocation(view, i, j, k);
	}

	public void showVoiceLevel(int i) {
		Log.d(TAG, "showVoiceLevel [" + i + "]");
		if (i >= 0) {
			for (int j = i; j < 10; j++) {
				voiceLeve[j].setVisibility(View.INVISIBLE);
			}
			for (int j = 0; j < i; j++) {
				voiceLeve[j].setVisibility(View.VISIBLE);
			}
		}
	}

	protected void updateLiveMenu(int i) {
		Log.d(TAG, "updateLiveMenu [" + i + "]");
		if (i > 1)
			source.setVisibility(View.VISIBLE);
		else
			source.setVisibility(View.GONE);
	}

	private AudioManager audioManager;
	private int autoDismiss;
	Runnable autoHide;
	private LinearLayout choose;
	private Context context;
	private LinearLayout fav;
	private Handler handler;
	private LinearLayout list;
	private int menuType;
	private LinearLayout scalor;
	private ImageView scalorLeft;
	private ImageView scalorRight;
	private TextView scalorTv;
	private LinearLayout source;
	private int sourceIndex;
	private ImageView sourceLeft;
	private ImageView sourceRight;
	private TextView sourveTv;
	private ArrayList<VideoSource> videoSources;
	private LinearLayout voice;
	private ImageView voiceLeft;
	private ImageView voiceLeve[];
	private ImageView voiceRight;

}
