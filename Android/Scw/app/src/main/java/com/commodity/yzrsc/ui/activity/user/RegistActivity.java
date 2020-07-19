package com.commodity.yzrsc.ui.activity.user;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.commodity.yzrsc.MainApplication;
import com.commodity.yzrsc.R;
import com.commodity.yzrsc.http.HttpManager;
import com.commodity.yzrsc.http.HttpMothed;
import com.commodity.yzrsc.http.IRequestConst;
import com.commodity.yzrsc.http.ServiceInfo;
import com.commodity.yzrsc.http.UpLoadUtils;
import com.commodity.yzrsc.manager.ConfigManager;
import com.commodity.yzrsc.manager.ImageLoaderManager;
import com.commodity.yzrsc.manager.SPKeyManager;
import com.commodity.yzrsc.manager.SPManager;
import com.commodity.yzrsc.ui.BaseActivity;
import com.commodity.yzrsc.ui.activity.HomeFragmentActivity;
import com.commodity.yzrsc.ui.activity.general.GeneralWebViewActivity;
import com.commodity.yzrsc.ui.adapter.PhotoPopupAdapter;
import com.commodity.yzrsc.ui.widget.imageview.CircleImageView;
import com.commodity.yzrsc.ui.widget.layout.TakePopupWin;
import com.commodity.yzrsc.utils.KeyBoardUtils;
import com.commodity.yzrsc.utils.PhotoUtils;
import com.yixia.camera.util.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import butterknife.Bind;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by liyushen on 2017/3/21 20:56
 * 注册界面
 */
public class RegistActivity extends BaseActivity {
    @Bind(R.id.tv_title)
    TextView tv_title;
    @Bind(R.id.btn_next)
    Button btn_next;
    @Bind(R.id.tv_rightbtn)
    TextView tv_rightbtn;
    @Bind(R.id.ll_regist1)
    LinearLayout ll_regist1;
    @Bind(R.id.ll_regist2)
    LinearLayout ll_regist2;
    @Bind(R.id.ll_regist3)
    LinearLayout ll_regist3;
    @Bind(R.id.et_phone)
    EditText et_phone;
    @Bind(R.id.checkbox)
    CheckBox checkbox;
    @Bind(R.id.tv_btn_agree)
    TextView tv_btn_agree;
    @Bind(R.id.tv_phoneinfo)
    TextView tv_phoneinfo;
    @Bind(R.id.et_yanzhengma)
    EditText et_yanzhengma;
    @Bind(R.id.tv_yanzhengma)
    TextView tv_yanzhengma;
    @Bind(R.id.et_pwd)
    EditText et_pwd;
    @Bind(R.id.im_header)
    CircleImageView im_header;
    @Bind(R.id.et_name)
    EditText et_name;
    @Bind(R.id.et_yaoqingma)
    EditText et_yaoqingma;
    private JSONObject otherLoginInfoJson;
    private boolean isOtherLogin = false;
    public int openCameraRequestCode = 1;
    public int openAblumRequestCode = 2;
    private int openCropRequestCode = 4;
    private String prctureNmae = "";
    private boolean isJumbHome=false;

    @Override
    protected int getContentView() {
        return R.layout.activity_regist;
    }

