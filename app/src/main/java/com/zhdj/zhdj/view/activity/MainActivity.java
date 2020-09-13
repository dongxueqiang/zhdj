package com.zhdj.zhdj.view.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.TimeUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jeremyliao.liveeventbus.LiveEventBus;
import com.zhdj.zhdj.R;
import com.zhdj.zhdj.base.BaseActivity;
import com.zhdj.zhdj.event.LiveEvent;
import com.zhdj.zhdj.model.MenuModel;
import com.zhdj.zhdj.model.MessageModel;
import com.zhdj.zhdj.model.SkinModel;
import com.zhdj.zhdj.utils.DateUtils;
import com.zhdj.zhdj.utils.Lunar;
import com.zhdj.zhdj.viewmodel.MainViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {
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
    @BindView(R.id.rv_menu)
    RecyclerView rvMenu;
    @BindView(R.id.rv_msg)
    RecyclerView rvMsg;
    @BindView(R.id.iv_background)
    ImageView ivBackground;

    private MainViewModel mMainViewModel;
    private BaseQuickAdapter<MenuModel, BaseViewHolder> menuAdapter;
    private BaseQuickAdapter<MessageModel, BaseViewHolder> msgAdapter;

    //在主线程里面处理消息并更新UI界面
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    setTextTime();
                    break;
                default:
                    break;

            }
        }
    };

    /**
     * 给控件设置时间
     */
    private void setTextTime() {
        long sysTime = System.currentTimeMillis();//获取系统时间
        CharSequence sysTimeStr = DateFormat.format("HH:mm", sysTime);//时间显示格式
        tvTime.setText(sysTimeStr); //更新时间
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
        LiveEventBus.get(LiveEvent.REFRESH_SKIN, SkinModel.class).observe(this, skinModel -> {
            if (skinModel != null) {
                updatePic(skinModel);
            }
        });
        mMainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
//        mMainViewModel.skinModel.observe(this, skinModel -> {
//            if (skinModel != null) {
//                updatePic(skinModel);
//            }
//        });
        mMainViewModel.menuList.observe(this, menuModels -> {
            if (menuModels != null) {
                updateMenu(menuModels);
            }
        });
        mMainViewModel.msgList.observe(this, menuModels -> {
            if (menuModels != null) {
                updateMsg(menuModels);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    private void getData() {
//        mMainViewModel.getSkin();
//        mMainViewModel.getMainMenu();
//        mMainViewModel.getMessage(1, 1, 4);
    }

    private void updateMenu(List<MenuModel> menuModels) {
        rvMenu.setLayoutManager(new GridLayoutManager(this, 2));
        menuAdapter = new BaseQuickAdapter<MenuModel, BaseViewHolder>(R.layout.recycler_image, menuModels) {
            @Override
            protected void convert(BaseViewHolder helper, MenuModel item) {
                helper.setText(R.id.tv_name, item.getModule_name());
                //设置图片圆角角度
                RoundedCorners roundedCorners = new RoundedCorners(10);
                //通过RequestOptions扩展功能,override:采样率,因为ImageView就这么大,可以压缩图片,降低内存消耗
                RequestOptions options = RequestOptions.bitmapTransform(roundedCorners)
                        .error(R.drawable.ic_menu_bg);
                Glide.with(mContext).load(item.getBack_imgs_url())
                        .apply(options).into((ImageView) helper.getView(R.id.iv_pic));
            }
        };
        menuAdapter.setOnItemClickListener((adapter, view, position) -> {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, ListActivity.class);
            intent.putExtra("model", menuAdapter.getItem(position));
            startActivity(intent);
        });
        rvMenu.setAdapter(menuAdapter);
    }

    private void updateMsg(List<MessageModel> menuModels) {
//        int spanCount = 2; // 3 columns
//        int spacing = 50; // 50px
//        boolean includeEdge = false;
        rvMsg.setLayoutManager(new GridLayoutManager(this, 2));
//        rvMsg.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        msgAdapter = new BaseQuickAdapter<MessageModel, BaseViewHolder>(R.layout.recycler_msg, menuModels) {
            @Override
            protected void convert(BaseViewHolder helper, MessageModel item) {
                helper.setText(R.id.tv_name, item.getResources_name());
                //设置图片圆角角度
                RoundedCorners roundedCorners = new RoundedCorners(10);
                //通过RequestOptions扩展功能,override:采样率,因为ImageView就这么大,可以压缩图片,降低内存消耗
                RequestOptions options = RequestOptions.bitmapTransform(roundedCorners)
                        .error(R.drawable.ic_menu_bg);
                Glide.with(mContext).load(item.getImgs_url())
                        .apply(options).into((ImageView) helper.getView(R.id.iv_pic));
            }
        };
        msgAdapter.setOnItemClickListener((adapter, view, position) -> {
            MessageModel model = msgAdapter.getItem(position);
            Intent intent = new Intent();
            intent.putExtra("model", model);
            if (model.getResources_type() == 1) {
                intent.setClass(MainActivity.this, ShowImageActivity.class);
            } else if (model.getResources_type() == 2) {
                intent.setClass(MainActivity.this, ShowVideoActivity.class);
            } else if (model.getResources_type() == 3) {
                intent.setClass(MainActivity.this, ShowWordActivity.class);
            }
            startActivity(intent);
        });
        rvMsg.setAdapter(msgAdapter);
    }

    private void updatePic(SkinModel skinModel) {
        Glide.with(this).load(skinModel.getBack_imgs_url())
                .apply(new RequestOptions().error(R.drawable.ic_background)
                        .placeholder(R.drawable.ic_background)).into(ivBackground);
        Glide.with(this).load(skinModel.getLogo_url()).apply(new RequestOptions().error(R.drawable.ic_main_logo)).into(ivLogo);
    }

    @Override
    protected void initView() {

        tvTextTop.setText(TimeUtils.getNowString(new SimpleDateFormat("MM月dd日")) +
                DateUtils.getWeek());
        Calendar today = Calendar.getInstance();
        today.setTime(new Date());
        Lunar lunar = new Lunar(today);
        tvTextBottom.setText(lunar.toString());
        setTextTime();
        startThread();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
