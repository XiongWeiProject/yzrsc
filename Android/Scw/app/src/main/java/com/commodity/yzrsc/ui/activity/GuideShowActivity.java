package com.commodity.yzrsc.ui.activity;


import java.util.ArrayList;
import java.util.List;

import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.commodity.yzrsc.R;
import com.commodity.yzrsc.manager.SPKeyManager;
import com.commodity.yzrsc.manager.SPManager;
import com.commodity.yzrsc.ui.BaseActivity;
import com.commodity.yzrsc.ui.activity.user.LoginActivity;
import com.commodity.yzrsc.utils.ScreenUtils;

/**
 * 作者：liyushen on 2016/11/28 15:08
 * 功能：引导页
 */

public class GuideShowActivity extends BaseActivity {
	
	private Button vEnter = null;
	
	private ViewPager vGuideViewpager = null;

	private ViewpagerAdapter mViewpagerAdapter;
	
	private View[] vImageViews;// 点点集合
	/**
	 * banner的点点
	 */
	private LinearLayout vBannerFlagLayout = null;
	/**
	 * 新手引导图
	 */
	private List<Integer> mGuideList = null;
	
	private int mCurPageIndex  = 0;

	@Override
	public boolean getNeedRemove() {
		return false;
	}

	@Override
	protected int getContentView() {
		return R.layout.activity_guide;
	}

	@Override
	protected void initView() {
		vGuideViewpager = (ViewPager)findViewById(R.id.guide_viewpager);
		vEnter = (Button)findViewById(R.id.enter);
		vBannerFlagLayout = (LinearLayout) findViewById(R.id.banner_flag);
		vBannerFlagLayout.setVisibility(View.VISIBLE);
		setOnPageChangeListener();
		mGuideList = new ArrayList<Integer>();
		mGuideList.add(R.drawable.ydy12x);
		mGuideList.add(R.drawable.ydy22x);
		mGuideList.add(R.drawable.ydy32x);
		mGuideList.add(R.drawable.ydy42x);

		mViewpagerAdapter = new ViewpagerAdapter();
		vGuideViewpager.setAdapter(mViewpagerAdapter);
		initBannerInfo();
	}

	@Override
	protected void initListeners() {
		vEnter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (SPManager.instance().getBoolean(SPKeyManager.USERINFO_isLogin)){
					jumpActivity(HomeFragmentActivity.class);
				}else {
					jumpActivity(LoginActivity.class);
				}
				finish();
			}
		});
	}





	/**
	 * viewpage监听
	 */
	private void setOnPageChangeListener() {
		// viewpager改变监听
		vGuideViewpager.setOnPageChangeListener(new OnPageChangeListener() {

			public void onPageSelected(int position) {
				if (mGuideList == null || mGuideList.size() <= position) {
					return;
				}
				setBannerAndFlag(position);
				if (position == mGuideList.size() - 1) {
					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
 						vEnter.setVisibility(View.VISIBLE);
						}
					}, 300);
				} else {
					vEnter.setVisibility(View.GONE);
				}
			}

			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
				
			}

			public void onPageScrollStateChanged(int position) {
			}
		});
	  
	}

	
	
	private void initBannerInfo() {
		if (mGuideList == null) {
			return;
		}
		if (vBannerFlagLayout == null) {
			return;
		}
		int iLen = mGuideList.size();
		vImageViews = new View[iLen];
		vBannerFlagLayout.removeAllViews();
		if (iLen == 0) {
			vBannerFlagLayout.setVisibility(View.GONE);
		} else {
			vBannerFlagLayout.setVisibility(View.VISIBLE);//不显示点点点
		}

		int sc_point = ScreenUtils.dp2px(this,7);
		LinearLayout.LayoutParams layoutParamsPoint=new LinearLayout.LayoutParams(sc_point,sc_point);
		layoutParamsPoint.setMargins(0,0,sc_point,0);

		for (int i = 0; i < iLen; i++) {
			View pointView=new View(this);
			pointView.setLayoutParams(layoutParamsPoint);
			vImageViews[i] = pointView;
			vBannerFlagLayout.addView(pointView);
		}
		setBannerAndFlag(mCurPageIndex);
	}
	
	private void setBannerAndFlag(int index) {
		if (mGuideList == null || mGuideList.size() <= index) {
			return;
		}
		if (vImageViews != null) {
			for (int i = 0; i < vImageViews.length; i++) {
				if (i == index) {
					vImageViews[i].setBackgroundResource(R.drawable.bg_circular_red_point);
				} else {
					vImageViews[i].setBackgroundResource(R.drawable.bg_circular_gray_point);
				}
			}
		}
	}

	/* ViewPager的适配器 */
	private class ViewpagerAdapter extends PagerAdapter {

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public void finishUpdate(View arg0) {
		}

		@Override
		public void finishUpdate(ViewGroup container) {
			super.finishUpdate(container);
		}

		@Override
		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

		@Override
		public int getCount() {
			return mGuideList.size();
		}

		@Override
		public Object instantiateItem(ViewGroup container, final int position) {
			LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
			View view = inflater.inflate(R.layout.view_guide, null);
			ImageView imageView = (ImageView) view.findViewById(R.id.image);
			imageView.setImageResource(mGuideList.get(position));
			container.addView(view, 0);
			imageView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (position==mGuideList.size()-1) {
//						 if (isIntent) {
////							 Intent _intent = new Intent();
////								_intent.setClass(getApplicationContext(), MainActivity.class);
////								startActivity(_intent);
////								finish();
////								overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//						}
					}
					
				}
			});
			return view;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}

	}
	
}