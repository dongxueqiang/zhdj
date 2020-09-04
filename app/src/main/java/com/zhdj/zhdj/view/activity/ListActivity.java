package com.zhdj.zhdj.view.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhdj.zhdj.R;
import com.zhdj.zhdj.base.BaseActivity;
import com.zhdj.zhdj.model.MenuModel;
import com.zhdj.zhdj.model.MessageModel;
import com.zhdj.zhdj.view.weight.GridSpacingItemDecoration;
import com.zhdj.zhdj.viewmodel.MainViewModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * @ClassName ListActivity
 * @Author dongxueqiang
 * @Date 2020/7/15 21:46
 * @Title
 */
public class ListActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private MainViewModel mMainViewModel;
    private MenuModel mMenuModel;

    private int mPageNo = 1;
    private int limit = 10;
    private boolean mRefresh;

    private BaseQuickAdapter<MessageModel, BaseViewHolder> mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_list;
    }

    @Override
    protected void initData() {
        mMenuModel = getIntent().getParcelableExtra("model");
        mMainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mMainViewModel.msgList.observe(this, messageModels -> {
            swipeRefreshLayout.setRefreshing(false);
            if (mRefresh) {
                mAdapter.setNewData(messageModels);
            } else {
                mAdapter.addData(messageModels);
            }
            if (messageModels.size() < limit) {
                mAdapter.loadMoreEnd(true);
            } else {
                mAdapter.loadMoreComplete();
            }
        });
    }

    @Override
    protected void initView() {
        tvTitle.setText(mMenuModel.getModule_name());
        initRecyclerView();
    }

    private void initRecyclerView() {
//        int spanCount = 2; // 3 columns
//        int spacing = 50; // 50px
//        boolean includeEdge = false;
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
//        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        mAdapter = new BaseQuickAdapter<MessageModel, BaseViewHolder>(R.layout.recycler_msg, new ArrayList<>()) {
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
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            MessageModel model = mAdapter.getItem(position);
            Intent intent = new Intent();
            intent.putExtra("model", model);
            if (model.getResources_type() == 1) {
                intent.setClass(ListActivity.this, ShowImageActivity.class);
            } else if (model.getResources_type() == 2) {
                intent.setClass(ListActivity.this, ShowVideoActivity.class);
            } else if (model.getResources_type() == 3) {
                intent.setClass(ListActivity.this, ShowWordActivity.class);
            }
            startActivity(intent);
        });
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnLoadMoreListener(() -> {
            mRefresh = false;
            mPageNo++;
            getDate();
        }, recyclerView);
        swipeRefreshLayout.setOnRefreshListener(this::refreshData);
        refreshData();
    }

    public void refreshData() {
        swipeRefreshLayout.setRefreshing(true);
        mRefresh = true;
        mPageNo = 1;
        getDate();
    }


    private void getDate() {
        mMainViewModel.getMessage(mMenuModel.getId(), mPageNo, limit);
    }


    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}
