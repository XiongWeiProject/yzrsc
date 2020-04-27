package com.commodity.yzrsc.ui.activity.personalcenter.orde;

import android.content.Intent;
import android.net.Uri;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.commodity.yzrsc.R;
import com.commodity.yzrsc.http.IRequestConst;
import com.commodity.yzrsc.http.UpLoadUtils;
import com.commodity.yzrsc.manager.ConfigManager;
import com.commodity.yzrsc.manager.Constanct;
import com.commodity.yzrsc.manager.SPKeyManager;
import com.commodity.yzrsc.ui.BaseActivity;
import com.commodity.yzrsc.ui.adapter.PhotoPopupAdapter;
import com.commodity.yzrsc.ui.adapter.UpLoadPictureAdapter;
import com.commodity.yzrsc.ui.widget.layout.TakePopupWin;
import com.commodity.yzrsc.ui.widget.specialview.MyGridView;
import com.commodity.yzrsc.utils.FileUtil;
import com.commodity.yzrsc.utils.PhotoUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.commodity.yzrsc.ui.activity.store.StoreSettingActivity.CROP_CODE;

/**
 * 申请退款
 * Created by yangxuqiang on 2017/4/4.
 */

public class BackMoneyActivity extends BaseActivity {
    @Bind(R.id.head_title)
    TextView title;
    @Bind(R.id.backmoney_serion)
    LinearLayout backmoney_serion;//退款原因
    @Bind(R.id.backmoney_edite)
    EditText backmoney_edite;
    @Bind(R.id.backmoney_serion_text)
    TextView backmoney_serion_text;
    @Bind(R.id.gridview_submit)
    MyGridView gridview_submit;
    @Bind(R.id.bg)
    View bg;
    private String orderId;
    private String description;
    private String reason="有色差";
    private UpLoadPictureAdapter uploadPictureAdapter;
    private static final String REFUND= ConfigManager.ROOT+"retund" + File.separator;
    private List<String> pictrueData=new ArrayList<>();
    private File savefile;
    private String imgName;
    private int openCamera=0x222;
    private int openAblum=0x223;
    private List<String> data=new ArrayList<>();
    boolean isReson=false;
    private String url;
    private int flag;

    @Override
    protected int getContentView() {
        return R.layout.activity_backmoney;
    }

    @Override
    protected void initView() {
        title.setText("退款详情");
        delete(ConfigManager.IMG_PATH);
        orderId = getIntent().getExtras().getString("ordeId");
        initData();
        uploadPictureAdapter = new UpLoadPictureAdapter(mContext, pictrueData, R.layout.item_upload);
        gridview_submit.setAdapter(uploadPictureAdapter);

        SPKeyManager.uploadmax=4;

        flag = getIntent().getExtras().getInt("flag");
        switch (flag){
            case 0:
                url = IRequestConst.RequestMethod.AskForOrderRefundment;//退款
                break;
            case 1:
                url = IRequestConst.RequestMethod.LeaveArbitrationMessage;//卖家
                break;
            case 2:
                url = IRequestConst.RequestMethod.MemberorderLeaveArbitrationMessage;;//买家
                break;
        }

    }
    private void initData() {
        savefile = new File(REFUND);
        if(!savefile.exists()){
            savefile.mkdirs();
        }
//        File[] files = savefile.listFiles();
//        for(File f:files){
//            pictrueData.add(f.getAbsolutePath());
//        }
        pictrueData.add("add");
    }