    @Override
    protected void initView() {
        if (getIntent() != null && getIntent().getExtras() != null) {
            if (getIntent().getExtras().containsKey("otherLoginInfo")) {
                String otherLoginInfo = getIntent().getExtras().getString("otherLoginInfo");
                if (!otherLoginInfo.isEmpty()) {
                    try {
                        otherLoginInfoJson = new JSONObject(otherLoginInfo);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        if (otherLoginInfoJson != null) {
            tv_title.setText("绑定信息");
            isOtherLogin = true;
        }
        setShowStatus(1);
    }

    @Override
    protected void initListeners() {
        tv_rightbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isJumbHome=true;
                sendRequest(5,"");
            }
        });
        tv_yanzhengma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRequest(1, "");
            }
        });
        tv_btn_agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putString("title","用户协议");
                bundle.putString("content_url", SPManager.instance().getString(SPKeyManager.APP_license_URL));
                jumpActivity(GeneralWebViewActivity.class,bundle);
            }
        });
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KeyBoardUtils.hindKeyBord(view);
                if (btn_next.getText().toString().equals("获取验证码")) {
                    if (et_phone.getText().toString().isEmpty() || et_phone.getText().toString().length() < 11) {
                        tip("请输入正确的手机号！");
                        return;
                    }
                    if (!checkbox.isChecked()) {
                        tip("请同意易州人商城用户协议！");
                        return;
                    }
                    if (isOtherLogin) {
                        sendRequest(3, "");
                    } else {
                        sendRequest(1, "");
                    }
                } else if (btn_next.getText().toString().equals("下一步")) {
                    if (et_yanzhengma.getText().toString().isEmpty()) {
                        tip("请输入验证码！");
                        return;
                    }
                    if (et_pwd.getText().toString().isEmpty()) {
                        tip("请输入密码！");
                        return;
                    }
                    if (isOtherLogin) {
                        sendRequest(4, "");
                    } else {
                        sendRequest(2, "");
                    }
                } else if (btn_next.getText().toString().equals("完成")) {
                    final String name = et_name.getText().toString().trim();
                    final String yaoqingma = et_yaoqingma.getText().toString().trim();
                    if (StringUtils.isEmpty(name)) {
                        tip("请输入昵称");
                        return;
                    }
                    if (StringUtils.isEmpty(prctureNmae)) {
                        tip("头像不能为空");
                        return;
                    }
                    startUpload(name,yaoqingma);
                    // sendRequest(6,"");

                }
            }
        });

        im_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KeyBoardUtils.hindKeyBord(view);
                List<String> moreData = new ArrayList<String>();
                moreData.add("拍照上传");
                moreData.add("从手机相册选择");
                moreData.add("取消");
                setHeadPictrue(0, moreData);
            }
        });
    }

    @Override
    public void sendRequest(int tag, Object... params) {
        super.sendRequest(tag, params);
        if (customLoadding!=null&&!customLoadding.isShowing()){
            customLoadding.show();
        }
        if (tag == 1) {//获取验证码
            Map<String, String> parmMap = new HashMap<String, String>();
            parmMap.put("mobile", et_phone.getText().toString());
            HttpManager httpManager = new HttpManager(tag, HttpMothed.POST,
                    IRequestConst.RequestMethod.GetVaildCode, parmMap, this);
            httpManager.request();
        } else if (tag == 2) {//注册
            Map<String, String> parmMap = new HashMap<String, String>();
            parmMap.put("mobile", et_phone.getText().toString());
            parmMap.put("vaildCode", et_yanzhengma.getText().toString());
            parmMap.put("passWord", et_pwd.getText().toString());
            parmMap.put("inviter_Id", "0");
            parmMap.put("platformType", "2");
            HttpManager httpManager = new HttpManager(tag, HttpMothed.POST,
                    IRequestConst.RequestMethod.Register, parmMap, this);
            httpManager.request();
        }
        if (tag == 3) {//第三方登录获取手机验证码
            Map<String, String> parmMap = new HashMap<String, String>();
            parmMap.put("mobile", et_phone.getText().toString());
            HttpManager httpManager = new HttpManager(tag, HttpMothed.POST,
                    IRequestConst.RequestMethod.GetVaildCodeOpen, parmMap, this);
            httpManager.request();
        } else if (tag == 4) {//第三方登录绑定用户
            Map<String, String> parmMap = new HashMap<String, String>();
            parmMap.put("mobile", et_phone.getText().toString());
            parmMap.put("vaildCode", et_yanzhengma.getText().toString());
            parmMap.put("passWord", et_pwd.getText().toString());
            parmMap.put("platformType", "2");
            parmMap.put("nickName", otherLoginInfoJson.optString("nickname"));
            parmMap.put("avatar", otherLoginInfoJson.optString("headimgurl"));
            parmMap.put("systemType", "4");//Sys = 1,QQ = 2,Sina = 3,Wechat = 4
            parmMap.put("openId", otherLoginInfoJson.optString("openid"));
            parmMap.put("deviceId", MainApplication.deviceId);
            parmMap.put("unionId", otherLoginInfoJson.optString("unionid"));
            HttpManager httpManager = new HttpManager(tag, HttpMothed.POST,
                    IRequestConst.RequestMethod.BindLoginAccountOpen, parmMap, this);
            httpManager.request();
        }else if (tag == 5) {//登录
            Map<String, String> parmMap = new HashMap<String, String>();
            parmMap.put("loginName", et_phone.getText().toString());
            parmMap.put("password", et_pwd.getText().toString());
            parmMap.put("platformType", "2");//2 android
            parmMap.put("deviceId", MainApplication.deviceId);
            HttpManager httpManager = new HttpManager(tag, HttpMothed.POST,
                    IRequestConst.RequestMethod.Login, parmMap, this);
            httpManager.request();
        }else if (tag == 6) {//完善信息
            Map<String, String> parmMap = new HashMap<String, String>();
            parmMap.put("memberId", ConfigManager.instance().getUser().getId());
            parmMap.put("nickName", et_name.getText().toString().trim());
            parmMap.put("promotionCode", et_yaoqingma.getText().toString().trim());
            HttpManager httpManager = new HttpManager(tag, HttpMothed.POST,
                    IRequestConst.RequestMethod.RegisterInfo, parmMap, this);
            httpManager.request();
        }
    }

    @Override
    public void OnSuccessResponse(int tag, ServiceInfo resultInfo) {
        super.OnSuccessResponse(tag, resultInfo);
        if (tag == 1) {
            JSONObject resultJson = (JSONObject) resultInfo.getResponse();
            if (resultJson != null && resultJson.optBoolean("success")) {
                setShowStatus(2);
            } else {
                if (resultJson != null && !resultJson.optBoolean("success")) {
                    tip(resultJson.optString("msg"));
                }
            }
        } else if (tag == 2) {
            JSONObject resultJson = (JSONObject) resultInfo.getResponse();
            if (resultJson != null && resultJson.optBoolean("success")) {
                isJumbHome=false;
                sendRequest(5,"");
            } else {
                if (resultJson != null && !resultJson.optBoolean("success")) {
                    tip(resultJson.optString("msg"));
                }
            }
        } else if (tag == 3) {
            JSONObject resultJson = (JSONObject) resultInfo.getResponse();
            if (resultJson != null && resultJson.optBoolean("success")) {
                setShowStatus(2);
            } else {
                if (resultJson != null && !resultJson.optBoolean("success")) {
                    tip(resultJson.optString("msg"));
                }
            }
        } else if (tag == 4) {
            JSONObject resultJson = (JSONObject) resultInfo.getResponse();
            if (resultJson != null && resultJson.optBoolean("success")) {
                tip("信息绑定成功");
                setResult(RESULT_OK);
                finish();
            } else {
                if (resultJson != null && !resultJson.optBoolean("success")) {
                    tip(resultJson.optString("msg"));
                }
            }
        }else if (tag == 5) {//登录
            JSONObject jsonObject = (JSONObject) resultInfo.getResponse();
            if (jsonObject != null) {
                if (jsonObject.optBoolean("success")) {
                    if (isJumbHome){
                        tip("登录成功");
                        SPManager.instance().setString(SPKeyManager.USERINFO_pwd, et_pwd.getText().toString());
                        ConfigManager.instance().writeInSP(jsonObject);
                        jumpActivity(HomeFragmentActivity.class);
                        finish();
                    }else {
                        SPManager.instance().setString(SPKeyManager.USERINFO_pwd, et_pwd.getText().toString());
                        ConfigManager.instance().writeInSP(jsonObject);
                        setShowStatus(3);
                        isJumbHome=true;
                    }
                } else {
                    tip(jsonObject.optString("msg"));
                }
            } else {
                tip("登录异常");
            }
        }else if (tag == 6) {//完善信息
            JSONObject resultJson = (JSONObject) resultInfo.getResponse();
            if (resultJson != null && resultJson.optBoolean("success")) {
                isJumbHome=true;
                sendRequest(5,"");
            } else {
                if (resultJson != null && !resultJson.optBoolean("success")) {
                    tip(resultJson.optString("msg"));
                }
            }
        }
    }

    @Override
    public void OnFailedResponse(int tag, String code, String msg) {
        super.OnFailedResponse(tag, code, msg);
        tip(msg);
    }

    //显示注册的几个状态
    private void setShowStatus(int status) {
        if (status == 1) {
            ll_regist1.setVisibility(View.VISIBLE);
            ll_regist2.setVisibility(View.GONE);
            ll_regist3.setVisibility(View.GONE);
            tv_rightbtn.setVisibility(View.GONE);
            btn_next.setText("获取验证码");
        }
        if (status == 2) {
            startVaildCodestatus();
            tv_phoneinfo.setText("已发送验证码至手机：" + et_phone.getText().toString());
            ll_regist1.setVisibility(View.GONE);
            ll_regist2.setVisibility(View.VISIBLE);
            ll_regist3.setVisibility(View.GONE);
            tv_rightbtn.setVisibility(View.GONE);
            btn_next.setText("下一步");
        }
        if (status == 3) {
            ll_regist1.setVisibility(View.GONE);
            ll_regist2.setVisibility(View.GONE);
            ll_regist3.setVisibility(View.VISIBLE);
            tv_rightbtn.setVisibility(View.VISIBLE);
            btn_next.setText("完成");
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == openAblumRequestCode) {
            if (data == null) {
                return;
            }
            Uri ablumUri = data.getData();
            if (ablumUri != null) {
                prctureNmae = UUID.randomUUID().toString() + ".png";
                PhotoUtils.cropImageUri(this, ablumUri, 1, 1, 300, 300, openCropRequestCode, PhotoUtils.getTempPath(), prctureNmae);
            } else {
                tip("请重新拍照");
            }

        } else if (requestCode == openCameraRequestCode) {
            File file = new File(PhotoUtils.getTempPath(), PhotoUtils.tempPath);
            if (file.exists()) {
                prctureNmae = UUID.randomUUID().toString() + ".png";
                PhotoUtils.cropImageUri(this, Uri.fromFile(file), 1, 1, 300, 300, openCropRequestCode, PhotoUtils.getTempPath(), prctureNmae);
            } else {
                tip("请重新拍照");
            }
        } else if (requestCode == openCropRequestCode) {
            ImageLoaderManager.getInstance().displayImage("file://" + ConfigManager.IMG_PATH + "/" + prctureNmae, im_header);
            prctureNmae=ConfigManager.IMG_PATH+"/"+prctureNmae;
            deleteTemp();
        }
    }

    private void deleteTemp() {
        File tempfile = new File(PhotoUtils.getTempPath(), PhotoUtils.tempPath);
        if (tempfile.exists()) {
            tempfile.delete();
        }
    }

    /**
     * 弹出选择
     */
    private void setHeadPictrue(int position, List moreData) {
        PhotoPopupAdapter photoPopupAdapter = new PhotoPopupAdapter(mContext, moreData, R.layout.item_photo_button);
        final TakePopupWin takePopupWin = new TakePopupWin(mContext, R.layout.item_photo, R.id.photo_contorl, R.id.photo_listview, photoPopupAdapter);
        final WindowManager.LayoutParams attributes = ((Activity) mContext).getWindow().getAttributes();
        takePopupWin.showAtLocation(findViewById(R.id.ll_rootview), Gravity.BOTTOM, 0, attributes.height - takePopupWin.getHeight());
        //点击时间
        takePopupWin.setOnItemClickListener(new TakePopupWin.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0://拍照
                        PhotoUtils.openCamera(RegistActivity.this, openCameraRequestCode, PhotoUtils.getTempPath(), PhotoUtils.tempPath);
                        break;
                    case 1://从文件中选择
                        PhotoUtils.openAlbum(RegistActivity.this, openAblumRequestCode);
                        break;
                    case 2://取消

                        break;

                }

            }
        });
        takePopupWin.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
            }
        });
    }

    /**
     * 开始上传
     */
    private void startUpload(String name,String yaoqingma) {
        customLoadding.show();
        customLoadding.setTip("开始上传...");
        MultipartBody.Builder multiparBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        multiparBody.addFormDataPart("memberId", ConfigManager.instance().getUser().getId());
        multiparBody.addFormDataPart("nickName", name);
        multiparBody.addFormDataPart("promotionCode", yaoqingma);
        File file = new File(prctureNmae);
        if (file.exists()) {
            multiparBody.addFormDataPart("images", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
        }
        UpLoadUtils.instance().uploadPicture(IRequestConst.RequestMethod.RegisterInfo, multiparBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("failure:****", e.getMessage());
                if (customLoadding.isShowing()) {
                    customLoadding.dismiss();
                }
                Looper.prepare();
                tip(e.getMessage());
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseStr= response.body().string();
                Log.e("RegisterInfo:****",responseStr+"===");
                if (customLoadding.isShowing()) {
                    customLoadding.dismiss();
                }
                if (responseStr.contains("error")||responseStr.contains("false")){
                    Looper.prepare();
                    tip("信息提交失败！");
                    Looper.loop();
                }else {
                    sendRequest(5,"");
                }
            }
        });
    }

    Handler handler=new Handler();
    int timeInt=60;
    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            timeInt--;
            if (timeInt>0){
                tv_yanzhengma.setText(timeInt+"秒");
                handler.postDelayed(runnable,1000);
            }else {
                tv_yanzhengma.setClickable(true);
                tv_yanzhengma.setText("重新获取验证码");
            }
        }
    };
    //验证码状态
    private void startVaildCodestatus(){
        tv_yanzhengma.setClickable(false);
        timeInt=60;
        tv_yanzhengma.setText(timeInt+"秒");
        handler.postDelayed(runnable,1000);
    }

    @Override
    protected void onDestroy() {
        delete(ConfigManager.IMG_PATH);
        super.onDestroy();
    }
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
