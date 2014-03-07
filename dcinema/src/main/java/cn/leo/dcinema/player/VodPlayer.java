package cn.leo.dcinema.player;

import android.app.ActionBar.LayoutParams;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.*;
import android.util.Log;
import android.view.*;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.*;
import cn.leo.dcinema.Constants;
import cn.leo.dcinema.R;
import cn.leo.dcinema.activity.BaseActivity;
import cn.leo.dcinema.adapters.PlayerChooseArtAdapter;
import cn.leo.dcinema.adapters.PlayerChooseTvAdapter;
import cn.leo.dcinema.app.MyApp;
import cn.leo.dcinema.biz.VodMovieAddrBiz;
import cn.leo.dcinema.biz.VodMovieDetailsBiz;
import cn.leo.dcinema.db.VodDataHelper;
import cn.leo.dcinema.model.*;
import cn.leo.dcinema.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class VodPlayer extends BaseActivity implements
        MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnPreparedListener, MediaPlayer.OnInfoListener,
        MediaPlayer.OnSeekCompleteListener {

    private static final int ADD_VOICE = 1;
    private static final int INDUCE_VOICE = 0;

    private class ChooseSetDiaLog extends Dialog implements
            View.OnFocusChangeListener {
        private void createGroupTitle() {
            if (groupTitles != null && !groupTitles.isEmpty()) {
                for (int i = 0; i < groupTitles.size(); i++) {
                    RadioButton radiobutton = new RadioButton(context);
                    radiobutton
                            .setBackgroundResource(R.drawable.video_details_btn10_selector);
                    radiobutton.setFocusable(true);
                    radiobutton.setGravity(17);
                    radiobutton.setRight(10);
                    radiobutton.setButtonDrawable(android.R.color.transparent);
                    radiobutton.setText((CharSequence) groupTitles.get(i));
                    radiobutton.setTextSize(24F);
                    groupRg.addView(radiobutton);
                }
                currentGroupIndex = currentSet / pageContain;
                groupRg.check(groupRg.getChildAt(currentGroupIndex).getId());
            }
        }

        private void initSetChooseAndListener() {

            if (tvSetGrid != null) {
                tvSetGrid.setSelector(new ColorDrawable(0));
                tvAdapter = new PlayerChooseTvAdapter(context,
                        (List<VideoSet>) pagesSets.get(currentGroupIndex),
                        currentGroupIndex);
                tvSetGrid.setAdapter(tvAdapter);

                tvSetGrid
                        .setOnItemClickListener(new AdapterView.OnItemClickListener() {

                            public void onItemClick(AdapterView<?> adapterview,
                                                    View view, int i, long l) {
                                playRecode.setIndex = i + currentGroupIndex
                                        * pageContain;
                                mHandler.sendMessage(mHandler.obtainMessage(
                                        MSG_SELECTSET, playRecode.sourceIndex,
                                        playRecode.setIndex));
                                playRecode.positon = 0;
                                txtFilmSet
                                        .setText(((VideoSet) ((VideoSource) media.playlist
                                                .get(playRecode.sourceIndex)).sets
                                                .get(playRecode.setIndex)).setName);
                                dismiss();
                            }
                        });

                tvSetGrid
                        .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                            public void onItemSelected(
                                    AdapterView<?> adapterview, View view,
                                    int i, long l) {
                                mHandler.removeMessages(MSG_DISMISS_CHOOSE);
                                mHandler.sendEmptyMessageDelayed(
                                        MSG_DISMISS_CHOOSE, 30000L);
                            }

                            public void onNothingSelected(
                                    AdapterView<?> adapterview) {
                            }
                        });
                tvSetGrid.setOnFocusChangeListener(this);
            } else {
                artAdapter = new PlayerChooseArtAdapter(context,
                        pagesSets.get(currentGroupIndex), currentGroupIndex);
                artSetList.setAdapter(artAdapter);

                artSetList
                        .setOnItemClickListener(new AdapterView.OnItemClickListener() {

                            public void onItemClick(AdapterView<?> adapterview,
                                                    View view, int i, long l) {
                                playRecode.setIndex = i + currentGroupIndex
                                        * pageContain;
                                mHandler.sendMessage(mHandler.obtainMessage(
                                        MSG_SELECTSET, playRecode.sourceIndex,
                                        playRecode.setIndex));
                                playRecode.positon = 0;
                                txtFilmSet
                                        .setText(((VideoSet) ((VideoSource) media.playlist
                                                .get(playRecode.sourceIndex)).sets
                                                .get(playRecode.setIndex)).setName);
                                dismiss();
                            }
                        });

                artSetList
                        .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                            public void onItemSelected(
                                    AdapterView<?> adapterview, View view,
                                    int i, long l) {
                                mHandler.removeMessages(MSG_DISMISS_CHOOSE);
                                mHandler.sendEmptyMessageDelayed(
                                        MSG_DISMISS_CHOOSE, 30000L);
                            }

                            public void onNothingSelected(
                                    AdapterView<?> adapterview) {
                            }
                        });
                artSetList.setOnFocusChangeListener(this);
            }
            groupRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                public void onCheckedChanged(RadioGroup radiogroup, int i) {
                    currentGroupIndex = groupRg.indexOfChild(groupRg
                            .findViewById(i));
                    if (tvSetGrid != null)
                        tvAdapter.setDataChanged((List<VideoSet>) pagesSets
                                .get(currentGroupIndex), currentGroupIndex);
                    else
                        artAdapter.setDataChanged((List<VideoSet>) pagesSets
                                .get(currentGroupIndex));
                }
            });
            groupRg.setOnFocusChangeListener(this);
        }

        private void spliteSets(List<VideoSet> list) {
            int i = list.size() % pageContain;
            int j = list.size() / pageContain;
            int k;

            if (i == 0)
                k = j;
            else
                k = j + 1;
            pagesSets = new ArrayList<List<VideoSet>>();
            groupTitles = new ArrayList<String>();
            for (int l = 0; l < k; l++) {
                if (l < k - 1 || i == 0) {
                    List<VideoSet> list1 = list.subList(l * pageContain,
                            (l + 1) * pageContain);
                    groupTitles.add((new StringBuilder(String.valueOf(1 + l
                            * pageContain))).append(" - ")
                            .append((l + 1) * pageContain).toString());
                    pagesSets.add(list1);
                } else {
                    List<VideoSet> list2 = list.subList(l * pageContain, i + l
                            * pageContain);
                    groupTitles.add((new StringBuilder(String.valueOf(1 + l
                            * pageContain))).append(" - ")
                            .append(i + l * pageContain).toString());
                    pagesSets.add(list2);
                }
            }
        }

        public void onFocusChange(View view, boolean flag) {
            mHandler.removeMessages(MSG_DISMISS_CHOOSE);
            mHandler.sendEmptyMessageDelayed(MSG_DISMISS_CHOOSE, 30000L);
        }

        private PlayerChooseArtAdapter artAdapter;
        private ListView artSetList;
        private Context context;
        private int currentGroupIndex;
        private int currentSet;
        private RadioGroup groupRg;
        private ArrayList<String> groupTitles;
        @SuppressWarnings("unused")
        private boolean isArt;
        private int pageContain;
        private ArrayList<List<VideoSet>> pagesSets;
        private PlayerChooseTvAdapter tvAdapter;
        private GridView tvSetGrid;

        public ChooseSetDiaLog(Context context, List<VideoSet> list,
                               boolean flag, int i) {
            super(context, R.style.MyDialog);
            isArt = false;
            this.context = context;
            isArt = flag;
            currentSet = i;
            LinearLayout linearlayout = (LinearLayout) LayoutInflater.from(
                    context).inflate(R.layout.player_choose_set, null);
            linearlayout.setMinimumWidth(620);
            linearlayout.setMinimumHeight(420);
            setContentView(linearlayout);
            groupRg = (RadioGroup) linearlayout
                    .findViewById(R.id.player_choose_table);
            if (flag) {
                ((ViewStub) linearlayout.findViewById(R.id.player_choose_art))
                        .inflate();
                artSetList = (ListView) linearlayout
                        .findViewById(R.id.player_choose_art);
                pageContain = 20;
            } else {
                ((ViewStub) linearlayout.findViewById(R.id.player_choose_tv))
                        .inflate();
                tvSetGrid = (GridView) linearlayout
                        .findViewById(R.id.player_choose_tv);
                pageContain = 30;
            }
            tvSetGrid = (GridView) linearlayout
                    .findViewById(R.id.player_choose_tv_gridView);
            artSetList = (ListView) linearlayout
                    .findViewById(R.id.player_choose_art_list);
            spliteSets(list);
            createGroupTitle();
            initSetChooseAndListener();
        }
    }

    public VodPlayer() {
        errorDialog = null;
        hdHeadValue = 3;
        seekPositon = 0;
        runnable = new Runnable() {

            public void run() {
                if (Constants.DEBUG)
                    Log.d(TAG, "runnable get VOD PlayUri");
                VodPlayer vodplayer = VodPlayer.this;
                vodplayer.hdHeadValue = -1 + vodplayer.hdHeadValue;
                // ArrayList<VideoPlayUrl> arraylist = vodBiz.getPlayUris(
                // baseplayurl,
                // ((VideoSet) ((VideoSource) media.playlist
                // .get(playRecode.sourceIndex)).sets
                // .get(playRecode.setIndex)).link,
                // new BasicHeader("Player-HD", (new StringBuilder(String
                // .valueOf(hdHeadValue))).toString()));
                ArrayList<VideoPlayUrl> arraylist = VodMovieAddrBiz
                        .parseVodMovieAddr(((VideoSet) ((VideoSource) media.playlist
                                .get(playRecode.sourceIndex)).sets
                                .get(playRecode.setIndex)).link);
                if (arraylist == null || arraylist.size() == 0) {
                    mHandler.sendEmptyMessage(MSG_ERROR);
                } else {
                    SuitSharp(arraylist);
                    matchSetandPlay(arraylist);
                    ((VideoSet) ((VideoSource) media.playlist
                            .get(playRecode.sourceIndex)).sets
                            .get(playRecode.setIndex)).playUrls = arraylist;
                }
            }
        };
        mHandler = new Handler() {

            public void handleMessage(Message message) {
                super.handleMessage(message);
                switch (message.what) {
                    case MSG_SHOWBUFF:
                        Log.d(TAG, "show..");
                        num_gif.setVisibility(View.VISIBLE);
                        loading_main_bg.setVisibility(View.VISIBLE);
                        break;
                    case MSG_HIDEBUFF:
                        loading_main_bg.startAnimation(AnimationUtils
                                .loadAnimation(getApplicationContext(),
                                        R.anim.fade_out));
                        num_gif.setVisibility(View.GONE);
                        loading_main_bg.setVisibility(View.GONE);
                        break;
                    case MSG_SELECTSET:
                        playRecode.sourceIndex = message.arg1;
                        playRecode.setIndex = message.arg2;
                        playVideo();
                        break;
                    case MSG_SELECTSOURCE:
                        playRecode.sourceIndex = message.arg1;
                        playRecode.positon = mVideoView.getCurrentPosition();
                        if (Constants.DEBUG)
                            Log.i(TAG, (new StringBuilder("MSG_SELECTSOURCE ="))
                                    .append(playRecode.sourceIndex).toString());
                        VideoSource videosource = (VideoSource) media.playlist
                                .get(playRecode.sourceIndex);
                        menuContrl.setVideoSources(playRecode.sourceIndex);
                        ctrbot.setSourceTag(StringUtil
                                .getSourceTag(videosource.sourceName));
                        ArrayList<VideoSet> arraylist = ((VideoSource) media.playlist
                                .get(playRecode.sourceIndex)).sets;
                        if (playRecode.setIndex > -1 + arraylist.size()) {
                            Toast.makeText(
                                    VodPlayer.this,
                                    (new StringBuilder("当前源未更新到"))
                                            .append(playRecode.setIndex)
                                            .append("集，自动选择当前源最后一集！").toString(),
                                    Toast.LENGTH_SHORT).show();
                            playRecode.setIndex = -1 + arraylist.size();
                        }
                        playVideo();
                        showChooseSetDialog(false);
                        break;
                    case MSG_PLAY:
                        playUrl = (String) message.obj;
                        if (Constants.DEBUG)
                            Log.d(TAG,
                                    (new StringBuilder("paly url = ")).append(
                                            message.obj).toString());
                        if (playUrl != null) {
                            if (message.arg1 > 0)
                                playRecode.positon = message.arg1;
                            mVideoView.setVideoURI(Uri.parse(playUrl));
                            mVideoView.start();
                            mHandler.sendEmptyMessage(MSG_SHOWBUFF);
                        }
                        break;
                    case MSG_ERROR:
                        if (hdHeadValue >= 1)
                            MyApp.pool.execute(runnable);
                        else if (isRunning()) {
                            // if (exitDialog == null || !exitDialog.isShowing())
                            // loading_again.setVisibility(View.VISIBLE);
                            // errorDialog.show();
                            gif.stop();
                            animation.cancel();
                            animation.reset();
                            loading_again.setVisibility(View.VISIBLE);
                            loading_again.requestFocus();
                            loading_error_tips.setVisibility(View.VISIBLE);
                            mVideoView.stopPlayback();
                        }
                        break;
                    case MSG_HIDE_SCAL:
                        if (scalText.getParent() != null)
                            wm.removeView(scalText);
                        break;
                    case MSG_RETRY:
                        return;
                    case MSG_DISMISS_CHOOSE:
                        if (chooseDialog != null && chooseDialog.isShowing())
                            chooseDialog.dismiss();
                        break;

                    case MSG_DISMISS_EXIT:
                        if (exitDialog != null && exitDialog.isShowing())
                            exitDialog.dismiss();
                        return;
                    case MSG_DISMISS_POP:
                        hintPop.dismiss();
                        break;
                }
            }

        };
        chooseDialog = null;
        exitDialog = null;
    }

    private void SuitSharp(ArrayList<VideoPlayUrl> arraylist) {
        ArrayList<SharpnessEnum> arraylist1 = new ArrayList<SharpnessEnum>();
        for (int i = 0; i < arraylist.size(); i++) {
            arraylist1.add(((VideoPlayUrl) arraylist.get(i)).sharp);
        }
        sharpness = SharpnessEnum.getSuitSharp(sharpness, arraylist1);
        // ctrbot.setSharpness(arraylist, sharpness);
    }

    private Dialog createExitDialog(Context context, boolean flag, boolean flag1) {
        final Dialog dialog = new Dialog(this, R.style.MyDialog);
        View view = LayoutInflater.from(context).inflate(R.layout.exit_windows,
                null);
        View.OnClickListener onclicklistener = new View.OnClickListener() {

            public void onClick(View view1) {
                switch (view1.getId()) {
                    case R.id.player_exist_exit_bt:
                        finish();
                        break;
                    case R.id.player_exist_play_bt:
                        mVideoView.start();
                        break;
                    case R.id.player_exist_next_bt:
                        playRecode.setIndex = 1 + playRecode.setIndex;
                        mHandler.sendMessage(mHandler.obtainMessage(MSG_SELECTSET,
                                playRecode.sourceIndex, playRecode.setIndex));
                        playRecode.positon = 0;
                        txtFilmSet
                                .setText(((VideoSet) ((VideoSource) media.playlist
                                        .get(playRecode.sourceIndex)).sets
                                        .get(playRecode.setIndex)).setName);

                        break;
                    case R.id.player_exist_fav_bt:
                        if (mVideoView.isPlaying()) {
                            VodRecode vodrecode = new VodRecode();
                            vodrecode.title = media.title;
                            vodrecode.id = media.id;
                            vodrecode.banben = media.banben;
                            vodrecode.imgUrl = media.img;
                            vodrecode.type = 1;
                            vodrecode.sourceIndex = playRecode.sourceIndex;
                            vodrecode.setIndex = playRecode.setIndex;
                            vodrecode.positon = mVideoView.getCurrentPosition();
                            dbHelper.insertRecode(vodrecode);
                        }
                        dialog.dismiss();
                        return;
                }
            }
        };
        ((Button) view.findViewById(R.id.player_exist_exit_bt))
                .setOnClickListener(onclicklistener);
        ((Button) view.findViewById(R.id.player_exist_play_bt))
                .setOnClickListener(onclicklistener);
        Button button = (Button) view.findViewById(R.id.player_exist_next_bt);
        button.setOnClickListener(onclicklistener);
        if (!flag)
            button.setVisibility(View.GONE);
        ((Button) view.findViewById(R.id.player_exist_fav_bt))
                .setOnClickListener(onclicklistener);
        dialog.setContentView(view);
        return dialog;
    }

    private void ctrBotSetVideoName(VideoSource videosource) {
        ctrbot.setVideoName(media.title);

    }

    private void initData() {
        if (media != null) {
            if (playRecode == null) {
                playRecode = new VodRecode();
                playRecode.id = media.id;
                playRecode.title = media.title;
                playRecode.banben = media.banben;
                playRecode.imgUrl = media.img;
                playRecode.type = 3;
                playRecode.sourceIndex = 0;
                playRecode.setIndex = 0;
                playRecode.positon = 0;
            }
            txtFilmName.setText(media.title);
            VideoSource videosource = (VideoSource) media.playlist
                    .get(playRecode.sourceIndex);
            txtFilmSet.setText(((VideoSet) videosource.sets
                    .get(playRecode.setIndex)).setName);
            ctrbot.setSourceTag(StringUtil.getSourceTag(videosource.sourceName));
            ctrbot.setScaleTag(scaleMod);
            ctrBotSetVideoName(videosource);
            // ctrbot.setPost(media.img);
        }
    }

    private void initDialog() {
        errorDialog = new Dialog(this, R.style.MyDialog);
        View view = LayoutInflater.from(this).inflate(R.layout.loading, null);
        loading_again = (ImageButton) findViewById(R.id.loadding_again);
        errorDialog.setContentView(view);
        // Button button = (Button)
        // view.findViewById(R.id.vod_error_dialog_btn);
        loading_again
                .setOnClickListener(new android.view.View.OnClickListener() {

                    public void onClick(View view1) {
                        errorDialog.dismiss();
                        loading_again.setVisibility(View.INVISIBLE);
                        if (playUrl != null)
                            mHandler.sendMessage(mHandler.obtainMessage(
                                    MSG_PLAY, playUrl));
                        else
                            MyApp.pool.execute(runnable);
                    }
                });
        loading_again.setOnKeyListener(new android.view.View.OnKeyListener() {

            public boolean onKey(View view1, int i, KeyEvent keyevent) {
                boolean flag;
                if (i == KeyEvent.KEYCODE_BACK
                        && keyevent.getAction() == KeyEvent.ACTION_DOWN) {
                    finish();
                    flag = true;
                } else {
                    flag = false;
                }
                return flag;
            }
        });
        errorDialog.setContentView(view);
    }

    private void initIntent() {
        if (Constants.DEBUG)
            Log.d(TAG, "initIntent");
        Bundle bundle = getIntent().getBundleExtra("VODEXTRA");
        playRecode = (VodRecode) bundle.get("playinfo");
        media = (VideoDetailInfo) bundle.get("media");
        if (media == null) {
            (new Thread(new Runnable() {

                public void run() {
                    media = VodMovieDetailsBiz.parseVodListData(playRecode.id);

                    if (media != null)
                        mHandler.post(new Runnable() {

                            public void run() {
                                initData();
                                playVideo();
                            }
                        });
                    else
                        mHandler.sendEmptyMessage(MSG_ERROR);
                }

            })).start();
        } else {
            if (Constants.DEBUG)
                Log.d(TAG, media.toString());
            initData();
            playVideo();
        }
    }

    private void initView() {
        loading_error_tips = (TextView) findViewById(R.id.loading_error_tips);
        mVideoView = (VideoView) findViewById(R.id.vod_player_videoView);
        mVideoView.setOnPreparedListener(this);
        mVideoView.setOnCompletionListener(this);
        mVideoView.setOnErrorListener(this);
        txtFilmName = (TextView) findViewById(R.id.vod_player_filmname_txt);
        txtFilmSet = (TextView) findViewById(R.id.vod_player_filmset_txt);
        loadingAgain = (ImageView) findViewById(R.id.loadding_again);
        loading_main_bg = findViewById(R.id.loading_main_bg);
        num_gif = (ImageView) findViewById(R.id.loading_num);
        cir_gif = (ImageView) findViewById(R.id.loading_cir);
        gif = (AnimationDrawable) num_gif.getDrawable();
        gif.start();
        if (Constants.DEBUG)
            Log.d(TAG, "init");
        animation = (AnimationSet) AnimationUtils.loadAnimation(this,
                R.anim.rotate);
        cir_gif.setAnimation(animation);
        animation.start();
        animation.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // TODO Auto-generated method stub
                gif.stop();
                loading_again.setVisibility(View.VISIBLE);
                loading_again.requestFocus();
            }
        });
        loading_again.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                // TODO Auto-generated method stub
                loading_error_tips.setVisibility(View.GONE);
                mVideoView.stopPlayback();
                loadingagain();
            }
        });
        // topFadeIn = AnimationUtils.loadAnimation(this,
        // R.anim.vod_wait_top_fade_in);
        // topFadeOut = AnimationUtils.loadAnimation(this,
        // R.anim.vod_wait_top_fade_out);
        // botFadeIn = AnimationUtils.loadAnimation(this,
        // R.anim.vod_wait_bot_fade_in);
        // botFadeOut = AnimationUtils.loadAnimation(this,
        // R.anim.vod_wait_bot_fade_out);
        loading_main_bg.startAnimation(AnimationUtils.loadAnimation(this,
                R.anim.fade_in));
        // viewTop.startAnimation(AnimationUtils.loadAnimation(this,
        // R.anim.vod_wait_top_fade_in));
        // viewBot.startAnimation(AnimationUtils.loadAnimation(this,
        // R.anim.vod_wait_bot_fade_in));
        scalText = new TextView(this);
        scalText.setTextColor(-1);
        scalText.setTextSize(30F);
        scalText.setGravity(17);
    }

    private void loadingagain() {
        loading_again.setVisibility(View.INVISIBLE);
        loading_again.requestFocus();
        gif.start();
        cir_gif.startAnimation(animation);
        if (playUrl != null)
            mHandler.sendMessage(mHandler.obtainMessage(MSG_PLAY, playUrl));
        else
            MyApp.pool.execute(runnable);
    }

    private void matchSetandPlay(ArrayList<VideoPlayUrl> arraylist) {
        String s = null;
        for (int i = 0; i < arraylist.size(); i++) {
            if (!((VideoPlayUrl) arraylist.get(i)).sharp.equals(sharpness))
                break;
            s = ((VideoPlayUrl) arraylist.get(i)).playurl;
        }
        if (s == null) {
            VideoPlayUrl videoplayurl = (VideoPlayUrl) arraylist.get(-1
                    + arraylist.size());
            System.out.println(videoplayurl.toString());
            s = videoplayurl.playurl;
            sharpness = videoplayurl.sharp;
        }
        if (Constants.DEBUG)
            Log.d(TAG, "matchSetandPlay [" + s + "]");
        mHandler.sendMessage(mHandler.obtainMessage(MSG_PLAY, s));
    }

    private void playVideo() {
        if (Constants.DEBUG)
            Log.d(TAG, "playVideo");
        ctrBotSetVideoName((VideoSource) media.playlist
                .get(playRecode.sourceIndex));
        hdHeadValue = 3;
        ArrayList<VideoPlayUrl> arraylist = ((VideoSet) ((VideoSource) media.playlist
                .get(playRecode.sourceIndex)).sets.get(playRecode.setIndex)).playUrls;
        if (arraylist != null && arraylist.size() > 0) {
            SuitSharp(arraylist);
            matchSetandPlay(arraylist);
            for (int i = 0; i < arraylist.size(); i++) {
                if (Constants.DEBUG)
                    Log.d(TAG, arraylist.get(i).toString());
            }
        } else {
            MyApp.pool.execute(runnable);
        }

    }

    public String changScale(int i) {
        Object obj = null;
        if (mVideoView.isPlaying()) {
            if (i == 1) {
                scaleMod = 1 + scaleMod;
                if (scaleMod > 2) {
                    scaleMod = 0;
                }
            } else if (i == 2) {
                if (scaleMod > 0)
                    scaleMod = -1 + scaleMod;
                else
                    scaleMod = 2;
            }
            switch (scaleMod) {
                case 0:
                    obj = "原始比例";
                    break;
                case 1:
                    obj = " 4 : 3 ";
                    break;
                case 2:
                    obj = " 16 : 9 ";
                    break;

                default:
                    return null;
            }
        }
        scalText.setText(((CharSequence) (obj)));
        mVideoView.selectScales(scaleMod);
        if (isRunning()) {
            if (scalText.getParent() == null) {
                wm.addView(scalText, p);
            } else {
                p.height = 1 + scalText.getHeight();
                wm.updateViewLayout(scalText, p);
            }
            mHandler.removeMessages(MSG_HIDE_SCAL);
            mHandler.sendEmptyMessageDelayed(MSG_HIDE_SCAL, 3000L);
        }
        return ((String) (obj));
    }

    public void initControl() {
        ctrbot = new VodCtrBot(this, mVideoView, mHandler);
    }

    public void onBackPressed() {
        boolean flag;
        if (-1
                + ((VideoSource) media.playlist.get(playRecode.sourceIndex)).sets
                .size() > playRecode.setIndex)
            flag = true;
        else
            flag = false;
        exitDialog = createExitDialog(this, flag, false);
        exitDialog.show();
        mHandler.sendEmptyMessageDelayed(MSG_DISMISS_EXIT, 30000L);
    }

    public void onCompletion(MediaPlayer mediaplayer) {
        if (((VideoSource) media.playlist.get(playRecode.sourceIndex)).sets
                .size() <= 1 + playRecode.setIndex) {
            boolean flag;
            Dialog dialog;
            if (-1
                    + ((VideoSource) media.playlist.get(playRecode.sourceIndex)).sets
                    .size() > playRecode.setIndex)
                flag = true;
            else
                flag = false;
            dialog = createExitDialog(this, flag, false);
            if (!isFinishing())
                dialog.show();
        } else {
            playRecode.setIndex = 1 + playRecode.setIndex;
            mHandler.sendMessage(mHandler.obtainMessage(MSG_SELECTSET,
                    playRecode.sourceIndex, playRecode.setIndex));
            playRecode.positon = 0;
            txtFilmSet
                    .setText(((VideoSet) ((VideoSource) media.playlist
                            .get(playRecode.sourceIndex)).sets
                            .get(playRecode.setIndex)).setName);

        }
        mVideoView.stopPlayback();
        playRecode.positon = 0;
        dbHelper.insertRecode(playRecode);
        return;
    }

    @SuppressWarnings("deprecation")
    private void initOperatHintPop() {
        hintPop = new PopupWindow();
        hintPoptext = new TextView(this);
        hintPoptext.setTextColor(getResources().getColor(R.color.white));
        hintPoptext.setTextSize(48);
        hintPoptext.setGravity(Gravity.CENTER_HORIZONTAL);
        hintPop.setWindowLayoutMode(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        hintPop.setBackgroundDrawable(new BitmapDrawable());
        WindowManager.LayoutParams layoutparams = new android.view.WindowManager.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        hintPoptext.setLayoutParams(layoutparams);
        hintPop.setContentView(hintPoptext);
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Log.d(TAG, "oncreate ...");
        setContentView(R.layout.loading);
        scaleMod = MyApp.scaleMod;
        sharpness = SharpnessEnum.getSharp(MyApp.sharpness);
        dbHelper = VodDataHelper.getInstance(this);
        p = new android.view.WindowManager.LayoutParams();
        p.gravity = 53;
        p.x = 50;
        p.y = 80;
        p.format = -2;
        p.width = -2;
        p.height = -2;
        p.flags = 56;
        scaleMod = MyApp.scaleMod;
        initDialog();
        initView();
        initControl();
        initIntent();
        initOperatHintPop();
    }

    protected void onDestroy() {
        Log.i(TAG, "onDestroy");
        mHandler.removeMessages(MSG_HIDE_SCAL);
        mHandler.removeMessages(MSG_ERROR);
        mHandler.removeMessages(MSG_PLAY);
        if (scalText.getParent() != null)
            wm.removeView(scalText);
        if (errorDialog.isShowing())
            loading_again.setVisibility(View.INVISIBLE);
        errorDialog.dismiss();
        if (exitDialog != null && exitDialog.isShowing())
            exitDialog.dismiss();
        super.onDestroy();
    }

    public boolean onError(MediaPlayer mediaplayer, int i, int j) {
        if (Constants.DEBUG)
            Log.i(TAG,
                    (new StringBuilder("onError : what = ")).append(i)
                            .append(" , extra = ").append(j).toString());
        try {
            playRecode.positon = mediaplayer.getCurrentPosition();
        } catch (NullPointerException e) {
            Log.e(TAG, e.toString());
        }
        dbHelper.insertRecode(playRecode);
        mHandler.sendEmptyMessage(MSG_ERROR);
        return true;
    }

    public boolean onInfo(MediaPlayer mediaplayer, int i, int j) {
        return true;
    }

    public boolean onKeyDown(int keyCode, KeyEvent keyevent) {
        if (Constants.DEBUG)
            Log.d(TAG, "onkey_down  = " + keyCode);
        boolean flag;
        flag = true;
        int j;
        if (keyCode != KeyEvent.KEYCODE_BACK
                && keyCode != KeyEvent.KEYCODE_VOLUME_UP
                && keyCode != KeyEvent.KEYCODE_VOLUME_DOWN
                && keyCode != KeyEvent.KEYCODE_VOLUME_MUTE
                && keyCode != KeyEvent.KEYCODE_CALL
                && keyCode != KeyEvent.KEYCODE_ENDCALL)
            j = ((flag) ? 1 : 0);
        else
            j = 0;
        if (j == 0)// goto _L2; else goto _L1
        {
            flag = super.onKeyDown(keyCode, keyevent);
            return flag;
        } else {
            if (keyCode != KeyEvent.KEYCODE_HEADSETHOOK
                    && keyCode != KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE) {
                if (keyCode == KeyEvent.KEYCODE_MEDIA_PLAY) {
                    if (!mVideoView.isPlaying())
                        mVideoView.start();

                }
                if (keyCode == KeyEvent.KEYCODE_MEDIA_STOP
                        || keyCode == KeyEvent.KEYCODE_MEDIA_PAUSE) {
                    if (mVideoView.isPlaying())
                        mVideoView.pause();
                }
                if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                    call_Audio(ADD_VOICE);
                }
                if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                    call_Audio(INDUCE_VOICE);
                }
                if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER
                        || keyCode == KeyEvent.KEYCODE_ENTER) {
                    // ctrbot.show();
                    if (mVideoView.isPlaying()) {
                        mHandler.removeMessages(MSG_DISMISS_POP);
                        hintPoptext.setText("暂停");
                        hintPop.showAtLocation(mVideoView, 17, 0, 0);
                        mVideoView.pause();
                    } else {
                        hintPoptext.setText("继续");
                        hintPop.showAtLocation(mVideoView, Gravity.CENTER, 0, 0);
                        mHandler.sendEmptyMessageDelayed(MSG_DISMISS_POP, 2000);
                        mVideoView.start();
                    }
                    // ctrbot.doPauseResume();
                    // ctrbot.playOrPauseRequesFocus();
                }
                if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT
                        || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                    ctrbot.setScaleTag(scaleMod);
                    ctrbot.show();
                    ctrbot.seekBarRequestFocus();
                }
                if (keyCode == KeyEvent.KEYCODE_PROG_YELLOW) {
                    changScale(j);
                }
                if (keyCode == KeyEvent.KEYCODE_MENU) {
                    Log.d(TAG, "menu down...");
                    if (menuContrl == null)
                        menuContrl = new PlayerMenuContrl(this, mHandler, j);
                    if (menuContrl.isShowing()) {
                        menuContrl.dismiss();
                    } else {
                        menuContrl.showAtLocation(mVideoView, 17, 0, 0);
                        menuContrl.setScalor(scaleMod);
                        menuContrl.showVoiceLevel(menuContrl.getVoice());
                        menuContrl.setVideoSources(media.playlist,
                                playRecode.sourceIndex);
                    }
                    return flag;
                }
            }
            // ctrbot.show();
            // ctrbot.doPauseResume();
            // ctrbot.playOrPauseRequesFocus();
        }
        return flag;
    }

    private void call_Audio(int audio) {
        AudioManager audiomanager = (AudioManager) getSystemService("audio");
        switch (audio) {
            case INDUCE_VOICE:
                audiomanager.adjustStreamVolume(3, -1, 1);
                break;
            case ADD_VOICE:
                audiomanager.adjustStreamVolume(3, 1, 1);
                break;
        }
    }

    public void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");
        if (mVideoView.isPlaying()) {
            mVideoView.pause();
            playRecode.positon = mVideoView.getCurrentPosition();
            dbHelper.insertRecode(playRecode);
        }
    }

    public void onPrepared(MediaPlayer mediaplayer) {
        Log.i(TAG, (new StringBuilder("onPrepared>>> sourceindex   =  "))
                .append(playRecode.sourceIndex).append(" ,   setIndex = ")
                .append(playRecode.setIndex).toString());
        mHandler.sendEmptyMessage(MSG_HIDEBUFF);
        // mHandler.removeMessages(MSG_SHOWBUFF);
        mVideoView.selectScales(MyApp.scaleMod % 3);
        int i = mediaplayer.getVideoHeight();
        System.out.println((new StringBuilder("video height:")).append(i)
                .toString());
        // ctrbot.setHdSdTag(i);
        mVideoView.seekTo(playRecode.positon);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.mytoast,
                (ViewGroup) findViewById(R.id.my_toast_root));
        Toast mytoast = new Toast(this);
        mytoast.setView(view);
        mytoast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
        mytoast.setGravity(Gravity.TOP, 0, 30);
        mytoast.setDuration(Toast.LENGTH_LONG);
        mytoast.show();
    }

    public void onResume() {
        mVideoView.start();
        super.onResume();
        Log.i(TAG, "onResume");
    }

    public void onSeekComplete(MediaPlayer mediaplayer) {
        playRecode.positon = 0;
        seekPositon = 0;
    }

    public void onStart() {
        super.onStart();
        Log.i(TAG, "onStart>>>>");
    }

    public int responseSetSize() {
        return ((VideoSource) media.playlist.get(playRecode.sourceIndex)).sets
                .size();
    }

    public String responseType() {
        return media.type;
    }

    public void showChooseSetDialog(boolean flag) {
        boolean flag1 = false;
        if (!flag) {
            ArrayList<VideoSet> arraylist1 = ((VideoSource) media.playlist
                    .get(playRecode.sourceIndex)).sets;
            if (media.type.contains("综艺") || media.type.contains("电影"))
                flag1 = true;
            chooseDialog = new ChooseSetDiaLog(this, arraylist1, true,
                    playRecode.setIndex);
        } else {
            if (chooseDialog == null) {
                ArrayList<VideoSet> arraylist = ((VideoSource) media.playlist
                        .get(playRecode.sourceIndex)).sets;
                if (media.type.contains("综艺") || media.type.contains("电影"))
                    flag1 = true;
                chooseDialog = new ChooseSetDiaLog(this, arraylist, flag1,
                        playRecode.setIndex);
            }
            chooseDialog.show();
            mHandler.sendEmptyMessageDelayed(MSG_DISMISS_CHOOSE, 10000L);
        }
    }

    private static final int MSG_DISMISS_CHOOSE = 16;
    private static final int MSG_DISMISS_EXIT = 17;
    public static final int MSG_ERROR = 13;
    public static final int MSG_HIDEBUFF = 6;
    public static final int MSG_HIDE_SCAL = 14;
    public static final int MSG_PLAY = 12;
    public static final int MSG_RETRY = 15;
    public static final int MSG_SELECTSET = 7;
    public static final int MSG_SELECTSOURCE = 10;
    public static final int MSG_SHOWBUFF = 5;
    public static final int MSG_DISMISS_POP = 8;
    public static final String TAG = "VodPlayer";
    Animation botFadeIn;
    Animation botFadeOut;
    Dialog chooseDialog;
    private VodCtrBot ctrbot;
    private VodDataHelper dbHelper;
    private Dialog errorDialog;
    Dialog exitDialog;
    AnimationDrawable gif;
    Animation animation;
    private int hdHeadValue;
    private ImageView num_gif, cir_gif, loadingAgain;
    private Handler mHandler;
    private VideoView mVideoView;
    private VideoDetailInfo media;
    private PlayerMenuContrl menuContrl;
    android.view.WindowManager.LayoutParams p;
    private VodRecode playRecode;
    private String playUrl;
    private Runnable runnable;
    private TextView scalText;
    private int scaleMod;
    int seekPositon;
    private SharpnessEnum sharpness;
    Animation topFadeIn;
    Animation topFadeOut;
    private TextView txtFilmName;
    private TextView txtFilmSet;
    private TextView loading_error_tips;
    private PopupWindow hintPop;
    private TextView hintPoptext;
    private View loading_main_bg;
    private ImageButton loading_again;
}