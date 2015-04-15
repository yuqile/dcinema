package cn.leo.dcinema.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.*;
import android.os.Process;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.*;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.*;

import net.youmi.android.spot.SpotManager;

import cn.leo.dcinema.Constants;
import cn.leo.dcinema.R;
import cn.leo.dcinema.adapters.*;
import cn.leo.dcinema.app.MyApp;
import cn.leo.dcinema.biz.VodMovieDetailsBiz;
import cn.leo.dcinema.db.VodDataHelper;
import cn.leo.dcinema.effect.Reflect3DImage;
import cn.leo.dcinema.model.*;
import cn.leo.dcinema.player.VodPlayer;
import cn.leo.dcinema.util.BitmapWorkerTask;
import cn.leo.dcinema.util.StringUtil;

import java.lang.ref.WeakReference;
import java.util.*;

public class VideoDetailsActivity extends BaseActivity implements
        View.OnKeyListener {
    public VideoDetailsActivity() {
        index = 0;
        handler = new GetVideoDetailInfoResultHandler(this);
    }

    private void CreateArtLayout(final ArrayList<VideoSet> sets) {
        System.out.println((new StringBuilder("集数")).append(sets.size())
                .toString());
        ListView listview = (ListView) findViewById(R.id.details_key_list);
        GridView gridview = (GridView) findViewById(R.id.details_key_grid);
        gridview.setSelector(new ColorDrawable(0));
        listview.setSelector(new ColorDrawable(0));
        gridview.setAdapter(new DetailsKeyGridAdapter(this, countStrArr(sets,
                false)));
        final DetailsKeyListAdapter listAdapter = new DetailsKeyListAdapter(
                this, pagingSets(sets, index));
        listview.setAdapter(listAdapter);
        gridview.setOnItemClickListener(new GridviewOnItemClickListener(
                listAdapter, sets));
        gridview.setOnKeyListener(this);
        listview.setOnItemClickListener(new ListViewOnItemClickListener());
        listview.setOnKeyListener(this);
    }

    private ArrayList<View> addViewToPager(ArrayList<VideoSet> arraylist,
                                           boolean flag) {
        int i;
        LinearLayout linearlayout;
        ArrayList<View> arraylist1;
        ViewGroup.LayoutParams layoutparams;
        int l;
        i = arraylist.size();
        System.out.println((new StringBuilder("集数")).append(i).toString());
        linearlayout = new LinearLayout(this);
        arraylist1 = new ArrayList<View>();
        layoutparams = new android.view.ViewGroup.LayoutParams(-1, 50);
        linearlayout.setLeft(40);
        linearlayout.setLayoutParams(layoutparams);
        if (flag)
            return null;

        for (l = 1; l <= i; l++) {
            linearlayout.addView(createSetBTN(l));
            if (l % 10 == 0) {
                arraylist1.add(linearlayout);
                linearlayout = new LinearLayout(this);
                linearlayout.setLayoutParams(layoutparams);
                linearlayout.setLeft(40);
            } else if (l == i)
                arraylist1.add(linearlayout);
        }
        int j = i;
        int k = 1;
        while (j >= 1) {
            linearlayout.addView(createSetBTN(j));
            if (k % 10 == 0) {
                arraylist1.add(linearlayout);
                linearlayout = new LinearLayout(this);
                linearlayout.setLayoutParams(layoutparams);
                linearlayout.setLeft(40);
            } else if (j == 1)
                arraylist1.add(linearlayout);
            j--;
            k++;
        }
        return arraylist1;
    }

    private List<String> countStrArr(ArrayList<VideoSet> arraylist, boolean flag) {
        int i;
        ArrayList<String> arraylist1;
        int l;
        StringBuilder stringbuilder1;
        i = arraylist.size();
        arraylist1 = new ArrayList<String>();
        stringbuilder1 = new StringBuilder();
        for (l = 1; l < i; l++) {
            if (l % 10 == 1) {
                stringbuilder1.append(l);
                stringbuilder1.append('-');
            } else if (l % 10 == 0) {
                stringbuilder1.append(l);
                arraylist1.add(stringbuilder1.toString());
                stringbuilder1 = new StringBuilder();
            } else if (l == i) {
                stringbuilder1.append(l);
                arraylist1.add(stringbuilder1.toString());
            }
        }

        int j = i;
        int k = 1;
        StringBuilder stringbuilder = new StringBuilder();
        while (j >= 1) {
            if (k % 10 == 1) {
                stringbuilder.append(j);
                stringbuilder.append('-');
            } else if (k % 10 == 0) {
                stringbuilder.append(j);
                arraylist1.add(stringbuilder.toString());
                stringbuilder = new StringBuilder();
            } else if (j == 1) {
                stringbuilder.append(j);
                arraylist1.add(stringbuilder.toString());
            }
            j--;
            k++;
        }
        return arraylist1;
    }

    private Button createSetBTN(int i) {
        final Button btn = new Button(this);
        btn.setWidth(120);
        btn.setHeight(55);
        btn.setText((new StringBuilder("第")).append(i).append("集").toString());
        btn.setTextSize(18F);
        btn.setTag(Integer.valueOf(i - 1));
        btn.setBackgroundResource(R.drawable.video_details_btn_selector);
        btn.setOnClickListener(new BtnOnClickListener(btn));
        btn.setOnKeyListener(this);
        btn.setTextColor(-3355444);
        return btn;
    }

    private void initData() {
        vodMovieItem = (VideoInfo) getIntent().getSerializableExtra(
                Constants.INTENT_KEY_VIDEODEAIL);
        (new ParseDetailInfoThread()).start();
    }

    private void initDialog() {
        dialog = (new AlertDialog.Builder(this)).setTitle("温馨提示")
                .setMessage("加载失败，稍后重试！").create();
        RetryDialog retryDialog = new RetryDialog();
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "确认", retryDialog);
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "重试", retryDialog);
    }

    private void initListener() {
        replay.setOnClickListener(new ReplayOnClickListener());
        replay.setOnKeyListener(this);
        play.setOnClickListener(new PlayOnClickListener());
        play.setOnKeyListener(this);
        sourcesLin.setOnCheckedChangeListener(new SourcesLinOnCheckListener());
        sourcesLin.setOnKeyListener(this);
        colection.setOnClickListener(new ColectionOnClickListener());
        colection.setOnKeyListener(this);
        choose.setOnClickListener(new ChooseOnClickListener());
        choose.setOnKeyListener(this);
        hotGrid.setOnItemClickListener(new HotGridOnItemClickListener());
        hotGrid.setOnKeyListener(this);
    }

    private void initView() {
        shadow = (ImageView) findViewById(R.id.details_poster_shadow);
        shadow.setVisibility(View.GONE);
        poster = (ImageView) findViewById(R.id.details_poster);
        poster.setImageBitmap(Reflect3DImage.skewImage(BitmapFactory
                .decodeResource(getResources(), R.drawable.hao260x366), 260,
                366, 50));
        sharpness = (ImageView) findViewById(R.id.details_sharpness);
        videoName = (TextView) findViewById(R.id.details_name);
        point = (TextView) findViewById(R.id.details_rate);
        editors = (TextView) findViewById(R.id.details_director);
        area = (TextView) findViewById(R.id.details_playTimes);
        actors = (TextView) findViewById(R.id.details_actors);
        type = (TextView) findViewById(R.id.details_update);
        year = (TextView) findViewById(R.id.details_year);
        introduce = (TextView) findViewById(R.id.details_video_introduce);
        sourcesLin = (RadioGroup) findViewById(R.id.video_details_resources);
        replay = (Button) findViewById(R.id.details_replay);
        play = (Button) findViewById(R.id.details_play);
        play.setVisibility(View.GONE);
        choose = (Button) findViewById(R.id.details_choose);
        choose.setVisibility(View.GONE);
        colection = (Button) findViewById(R.id.details_colection);
        colection.setVisibility(View.GONE);
        hotGrid = (GridView) findViewById(R.id.details_recommend);
        keyLayoutT = (LinearLayout) findViewById(R.id.details_key_tv);
        keyLayoutA = (LinearLayout) findViewById(R.id.details_key_arts);
    }

    private List<VideoSet> pagingSets(ArrayList<VideoSet> arraylist, int i) {
        Log.i("info", (new StringBuilder("Current Page=")).append(i).toString());
        boolean flag;
        List<VideoSet> list;
        Iterator<VideoSet> iterator;
        if (10 * (i + 1) <= arraylist.size())
            flag = true;
        else
            flag = false;
        if (flag)
            list = arraylist.subList(i * 10, 10 * (i + 1));
        else
            list = arraylist.subList(i * 10, arraylist.size());
        iterator = list.iterator();
        do {
            if (!iterator.hasNext())
                return list;
            VideoSet videoset = (VideoSet) iterator.next();
            System.out.println(videoset.toString());
        } while (true);
    }

    private void setSharpNessLog(String s) {
        if (s != null && !s.equals(""))
            if (s.contains("超清") || s.contains("SD"))
                sharpness.setImageResource(R.drawable.video_details_superhd);
            else if (s.contains("高清") || s.contains("HD"))
                sharpness.setImageResource(R.drawable.video_details_hd);
            else if (s.contains("DVD") || s.contains("流畅") || s.contains("标清"))
                sharpness.setImageResource(R.drawable.video_details_dvd);
            else
                point.setText(s);
    }

    private void showDetailInfo() {
        if (media != null) {
            // if (!media.banben.contains("更新")) {
            // if (dbHelper.queryHasRecode(media.id, 1)) {
            // colection
            // .setBackgroundResource(R.drawable.video_details_zhuiju_selector);
            // colection.setText("取消");
            // } else {
            // colection
            // .setBackgroundResource(R.drawable.video_details_zhuiju_n_selector);
            // colection.setText("收藏");
            // }
            // } else {
            // if (dbHelper.queryHasRecode(media.id, 2)) {
            // colection
            // .setBackgroundResource(R.drawable.video_details_zhuiju_selector);
            // colection.setText("取消");
            // } else {
            // colection
            // .setBackgroundResource(R.drawable.video_details_zhuiju_n_selector);
            // colection.setText("追剧");
            // }
            // }
            // playRecode = dbHelper.queryRecode(media.id, 3);
            // if (playRecode == null) {
            // play.setText("播放");
            // // createSourceLayout(media.playlist, 0);
            // } else {
            // replay.setVisibility(0);
            // play.setText("续播");
            // play.setBackgroundResource(R.drawable.details_replay_sel);
            // // createSourceLayout(media.playlist, playRecode.sourceIndex);
            // }
            create3DPost(poster, media.img);
            // createHotLayout(media.recommends);
            play.setVisibility(View.VISIBLE);
            videoName.setText(media.title);
            editors.setText((new StringBuilder("导演：")).append(media.director)
                    .toString());
            actors.setText((new StringBuilder("演员：")).append(media.actor)
                    .toString());
            introduce.setText(media.info);
            // area.setText((new StringBuilder("地区：")).append(media.area)
            // .toString());
            // type.setText((new StringBuilder("类别：")).append(media.cate)
            // .toString());
            // year.setText(media.year);
            // setSharpNessLog(media.banben);
            progressDismiss();
            play.requestFocus();
        }
    }

    protected void CreateTvLayout(ArrayList<VideoPlayUrl> arraylist) {
        final Gallery keyGallery = (Gallery) findViewById(R.id.details_key_gallery);
        final ViewPager keyPager = (ViewPager) findViewById(R.id.details_key_pager);
        // // keyGallery.setAdapter(new DetailsKeyTabAdapter(this, countStrArr(
        // // arraylist, media.banben.contains("更新"))));
        // // AllPagesAdapter allPagesAdapter = new
        // AllPagesAdapter(addViewToPager(
        // // arraylist, media.banben.contains("更新")));
        // keyPager.setAdapter(allPagesAdapter);
        keyGallery.setOnItemSelectedListener(new KeyGalleryOnItemSelListener(
                keyPager));
        keyPager.setOnPageChangeListener(new KeyPagerOnChangeListener(
                keyGallery));
        keyPager.setOnKeyListener(this);
    }

    protected void create3DPost(final ImageView imageView, String s) {
        BitmapWorkerTask bitmapworkertask = new BitmapWorkerTask(this, null,
                false, false);
        bitmapworkertask.setCallback(new VideoBtPostCallBack(imageView));
        String as[] = new String[1];
        as[0] = s;
        bitmapworkertask.execute(as);
    }

    protected void createHotLayout(ArrayList<VideoInfo> arraylist) {
        hotGrid.setSelector(new ColorDrawable(0));
        adapter = new HotVideoAdapter(this, arraylist);
        hotGrid.setAdapter(adapter);
    }

    protected void createSourceLayout(ArrayList<VideoSource> arraylist, int i) {
        if (arraylist != null && !arraylist.isEmpty()) {
            for (int j = 0; j < arraylist.size(); j++) {
                RadioButton radiobutton = (RadioButton) inflater.inflate(
                        R.layout.vediodetail_rb, null);
                radiobutton
                        .setCompoundDrawablesWithIntrinsicBounds(
                                0,
                                0,
                                StringUtil
                                        .sourceStringToResourceID(((VideoSource) arraylist
                                                .get(j)).sourceName), 0);
                radiobutton.setFocusable(true);
                sourcesLin.addView(radiobutton, j, new ViewGroup.LayoutParams(
                        LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            }
            if (playRecode != null)
                i = playRecode.sourceIndex;
            if (i < 0)
                i = 0;
            sourcesLin.check(sourcesLin.getChildAt(i).getId());
        } else {
            play.setEnabled(false);
        }
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.video_details);
        Process.setThreadPriority(Process.myTid(), -3);
        inflater = (LayoutInflater) getSystemService("layout_inflater");
        dbHelper = VodDataHelper.getInstance(this);
        hostUrl = (new StringBuilder(String.valueOf(MyApp.baseServer))).append(
                "v_info/").toString();
        initDialog();
        initView();
        initData();
        initListener();
        progressShow();
    }

    protected void onDestroy() {
        super.onDestroy();
    }

    public boolean onKey(View view, int i, KeyEvent keyevent) {
        boolean flag;
        if (keyevent.getAction() == 0) {
            if (i == KeyEvent.KEYCODE_BACK) {
                finish();
                overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
            }
            flag = super.onKeyDown(i, keyevent);
        } else {
            flag = false;
        }
        return flag;
    }


    private static final int MSG_ERROR = 12;
    private static final int MSG_INIT = 11;
    private static final String TAG = "VideoDetailsActivity";
    private TextView actors;
    HotVideoAdapter adapter;
    private TextView area;
    private Button choose;
    private Button colection;
    private VodDataHelper dbHelper;
    private AlertDialog dialog;
    private TextView editors;
    private Handler handler;
    String hostUrl;
    private GridView hotGrid;
    private VideoInfo vodMovieItem;
    private int index;
    LayoutInflater inflater;
    private TextView introduce;
    private LinearLayout keyLayoutA;
    private LinearLayout keyLayoutT;
    public static VideoDetailInfo media;
    private Button play;
    private VodRecode playRecode;
    private TextView point;
    private ImageView poster;
    private Button replay;
    private int selectSource;
    private ImageView shadow;
    private ImageView sharpness;
    private RadioGroup sourcesLin;
    private TextView type;
    private TextView videoName;
    private TextView year;

    private static class GetVideoDetailInfoResultHandler extends Handler {
        private final WeakReference<VideoDetailsActivity> mActivity;

        public GetVideoDetailInfoResultHandler(VideoDetailsActivity activity) {
            mActivity = new WeakReference<VideoDetailsActivity>(activity);
        }

        public void handleMessage(Message message) {
            switch (message.what) {
                case MSG_INIT:
                    media = (VideoDetailInfo) message.obj;
                    if (Constants.DEBUG)
                        Log.d(TAG, media.toString());
                    mActivity.get().showDetailInfo();
                    break;
                case MSG_ERROR:
                    if (mActivity.get().getWindow() != null
                            && !mActivity.get().dialog.isShowing())
                        mActivity.get().dialog.show();
                    break;
                default:
                    break;
            }
        }
    }

    private class GridviewOnItemClickListener implements
            AdapterView.OnItemClickListener {

        public void onItemClick(AdapterView<?> adapterview, View view, int i,
                                long l) {
            index = i;
            if (Constants.DEBUG)
                Log.i(TAG, (new StringBuilder("Click=")).append(i).toString());
            listAdapter.setDataChanged(pagingSets(sets, index));
        }

        private final DetailsKeyListAdapter listAdapter;
        private final ArrayList<VideoSet> sets;

        GridviewOnItemClickListener(DetailsKeyListAdapter listAdapter,
                                    ArrayList<VideoSet> sets) {
            super();
            this.listAdapter = listAdapter;
            this.sets = sets;
        }
    }

    private class ListViewOnItemClickListener implements
            AdapterView.OnItemClickListener {

        public void onItemClick(AdapterView<?> adapterview, View view, int i,
                                long l) {
            // playRecode = new VodRecode();
            // playRecode.id = media.id;
            // playRecode.name = media.name;
            // playRecode.banben = media.banben;
            // playRecode.imgUrl = media.img;
            // playRecode.type = 3;
            // playRecode.sourceIndex = selectSource;
            // playRecode.setIndex = i + 10 * index;
            // playRecode.positon = 0;
            if (media != null) {
                Intent intent = new Intent(VideoDetailsActivity.this,
                        VodPlayer.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("playinfo", playRecode);
                bundle.putSerializable("media", media);
                intent.putExtra("VODEXTRA", bundle);
                startActivity(intent);
                finish();
            }
        }
    }

    private class BtnOnClickListener implements View.OnClickListener {

        public void onClick(View view) {
            // playRecode = new VodRecode();
            // playRecode.id = media.id;
            // playRecode.title = media.title;
            // playRecode.banben = media.banben;
            // playRecode.imgUrl = media.img;
            // playRecode.type = 3;
            // playRecode.sourceIndex = selectSource;
            // playRecode.setIndex = ((Integer) btn.getTag()).intValue();
            // playRecode.positon = 0;
            if (media != null) {
                Intent intent = new Intent(VideoDetailsActivity.this,
                        VodPlayer.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("playinfo", playRecode);
                // bundle.putSerializable("media", media);
                intent.putExtra("VODEXTRA", bundle);
                startActivity(intent);
                finish();
            }
        }

        private final Button btn;

        BtnOnClickListener(Button button) {
            super();
            btn = button;
        }
    }

    private class ParseDetailInfoThread extends Thread {

        public void run() {
            if (Constants.DEBUG)
                Log.d(TAG, "Thread [" + this.getId() + "]");
            Process.setThreadPriority(-2);
            VideoDetailInfo videodetailinfo = VodMovieDetailsBiz
                    .parseVodListData(vodMovieItem.id);
            if (videodetailinfo != null)
                handler.sendMessage(handler.obtainMessage(MSG_INIT,
                        videodetailinfo));
            else
                handler.sendEmptyMessage(MSG_ERROR);
        }
    }

    private class RetryDialog implements DialogInterface.OnClickListener {

        public void onClick(DialogInterface dialoginterface, int i) {
            switch (i) {
                case DialogInterface.BUTTON_NEGATIVE:
                    break;
                case DialogInterface.BUTTON_POSITIVE:
                    finish();
                    break;
            }
        }
    }

    private class ReplayOnClickListener implements View.OnClickListener {

        public void onClick(View view) {
            if (media != null) {
                Intent intent = new Intent(VideoDetailsActivity.this,
                        VodPlayer.class);
                Bundle bundle = new Bundle();
                // bundle.putSerializable("media", media);
                playRecode.setIndex = 0;
                playRecode.positon = 0;
                bundle.putSerializable("playinfo", playRecode);
                intent.putExtra("VODEXTRA", bundle);
                startActivity(intent);
            }
        }
    }

    private class PlayOnClickListener implements View.OnClickListener {

        public void onClick(View view) {
            if (media != null) {
                if (Constants.DEBUG)
                    Log.d(TAG, "media [" + media.toString() + "]");
                Intent intent = new Intent(VideoDetailsActivity.this,
                        VodPlayer.class);
                Bundle bundle = new Bundle();
                // bundle.putSerializable("media", media);
                if (playRecode != null) {
                    if (playRecode.sourceIndex != selectSource) {
                        playRecode.sourceIndex = selectSource;
                        playRecode.setIndex = 0;
                        playRecode.positon = 0;
                    }
                } else {
                    playRecode = new VodRecode();
                    playRecode.sourceIndex = selectSource;
                    playRecode.id = media.id;
                    playRecode.title = media.title;
                    playRecode.banben = media.banben;
                    playRecode.imgUrl = media.img;
                    playRecode.type = 3;
                    playRecode.setIndex = 0;
                    playRecode.positon = 0;
                }
                bundle.putSerializable("playinfo", playRecode);
                intent.putExtra("VODEXTRA", bundle);
                startActivity(intent);
            }
        }
    }

    private class SourcesLinOnCheckListener implements
            RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup radiogroup, int i) {
            selectSource = sourcesLin.indexOfChild(sourcesLin.findViewById(i));
            MyApp.playSound("comfire");
            if (keyLayoutT.getVisibility() == View.VISIBLE
                    || keyLayoutA.getVisibility() == View.VISIBLE) {
                // ArrayList<VideoPlayUrl> arraylist = media.playlist;
                // CreateTvLayout(arraylist);
                // hotGrid.setVisibility(View.GONE);
                // keyLayoutT.setVisibility(View.VISIBLE);
                // if (!media.contains("综艺")) {
                // CreateTvLayout(arraylist);
                // hotGrid.setVisibility(View.GONE);
                // keyLayoutT.setVisibility(View.VISIBLE);
                // } else {
                // CreateArtLayout(arraylist);
                // hotGrid.setVisibility(View.GONE);
                // keyLayoutA.setVisibility(View.VISIBLE);
                // }
            }
            if (playRecode != null && selectSource == playRecode.sourceIndex) {
                play.setText("续播");
                play.setBackgroundResource(R.drawable.details_replay_sel);
                replay.setVisibility(View.VISIBLE);
            } else {
                play.setText("播放");
                play.setBackgroundResource(R.drawable.video_details_play_selector);
                replay.setVisibility(View.GONE);
            }
        }
    }

    private class ColectionOnClickListener implements View.OnClickListener {

        public void onClick(View view) {
            // if (media.banben.contains("更新")) {
            // if (dbHelper.queryRecode(media.id, 2) == null) {
            // VodRecode vodrecode1 = new VodRecode();
            // // vodrecode1.id = media.id;
            // // vodrecode1.title = media.name;
            // // vodrecode1.banben = media.banben;
            // vodrecode1.imgUrl = media.img;
            // vodrecode1.type = 2;
            // dbHelper.insertRecode(vodrecode1);
            // colection.setText("取消");
            // colection
            // .setBackgroundResource(R.drawable.video_details_zhuiju_selector);
            // Toast.makeText(VideoDetailsActivity.this, "新增追剧成功！",
            // Toast.LENGTH_SHORT).show();
            // } else {
            // dbHelper.deleteRecodes(media.id, 2);
            // colection.setText("追剧");
            // colection
            // .setBackgroundResource(R.drawable.video_details_zhuiju_n_selector);
            // Toast.makeText(VideoDetailsActivity.this, "取消追剧成功！",
            // Toast.LENGTH_SHORT).show();
            // }
            // } else if (dbHelper.queryRecode(media.id, 1) == null) {
            // VodRecode vodrecode = new VodRecode();
            // vodrecode.id = media.id;
            // vodrecode.title = media.title;
            // vodrecode.banben = media.banben;
            // vodrecode.imgUrl = media.img;
            // vodrecode.type = 1;
            // dbHelper.insertRecode(vodrecode);
            // colection.setText("取消");
            // colection
            // .setBackgroundResource(R.drawable.video_details_zhuiju_selector);
            // Toast.makeText(VideoDetailsActivity.this, "新增收藏成功！",
            // Toast.LENGTH_SHORT).show();
            // } else {
            // dbHelper.deleteRecodes(media.id, 1);
            // colection.setText("收藏");
            // colection
            // .setBackgroundResource(R.drawable.video_details_zhuiju_n_selector);
            // Toast.makeText(VideoDetailsActivity.this, "取消收藏成功！",
            // Toast.LENGTH_SHORT).show();
            // }
        }
    }

    private class ChooseOnClickListener implements View.OnClickListener {

        public void onClick(View view) {
            // String s = media.type;
            // if (keyLayoutT.getVisibility() == 0
            // || keyLayoutA.getVisibility() == 0) {
            // keyLayoutA.setVisibility(View.GONE);
            // keyLayoutT.setVisibility(View.GONE);
            // hotGrid.setVisibility(View.VISIBLE);
            // } else {
            // ArrayList<VideoSet> arraylist = ((VideoSource) media.playlist
            // .get(selectSource)).sets;
            // if (arraylist != null && !arraylist.isEmpty()) {
            // if (s.contains("综艺")) {
            // CreateArtLayout(arraylist);
            // hotGrid.setVisibility(View.GONE);
            // keyLayoutA.setVisibility(View.VISIBLE);
            // } else {
            // CreateTvLayout(arraylist);
            // hotGrid.setVisibility(View.GONE);
            // keyLayoutT.setVisibility(View.VISIBLE);
            // }
            // } else {
            // Toast.makeText(VideoDetailsActivity.this,
            // "Sorry,未找到可播放的剧集……", Toast.LENGTH_LONG).show();
            // }
            // }
        }
    }

    private class HotGridOnItemClickListener implements
            AdapterView.OnItemClickListener {

        public void onItemClick(AdapterView<?> adapterview, View view, int i,
                                long l) {
            if (Constants.DEBUG)
                Log.i(TAG, (new StringBuilder()).append(adapter).toString());
            VideoInfo videoinfo = (VideoInfo) adapter.getItem(i);
            Intent intent = new Intent(VideoDetailsActivity.this,
                    VideoDetailsActivity.class);
            intent.putExtra(Constants.INTENT_KEY_VIDEODEAIL, videoinfo);
            startActivity(intent);
            overridePendingTransition(R.anim.slid2, R.anim.slid1);
            finish();
        }
    }

    private class KeyGalleryOnItemSelListener implements
            AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> adapterview, View view,
                                   int i, long l) {
            keyPager.setCurrentItem(i);
            MyApp.playSound("top_page_move");
        }

        public void onNothingSelected(AdapterView<?> adapterview) {
        }

        private final ViewPager keyPager;

        KeyGalleryOnItemSelListener(ViewPager viewpager) {
            super();
            this.keyPager = viewpager;
        }
    }

    private class KeyPagerOnChangeListener implements
            ViewPager.OnPageChangeListener {

        public void onPageScrollStateChanged(int i) {
        }

        public void onPageScrolled(int i, float f, int j) {
        }

        public void onPageSelected(int i) {
            keyGallery.setSelection(i);
        }

        private final Gallery keyGallery;

        KeyPagerOnChangeListener(Gallery gallery) {
            super();
            keyGallery = gallery;
        }
    }

    private class VideoBtPostCallBack implements BitmapWorkerTask.PostCallBack {

        public void post(Bitmap bitmap) {
            if (bitmap != null)
                imageView.setImageBitmap(Reflect3DImage.skewImage(bitmap, 260,
                        366, 50));
            android.view.animation.Animation animation = AnimationUtils
                    .loadAnimation(VideoDetailsActivity.this,
                            R.anim.triangleeffect);
            shadow.startAnimation(animation);
            shadow.setVisibility(View.VISIBLE);
            colection.setVisibility(View.VISIBLE);
        }

        private final ImageView imageView;

        VideoBtPostCallBack(ImageView imageView) {
            super();
            this.imageView = imageView;

        }
    }
}
