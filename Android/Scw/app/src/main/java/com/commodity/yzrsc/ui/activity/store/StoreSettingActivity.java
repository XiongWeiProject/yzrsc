package com.commodity.yzrsc.ui.activity.store;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.commodity.yzrsc.R;
import com.commodity.yzrsc.http.HttpManager;
import com.commodity.yzrsc.http.HttpMothed;
import com.commodity.yzrsc.http.IRequestConst;
import com.commodity.yzrsc.http.ServiceInfo;
import com.commodity.yzrsc.http.UpLoadUtils;
import com.commodity.yzrsc.manager.ConfigManager;
import com.commodity.yzrsc.manager.Constanct;
import com.commodity.yzrsc.manager.ImageLoaderManager;
import com.commodity.yzrsc.manager.SPManager;
import com.commodity.yzrsc.model.ShareEntity;
import com.commodity.yzrsc.ui.BaseActivity;
import com.commodity.yzrsc.ui.activity.general.GeneralWebViewActivity;
import com.commodity.yzrsc.ui.adapter.PhotoPopupAdapter;
import com.commodity.yzrsc.ui.dialog.CommonDialog;
import com.commodity.yzrsc.ui.dialog.ShareShopDialog;
import com.commodity.yzrsc.ui.widget.imageview.CircleImageView;
import com.commodity.yzrsc.ui.widget.layout.TakePopupWin;
import com.commodity.yzrsc.utils.PhotoUtils;
import com.ta.utdid2.android.utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cn.yohoutils.StringUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 店铺设置
 * Created by yangxuqiang on 2017/3/25.
 */

public class StoreSettingActivity extends BaseActivity {
    @Bind(R.id.backage)
    View backage;
    @Bind(R.id.store_touxiang)
    CircleImageView headImag;//头像
    @Bind(R.id.store_dowloap_shuiyin)
    ImageView store_touxiang;
    @Bind(R.id.store_name)
    TextView storeName;//店铺名称
    @Bind(R.id.store_phone)
    TextView storePhone;//电话
    @Bind(R.id.store_code)
    TextView store_code;
    private boolean isAddYinJi;
    private List<String> data=new ArrayList<>();
    private File file;
    private String url="http://www.soucangwang.com";
    private String avatar;
    private String code;
    private String shopName;

    @Override
    protected int getContentView() {
        return R.layout.activity_stroe_setting;
    }
    public static final String HEADIMG= ConfigManager.ROOT+"headimg" + File.separator;

    @Override
    protected void initView() {
        Bundle extras = getIntent().getExtras();
        url = SPManager.instance().getString(Constanct.propagationUrl);
        avatar = extras.getString("avatar");
        code = extras.getString("code");
        data.add("拍照上传");
        data.add("从手机相册选择");
        data.add("取消");
        file = new File(HEADIMG);
        if(!file.exists()){
            file.mkdirs();
        }
        storePhone.setText(ConfigManager.instance().getUser().getMobile());
        store_code.setText(code);
        shopName = extras.getString("shopName");
        if(StringUtils.isEmpty(shopName)){
            shopName=ConfigManager.instance().getUser().getNickname();
        }
        storeName.setText(shopName);
        //获取头像
        settingHead();
        if(SPManager.instance().getBoolean(Constanct.wather)){//加
            store_touxiang.setImageResource(R.drawable.icon_open);
        }else {
            store_touxiang.setImageResource(R.drawable.icon_close);
        }
    }

    private void settingHead() {
//        File file = new File(HEADIMG);
//        if(file.exists()){
//            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath() + PhotoUtils.headImgPath);
//            headImag.setImageBitmap(bitmap);
//        }
        ImageLoaderManager.getInstance().displayImage(avatar,headImag);
    }

