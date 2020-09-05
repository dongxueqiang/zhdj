package com.zhdj.zhdj.view.activity;

import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jeremyliao.liveeventbus.LiveEventBus;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.loader.ImageLoader;
import com.zhdj.zhdj.R;
import com.zhdj.zhdj.base.BaseActivity;
import com.zhdj.zhdj.event.LiveEvent;
import com.zhdj.zhdj.global.SpConstant;
import com.zhdj.zhdj.model.LiveMessageModel;
import com.zhdj.zhdj.model.MessageModel;
import com.zhdj.zhdj.model.SkinModel;
import com.zhdj.zhdj.utils.DateUtils;
import com.zhdj.zhdj.utils.Lunar;
import com.zhdj.zhdj.view.adapter.LoopViewAdapter;
import com.zhdj.zhdj.view.service.GetMessageService;
import com.zhdj.zhdj.view.service.GetSkinService;
import com.zhdj.zhdj.view.service.GetTimeService;
import com.zhdj.zhdj.view.weight.FullScreen;
import com.zhdj.zhdj.viewmodel.MainViewModel;
import com.zhdj.zhdj.viewmodel.UploadViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * @ClassName ShowMessageActivity
 * @Author dongxueqiang
 * @Date 2020/7/16 18:48
 * @Title
 */
