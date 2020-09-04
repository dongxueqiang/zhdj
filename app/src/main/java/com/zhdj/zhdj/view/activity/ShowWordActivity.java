package com.zhdj.zhdj.view.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.zhdj.zhdj.R;
import com.zhdj.zhdj.base.BaseActivity;
import com.zhdj.zhdj.model.MessageModel;
import com.zhdj.zhdj.viewmodel.MainViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * @ClassName ShowWordActivity
 * @Author dongxueqiang
 * @Date 2020/7/15 19:25
 * @Title
 */
public class ShowWordActivity extends BaseActivity {
    @BindView(R.id.video_view)
    WebView webView;
    @BindView(R.id.iv_back)
    ImageView ivBack;

    private MessageModel model;
    private MainViewModel mMainViewModel;

    String mUrl;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_show_word;
    }

    @Override
    protected void initData() {
        model = getIntent().getParcelableExtra("model");
        mMainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mMainViewModel.getMessageDetail(model.getId());
        mMainViewModel.msgDetail.observe(this, messageModels -> {
            if (messageModels != null) {
                mUrl = messageModels.getResources_url();
                webView.loadUrl("http://view.officeapps.live.com/op/view.aspx?src=" + mUrl + "");
            }
        });
    }

    @Override
    protected void initView() {
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
    }

//    public JavaObject getJavaObject(){
//        return new JavaObject(this);
//    }
}