    @Override
    protected void initListeners() {
        uploadPictureAdapter.addPictureListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isReson=false;
                data.clear();
                data.add("拍照上传");
                data.add("从手机相册选择");
                data.add("取消");
                setHeadPictrue();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==openCamera){
            if(data==null){
                Uri uri = data.getData();
                if(uri!=null){
                    imgName= UUID.randomUUID().toString()+".png";
                    File file = new File(savefile, PhotoUtils.tempPath);
                    PhotoUtils.cropImageUri(this, Uri.fromFile(file),1,1,600,600,CROP_CODE,savefile,imgName);
                }

            }else {
                tip("请从新拍照");
            }
        }else if(requestCode==openAblum){
            if(data!=null){
                Uri uri = data.getData();
                if(uri!=null){
                    imgName= UUID.randomUUID().toString()+".png";
                    PhotoUtils.cropImageUri(this,data.getData(),1,1,600,600,CROP_CODE,savefile,imgName);
                }

            }else {
                tip("请重新选择");
            }
        }if(requestCode==CROP_CODE){
            pictrueData.remove(pictrueData.size()-1);
            pictrueData.add(REFUND+imgName);
            pictrueData.add("add");
            uploadPictureAdapter.notifyDataSetChanged();
        }

    }
    @OnClick({R.id.backmoney_button,R.id.head_back,R.id.backmoney_serion})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.head_back:
                finish();
                break;
            case R.id.backmoney_button://提交申请
                description = backmoney_edite.getText().toString();
                if(reason==null){
                    tip("请输入原因");
                    return;
                }else {
                    submit();
                }
                break;
            case R.id.backmoney_serion://退款原因
                data.clear();
                isReson=true;
                data.add("有色差");
                data.add("有瑕疵或裂纹");
                data.add("尺寸不合适");
                data.add("卖家无货");
                data.add("其他原因");
                setHeadPictrue();
                break;
        }
    }

    private void submit() {
        customLoadding.setTip("提交中...");
        customLoadding.show();
        MultipartBody.Builder multiparBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        multiparBody.addFormDataPart("orderId",orderId);
        if(flag==0){
            multiparBody.addFormDataPart("reason",reason);
            multiparBody.addFormDataPart("description",description);
        }else {
            multiparBody.addFormDataPart("message",description);
        }

        pictrueData.remove(pictrueData.size()-1);
        for (String s:pictrueData){
            File file = new File(s);
            multiparBody.addFormDataPart("image",file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
        }
        UpLoadUtils.instance().uploadPicture(url, multiparBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                tip(e.getMessage());
                Log.e("failure:****",e.getMessage());
                if(customLoadding.isShowing()){
                    customLoadding.dismiss();
                }
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseStr=response.body().string();
                Log.e("onResponse:****",responseStr);
                if(customLoadding.isShowing()){
                    customLoadding.dismiss();
                }
                JSONObject jsob= null;
                try {
                    jsob = new JSONObject(responseStr);
                    if (jsob!=null&&jsob.optBoolean("success")){
                        //提交成功
                        Looper.prepare();
                        tip("提交成功");
                        delete(ConfigManager.IMG_PATH);
                        SPKeyManager.curDetailMyOrdeEntity.setState(Constanct.applyRefund);
                        finish();
                        Looper.loop();
                    }else {
                        //提交失败
                        Looper.prepare();
                        tip(jsob.optString("msg"));
                        Looper.loop();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    tip("json解析异常");
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        FileUtil.deleteFolderRecursively(new File(REFUND));
        super.onDestroy();
    }
    /**
     * popupwindow
     */
    private void setHeadPictrue() {
        PhotoPopupAdapter photoPopupAdapter = new PhotoPopupAdapter(mContext, data, R.layout.item_photo_button);
        final TakePopupWin takePopupWin = new TakePopupWin(mContext, R.layout.item_photo, R.id.photo_contorl, R.id.photo_listview, photoPopupAdapter);
        bg.setVisibility(View.VISIBLE);
        final WindowManager.LayoutParams attributes = getWindow().getAttributes();
        takePopupWin.showAtLocation(findViewById(R.id.bk_rg), Gravity.BOTTOM,0,attributes.height-takePopupWin.getHeight());
        //点击时间
        takePopupWin.setOnItemClickListener(new TakePopupWin.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0://拍照
                        if(isReson){
                            initSerson(position);
                        }else {
                            PhotoUtils.openCamera(BackMoneyActivity.this,openCamera,savefile,PhotoUtils.tempPath);
                        }
                        break;
                    case 1://相册
                        if(isReson){
                            initSerson(position);
                        }else {
                            PhotoUtils.openAlbum(BackMoneyActivity.this,openAblum);
                        }

                        break;
                    case 2://
                        if(isReson){
                            initSerson(position);
                        }
                        break;
                    case 3://
                        initSerson(position);
                        break;
                    case 4://
                        initSerson(position);
                        break;
                }
            }
        });
        takePopupWin.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                bg.setVisibility(View.GONE);
            }
        });

    }

    private void initSerson(int position) {
        reason=data.get(position);
        backmoney_serion_text.setText(reason);
    }
    //    @Override
//    public void sendRequest(int tag, Object... params) {
//        super.sendRequest(tag, params);
//        customLoadding.show();
//        if(tag==0){
//            HashMap<String, String> map = new HashMap<>();
//            map.put("orderId",ordeId);
//            map.put("reason",reason);
//            map.put("description",description);
//            new HttpManager(tag, HttpMothed.GET, IRequestConst.RequestMethod.BuyGetOrderDetail,map,this).request();
//        }
//    }
//
//    @Override
//    public void OnSuccessResponse(int tag, ServiceInfo resultInfo) {
//        super.OnSuccessResponse(tag, resultInfo);
//        JSONObject response = (JSONObject) resultInfo.getResponse();
//        if(response.optBoolean("success")){
//            if(tag==0){
//                JSONObject data = response.optJSONObject("data");
//
//
//            }
//        }
//
//    }
//
//    @Override
//    public void OnFailedResponse(int tag, String code, String msg) {
//        super.OnFailedResponse(tag, code, msg);
//        tip(msg);
//    }
    public boolean delete(String path) {
        File file = new File(path);
        if (file.isDirectory()) {
            // delete(file.)
            File[] files = file.listFiles();

            for (File f : files) {
                delete(f.getPath());
            }

        } else if (file.exists() && file.isFile()) {
            Log.v("FileUtils", "删除文件 -> " + file.getPath());
            file.delete();
        } else if (!file.exists()) {
            Log.v("FileUtils", "该目录下无任何文件(或 该文件不存在) -> " + file.getPath());
        }

        return true;
    }
}
