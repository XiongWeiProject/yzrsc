package com.commodity.yzrsc.ui.widget.customview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.commodity.yzrsc.MainApplication;
import com.commodity.yzrsc.R;
import com.commodity.yzrsc.manager.ImageLoaderManager;
import com.commodity.yzrsc.manager.SPKeyManager;
import com.commodity.yzrsc.model.Banner;
import com.commodity.yzrsc.model.ImgType;
import com.commodity.yzrsc.model.PicInfo;
import com.commodity.yzrsc.ui.activity.commodity.CommodityDetailActivity;
import com.commodity.yzrsc.ui.activity.general.GeneralWebViewActivity;
import com.commodity.yzrsc.utils.ImageUtil;
import com.commodity.yzrsc.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liyushen on 2017/3/19 21:36
 * 自定义轮播控件
 */
public class PageFlipperLayout extends LinearLayout {
    private Context context;
    PageFlipperViewPager pageFlipperViewPager;
    LinearLayout pointLinear;
    LayoutParams layoutParamsPage;
    LayoutParams layoutParams_linear;
    LayoutParams layoutParamsPoint ;
    int defaultHight=0;
    double percentageHight=(double)2/5;
    private List<ImgType> imgTypeList;
    private List<Banner> bannerList;
    private List<View> pointViewList;
    public PageFlipperLayout(Context context) {
        super(context);
        this.context=context;
        init();
    }

