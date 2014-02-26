package cn.leo.dcinema.activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.*;
import android.widget.*;
import cn.leo.dcinema.adapters.TypeDetailsSubMenuAdapter;
import cn.leo.dcinema.adapters.VideoInfoAdapter;
import cn.leo.dcinema.app.MyApp;
import cn.leo.dcinema.biz.ApkUpdatebiz;
import cn.leo.dcinema.biz.VodListBiz;
import cn.leo.dcinema.effect.AnimationSetUtils;
import cn.leo.dcinema.model.ApkUpdateInfo;
import cn.leo.dcinema.model.VideoList;
import cn.leo.dcinema.util.FileUtils;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import cn.leo.dcinema.Constants;
import cn.leo.dcinema.R;

public class TypeDetailsActivity extends BaseActivity implements
		View.OnKeyListener, AdapterView.OnItemClickListener {

	Runnable apkUpdate = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				ApkUpdateInfo updateInfo = ApkUpdatebiz.parseUpdataInfo(
						TypeDetailsActivity.this, Constants.updateDataJSONUrl);
				if (updateInfo != null) {
					int curVerCode = TypeDetailsActivity.this
							.getPackageManager().getPackageInfo(
									TypeDetailsActivity.this.getApplication()
											.getPackageName(), 0).versionCode;
					if (updateInfo.verCode > curVerCode) {
						String str = ApkUpdatebiz.downLoadFile(
								TypeDetailsActivity.this, updateInfo.apkurl,
								updateInfo.apkmd5);
						if (str != null) {
							updateInfo.apkpath = str;
							Intent intent = new Intent(
									Constants.ApkUpdatebiz_ACTION);
							intent.putExtra("updatemsg", updateInfo.instruction);
							intent.putExtra("filepath", updateInfo.apkpath);
							sendStickyBroadcast(intent);
						}
					}
				}
			} catch (Exception e) {
				Log.e(TAG, e.toString());
			}
		}
	};

	public TypeDetailsActivity() {
		currentPage = 1;
		param = new HashMap<String, String>();
		popHint = "";
		popdissmiss = new DissmissPop();
		handler = new MainHandler(this);
	}

	private void clearFilter() {
		((TypeDetailsSubMenuAdapter) rankLv.getAdapter()).setSelctItem(-1);
		rankLv.setItemChecked(-1, true);
		((TypeDetailsSubMenuAdapter) areaLv.getAdapter()).setSelctItem(-1);
		areaLv.setItemChecked(-1, true);
		((TypeDetailsSubMenuAdapter) sharpLv.getAdapter()).setSelctItem(-1);
		sharpLv.setItemChecked(-1, true);
		((TypeDetailsSubMenuAdapter) typeLv.getAdapter()).setSelctItem(-1);
		typeLv.setItemChecked(-1, true);
		((TypeDetailsSubMenuAdapter) timeLv.getAdapter()).setSelctItem(-1);
		timeLv.setItemChecked(-1, true);
		((TypeDetailsSubMenuAdapter) sortLv.getAdapter()).setSelctItem(-1);
		sortLv.setItemChecked(-1, true);
		param.clear();
		param.put("num", "30");
		fliterType.setText(null);
		videoList = null;
		initData(currentPage);
	}

	private void getFlitedData() {
		splitJointString();
		videoList = null;
		initData(currentPage);
		menuLayout.setVisibility(8);
		menuLayout.clearFocus();
		grid.setFocusable(true);
	}

	private int getGridSelectionState() {
		int i = grid.getSelectedItemPosition();
		int j = grid.getChildCount();
		byte byte0;
		if (i + 12 >= j)
			byte0 = BOTTOM;
		else if (i % 6 == 0)
			byte0 = LEFT;
		else if (i % 6 == 5)
			byte0 = RIGHT;
		else
			byte0 = -1;
		return byte0;
	}

	private void initData(int page) {
		(new ParseVideoListThread(page)).start();
	}

	private void initIntent() {
		param.put("num", "30");
	}

	private void initListener() {
		typeLv.setOnKeyListener(this);
		typeLv.setOnItemClickListener(this);
		areaLv.setOnKeyListener(this);
		areaLv.setOnItemClickListener(this);
		timeLv.setOnKeyListener(this);
		timeLv.setOnItemClickListener(this);
		rankLv.setOnKeyListener(this);
		rankLv.setOnItemClickListener(this);
		sharpLv.setOnKeyListener(this);
		sharpLv.setOnItemClickListener(this);
		sortLv.setOnKeyListener(this);
		sortLv.setOnItemClickListener(this);
		grid.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> adapterview, View view,
					int i, long l) {
				MyApp.playSound("top_float");
			}

			public void onNothingSelected(AdapterView<?> adapterview) {
			}
		});
		detailMenuKey.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				if (menuLayout.getVisibility() == 0)
					menuLayout.setVisibility(4);
				else
					menuLayout.setVisibility(0);
			}
		});
		grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> adapterview, View view,
					int i, long l) {
				MyApp.playSound(Constants.SOUND_COMFIRE);
				Intent intent = new Intent(TypeDetailsActivity.this,
						VideoDetailsActivity.class);
				intent.putExtra(Constants.INTENT_KEY_VIDEODEAIL,
						videoList.video.get(i));
				startActivity(intent);
				overridePendingTransition(R.anim.zoout, R.anim.zoin);
			}
		});
		grid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> adapterview,
					View view, int i, long l) {
				menuLayout.setVisibility(0);
				grid.clearFocus();
				grid.setFocusable(false);
				menuLayout.requestFocus();
				return true;
			}
		});
		grid.setOnScrollListener(new AbsListView.OnScrollListener() {

			public void onScroll(AbsListView abslistview, int i, int j, int k) {
				if (i >= k - j)
					pageDown();
			}

			public void onScrollStateChanged(AbsListView abslistview, int i) {
			}
		});
	}

	private void initView() {
		typeName = (TextView) findViewById(R.id.type_details_type);
		sum = (TextView) findViewById(R.id.type_details_sum);
		detailMenuKey = (ImageView) findViewById(R.id.detail_menu_key);
		AnimationSetUtils.SetMenuAnimation(detailMenuKey, R.drawable.menu_key,
				R.drawable.menu_key_blue);
		grid = (GridView) findViewById(R.id.type_details_grid);
		grid.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
		grid.setSelector(new ColorDrawable(0));
		gridAdapter = new VideoInfoAdapter(this, null);
		grid.setAdapter(gridAdapter);
		grid.setFocusableInTouchMode(true);
		fliterType = (TextView) findViewById(R.id.type_details_fliter_type);
		menuLayout = findViewById(R.id.type_details_menulayout);
		menuLayout.setVisibility(View.GONE);
		typeLv = (ListView) menuLayout.findViewById(R.id.filter_list_type);
		typeLv.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
		areaLv = (ListView) menuLayout.findViewById(R.id.filter_list_area);
		areaLv.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
		timeLv = (ListView) menuLayout.findViewById(R.id.filter_list_year);
		timeLv.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
		rankLv = (ListView) menuLayout.findViewById(R.id.filter_list_rank);
		rankLv.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
		sharpLv = (ListView) menuLayout.findViewById(R.id.filter_list_sharp);
		sharpLv.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
		sortLv = (ListView) menuLayout.findViewById(R.id.filter_list_sort);
		sortLv.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
	}

	private void loadData() {
		if (videoList != null) {
			sumPages = videoList.maxpage;
			typeName.setText("3D电影");
			sum.setText((new StringBuilder("共 ")).append(videoList.video_count)
					.append(" 部").toString());
			gridAdapter.changData(videoList.video);
		}
		progressDismiss();
	}

	private int makePageEnable(String s) {
		int i = Integer.valueOf(s).intValue();
		if (i >= 1) {
			if (i > sumPages)
				i = sumPages;
		} else {
			i = 1;
		}
		return i;
	}

	private void pageDown() {
		if (currentPage < sumPages) {
			currentPage++;
			Log.d(TAG, (new StringBuilder("页码：")).append(currentPage)
					.toString());
			param.put("page",
					(new StringBuilder(String.valueOf(currentPage))).toString());
			initData(currentPage);
		}
	}

	private void pageUp() {
		if (currentPage >= 1) {
			currentPage--;
			param.put("page",
					(new StringBuilder(String.valueOf(currentPage))).toString());
			initData(currentPage);
		}
	}

	private void splitJointString() {
		StringBuilder stringbuilder;
		int i;
		int l;
		int i1;
		int j1;
		String s1;
		String s2;
		currentPage = 1;
		param.put("page",
				(new StringBuilder(String.valueOf(currentPage))).toString());
		stringbuilder = new StringBuilder();
		i = rankLv.getCheckedItemPosition();
		if (!(i <= 0 || i >= 3)) {
			String s5 = (String) rankLv.getAdapter().getItem(i);
			if (!s5.equals("最新上线")) {
				if (s5.equals("最热门"))
					param.put("top", "2");
			} else {
				param.put("top", "1");
			}
			stringbuilder.append((new StringBuilder(String.valueOf(s5)))
					.append(" ").toString());
		}
		int j = areaLv.getCheckedItemPosition();
		if (j >= 0) {
			String s4 = (String) areaLv.getAdapter().getItem(j);
			param.put("area", s4);
			stringbuilder.append((new StringBuilder(String.valueOf(s4)))
					.append(" ").toString());
		}
		int k = sharpLv.getCheckedItemPosition();
		if (k >= 0) {
			String s3 = (String) sharpLv.getAdapter().getItem(k);
			if (s3.equals("超清"))
				param.put("qxd", "3");
			else if (s3.equals("高清"))
				param.put("qxd", "2");
			else
				param.put("qxd", "");
			stringbuilder.append((new StringBuilder(String.valueOf(s3)))
					.append(" ").toString());
		}
		l = typeLv.getCheckedItemPosition();
		if (l >= 0) {
			s2 = (String) typeLv.getAdapter().getItem(l);
			param.put("item", s2);
			stringbuilder.append((new StringBuilder(String.valueOf(s2)))
					.append(" ").toString());
		}
		i1 = timeLv.getCheckedItemPosition();
		if (i1 >= 0) {
			s1 = (String) timeLv.getAdapter().getItem(i1);
			param.put("year", s1);
			stringbuilder.append((new StringBuilder(String.valueOf(s1)))
					.append(" ").toString());
		}
		j1 = sortLv.getCheckedItemPosition();
		if (j1 >= 0) {
			String s = (String) sortLv.getAdapter().getItem(j1);
			if (s.equals("从新到旧"))
				param.put("sort", "1");
			else if (s.equals("从旧到新"))
				param.put("sort", "2");
			else if (s.equals("评分从高到低"))
				param.put("sort", "3");
			else if (s.equals("评分从低到高"))
				param.put("sort", "4");
			else
				param.put("sort", "0");
			stringbuilder.append(s);
		}
		fliterType.setText(stringbuilder.toString());
	}

	private ApkUpdateReciver apkUpdateReciver;

	private void registerReceiver() {
		apkUpdateReciver = new ApkUpdateReciver();
		registerReceiver(this.apkUpdateReciver, new IntentFilter(
				Constants.ApkUpdatebiz_ACTION));
	}

	@Override
	protected void onCreate(Bundle bundle) {
		// TODO Auto-generated method stub
		super.onCreate(bundle);
		setContentView(R.layout.type_details);
		initIntent();
		initView();
		initData(currentPage);
		initListener();
		progressShow();
		registerReceiver();
		new Thread(apkUpdate).start();
	}

	private void unregisterReceiver() {
		unregisterReceiver(apkUpdateReciver);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if ((updatDialog != null) && (updatDialog.isShowing()))
			updatDialog.dismiss();
		unregisterReceiver();
		super.onDestroy();
	}

	public void onItemClick(AdapterView<?> adapterview, View view, int i, long l) {
		if (!adapterview.equals(rankLv)) {
			if (adapterview.equals(areaLv)) {
				areaLv.setItemChecked(i, true);
				((TypeDetailsSubMenuAdapter) areaLv.getAdapter())
						.setSelctItem(i);
				getFlitedData();
			} else if (adapterview.equals(sharpLv)) {
				sharpLv.setItemChecked(i, true);
				((TypeDetailsSubMenuAdapter) sharpLv.getAdapter())
						.setSelctItem(i);
				getFlitedData();
			} else if (adapterview.equals(typeLv)) {
				typeLv.setItemChecked(i, true);
				((TypeDetailsSubMenuAdapter) typeLv.getAdapter())
						.setSelctItem(i);
				getFlitedData();
			} else if (adapterview.equals(timeLv)) {
				timeLv.setItemChecked(i, true);
				((TypeDetailsSubMenuAdapter) timeLv.getAdapter())
						.setSelctItem(i);
				getFlitedData();
			} else if (adapterview.equals(sortLv)) {
				sortLv.setItemChecked(i, true);
				((TypeDetailsSubMenuAdapter) sortLv.getAdapter())
						.setSelctItem(i);
				getFlitedData();
			}
		} else {
			if (i == 0) {
				startActivity(new Intent(this, SearchActivity.class));
				overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
				menuLayout.setVisibility(View.GONE);
				menuLayout.clearFocus();
				grid.setFocusable(true);
			} else if (i == 3) {
				clearFilter();
			} else {
				rankLv.setItemChecked(i, true);
				((TypeDetailsSubMenuAdapter) rankLv.getAdapter())
						.setSelctItem(i);
				getFlitedData();
			}
		}
	}

	public boolean onKey(View view, int i, KeyEvent keyevent) {
		if (!(keyevent.getAction() != 0 || i != 21 && i != 22)) {
			switch (view.getId()) {
			case R.id.filter_list_rank:
				int k1 = rankLv.getSelectedItemPosition();
				if (k1 == 1 || k1 == 2) {
					rankLv.setItemChecked(k1, true);
					((TypeDetailsSubMenuAdapter) rankLv.getAdapter())
							.setSelctItem(k1);
				} else {
					rankLv.setItemChecked(-1, true);
					((TypeDetailsSubMenuAdapter) rankLv.getAdapter())
							.setSelctItem(-1);
				}
				break;
			case R.id.filter_list_area:
				int j1 = areaLv.getSelectedItemPosition();
				areaLv.setItemChecked(j1, true);
				((TypeDetailsSubMenuAdapter) areaLv.getAdapter())
						.setSelctItem(j1);
				break;
			case R.id.filter_list_sharp:
				int i1 = sharpLv.getSelectedItemPosition();
				sharpLv.setItemChecked(i1, true);
				((TypeDetailsSubMenuAdapter) sharpLv.getAdapter())
						.setSelctItem(i1);
				break;
			case R.id.filter_list_type:
				int l = typeLv.getSelectedItemPosition();
				typeLv.setItemChecked(l, true);
				((TypeDetailsSubMenuAdapter) typeLv.getAdapter())
						.setSelctItem(l);
				break;
			case R.id.filter_list_year:
				int k = timeLv.getSelectedItemPosition();
				timeLv.setItemChecked(k, true);
				((TypeDetailsSubMenuAdapter) timeLv.getAdapter())
						.setSelctItem(k);
				break;
			case R.id.filter_list_sort:
				int j = sortLv.getSelectedItemPosition();
				sortLv.setItemChecked(j, true);
				((TypeDetailsSubMenuAdapter) sortLv.getAdapter())
						.setSelctItem(j);
				break;
			default:
				break;
			}
		}
		return false;
	}

	public boolean onKeyDown(int keyCode, KeyEvent keyevent) {
		boolean flag = true;
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			if (menuLayout.getVisibility() == View.VISIBLE) {
				menuLayout.clearFocus();
				menuLayout.setVisibility(View.GONE);
				grid.setFocusable(flag);
				return flag;
			}
			return super.onKeyDown(keyCode, keyevent);
		case KeyEvent.KEYCODE_DPAD_DOWN:
			if (menuLayout.getVisibility() == 8
					&& getGridSelectionState() == BOTTOM)
				pageDown();
			break;
		case KeyEvent.KEYCODE_DPAD_LEFT:
			if (menuLayout.getVisibility() == 8
					&& getGridSelectionState() == LEFT) {
				int j = -1 + grid.getSelectedItemPosition();
				grid.setSelection(j);
			}
			flag = false;
			return flag;
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			if (menuLayout.getVisibility() == View.GONE)
				if (getGridSelectionState() == RIGHT) {
					int k = 1 + grid.getSelectedItemPosition();
					grid.setSelection(k);
				} else if (getGridSelectionState() == BOTTOM)
					pageDown();
			break;
		case KeyEvent.KEYCODE_MENU:
			if (menuLayout.getVisibility() == View.GONE) {
				MyApp.playSound(Constants.SOUND_COMFIRE);
				menuLayout.setVisibility(View.VISIBLE);
				grid.clearFocus();
				grid.setFocusable(false);
				menuLayout.requestFocus();
			} else {
				if (menuLayout.getVisibility() == View.VISIBLE) {
					MyApp.playSound(Constants.SOUND_BACK);
					menuLayout.setVisibility(View.GONE);
					menuLayout.clearFocus();
					grid.setFocusable(flag);
				}
			}
			break;
		default:
			break;
		}
		return flag;
	}

	private static final int BOTTOM = 2;
	private static final int LEFT = 0;
	private static final int MSG_FAILED = 3;
	private static final int MSG_INIT_MENU = 2;
	private static final int MSG_LOAD_ERR = 4;
	private static final int MSG_VIDEOINFO_LOADED = 0;
	private static final int RIGHT = 1;
	private static final String TAG = "TypeDetailsActivity";
	@SuppressWarnings("unused")
	private static final int popWait = 2000;
	private ListView areaLv;
	private static int currentPage;
	private ImageView detailMenuKey;
	private TextView fliterType;
	private GridView grid;
	private VideoInfoAdapter gridAdapter;
	private Handler handler;
	private View menuLayout;
	private PopupWindow pagePop;
	private HashMap<String, String> param;
	private String popHint;
	@SuppressWarnings("unused")
	private DissmissPop popdissmiss;
	private ListView rankLv;
	private ListView sharpLv;
	private ListView sortLv;
	private TextView sum;
	private int sumPages;
	private ListView timeLv;
	private ListView typeLv;
	private TextView typeName;
	private VideoList videoList;

	private class DissmissPop implements Runnable {

		public void run() {
			currentPage = makePageEnable(popHint);
			param.put("page",
					(new StringBuilder(String.valueOf(currentPage))).toString());
			initData(currentPage);
			popHint = "";
			pagePop.dismiss();
		}
	}

	private static class MainHandler extends Handler {
		private final WeakReference<TypeDetailsActivity> mActivity;

		public MainHandler(TypeDetailsActivity activity) {
			mActivity = new WeakReference<TypeDetailsActivity>(activity);
		}

		@Override
		public void handleMessage(Message message) {
			switch (message.what) {
			case MSG_VIDEOINFO_LOADED:
				mActivity.get().loadData();
				break;
			case MSG_INIT_MENU:

				break;
			case MSG_FAILED:
				AlertDialog alertdialog = (new AlertDialog.Builder(
						mActivity.get())).setTitle("网络异常")
						.setMessage("网络异常，请稍后重试！").create();
				class NetAlertDiaClickListener implements
						DialogInterface.OnClickListener {
					int currentPage = 1;

					public NetAlertDiaClickListener(int page) {
						// TODO Auto-generated constructor stub
						currentPage = page;
					}

					@Override
					public void onClick(DialogInterface dialoginterface, int i) {
						// TODO Auto-generated method stub
						switch (i) {
						case DialogInterface.BUTTON_NEUTRAL:
							mActivity.get().initData(currentPage);
							break;
						case DialogInterface.BUTTON_NEGATIVE:

							break;
						case DialogInterface.BUTTON_POSITIVE:
							mActivity.get().finish();
							break;

						default:
							break;
						}
					}
				}
				NetAlertDiaClickListener listener = new NetAlertDiaClickListener(
						currentPage);
				alertdialog.setButton(DialogInterface.BUTTON_POSITIVE, "确认",
						listener);
				alertdialog.setButton(DialogInterface.BUTTON_NEUTRAL, "重试",
						listener);
				alertdialog.show();
				break;
			case MSG_LOAD_ERR:
				Toast.makeText(mActivity.get(), "请求当前页面数据失败，请稍候重试或者继续！",
						Toast.LENGTH_SHORT).show();

				break;
			}
		}
	}

	private class ParseVideoListThread extends Thread {
		int currentPage = 1;

		public ParseVideoListThread(int page) {
			// TODO Auto-generated constructor stub
			currentPage = page;
		}

		public void run() {
			android.os.Process
					.setThreadPriority(android.os.Process.THREAD_PRIORITY_FOREGROUND);
			if (videoList != null) {
				VideoList videolist = VodListBiz.parseVodListData(currentPage,
						30);
				if (videolist != null) {
					videoList.video.addAll(videolist.video);
					handler.sendEmptyMessage(MSG_VIDEOINFO_LOADED);
				}
			} else {
				videoList = VodListBiz.parseVodListData(currentPage, 30);
				handler.sendEmptyMessage(MSG_VIDEOINFO_LOADED);
			}
		}
	}

	public AlertDialog updatDialog;

	private AlertDialog createUpdateDialog(String updateMsg, final String path) {
		AlertDialog alertdialog = (new AlertDialog.Builder(this))
				.setTitle("软件版本更新").setMessage(updateMsg).create();
		DialogInterface.OnClickListener onclicklistener = new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int key) {
				// TODO Auto-generated method stub
				switch (key) {
				case DialogInterface.BUTTON_NEUTRAL:

					break;
				case DialogInterface.BUTTON_POSITIVE:
					FileUtils.modifyFile(new File(path));
					Intent localIntent = new Intent(Intent.ACTION_DEFAULT);
					localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					localIntent.setDataAndType(Uri.parse("file://" + path),
							"application/vnd.android.package-archive");
					TypeDetailsActivity.this.getApplicationContext()
							.startActivity(localIntent);
					break;
				default:
					break;
				}
			}
		};
		alertdialog.setButton(DialogInterface.BUTTON_POSITIVE, "立即更新",
				onclicklistener);
		alertdialog.setButton(DialogInterface.BUTTON_NEUTRAL, "下次更新",
				onclicklistener);

		return alertdialog;
	}

	class ApkUpdateReciver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String s = intent.getAction();
			System.out.println(s);
			if (s.equals(Constants.ApkUpdatebiz_ACTION)) {
				String updateMsg = intent.getStringExtra("updatemsg");
				String filePath = intent.getStringExtra("filepath");
				Log.d("HomeActivity",
						(new StringBuilder("ApkUpdateReciver    msg ="))
								.append(updateMsg).append(" ,path =")
								.append(filePath).toString());
				updatDialog = createUpdateDialog(updateMsg, filePath);
				updatDialog
						.setOnShowListener(new DialogInterface.OnShowListener() {

							@Override
							public void onShow(DialogInterface dialog) {
								// TODO Auto-generated method stub

								// TODO Auto-generated method stub
								updatDialog.getButton(
										DialogInterface.BUTTON_NEUTRAL)
										.requestFocus();

							}
						});
				updatDialog.show();
				removeStickyBroadcast(intent);
			}
		}
	}

}