    @Override
    protected void initListeners() {

    }
    @OnClick({R.id.store_setting_proview,R.id.store_setting_back,R.id.store_picture,R.id.setting_store_name,R.id.store_yaoping,R.id.setting_store_phone,R.id.store_hezuo,R.id.store_dowloap_shuiyin,R.id.store_push})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.store_setting_proview://店铺预览
//                Bundle bundle = new Bundle();
//                bundle.putString("url",SPManager.instance().getString(Constanct.propagationUrl));
//                bundle.putString("falg","stroe");
//                jumpActivity(StoreProviewActivity.class,bundle);
                Bundle bundle=new Bundle();
                bundle.putString("title","店铺预览");
                bundle.putString("content_url",SPManager.instance().getString(Constanct.propagationUrl));
                jumpActivity(GeneralWebViewActivity.class,bundle);
                break;
            case R.id.store_picture://店铺头像
                setHeadPictrue();
                break;
            case R.id.setting_store_name://店铺名称
                String string = SPManager.instance().getString(Constanct.shopName);
                Log.e("shrig",string);
                if(string==null||string.trim().length()==0){
                    setShopName();
                }else {
                    tip("店铺名称只能修改一次");
                }

                break;
            case R.id.store_yaoping://邀请
                break;
            case R.id.setting_store_phone://联系电话
                break;
            case R.id.store_hezuo://店铺合作
                jumpActivity(ShopFriendActivity.class);
                break;
            case R.id.store_dowloap_shuiyin://水印
                if(!isAddYinJi){//加
                    store_touxiang.setImageResource(R.drawable.icon_open);
                    SPManager.instance().setBoolean(Constanct.wather,true);
                    isAddYinJi=true;
                }else {
                    store_touxiang.setImageResource(R.drawable.icon_close);
                    SPManager.instance().setBoolean(Constanct.wather,false);
                    isAddYinJi=false;
                }
                break;
            case R.id.store_push://推广
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath() + PhotoUtils.headImgPath);
                if(bitmap==null){
                    bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
                }
                String u = SPManager.instance().getString(Constanct.propagationUrl);
                if(u!=null&&u.length()!=0){
                    url=u;
                }
                ShareEntity shareEntity = new ShareEntity();
                shareEntity.desc=shopName;
                shareEntity.images=new ArrayList<>();
//                shareEntity.bitmap=bitmap;
                shareEntity.imageUrl=avatar;
                shareEntity.shareURL= SPManager.instance().getString(Constanct.propagationUrl);
                ShareShopDialog shareDialog = new ShareShopDialog(mContext,shareEntity);
                shareDialog.show();