    public PageFlipperLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        init();

    }
    public PageFlipperLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
        init();
    }

    private void init() {
        setOrientation(VERTICAL);
        defaultHight= (int) (MainApplication.SCREEN_W*percentageHight);
        layoutParamsPage =new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, defaultHight);
        //底部LinearLayout的
        layoutParams_linear =new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 30);
        layoutParams_linear.setMargins(0,-30,0,0);
        //小球的
        int sc_point = ScreenUtils.dp2px(context,7);
        layoutParamsPoint=new LayoutParams(sc_point,sc_point);
        layoutParamsPoint.setMargins(0,0,sc_point,0);
        //初始化ViewPager
        pageFlipperViewPager=new PageFlipperViewPager(context);
        pageFlipperViewPager.setLayoutParams(layoutParamsPage);
        //初始化底部栏
        pointLinear= new LinearLayout(context);
        pointLinear.setOrientation(HORIZONTAL);
        pointLinear.setGravity(Gravity.CENTER_VERTICAL|Gravity.RIGHT);
        pointLinear.setLayoutParams(layoutParams_linear);
        //addview
        addView(pageFlipperViewPager);
        addView(pointLinear);
    }

    //设置图片集合
    public void setImgTypeList(List<ImgType> imgTypeList) {
        this.imgTypeList=imgTypeList;
        initPoint(imgTypeList.size());
        pageFlipperViewPager.startImageView();
    }

    //设置banber集合
    public void setBannerList(List<Banner> banners) {
        this.bannerList=banners;
        if (bannerList.size()==0){
            Banner banner=new Banner();
            banner.setLink("");
            banner.setMediaUrl("");
            bannerList.add(banner);
        }
        initPoint(banners.size());
        pageFlipperViewPager.startBannerView();
    }

    //设置高度
    public void setLayoutHight(double percentageHight) {
        this.percentageHight=percentageHight;
        defaultHight= (int) (MainApplication.SCREEN_W*percentageHight);
        layoutParamsPage =new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, defaultHight);
        pageFlipperViewPager.setLayoutParams(layoutParamsPage);
    }


    //加载point和图片
    private void initPoint(int count){
        pointLinear.removeAllViews();
        pointViewList=new ArrayList<>();
        for (int i = 0; i < count; i++) {
            View pointView=new View(context);
            pointView.setLayoutParams(layoutParamsPoint);
            pointViewList.add(pointView);
            pointLinear.addView(pointView);
        }
        if (count<2){
            pointLinear.setVisibility(INVISIBLE);
        }else {
            pointLinear.setVisibility(VISIBLE);
        }
    }

    class PageFlipperViewPager extends ViewPager {
        private List<View> views=new ArrayList<>();
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
                for (int i = 0; i < pointViewList.size(); i++) {
                    if (i==position-1){
                        pointViewList.get(i).setBackgroundResource(R.drawable.bg_circular_red_point);
                    }else {
                        pointViewList.get(i).setBackgroundResource(R.drawable.bg_circular_gray_point);
                    }
                }
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

                        if (views!=null&&views.size() > 3) // 因为前后页是辅助页，所以此处3也就是只有1页
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

        public void startImageView() {
            this.views.clear();
            for (int i = 0; i < imgTypeList.size() + 2; i++) { // 头部新增一个尾页，尾部新增一个首页
                ImageView iv = new ImageView(getContext());
                iv.setScaleType(ImageView.ScaleType.FIT_XY);
                final int dd = i == 0 ? imgTypeList.size() - 1 : (i > imgTypeList.size() ? 0 : i - 1);
                ImageLoaderManager.getInstance().displayImage(imgTypeList.get(dd).getImgpath(), iv, R.drawable.ico_pic_fail_defalt);
                iv.setScaleType(ImageView.ScaleType.FIT_XY);
                this.views.add(iv);
                iv.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (imgTypeList.get(dd).isVideo()){
                            ImageUtil.jumbVideoPlayer(context, imgTypeList.get(dd).getVideopath(), imgTypeList.get(dd).getImgpath(), imgTypeList.get(dd).getDescribe());
                            return;
                        }
                        SPKeyManager.picInfoList.clear();
                        for (int j = 0; j < imgTypeList.size(); j++) {
                            PicInfo pic=new  PicInfo();
                            pic.setImg(imgTypeList.get(j).getImgpath());
                            if (!imgTypeList.get(j).isVideo()){
                                SPKeyManager.picInfoList.add(pic);
                            }
                        }
                        ImageUtil.jumbPicShowActivity((Activity) context,0);
                    }
                });
            }
            this.adapter.notifyDataSetChanged();
            setCurrentItem(1); // 首页
        }

        public void startBannerView() {
            this.views.clear();
            for (int i = 0; i < bannerList.size() + 2; i++) { // 头部新增一个尾页，尾部新增一个首页
                ImageView iv = new ImageView(getContext());
                final int dd = i == 0 ? bannerList.size() - 1 : (i > bannerList.size() ? 0 : i - 1);
                ImageLoaderManager.getInstance().displayImage(bannerList.get(dd).getMediaUrl(), iv, R.drawable.ico_pic_fail_defalt);
                iv.setScaleType(ImageView.ScaleType.FIT_XY);
                this.views.add(iv);
                iv.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(context, GeneralWebViewActivity.class);
                        Bundle bundle=new Bundle();
                        bundle.putString("title","");//http://www.soucangwang.com:8090/web/StaticPage/GuideDetail?id=
                        if (bannerList.get(dd).getLinkType().contains("消息")){
                            bundle.putString("content_url","http://www.soucangwang.com:8090/web/StaticPage/GuideDetail?id="+bannerList.get(dd).getLink());
                            intent.putExtras(bundle);
                            if (!bannerList.get(dd).getLink().isEmpty()){
                                context.startActivity(intent);
                            }
                        }else if (bannerList.get(dd).getLinkType().contains("商品")){
                            Intent intent1 = new Intent(context, CommodityDetailActivity.class);
                            Bundle bundle1= new Bundle();
                            bundle1.putString("goodsSaleId",bannerList.get(dd).getLink());
                            intent1.putExtras(bundle1);
                            context.startActivity(intent1);
                        }else {
                            bundle.putString("content_url",bannerList.get(dd).getLink());
                            intent.putExtras(bundle);
                            if (!bannerList.get(dd).getLink().isEmpty()){
                                context.startActivity(intent);
                            }
                        }
                    }
                });
            }
            this.adapter.notifyDataSetChanged();
            setCurrentItem(1); // 首页
        }

        @Override
        public void setOnPageChangeListener(OnPageChangeListener listener) {
            this.listener2 = listener;
        }
    }


}
