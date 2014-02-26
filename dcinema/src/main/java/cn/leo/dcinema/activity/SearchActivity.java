package cn.leo.dcinema.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import cn.leo.dcinema.R;
import cn.leo.dcinema.adapters.VideoInfoAdapter;
import cn.leo.dcinema.app.MyApp;
import cn.leo.dcinema.biz.SerachBiz;
import cn.leo.dcinema.model.VideoInfo;
import cn.leo.dcinema.model.VideoList;

public class SearchActivity extends BaseActivity implements
		View.OnClickListener {
	public SearchActivity() {
		page = 1;
		loadThread = new _cls1();
		handler = new MyHandler();
	}

	private void initKeyBoard() {
		((Button) findViewById(R.id.keypad_A)).setOnClickListener(this);
		((Button) findViewById(R.id.keypad_B)).setOnClickListener(this);
		((Button) findViewById(R.id.keypad_C)).setOnClickListener(this);
		((Button) findViewById(R.id.keypad_D)).setOnClickListener(this);
		((Button) findViewById(R.id.keypad_E)).setOnClickListener(this);
		((Button) findViewById(R.id.keypad_F)).setOnClickListener(this);
		((Button) findViewById(R.id.keypad_G)).setOnClickListener(this);
		((Button) findViewById(R.id.keypad_H)).setOnClickListener(this);
		((Button) findViewById(R.id.keypad_I)).setOnClickListener(this);
		((Button) findViewById(R.id.keypad_J)).setOnClickListener(this);
		((Button) findViewById(R.id.keypad_K)).setOnClickListener(this);
		((Button) findViewById(R.id.keypad_L)).setOnClickListener(this);
		((Button) findViewById(R.id.keypad_M)).setOnClickListener(this);
		((Button) findViewById(R.id.keypad_N)).setOnClickListener(this);
		((Button) findViewById(R.id.keypad_O)).setOnClickListener(this);
		((Button) findViewById(R.id.keypad_P)).setOnClickListener(this);
		((Button) findViewById(R.id.keypad_Q)).setOnClickListener(this);
		((Button) findViewById(R.id.keypad_R)).setOnClickListener(this);
		((Button) findViewById(R.id.keypad_S)).setOnClickListener(this);
		((Button) findViewById(R.id.keypad_T)).setOnClickListener(this);
		((Button) findViewById(R.id.keypad_U)).setOnClickListener(this);
		((Button) findViewById(R.id.keypad_V)).setOnClickListener(this);
		((Button) findViewById(R.id.keypad_W)).setOnClickListener(this);
		((Button) findViewById(R.id.keypad_X)).setOnClickListener(this);
		((Button) findViewById(R.id.keypad_Y)).setOnClickListener(this);
		((Button) findViewById(R.id.keypad_Z)).setOnClickListener(this);
		((Button) findViewById(R.id.keypad_0)).setOnClickListener(this);
		((Button) findViewById(R.id.keypad_1)).setOnClickListener(this);
		((Button) findViewById(R.id.keypad_2)).setOnClickListener(this);
		((Button) findViewById(R.id.keypad_3)).setOnClickListener(this);
		((Button) findViewById(R.id.keypad_4)).setOnClickListener(this);
		((Button) findViewById(R.id.keypad_5)).setOnClickListener(this);
		((Button) findViewById(R.id.keypad_6)).setOnClickListener(this);
		((Button) findViewById(R.id.keypad_7)).setOnClickListener(this);
		((Button) findViewById(R.id.keypad_8)).setOnClickListener(this);
		((Button) findViewById(R.id.keypad_9)).setOnClickListener(this);
		((Button) findViewById(R.id.keypad_0)).setOnClickListener(this);
		((Button) findViewById(R.id.keypad_CLEAR)).setOnClickListener(this);
		((Button) findViewById(R.id.keypad_EN_CH)).setOnClickListener(this);
		((Button) findViewById(R.id.keypad_BACK)).setOnClickListener(this);
	}

	public void initListener() {
		grid.setOnItemClickListener(new _cls3());
		grid.setOnScrollListener(new _cls4());
	}

	public void initView() {
		initKeyBoard();
		input = (TextView) findViewById(R.id.keypad_input);
		empteyTv = (TextView) findViewById(R.id.serch_emptey_text);
		grid = (GridView) findViewById(R.id.search_result);
		grid.setSelector(new ColorDrawable(0));
		adapter = new VideoInfoAdapter(this, null, true);
		grid.setAdapter(adapter);
		grid.setEmptyView(empteyTv);
		sumResult = (TextView) findViewById(R.id.search_sum);
	}

	public void onBackPressed() {
		if (sb.length() > 0) {
			sb.deleteCharAt(-1 + sb.length());
			input.setText(sb.toString());
			page = 1;
			handler.removeMessages(0);
			handler.sendEmptyMessageDelayed(0, 500L);
		} else {
			super.onBackPressed();
			overridePendingTransition(R.anim.fade1, R.anim.fade2);
		}
	}

	public void onClick(View view) {
		int i = view.getId();
		if (i != R.id.keypad_CLEAR) // goto _L2; else goto _L1
		{
			if (i == R.id.keypad_BACK) {
				if (sb.length() > 0)
					sb.deleteCharAt(-1 + sb.length());
			} else if (i != R.id.keypad_EN_CH)
				sb.append(((Button) view).getText());
		} else {
			sb = new StringBuilder();
		}
		input.setText(sb.toString());
		page = 1;
		handler.removeMessages(0);
		searchPageData = null;
		handler.sendEmptyMessageDelayed(0, 500L);
	}

	@Override
	protected void onCreate(Bundle bundle) {
		// TODO Auto-generated method stub
		super.onCreate(bundle);
		setContentView(R.layout.search);
		hostURL = (new StringBuilder(String.valueOf(MyApp.baseServer))).append(
				"so").toString();
		sb = new StringBuilder();
		initView();
		initListener();
	}

	private static final int MSG_SERCH = 0;
	private static final int MSG_SERCH_RESULT = 1;
	private static final String NUM = "&num=30";
	private static final String PAGE = "&page=";
	private VideoInfoAdapter adapter;
	private TextView empteyTv;
	private GridView grid;
	private Handler handler;
	private String hostURL;
	private TextView input;
	private Runnable loadThread;
	private int page;
	private StringBuilder sb;
	private VideoList searchPageData;
	private TextView sumResult;

	private class _cls1 implements Runnable {

		public void run() {
			String s = (new StringBuilder(String.valueOf(hostURL)))
					.append(sb.toString()).append(NUM).append(PAGE)
					.append(page).toString();
			VideoList videolist = SerachBiz.parseSerachResult(hostURL,
					sb.toString(), page);
			if (s.equals((new StringBuilder(String.valueOf(hostURL)))
					.append(sb.toString()).append(NUM).append(PAGE)
					.append(page).toString())) {
				Message message = new Message();
				message.obj = videolist;
				message.what = 1;
				handler.sendMessage(message);
			}
		}
	}

	@SuppressLint("HandlerLeak")
	private class MyHandler extends Handler {

		public void handleMessage(Message message) {
			switch (message.what) {
			case MSG_SERCH:
				if (!TextUtils.isEmpty(sb.toString()))
					(new Thread(loadThread)).start();
				break;
			case MSG_SERCH_RESULT:
				if (searchPageData != null) {
					VideoList videolist = (VideoList) message.obj;
					if (videolist != null && videolist.video != null)
						if (searchPageData.video == null)
							searchPageData = videolist;
						else
							searchPageData.video.addAll(videolist.video);
				} else {
					searchPageData = (VideoList) message.obj;
				}
				break;

			default:
				break;
			}
			if (searchPageData != null) {
//				adapter.changData(searchPageData.video);
				sumResult.setText((new StringBuilder("总共"))
						.append(searchPageData.video_count).append("部")
						.toString());
			} else {
				adapter.changData(null);
				sumResult.setText("总共0部");
			}
		}
	}

	private class _cls3 implements AdapterView.OnItemClickListener {

		public void onItemClick(AdapterView<?> adapterview, View view, int i,
				long l) {
			Intent intent = new Intent(SearchActivity.this,
					VideoDetailsActivity.class);
			intent.putExtra("VIDEODEAIL", ((VideoInfo) adapter.getItem(i)).id);
			startActivity(intent);
			overridePendingTransition(R.anim.zoomout, R.anim.zoomin);
		}

	}

	private class _cls4 implements android.widget.AbsListView.OnScrollListener {

		public void onScroll(AbsListView abslistview, int i, int j, int k) {
			if (i >= k - j && searchPageData != null
					&& page < searchPageData.maxpage) {
				SearchActivity searchactivity = SearchActivity.this;
				searchactivity.page = 1 + searchactivity.page;
				handler.sendEmptyMessage(0);
			}
		}

		@Override
		public void onScrollStateChanged(AbsListView arg0, int arg1) {
			// TODO Auto-generated method stub

		}
	}

}