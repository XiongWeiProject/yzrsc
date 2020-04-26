package com.commodity.scw.ui.activity.general;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView.ScaleType;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.commodity.scw.R;
import com.commodity.scw.http.BaseHttpManager;
import com.commodity.scw.http.HttpManageNew;
import com.commodity.scw.http.HttpManager;
import com.commodity.scw.http.HttpMothed;
import com.commodity.scw.http.IRequestConst;
import com.commodity.scw.http.ServiceInfo;
import com.commodity.scw.manager.ConfigManager;
import com.commodity.scw.manager.Constanct;
import com.commodity.scw.manager.ImageLoaderManager;
import com.commodity.scw.manager.ImageLoadingListener;
import com.commodity.scw.manager.SPKeyManager;
import com.commodity.scw.manager.SPManager;
import com.commodity.scw.manager.SystemPermissUtil;
import com.commodity.scw.model.PicInfo;
import com.commodity.scw.ui.BaseActivity;
import com.commodity.scw.ui.dialog.CommonDialog;
import com.commodity.scw.ui.dialog.CustomLoadding;
import com.commodity.scw.ui.widget.photoview.PhotoView;
import com.commodity.scw.ui.widget.photoview.PhotoViewAttacher;
import com.commodity.scw.ui.widget.viewpage.CustomViewPager;
import com.commodity.scw.utils.CustomToast;
import com.commodity.scw.utils.ImageUtil;
import com.nostra13.universalimageloader.core.assist.FailReason;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.yohoutils.SafetyUtil;

/**
 * 单张图片展示
 *
 * @author 李玉深
 */
public class PicShowActivity extends BaseActivity {

    private ViewpagerAdapter mAdapter = null;
    private CustomViewPager vPictures = null;
    private TextView vDesc = null;
    // private List<String> mPicUrls = null;// 所有图片路径
    private int mIndex = 0;// 图片当前位置
    // private int mTotal = 0;// 总数
    private ImageButton back;

    @Override
    public boolean getNeedRemove() {
        return false;
    }

    @Override
    protected void initView() {
        vPictures = (CustomViewPager) findViewById(R.id.pic_viewpager);
        vDesc = (TextView) findViewById(R.id.pic_desc);
        back = (ImageButton) findViewById(R.id.back);
        if (getIntent().getExtras()!=null&&getIntent().getExtras().containsKey("mIndex")){
            mIndex=getIntent().getExtras().getInt("mIndex");
        }
        init();
    }

