package com.zhdj.zhdj.view.weight;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

/**
 * @ClassName FullScreen
 * @Author dongxueqiang
 * @Date 2020/7/16 19:43
 * @Title
 */
public class FullScreen extends VideoView {
    public FullScreen(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
// TODO Auto-generated constructor stub
    }

    public FullScreen(Context context, AttributeSet attrs) {
        super(context, attrs);
// TODO Auto-generated constructor stub
    }

    public FullScreen(Context context) {
        super(context);
// TODO Auto-generated constructor stub
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {//这里重写onMeasure的方法
// TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getDefaultSize(0, widthMeasureSpec);//得到默认的大小（0，宽度测量规范）
        int height = getDefaultSize(0, heightMeasureSpec);//得到默认的大小（0，高度度测量规范）
        setMeasuredDimension(width, height); //设置测量尺寸,将高和宽放进去
    }
}