public class ShowMessageNewActivity extends BaseActivity {
    @BindView(R.id.banner)
    ViewPager banner;
    @BindView(R.id.video_view)
    FullScreen mVideoView;
//    @BindView(R.id.web_view)
//    WebView webView;
    @BindView(R.id.rl_show)
    RelativeLayout rlShow;
    @BindView(R.id.iv_background)
    ImageView ivBackground;
    @BindView(R.id.iv_logo)
    ImageView ivLogo;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_text_top)
    TextView tvTextTop;
    @BindView(R.id.tv_text_bottom)
    TextView tvTextBottom;
    @BindView(R.id.ll_time)
    LinearLayout llTime;
    @BindView(R.id.rl_no_show)
    RelativeLayout rlNoShow;

    private List<MessageModel> mList = new ArrayList<>();
    private ArrayList<ImageView> mImageList = new ArrayList<>();
    private LoopViewAdapter mAdapter;


    private MainViewModel mMainViewModel;
    private UploadViewModel uploadViewModel;
    private String mUrl;
    private int mIndex = 0;
    private boolean isFirst = true;
    private ProgressDialog mProgressDialog;
    private boolean needPlay = true;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_show_message_new;
    }

    @Override
    protected void initData() {
//        startService(new Intent(this, GetTimeService.class));
        startService(new Intent(this, GetSkinService.class));
        mMainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        uploadViewModel = ViewModelProviders.of(this).get(UploadViewModel.class);
        LiveEventBus.get(LiveEvent.REFRESH_SKIN, SkinModel.class).observe(this, skinModel -> {
            if (skinModel != null) {
                updatePic(skinModel);
            }
        });

        startService(new Intent(this, GetMessageService.class));
        LiveEventBus.get(LiveEvent.REFRESH_MESSAGE, LiveMessageModel.class).observe(this, models -> {
            if (models != null && models.getList().size() != 0) {
                getLiveMessageModel = models;
                needPlay = models.getRunning_state() == 1;
                if (needPlay) {
                    rlNoShow.setVisibility(View.GONE);
                    rlShow.setVisibility(View.VISIBLE);
                    MessageModel model = models.getList().get(0);
//                    Log.i("www", model.toString());
                    if (model.getResources_type() == 1) {//播放图片
                        banner.setVisibility(View.VISIBLE);
                        mVideoView.setVisibility(View.GONE);
                        mVideoView.stopPlayback();
//                        webView.setVisibility(View.GONE);
                        if (isFirst || models.getIs_change() == 1) {
//                            startBanner(models.getRotation_time(), models.getRunning_state() == 1);
                            mList.clear();
                            mList.addAll(models.getList());
                            setViewPager(models);

                        }
                    } else if (model.getResources_type() == 2) {//视频
                        mIndex = 0;
                        banner.setVisibility(View.GONE);
//                        banner.stopAutoPlay();
                        mVideoView.setVisibility(View.VISIBLE);
//                        webView.setVisibility(View.GONE);
                        mMainViewModel.setPlayTerminal(model.getId());
                        if (isFirst || models.getIs_change() == 1) {
                            mList.clear();
                            mList.addAll(models.getList());
                            playVideo(mList.get(mIndex).getResources_url(), models.getRunning_state() == 1);
                        }
                    } else if (model.getResources_type() == 3) {//文档
                        banner.setVisibility(View.GONE);
//                        banner.stopAutoPlay();
                        mVideoView.setVisibility(View.GONE);
                        mVideoView.stopPlayback();
//                        webView.setVisibility(View.VISIBLE);
                        mUrl = model.getResources_url();
                        mMainViewModel.setPlayTerminal(model.getId());
                        if (isFirst || models.getIs_change() == 1) {
                            showLoading();
//                            webView.loadUrl("http://view.officeapps.live.com/op/view.aspx?src=" + mUrl + "");
                        }
                    }
                    isFirst = false;
                } else {
                    rlNoShow.setVisibility(View.VISIBLE);
                    rlShow.setVisibility(View.GONE);
                    if (banner != null) {
//                        banner.update(new ArrayList<>());
//                        banner.stopAutoPlay();
                    }
                    if (mVideoView != null && mVideoView.isPlaying()) {
                        mVideoView.stopPlayback();
                    }
                    isFirst = true;
                }
            }
        });
    }

    ImageView imageView;
    private String formatTime = "yyyy-MM-dd HH:mm:ss.SSS";
    private MessageModel showModel;
    private LiveMessageModel getLiveMessageModel;
    private long zuLunboTime;
    private int nowPos = 0;

    private void setViewPager(LiveMessageModel models) {

        zuLunboTime = models.getRotation_time() * mList.size();
        SPUtils.getInstance().put(SpConstant.ROTATION_TIME, models.getRotation_time());
//        Log.i("www", "一组需要" + (zuLunboTime / 1000) + "秒");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(TimeUtils.getNowDate());
        calendar.set(Calendar.HOUR_OF_DAY, 7);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long startMills = calendar.getTimeInMillis();
//        Log.i("www", "清零时间 = " + TimeUtils.millis2String(startMills, new SimpleDateFormat(formatTime)));

        long nowTime = TimeUtils.getNowMills();
//        Log.i("www", "现在时间 = " + TimeUtils.millis2String(nowTime, new SimpleDateFormat(formatTime)));

        long cha = nowTime - startMills;
        long yushu = cha % zuLunboTime;
//        Log.i("www", "应该多出来" + (yushu / 1000) + "秒");

        nowPos = (int) (yushu / models.getRotation_time());
//        Log.i("www", "应该初于第" + pos + "张");

        long zaiyu = yushu % models.getRotation_time();
//        Log.i("www", "第" + nowPos + "张运行了" + (zaiyu / 1000) + "秒");

        loopTime = models.getRotation_time() - zaiyu;
//        Log.i("www", "应该睡眠" + loopTime + "毫秒");

        long messStart = nowTime - yushu;
        long messEnd = messStart + models.getRotation_time();

//        Log.i("www", "start = " + TimeUtils.millis2String(messStart, new SimpleDateFormat(formatTime)));
//        Log.i("www", "  end = " + TimeUtils.millis2String(messEnd, new SimpleDateFormat(formatTime)));

        for (int i = 0; i < mList.size(); i++) {
            MessageModel messageModel = mList.get(i);
            if (i < nowPos) {
                messageModel.setEndTime(messEnd + (i * models.getRotation_time()) + zuLunboTime);
            } else {
                messageModel.setEndTime(messEnd + (i * models.getRotation_time()));
            }
            //初始化要显示的图片对象
            imageView = new ImageView(this);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            Glide.with(this).load(messageModel.getImgs_url()).apply(new RequestOptions().error(R.drawable.ic_background)).into(imageView);

            mImageList.add(imageView);
        }
        showModel = mList.get(nowPos);
        mAdapter = new LoopViewAdapter(mImageList);
        //设置适配器
        banner.setAdapter(mAdapter);
        banner.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                nowPos = i % mImageList.size();
                showModel = mList.get(nowPos);
                loopTime = showModel.getEndTime() - TimeUtils.getNowMills();
//                Log.i("www", "动完：" + TimeUtils.getNowString(new SimpleDateFormat(formatTime)));

//                Log.i("www", "现在的" + nowPos+" 结束时间为："+
//                        TimeUtils.millis2String(showModel.getEndTime(),new SimpleDateFormat(formatTime))
//                        + "需要睡眠" + (loopTime/1000) + "秒");
                mHandler.sendEmptyMessageDelayed(3, loopTime);
                showModel.setEndTime(showModel.getEndTime() + zuLunboTime);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        if (nowPos == 0) {
            showModel = mList.get(nowPos);
            loopTime = showModel.getEndTime() - TimeUtils.getNowMills();
            mHandler.sendEmptyMessageDelayed(3, loopTime);
            showModel.setEndTime(showModel.getEndTime() + zuLunboTime);
        }
        // 把ViewPager设置为默认选中Integer.MAX_VALUE / t2，从十几亿次开始轮播图片，达到无限循环目的;
        int m = (Integer.MAX_VALUE / 2) % mImageList.size();
        int currentPosition = Integer.MAX_VALUE / 2 - m;
        banner.setCurrentItem(nowPos);
        isRunning = false;
    }

    boolean isRunning = false;
    long loopTime = 5000;

    //在主线程里面处理消息并更新UI界面
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    setTextTime();
                    break;
                case 3:
