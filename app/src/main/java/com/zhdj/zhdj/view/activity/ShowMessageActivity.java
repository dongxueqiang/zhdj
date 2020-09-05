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

import com.blankj.utilcode.util.TimeUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jeremyliao.liveeventbus.LiveEventBus;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.loader.ImageLoader;
import com.zhdj.zhdj.R;
import com.zhdj.zhdj.base.BaseActivity;
import com.zhdj.zhdj.event.LiveEvent;
import com.zhdj.zhdj.model.LiveMessageModel;
import com.zhdj.zhdj.model.MessageModel;
import com.zhdj.zhdj.model.SkinModel;
import com.zhdj.zhdj.utils.DateUtils;
import com.zhdj.zhdj.utils.Lunar;
import com.zhdj.zhdj.view.service.GetMessageService;
import com.zhdj.zhdj.view.weight.FullScreen;
import com.zhdj.zhdj.viewmodel.MainViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * @ClassName ShowMessageActivity
 * @Author dongxueqiang
 * @Date 2020/7/16 18:48
 * @Title
 */
public class ShowMessageActivity extends BaseActivity {
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.video_view)
    FullScreen mVideoView;
    @BindView(R.id.web_view)
    WebView webView;
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
    @BindView(R.id.tv_start_time)
    TextView tvStartTime;

    private List<MessageModel> mList = new ArrayList<>();
    private MainViewModel mMainViewModel;
    private String mUrl;
    private int mIndex = 0;
    private boolean isFirst = true;
    private ProgressDialog mProgressDialog;
    private boolean needPlay = true;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_show_message;
    }

    @Override
    protected void initData() {
        mMainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        LiveEventBus.get(LiveEvent.REFRESH_SKIN, SkinModel.class).observe(this, skinModel -> {
            if (skinModel != null) {
                updatePic(skinModel);
            }
        });

        startService(new Intent(this, GetMessageService.class));
        LiveEventBus.get(LiveEvent.REFRESH_MESSAGE, LiveMessageModel.class).observe(this, models -> {
            if (models != null && models.getList().size() != 0) {
                needPlay = models.getRunning_state() == 1;
                if (needPlay) {
                    rlNoShow.setVisibility(View.GONE);
                    rlShow.setVisibility(View.VISIBLE);
                    mList.clear();
                    mList.addAll(models.getList());
                    MessageModel model = models.getList().get(0);
//                    Log.i("www", model.toString());
                    if (model.getResources_type() == 1) {//播放图片
                        banner.setVisibility(View.VISIBLE);
                        mVideoView.setVisibility(View.GONE);
                        mVideoView.stopPlayback();
                        webView.setVisibility(View.GONE);
                        if (isFirst || models.getIs_change() == 1) {
                            startBanner(models.getRotation_time(), models.getRunning_state() == 1);
                        }
                    } else if (model.getResources_type() == 2) {//视频
                        mIndex = 0;
                        banner.setVisibility(View.GONE);
                        banner.stopAutoPlay();
                        mVideoView.setVisibility(View.VISIBLE);
                        webView.setVisibility(View.GONE);
                        mMainViewModel.setPlayTerminal(model.getId());
                        if (isFirst || models.getIs_change() == 1) {
                            playVideo(mList.get(mIndex).getResources_url(), models.getRunning_state() == 1);
                        }
                    } else if (model.getResources_type() == 3) {//文档
                        banner.setVisibility(View.GONE);
                        banner.stopAutoPlay();
                        mVideoView.setVisibility(View.GONE);
                        mVideoView.stopPlayback();
                        webView.setVisibility(View.VISIBLE);
                        mUrl = model.getResources_url();
                        mMainViewModel.setPlayTerminal(model.getId());
                        if (isFirst || models.getIs_change() == 1) {
                            showLoading();
                            webView.loadUrl("http://view.officeapps.live.com/op/view.aspx?src=" + mUrl + "");
                        }
                    }
                    isFirst = false;
                } else {
                    rlNoShow.setVisibility(View.VISIBLE);
                    rlShow.setVisibility(View.GONE);
                    if (banner != null) {
                        banner.update(new ArrayList<>());
//                        banner.stopAutoPlay();
                    }
                    if (mVideoView != null && mVideoView.isPlaying()) {
                        mVideoView.stopPlayback();
                    }
                }
            }
        });
    }

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

    //图片
    private void startBanner(int rotation_time, boolean b) {
        //设置banner样式
        banner.setBannerStyle(BannerConfig.NOT_INDICATOR);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(mList);
        //设置banner动画效果
//        banner.setBannerAnimation(Transformer.DepthPage);
        banner.setBannerAnimation(Transformer.Stack);
        //设置自动轮播，默认为true
        banner.isAutoPlay(false);
        //设置轮播时间
//        banner.setDelayTime(3000);
        banner.setDelayTime(rotation_time);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);
