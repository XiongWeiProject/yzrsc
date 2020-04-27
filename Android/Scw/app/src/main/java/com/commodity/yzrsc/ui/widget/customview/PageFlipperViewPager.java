package com.commodity.yzrsc.ui.widget.customview;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liyushen on 2017/3/19 18:18
 * 自定义轮播控件
 */
public class PageFlipperViewPager extends ViewPager {

    private String TAG = PageFlipperViewPager.class.getSimpleName();
    private List<View> views;
    private PagerAdapter adapter = new PagerAdapter() {

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View v = views.get(position);
            container.addView(v);
            return v;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public int getItemPosition(Object object) {
            return views.indexOf(object);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return views == null ? 0 : views.size();
        }
    };
    private OnPageChangeListener listener = new OnPageChangeListener() {

        /**
         * 将控件位置转化为数据集中的位置
         */
        public int convert(int position) {
            return position == 0 ? views.size() - 1 : (position > views.size() ? 0 : position - 1);
        }

        @Override
        public void onPageSelected(int position) {
            if (listener2 != null) {
                listener2.onPageSelected(convert(position));
            }
        }

        @Override
        public void onPageScrolled(int position, float percent, int offset) {
            if (listener2 != null) {
                listener2.onPageScrolled(convert(position), percent, offset);
            }

            if (percent == 0) {
                if (position == 0) // 切换到倒数第二页
                    setCurrentItem((views.size() - 2) % views.size(), false);
                else if (position == views.size() - 1) // 切换到正数第二页
                    setCurrentItem(1, false);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (listener2 != null) {
                listener2.onPageScrollStateChanged(state);
            }

            switch (state) {
                case SCROLL_STATE_IDLE: // 闲置

                    if (!handler.hasMessages(START_FLIPPING))
                        handler.sendEmptyMessageDelayed(START_FLIPPING, 3000);  // 延时滚动

                    break;
                case SCROLL_STATE_DRAGGING: // 拖动中

                    handler.sendEmptyMessage(STOP_FLIPPING); // 取消滚动

                    break;
                case SCROLL_STATE_SETTLING: // 拖动结束
                    break;
            }
        }
    }, listener2;

    private final int START_FLIPPING = 0;
    private final int STOP_FLIPPING = 1;

    private Handler handler = new Handler() {

        public void handleMessage(Message msg) {

            switch (msg.what) {
                case START_FLIPPING:

                    if (views.size() > 3) // 因为前后页是辅助页，所以此处3也就是只有1页
                        setCurrentItem((getCurrentItem() + 1) % views.size());

                    handler.sendEmptyMessageDelayed(START_FLIPPING, 3000);  // 延时滚动

                    break;
                case STOP_FLIPPING:

                    handler.removeMessages(START_FLIPPING);

                    break;
            }
        }
    };

    public PageFlipperViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PageFlipperViewPager(Context context) {
        super(context);
        init();
    }

    private void init() {

        setOffscreenPageLimit(1); // 最大页面缓存数量
        setAdapter(adapter); // 适配器
        super.setOnPageChangeListener(listener); // 监听器

        handler.sendEmptyMessageDelayed(START_FLIPPING, 3000);  // 延时滚动
    }

    public void setViews(int[] ids) {
        this.views = new ArrayList<View>();
        for (int i = 0; i < ids.length + 2; i++) { // 头部新增一个尾页，尾部新增一个首页

            ImageView iv = new ImageView(getContext());
            iv.setImageResource(ids[i == 0 ? ids.length - 1 : (i > ids.length ? 0 : i - 1)]);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            this.views.add(iv);
        }
        setCurrentItem(1); // 首页
        this.adapter.notifyDataSetChanged();
    }

    @Override
    public void setOnPageChangeListener(OnPageChangeListener listener) {
        this.listener2 = listener;
    }

}