//                    Log.i("www", "要动：" + TimeUtils.getNowString(new SimpleDateFormat(formatTime)));
                    banner.setCurrentItem(banner.getCurrentItem() + 1);
                    break;
                default:
                    break;

            }
        }
    };

    private void showLoading() {
        mProgressDialog = ProgressDialog.show(this, null, "加载中...", true, true);
    }

    @Override
    protected void initView() {
        initVideoView();
        initWebView();
        setDate();
        setTextTime();
        startThread();
    }

    private void setDate() {
        tvTextTop.setText(TimeUtils.getNowString(new SimpleDateFormat("MM月dd日")) +
                DateUtils.getWeek());
        Calendar today = Calendar.getInstance();
        today.setTime(new Date());
        Lunar lunar = new Lunar(today);
        tvTextBottom.setText(lunar.toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    private void playVideo(String uri, boolean b) {
        mVideoView.setVideoURI(Uri.parse(uri));//设置视频文件
        showLoading();
        mVideoView.start();
    }

    private void initVideoView() {
        MediaController mediaController = new MediaController(this);
        mediaController.setVisibility(View.GONE);
        mVideoView.setMediaController(mediaController);
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                //视频加载完成,准备好播放视频的回调
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
            }
        });
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                //视频播放完成后的回调
                mIndex++;
                if (mIndex == mList.size()) {
                    mIndex = 0;
                }
                playVideo(mList.get(mIndex).getResources_url(), needPlay);
            }
        });
        mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                //异常回调
                return false;//如果方法处理了错误，则为true；否则为false。返回false或根本没有OnErrorListener，将导致调用OnCompletionListener。
            }
        });
        mVideoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                //信息回调

                return false; //如果方法处理了信息，则为true；如果没有，则为false。返回false或根本没有OnInfoListener，将导致丢弃该信息。
            }
        });
    }

    protected void initWebView() {
//        webView.getSettings().setJavaScriptEnabled(true);
//
////        webView.addJavascriptInterface(getJavaObject(), "javaObject");
//        webView.setWebViewClient(new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                view.loadUrl(url);
//                return true;
//            }
//        });
//        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
//        // 必须保留，否则无法播放优酷视频，其他的OK
//        webView.getSettings().setDomStorageEnabled(true);
//        // 重写一下，有的时候可能会出现问题
////        webView.setWebChromeClient(new MyWebChromeClient());
//        webView.getSettings().setAllowFileAccess(true);
//        webView.getSettings().setAllowFileAccessFromFileURLs(true);
//        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
//        //自适应屏幕
//        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//        webView.setWebViewClient(new WebViewClient() {
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                if (mProgressDialog != null) {
//                    mProgressDialog.dismiss();
//                }
//            }
//        });
    }

    private void startThread() {
        new TimeThread().start();
    }

    class TimeThread extends Thread {
        @Override
        public void run() {
            do {
                try {
                    Thread.sleep(1000);
                    Message msg = new Message();
                    msg.what = 1;  //消息(一个整型值)
                    mHandler.sendMessage(msg);// 每隔1秒发送一个msg给mHandler
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (true);
        }
    }

    /**
     * 给控件设置时间
     */
    private void setTextTime() {
        long sysTime = System.currentTimeMillis();//获取系统时间
        CharSequence sysTimeStr = DateFormat.format("HH:mm", sysTime);//时间显示格式
        tvTime.setText(sysTimeStr); //更新时间
    }

    private void updatePic(SkinModel skinModel) {
        Glide.with(this).load(skinModel.getBack_imgs_url())
                .apply(new RequestOptions().error(R.drawable.ic_background)
                        .placeholder(R.drawable.ic_background)).into(ivBackground);
        Glide.with(this).load(skinModel.getLogo_url()).apply(new RequestOptions().error(R.drawable.ic_main_logo)).into(ivLogo);
    }
}
