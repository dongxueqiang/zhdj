package com.zhdj.zhdj.view.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.loader.ImageLoader;
import com.zhdj.zhdj.R;
import com.zhdj.zhdj.base.BaseActivity;
import com.zhdj.zhdj.model.MessageModel;
import com.zhdj.zhdj.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;


/**
 * @ClassName ShowImageActivity
 * @Author dongxueqiang
 * @Date 2020/7/14 21:39
 * @Title
 */
public class ShowImageActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.banner)
    Banner banner;

    private MessageModel model;
    private MainViewModel mMainViewModel;

    private List<MessageModel> mList = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_show_image;
    }

    @Override
    protected void initData() {
        model = getIntent().getParcelableExtra("model");
        mMainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mMainViewModel.getMessageDetail(model.getId());
        mMainViewModel.msgDetail.observe(this,messageModel -> {
            mList.add(messageModel);
            startBanner();
        });
    }

    @Override
    protected void initView() {
//        List<Integer> images = new ArrayList<>();
//        images.add(R.drawable.ic_background);
//        images.add(R.drawable.ic_demo);
    }

    private void startBanner() {
        //设置banner样式
        banner.setBannerStyle(BannerConfig.NOT_INDICATOR);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(mList);
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.Default);
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(5000);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
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
            Glide.with(context).load(model.getResources_url()).into(imageView);

        }

    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}