//                SharetUtil.showShop(mContext,url,bitmap,shopName,"店铺链接：", new ShareCallBack() {
//                    @Override
//                    public void onResult() {
//
//                    }
//
//                    @Override
//                    public void onError() {
//
//                    }
//
//                    @Override
//                    public void onCancel() {
//
//                    }
//                });
                break;
            case R.id.store_setting_back://返回
                finish();
                break;
        }
    }

    private void setShopName() {
        final CommonDialog commonDialog = new CommonDialog(StoreSettingActivity.this);
        commonDialog.show();
        commonDialog.setContext("店铺名称只能修改一次，\n" +
                "确定要修改吗？");
        commonDialog.setClickCancelListener(new CommonDialog.OnClickCancelListener() {
            @Override
            public void clickCance() {
                commonDialog.dismiss();
            }
        });
        commonDialog.setClickSubmitListener(new CommonDialog.OnClickSubmitListener() {
            @Override
            public void clickSubmit() {
                jumpActivity(SettingShopName.class);
            }
        });

    }

    /**
     * popupwindow
     */
    private void setHeadPictrue() {
        PhotoPopupAdapter photoPopupAdapter = new PhotoPopupAdapter(mContext, data, R.layout.item_photo_button);
        final TakePopupWin takePopupWin = new TakePopupWin(mContext, R.layout.item_photo, R.id.photo_contorl, R.id.photo_listview, photoPopupAdapter);
        backage.setVisibility(View.VISIBLE);
        final WindowManager.LayoutParams attributes = getWindow().getAttributes();
        takePopupWin.showAtLocation(findViewById(R.id.stroe_zuiwai),Gravity.BOTTOM,0,attributes.height-takePopupWin.getHeight());
        //点击时间
        takePopupWin.setOnItemClickListener(new TakePopupWin.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(PhotoUtils.getTempPath()==null){
                    tip("没有内存卡");
                    return;
                }
                switch (position){
                    case 0://拍照
                        PhotoUtils.openCamera(StoreSettingActivity.this,CAMERA_CODE,file,PhotoUtils.tempPath);
                        break;
                    case 1://相册
                        PhotoUtils.openAlbum(StoreSettingActivity.this,IMAGE_REQUEST_CODE);
                        break;
                }
            }
        });
        takePopupWin.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backage.setVisibility(View.GONE);
            }
        });

    }
    public static final int IMAGE_REQUEST_CODE = 0x102;
    public static final int CAMERA_CODE = 0x103;
    public static final int CROP_CODE = 0x104;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(data==null){
            if(requestCode==CAMERA_CODE) {
                Uri headimgUri=Uri.fromFile(new File(file,PhotoUtils.tempPath));
                PhotoUtils.cropImageUri(this,headimgUri,1,1,600,600,CROP_CODE,file,PhotoUtils.headImgPath);
            }
        }else if((requestCode==IMAGE_REQUEST_CODE)){//相册
            Uri imgUri = data.getData();
            if(imgUri!=null){
                PhotoUtils.cropImageUri(this,imgUri,1,1,600,600,CROP_CODE,file,PhotoUtils.headImgPath);
            }
        }else if(requestCode==CROP_CODE){
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath() + PhotoUtils.headImgPath);
            headImag.setImageBitmap(bitmap);
            //上传头像
            upLoadHead();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void upLoadHead() {
        File headFile = new File(this.file, PhotoUtils.headImgPath);
        if(!headFile.exists()){
            tip("请从新拍摄");
            return;
        }
        customLoadding.setTip("正在上传头像...");
        customLoadding.show();
        MultipartBody.Builder multiparBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        multiparBody.addFormDataPart("head",headFile.getName(), RequestBody.create(MediaType.parse("image/*"),headFile));
        UpLoadUtils.instance().uploadPicture(IRequestConst.RequestMethod.UploadAvatar, multiparBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("onFailure:",e.getMessage());
                tip(e.getMessage());
                customLoadding.dismiss();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                customLoadding.dismiss();
                String string = response.body().string();
                Log.e("response:",string);

                JSONObject jsob= null;
                try {
                    jsob = new JSONObject(string);
                    if (jsob!=null&&jsob.optBoolean("success")){
                        if(jsob.optBoolean("data")){
                            Looper.prepare();
                            tip("头像上传成功");
                            Looper.loop();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        sendRequest(0,"");
    }

    @Override
    public void sendRequest(int tag, Object... params) {
        super.sendRequest(tag, params);
        if(tag==0){
            new HttpManager(tag, HttpMothed.GET, IRequestConst.RequestMethod.StoreInfo,null,this).request();
        }
    }

    @Override
    public void OnSuccessResponse(int tag, ServiceInfo resultInfo) {
        super.OnSuccessResponse(tag, resultInfo);
        JSONObject response = (JSONObject) resultInfo.getResponse();
        if(response.optBoolean("success")){
            JSONObject data = response.optJSONObject("data");
            if(!StringUtil.isEmpty(data.optString("name"))){
                storeName.setText(data.optString("name"));
            }
        }else {
            tip(response.optString("msg"));
        }
    }

    @Override
    public void OnFailedResponse(int tag, String code, String msg) {
        super.OnFailedResponse(tag, code, msg);
        tip(msg);
    }
}
