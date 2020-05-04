package com.commodity.yzrsc.ui.activity.general;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.commodity.yzrsc.MainApplication;
import com.commodity.yzrsc.R;
import com.commodity.yzrsc.manager.ImageLoaderManager;
import com.commodity.yzrsc.ui.BaseActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by yangxuqiang on 2017/6/17.
 * 显示大图片
 */

public class BigPictureActivity extends BaseActivity {
    @Bind(R.id.viewpager_)
    ViewPager viewpager_;
    private List<String> urlList;
    @Override
    protected int getContentView() {
        return R.layout.activity_picture;
    }

    @Override
    protected void initView() {
        Bundle extras = getIntent().getExtras();
        urlList = (List<String>) extras.getSerializable("urls");

        int index = extras.getInt("index");
        int type = extras.getInt("type");
        if (type!=0){
            urlList.remove(urlList.size()-1);
        }
        mAdapter adapter = new mAdapter();
        viewpager_.setAdapter(adapter);
        viewpager_.setCurrentItem(index);
    }

    @Override
    protected void initListeners() {

    }
    @OnClick({R.id.head_back})
    public void click(View v){
        finish();
    }

    class mAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return urlList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View inflate = View.inflate(BigPictureActivity.this, R.layout.activity_bigpicture, null);
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(MainApplication.SCREEN_W,MainApplication.SCREEN_W);
            ImageView imageveiw = (ImageView) inflate.findViewById(R.id.image_big);
            imageveiw.setLayoutParams(params);
            String url = urlList.get(position);
            if(!url.startsWith("http")){
                url="file:///"+url;
            }
            ImageLoaderManager.getInstance().displayImage(url,imageveiw);
            container.addView(inflate);
            imageveiw.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BigPictureActivity.this.finish();
                }
            });
            return inflate;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View)object);
        }
    }
}
