package com.commodity.yzrsc.ui.activity.store;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.commodity.yzrsc.R;
import com.commodity.yzrsc.http.IRequestConst;
import com.commodity.yzrsc.http.UpLoadUtils;
import com.commodity.yzrsc.manager.ConfigManager;
import com.commodity.yzrsc.manager.Constanct;
import com.commodity.yzrsc.manager.SPKeyManager;
import com.commodity.yzrsc.manager.SPManager;
import com.commodity.yzrsc.ui.BaseActivity;
import com.commodity.yzrsc.ui.adapter.PhotoPopupAdapter;
import com.commodity.yzrsc.ui.adapter.UpLoadPictureAdapter;
import com.commodity.yzrsc.ui.dialog.RenzhengSuccessDialog;
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
import cn.yohoutils.StringUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 认证
 * Created by yangxuqiang on 2017/4/7.
 */

public class RenzhengActivity extends BaseActivity {
    @Bind(R.id.head_title)
    TextView title;
    @Bind(R.id.ren_address)
    EditText ren_address;
    @Bind(R.id.ren_name)
    EditText ren_name;
    @Bind(R.id.ren_person)
    EditText ren_person;
    @Bind(R.id.ren_grid)
    MyGridView ren_grid;
    @Bind(R.id.bg)
    View bg;

    public final int openCamera=1;
    public final int openAblum=2;
    public final int CROP_CODE=3;
    public String imgName="";
    private static final String RENZHENG= ConfigManager.ROOT+"renzheng" + File.separator;
    private static final String RENZHENG_DELETE= ConfigManager.ROOT+"renzheng";
    public MediaType MEDIA_TYPE_PNG=MediaType.parse("image/*");

    private  List<String> pictrueData=new ArrayList<>();
    private List<String> data=new ArrayList<>();
    private File savefile;
    private UpLoadPictureAdapter uploadPictureAdapter;

    @Override
    protected int getContentView() {
        return R.layout.activity_renzheng;
    }

    @Override
    protected void initView() {
        title.setText("认证资料");
        SPKeyManager.uploadmax=3;
        data.add("拍照上传");
        data.add("从手机相册选择");
        data.add("取消");
        FileUtil.deleteFolderRecursively(new File(RENZHENG_DELETE));
        initData();
        uploadPictureAdapter = new UpLoadPictureAdapter(mContext, pictrueData, R.layout.item_upload);
        ren_grid.setAdapter(uploadPictureAdapter);
    }

    private void initData() {
        savefile = new File(RENZHENG);
        if(!savefile.exists()){
            savefile.mkdirs();
        }
        File[] files = savefile.listFiles();
        for(File f:files){
            pictrueData.add(f.getAbsolutePath());
        }
        pictrueData.add("add");
    }

    @Override
    protected void initListeners() {
        uploadPictureAdapter.addPictureListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHeadPictrue();
            }
        });

    }
    @OnClick({R.id.ren_submit,R.id.head_back})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.head_back:
                finish();
                break;
            case R.id.ren_submit:
                String address = ren_address.getText().toString().trim();
                String name = ren_name.getText().toString().trim();
                String person = ren_person.getText().toString().trim();
                if(StringUtil.isEmpty(name)){
                    tip("请输入姓名");
                }else if(StringUtil.isEmpty(person)){
                    tip("请输入身份证号");
                }else if(StringUtil.isEmpty(address)){
                    tip("请输入地址");
                }else {//提交
                    if(pictrueData.size()<3){
                        tip("请上传身份证");
                        return;
                    }
                    customLoadding.setTip("上传中...");
                    customLoadding.show();
                    MultipartBody.Builder multiparBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
                    multiparBody.addFormDataPart("realName",name);
                    multiparBody.addFormDataPart("idCode",person);
                    multiparBody.addFormDataPart("domicile",address);
                    for (int i=0;i<pictrueData.size()-1;i++){
                        File file = new File(pictrueData.get(i));
                        multiparBody.addFormDataPart("image",file.getName(), RequestBody.create(MEDIA_TYPE_PNG, file));
                    }
                    UpLoadUtils.instance().uploadPicture(IRequestConst.RequestMethod.ShopAuthentication,multiparBody,new Callback(){
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
                                if (jsob!=null&&jsob.optBoolean("data")){
                                    //提交成功
                                    Looper.prepare();
                                    RenzhengSuccessDialog renzhengSuccessDialog = new RenzhengSuccessDialog(RenzhengActivity.this);
                                    renzhengSuccessDialog.show();
                                    renzhengSuccessDialog.setOnclickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
//                                            SPManager.put(RenzhengActivity.this, Constanct.RENZHENG,true);
                                            SPManager.instance().setBoolean(Constanct.RENZHENG,true);
                                            finish();
                                        }
                                    });
                                    renzhengSuccessDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                                        @Override
                                        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                                            switch (keyCode) {
                                                case KeyEvent.KEYCODE_BACK:
                                                    return true;
                                                default:
                                                    break;
                                            }
                                            return false;
                                        }
                                    });
                                    Looper.loop();
                                }else {
                                    tip(jsob.optString("msg"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                tip("json解析异常");
                            }

                        }
                    });

                }
                break;
        }
    }

    /**
     * popupwindow
     */
    private void setHeadPictrue() {
        PhotoPopupAdapter photoPopupAdapter = new PhotoPopupAdapter(mContext, data, R.layout.item_photo_button);
        final TakePopupWin takePopupWin = new TakePopupWin(mContext, R.layout.item_photo, R.id.photo_contorl, R.id.photo_listview, photoPopupAdapter);
        bg.setVisibility(View.VISIBLE);
        final WindowManager.LayoutParams attributes = getWindow().getAttributes();
        takePopupWin.showAtLocation(findViewById(R.id.ren_bg), Gravity.BOTTOM,0,attributes.height-takePopupWin.getHeight());
        //点击时间
        takePopupWin.setOnItemClickListener(new TakePopupWin.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0://拍照
                        PhotoUtils.openCamera(RenzhengActivity.this,openCamera,savefile,PhotoUtils.tempPath);
                        break;
                    case 1://相册
                        PhotoUtils.openAlbum(RenzhengActivity.this,openAblum);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==openCamera&&resultCode==Activity.RESULT_OK){
            if(data==null){
                imgName= UUID.randomUUID().toString()+".png";
                File file = new File(savefile, PhotoUtils.tempPath);
                PhotoUtils.cropImageUri(this, Uri.fromFile(file),1,1,600,600,CROP_CODE,savefile,imgName);

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
        }if(requestCode==CROP_CODE&&resultCode==Activity.RESULT_OK){
            pictrueData.remove(pictrueData.size()-1);
            pictrueData.add(RENZHENG+imgName);
            pictrueData.add("add");
            uploadPictureAdapter.notifyDataSetChanged();
            deleteTemp();
        }

    }

    private void deleteTemp() {
        File tempfile = new File(savefile, PhotoUtils.tempPath);
        if(tempfile.exists()){
            tempfile.delete();
        }
    }

    @Override
    public void sendRequest(int tag, Object... params) {
        super.sendRequest(tag, params);
        if(tag==0){

        }
    }

    @Override
    protected void onDestroy() {
        FileUtil.deleteFolderRecursively(new File(RENZHENG_DELETE));
        super.onDestroy();
    }

}
