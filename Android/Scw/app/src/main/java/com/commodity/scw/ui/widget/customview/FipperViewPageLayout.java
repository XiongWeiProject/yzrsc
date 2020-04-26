package com.commodity.scw.ui.widget.customview;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.commodity.scw.MainApplication;
import com.commodity.scw.R;
import com.commodity.scw.utils.ScreenUtils;

import java.util.List;

/**
 * Created by liyushen on 2017/3/28 21:40
 * 自定义控件左右滑动
 */
public class FipperViewPageLayout extends LinearLayout {
    private Context context;
    PageFlipperViewPager pageFlipperViewPager;
    LinearLayout titleLinear;
    LayoutParams layoutParamsPage;
    LayoutParams layoutParamsTitle;
    int defaultHight=0;
    double percentageHight=(double)2/5;
    private List<TextView> titleTextViews;
    private List<View>  PagerViewList;
    public FipperViewPageLayout(Context context) {
        super(context);
        this.context=context;
        init();
    }

    public FipperViewPageLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        init();

    }
    public FipperViewPageLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
        init();
    }

    private void init() {
        setOrientation(VERTICAL);
        defaultHight= (int) (MainApplication.SCREEN_W*percentageHight);
        layoutParamsPage =new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, defaultHight);

        //
        layoutParamsTitle =new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ScreenUtils.dp2px(context,50));
        //初始化ViewPager
        pageFlipperViewPager=new PageFlipperViewPager(context);
        pageFlipperViewPager.setLayoutParams(layoutParamsPage);
        //初始化底部栏
        titleLinear = new LinearLayout(context);
        titleLinear.setOrientation(HORIZONTAL);
        titleLinear.setGravity(Gravity.CENTER);
        titleLinear.setLayoutParams(layoutParamsTitle);
        titleLinear.setBackgroundResource(R.drawable.one_line_bottom_gray_bg);
        //addview
        addView(titleLinear);
        addView(pageFlipperViewPager);
        pageFlipperViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i <titleTextViews.size() ; i++) {
                    if (i==position){
                        titleTextViews.get(i).setTextColor(getResources().getColor(R.color.co_F95F3D));
                        titleTextViews.get(i).setBackgroundResource(R.drawable.one_line_bottom_red_bg);
                    }else {
                        titleTextViews.get(i).setTextColor(getResources().getColor(R.color.second_black));
                        titleTextViews.get(i).setBackgroundResource(R.drawable.icon_transparent);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    //设置图片集合
    public void setViewList(List<TextView> titleTextViews,List<View>  PagerViewList) {
        this.titleTextViews=titleTextViews;
        this.PagerViewList=PagerViewList;
        initTitleTextViews();
        if (titleTextViews.size()>0){
            titleTextViews.get(0).setTextColor(getResources().getColor(R.color.co_F95F3D));
            titleTextViews.get(0).setBackgroundResource(R.drawable.one_line_bottom_red_bg);
        }

        for (int i = 0; i <titleTextViews.size() ; i++) {
            final int finalI = i;
            titleTextViews.get(i).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    pageFlipperViewPager.setCurrentItem(finalI);
                }
            });
        }
    }

    //设置高度
    public void setLayoutHight(double percentageHight) {
        this.percentageHight=percentageHight;
        defaultHight= (int) (MainApplication.SCREEN_W*percentageHight);
        layoutParamsPage =new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, defaultHight);
        pageFlipperViewPager.setLayoutParams(layoutParamsPage);
    }


    //加载point和图片
    private void initTitleTextViews(){
        LinearLayout.LayoutParams la=new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
         la.weight=1;
        titleLinear.removeAllViews();
        for (int i = 0; i < titleTextViews.size(); i++) {
            View itemView=new View(context);
            itemView.setLayoutParams(la);
            titleLinear.addView(itemView);
            titleLinear.addView(titleTextViews.get(i));
            if (i==titleTextViews.size()-1){
                View itemView2=new View(context);
                itemView2.setLayoutParams(la);
                titleLinear.addView(itemView2);
            }
        }
        pageFlipperViewPager.startView();
    }

    class PageFlipperViewPager extends ViewPager {
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
                return position ;
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
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (listener2 != null) {
                    listener2.onPageScrollStateChanged(state);
                }
            }
        }, listener2;


        public PageFlipperViewPager(Context context, AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        public PageFlipperViewPager(Context context) {
            super(context);
            init();
        }

        private void init() {
            //setOffscreenPageLimit(1); // 最大页面缓存数量
            setAdapter(adapter); // 适配器
            super.setOnPageChangeListener(listener); // 监听器
        }

        public void startView() {
            this.views = PagerViewList;
            setCurrentItem(0); // 首页
            this.adapter.notifyDataSetChanged();
        }

        @Override
        public void setOnPageChangeListener(OnPageChangeListener listener) {
            this.listener2 = listener;
        }
    }
}