//        banner.setOnBannerListener(position -> {
//           banner.
//        });
        //banner设置方法全部调用完毕时最后调用
        banner.start();
        banner.toRealPosition(3);
//        mHandler.sendEmptyMessageDelayed(2, 5000);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            /**
             注意：
             1.图片加载器由自己选择，这里不限制，只是提供几种使用方法
             2.返回的图片路径为Object类型，由于不能确定你到底使用的那种图片加载器，
             传输的到的是什么格式，那么这种就使用Object接收和返回，你只需要强转成你传输的类型就行，
             切记不要胡乱强转！
             */
            //Glide 加载图片简单用法
            MessageModel model = (MessageModel) path;
            mMainViewModel.setPlayTerminal(model.getId());
            Glide.with(context).load(model.getResources_url()).into(imageView);

        }

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
//                what 对应返回的值如下
//                public static final int MEDIA_INFO_UNKNOWN = 1;  媒体信息未知
//                public static final int MEDIA_INFO_VIDEO_TRACK_LAGGING = 700; 媒体信息视频跟踪滞后
//                public static final int MEDIA_INFO_VIDEO_RENDERING_START = 3; 媒体信息\视频渲染\开始
//                public static final int MEDIA_INFO_BUFFERING_START = 701; 媒体信息缓冲启动
//                public static final int MEDIA_INFO_BUFFERING_END = 702; 媒体信息缓冲结束
//                public static final int MEDIA_INFO_NETWORK_BANDWIDTH = 703; 媒体信息网络带宽（703）
//                public static final int MEDIA_INFO_BAD_INTERLEAVING = 800; 媒体-信息-坏-交错
//                public static final int MEDIA_INFO_NOT_SEEKABLE = 801; 媒体信息找不到
//                public static final int MEDIA_INFO_METADATA_UPDATE = 802; 媒体信息元数据更新
//                public static final int MEDIA_INFO_UNSUPPORTED_SUBTITLE = 901; 媒体信息不支持字幕
//                public static final int MEDIA_INFO_SUBTITLE_TIMED_OUT = 902; 媒体信息字幕超时

                return false; //如果方法处理了信息，则为true；如果没有，则为false。返回false或根本没有OnInfoListener，将导致丢弃该信息。
            }
        });
    }

    protected void initWebView() {
        webView.getSettings().setJavaScriptEnabled(true);

//        webView.addJavascriptInterface(getJavaObject(), "javaObject");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        // 必须保留，否则无法播放优酷视频，其他的OK
        webView.getSettings().setDomStorageEnabled(true);
        // 重写一下，有的时候可能会出现问题
//        webView.setWebChromeClient(new MyWebChromeClient());
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAllowFileAccessFromFileURLs(true);
        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        //自适应屏幕
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
            }
        });
    }

    //在主线程里面处理消息并更新UI界面
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    setTextTime();
                    break;
                case 2:
                    tvStartTime.setText("开始时间\n" + TimeUtils.getNowString(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")));
//                    Log.i("www", TimeUtils.getNowString(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")));
                    banner.start();
                    banner.onPageSelected(3);
                    break;
                default:
                    break;

            }
        }
    };

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
        CharSequence sysTimeStr = DateFormat.format("HH:mm:ss.SSS", sysTime);//时间显示格式
        tvTime.setText(sysTimeStr); //更新时间
    }

    private void updatePic(SkinModel skinModel) {
        Glide.with(this).load(skinModel.getBack_imgs_url())
                .apply(new RequestOptions().error(R.drawable.ic_background)
                        .placeholder(R.drawable.ic_background)).into(ivBackground);
        Glide.with(this).load(skinModel.getLogo_url()).apply(new RequestOptions().error(R.drawable.ic_main_logo)).into(ivLogo);
    }
}