    @Override
    protected void initListeners() {
        vPictures.setOnPageChangeListener(new CustomViewPager.OnPageChangeListener() {

            public void onPageSelected(int index) {
                mIndex = index;
                setDescShow();
            }

            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            public void onPageScrollStateChanged(int arg0) {
            }
        });
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                noAnimFinish();
            }
        });
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_picshow;
    }


    protected void init() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setDescShow();
        mAdapter = new ViewpagerAdapter();
        vPictures.setAdapter(mAdapter);
        vPictures.setCurrentItem(mIndex);
        vPictures.setOffscreenPageLimit(0);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setDescShow() {
        vDesc.setVisibility(View.VISIBLE);
        vDesc.setText((mIndex + 1) + "/" + SPKeyManager.picInfoList.size());
    }

    /**
     * 获取商品信息
     *
     * @author
     */

    class ViewpagerAdapter extends PagerAdapter {

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
            return super.getItemPosition(object);
        }

        @Override
        public int getCount() {
            if (SPKeyManager.picInfoList == null) {
                return 0;
            }
            return SPKeyManager.picInfoList.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            // PhotoView imageView = new PhotoView(getApplicationContext());

            LayoutInflater inflater = LayoutInflater
                    .from(getApplicationContext());

            View view = inflater.inflate(R.layout.item_single_picshow, null);

            final PhotoView imageView = (PhotoView) view.findViewById(R.id.photoshow);
            final ProgressBar progressBar = (ProgressBar) view
                    .findViewById(R.id.progress);
            imageView.setProgress(100);
            ImageButton saveBtn = (ImageButton) view.findViewById(R.id.picsave);
            imageView.setScaleType(ScaleType.FIT_CENTER);
            PicInfo info = SPKeyManager.picInfoList.get(position);
            String imgUrl = null;
            if (info != null) {
                imgUrl = info.getImg();
            }
            if (imgUrl != null
                    && !(imgUrl.startsWith("http:") || imgUrl
                    .startsWith("https:"))) {
                imgUrl = "file:///" + imgUrl;
                saveBtn.setVisibility(View.GONE);
            }
            final String imgPath = imgUrl;
            Log.e("url", "full image mPicList:" + imgPath);
            ImageLoaderManager.getInstance().displayImage(imgPath, imageView,
                    new ImageLoadingListener() {

                        @Override
                        public void onLoadingStarted(String imageUri, View view) {
                            progressBar.setVisibility(View.VISIBLE);
                            //  imageView.setProgress(0);
                            super.onLoadingStarted(imageUri, view);
                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view,
                                                    FailReason failReason) {
                            tip("加载失败");
                            super.onLoadingFailed(imageUri, view, failReason);
                        }

                        @Override
                        public void onLoadingComplete(String imageUri,
                                                      View view, Bitmap loadedImage) {
                            super.onLoadingComplete(imageUri, view, loadedImage);
                            progressBar.setVisibility(View.GONE);
                            //  imageView.setProgress(100);
                        }

                        @Override
                        public void onProgressUpdate(String imageUri,
                                                     View view, int current, int total) {
                            super.onProgressUpdate(imageUri, view, current,
                                    total);
                           /* float progress=(current*1.00f/total*1.00f);
                            Log.e("url", "onProgressUpdate: " + progress);
                            int pro= (int) (progress*100);
                            if(pro%10==0&&pro!=100){
                                imageView.setProgress(pro);
                            }*/
                        }
                    });
            container.addView(view, 0);

            imageView.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
                @Override
                public void onViewTap(View view, float x, float y) {
                    noAnimFinish();
                }
            });

            imageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    CommonDialog commonDialog = new CommonDialog(PicShowActivity.this);
                    commonDialog.show();
                    commonDialog.setContext("保存图片？");
                    commonDialog.setClickSubmitListener(new CommonDialog.OnClickSubmitListener() {
                        @Override
                        public void clickSubmit() {
                            if (!SystemPermissUtil.instance().IsHasStoragePermiss(mContext)) {
                                SystemPermissUtil.instance().MakeStorage(mContext);
                                return;
                            }
                            if (SPManager.instance().getBoolean(Constanct.wather)){//需要水印
                                getWaterImgpath(imgPath);
                            }else {//不需要水印
                                downLoad(imgPath);
                            }
                        }
                    });
                    return false;
                }
            });

            // 保存图片到本地
            saveBtn.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    String fileName = SafetyUtil.encryptStringToMd5(imgPath,
                            "32");
                    mPath = ConfigManager.DOWNLOAD_PATH + "jhl_" + fileName
                            + ".jpg";
                    if (mIsImageLoading) {
                        tip("下载中,请稍候...");
                        return;
                    }

                    new DownLoadImage()
                            .execute(new String[]{imgPath, mPath});
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

    private String mPath = null;
    private boolean mIsImageLoading = false;

    /**
     * 下载图片
     */
    class DownLoadImage extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            mIsImageLoading = true;
        }

        @Override
        protected Void doInBackground(String... arg0) {
            ImageUtil.downLoadImage(arg0[0], arg0[1]);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            mIsImageLoading = false;
            tip("图片保存到：" + mPath);
            ImageUtil.scanPhotos(mPath, getApplicationContext());
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            noAnimFinish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    String imgIndex = "";
    //获取水印图片
    private String getWaterImgpath(String imgpath){
        imgIndex = imgpath;
        String url = IRequestConst.RequestMethod.GetWaterMarkImages;
        Map<String, Object> parmMap = new HashMap<String, Object>();
        parmMap.put("images",new JSONArray().put(imgpath));
        HttpManageNew httpManager = new HttpManageNew(0, HttpMothed.POST, url, parmMap, new BaseHttpManager.IRequestListener() {
            @Override
            public void onPreExecute(int Tag) {
            }
            @Override
            public void onSuccess(int Tag, ServiceInfo result) {
                JSONObject resultJson= (JSONObject) result.getResponse();
                if (resultJson!=null&&resultJson.optBoolean("success")){
                    JSONArray arrayImg=resultJson.optJSONArray("data");
                    if (arrayImg!=null&&arrayImg.length()>0){
                        try {
                            imgIndex=arrayImg.getString(0);
                            downLoad(imgIndex);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }else {
                    if (resultJson!=null&&!resultJson.optBoolean("success")){
                       // CustomToast.showToast(resultJson.optString("msg"));
                        downLoad(imgIndex);
                    }
                }
            }
            @Override
            public void onError(int Tag, String code, String msg) {
                downLoad(imgIndex);
            }

            @Override
            public void OnTimeOut(int Tag, boolean isShowTip) {
                downLoad(imgIndex);
            }

            @Override
            public void OnNetError(int Tag, boolean isShowTip) {
                customLoadding.dismiss();
            }
        });
        httpManager.request();
        return imgIndex;
    }

    private void downLoad(String imgPath){
        String fileName = SafetyUtil.encryptStringToMd5(imgPath,
                "32");
        mPath = ConfigManager.DOWNLOAD_PATH + "jhl_" + fileName
                + ".jpg";
        if (mIsImageLoading) {
            tip("下载中,请稍候...");
            return;
        }
        new DownLoadImage()
                .execute(new String[]{imgPath, mPath});
    }

}