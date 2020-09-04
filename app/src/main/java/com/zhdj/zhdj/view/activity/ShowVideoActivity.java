package com.zhdj.zhdj.view.activity;

import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.zhdj.zhdj.R;
import com.zhdj.zhdj.base.BaseActivity;
import com.zhdj.zhdj.model.MessageModel;
import com.zhdj.zhdj.view.weight.FullScreen;
import com.zhdj.zhdj.viewmodel.MainViewModel;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * @ClassName ShowVideoActivity
 * @Author dongxueqiang
 * @Date 2020/7/15 18:30
 * @Title
 */
public class ShowVideoActivity extends BaseActivity {
    @BindView(R.id.video_view)
    FullScreen mVideoView;
    @BindView(R.id.iv_back)
    ImageView ivBack;

    private MessageModel model;
    private MainViewModel mMainViewModel;

    private String mUrl;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_show_video;
    }

    @Override
    protected void initData() {
        model = getIntent().getParcelableExtra("model");
        mMainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mMainViewModel.getMessageDetail(model.getId());
        mMainViewModel.msgDetail.observe(this,messageModels -> {
           if (messageModels!=null){
               mUrl = messageModels.getResources_url();
               playVideo(mUrl);
           }
        });
    }

    @Override
    protected void initView() {
        initVideoView();
    }

    private void playVideo(String uri) {
        showLoading();
        mVideoView.setVideoURI(Uri.parse(uri));//设置视频文件
        mVideoView.start();
    }

    private void initVideoView() {
        MediaController mediaController = new MediaController(this);
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
                playVideo(mUrl);
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

    private ProgressDialog mProgressDialog;

    private void showLoading() {
        mProgressDialog = ProgressDialog.show(this, null, "加载中...", true, true);
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}
