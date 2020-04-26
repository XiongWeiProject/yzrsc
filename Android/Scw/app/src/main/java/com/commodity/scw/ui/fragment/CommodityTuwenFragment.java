package com.commodity.scw.ui.fragment;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.commodity.scw.MainApplication;
import com.commodity.scw.R;
import com.commodity.scw.manager.ImageLoaderManager;
import com.commodity.scw.manager.SPKeyManager;
import com.commodity.scw.model.PicInfo;
import com.commodity.scw.ui.BaseFragment;
import com.commodity.scw.utils.ImageUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;

/**
 * Created by liyushen on 2017/4/8 11:28
 * 商品图文详情
 */
public class CommodityTuwenFragment extends BaseFragment {
    @Bind(R.id.ll_content)
    LinearLayout ll_content;
    JSONObject dataJson;
    @Override
    protected int getContentView() {
        return R.layout.view_commodity_tuwenxiangqing;
    }

    @Override
    protected void initView() {
        ll_content.setMinimumHeight(MainApplication.SCREEN_H-(MainApplication.SCREEN_W/3));
        if (dataJson!=null){
            ll_content.removeAllViews();
            if (!dataJson.optString("video").isEmpty()){
                ImageView itemImgView=getImageView();
                ll_content.addView(itemImgView);
                ImageLoaderManager.getInstance().displayImage(dataJson.optString("videoSnap"),itemImgView,R.drawable.ico_pic_fail_defalt);
                itemImgView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ImageUtil.jumbVideoPlayer(getActivity(), dataJson.optString("video"), dataJson.optString("videoSnap"), dataJson.optString("description"));
                    }
                });
            }

            JSONArray imagesArray=dataJson.optJSONArray("images");
            SPKeyManager.picInfoList.clear();
            if (imagesArray!=null&&imagesArray.length()>0){
                for (int i = 0; i <imagesArray.length() ; i++) {
                    ImageView itemImgView=getImageView();
                    ll_content.addView(itemImgView);
                    try {
                        ImageLoaderManager.getInstance().displayImage(imagesArray.getString(i),itemImgView,R.drawable.ico_pic_fail_defalt);
                        PicInfo picInfo=new PicInfo(imagesArray.getString(i));
                        SPKeyManager.picInfoList.add(picInfo);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    itemImgView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ImageUtil.jumbPicShowActivity(getActivity(),0);
                        }
                    });
                }
            }else {

            }
        }
    }

    @Override
    protected void initListeners() {

    }

    public void setDataView(JSONObject jsonObject){
        dataJson=jsonObject;
        if (this.isVisible()&&dataJson!=null) {
            ll_content.removeAllViews();
            if (!dataJson.optString("video").isEmpty()){
                ImageView itemImgView=getImageView();
                ll_content.addView(itemImgView);
                ImageLoaderManager.getInstance().displayImage(dataJson.optString("videoSnap"),itemImgView,R.drawable.ico_pic_fail_defalt);
                itemImgView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ImageUtil.jumbVideoPlayer(getActivity(), dataJson.optString("video"), dataJson.optString("videoSnap"), dataJson.optString("description"));
                    }
                });
            }

            JSONArray imagesArray=dataJson.optJSONArray("images");
            SPKeyManager.picInfoList.clear();
            if (imagesArray!=null&&imagesArray.length()>0){
                for (int i = 0; i <imagesArray.length() ; i++) {
                    ImageView itemImgView=getImageView();
                    ll_content.addView(itemImgView);
                    try {
                        ImageLoaderManager.getInstance().displayImage(imagesArray.getString(i),itemImgView,R.drawable.ico_pic_fail_defalt);
                        PicInfo picInfo=new PicInfo(imagesArray.getString(i));
                        SPKeyManager.picInfoList.add(picInfo);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    itemImgView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ImageUtil.jumbPicShowActivity(getActivity(),0);
                        }
                    });
                }
            }else {

            }
        }
    }

    LinearLayout.LayoutParams  layoutParamsImg =new LinearLayout.LayoutParams(MainApplication.SCREEN_W, ViewGroup.LayoutParams.WRAP_CONTENT);
    private ImageView getImageView(){
        ImageView imageView=new ImageView(getActivity());
        imageView.setLayoutParams(layoutParamsImg);
        imageView.setAdjustViewBounds(true);
        return imageView;
    }
}
