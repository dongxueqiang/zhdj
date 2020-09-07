package com.zhdj.zhdj.view.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * @ClassName LoopViewAdapter
 * @Author dongxueqiang
 * @Date 2020/9/2 22:15
 * @Title
 */
public class LoopViewAdapter extends PagerAdapter {
    private ArrayList<ImageView> imageViewList;

    public LoopViewAdapter(ArrayList<ImageView> mImgList) {
        imageViewList = mImgList;
    }

//    public int getPostion(){
//        return getPostion();
//    }

    // 1. 返回要显示的条目内容, 创建条目

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // container: 容器: ViewPager
        // position: 当前要显示条目的位置 0 -> 4
        //newPosition = position % 5
        int newPosition = position % imageViewList.size();
        ImageView img = imageViewList.get(newPosition);
        // a. 把View对象添加到container中
        container.addView(img);
        // b. 把View对象返回给框架, 适配器
        return img;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;   //返回一个无限大的值，可以 无限循环!!!!!
    }

    /**
     * 判断是否使用缓存, 如果返回的是true, 使用缓存. 不去调用instantiateItem方法创建一个新的对象
     */
    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
