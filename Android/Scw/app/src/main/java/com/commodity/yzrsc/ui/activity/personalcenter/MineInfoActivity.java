package com.commodity.yzrsc.ui.activity.personalcenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.commodity.yzrsc.R;
import com.commodity.yzrsc.http.HttpManager;
import com.commodity.yzrsc.http.HttpMothed;
import com.commodity.yzrsc.http.IRequestConst;
import com.commodity.yzrsc.http.ServiceInfo;
import com.commodity.yzrsc.http.UpLoadUtils;
import com.commodity.yzrsc.manager.ConfigManager;
import com.commodity.yzrsc.manager.ImageLoaderManager;
import com.commodity.yzrsc.model.PresonInfoModel;
import com.commodity.yzrsc.model.TypeModel;
import com.commodity.yzrsc.ui.BaseActivity;
import com.commodity.yzrsc.ui.activity.store.StoreSettingActivity;
import com.commodity.yzrsc.ui.activity.user.ChangePwdActivity;
import com.commodity.yzrsc.ui.activity.user.UserAdressListActivity;
import com.commodity.yzrsc.ui.adapter.PhotoPopupAdapter;
import com.commodity.yzrsc.ui.widget.layout.TakePopupWin;
import com.commodity.yzrsc.utils.PhotoUtils;
import com.commodity.yzrsc.view.RoundAngleImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 个人信息
 * Created by yangxuqiang on 2017/3/26.
 */

public class MineInfoActivity extends BaseActivity {
    //    @Bind(R.id.mine_heart)
//    LinearLayout mine_heart;//等级
    @Bind(R.id.mine_phone_text)
    TextView mine_phone_text;
    @Bind(R.id.image)
    RoundAngleImageView image;
    @Bind(R.id.name)
    TextView name;
    @Bind(R.id.backage)
    View backage;
    private InputMethodManager mInputManager;
    private PopupWindow popupWindow;
    private View popupView = null;
    private EditText inputComment;
    private String nInputContentText;
    private TextView btn_submit;
    private RelativeLayout rl_input_container;
    private List<String> data = new ArrayList<>();
    private File file;
    private  PresonInfoModel presonInfoModel;
    public static final String HEADIMG = ConfigManager.ROOT + "headimg" + File.separator;

    @Override
    protected int getContentView() {
        return R.layout.activity_mineinfo;
    }

    @Override
    protected void initView() {
        sendRequest(1);
        mine_phone_text.setText(ConfigManager.instance().getUser().getMobile());
        data.add("拍照上传");
        data.add("从手机相册选择");
        data.add("取消");
        file = new File(HEADIMG);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    private void addHeart(int pic) {
//        for (int i=0;i<pic;i++){
//            ImageView imageView = new ImageView(this);
//            imageView.setImageResource(R.drawable.icon_love);
//            LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams((int) ScreenUtils.dp2px(mContext,16),(int) ScreenUtils.dp2px(mContext,16));
//            layoutParams.leftMargin= (int) ScreenUtils.dp2px(mContext,20);
//            mine_heart.addView(imageView);
//        }


    }

    @Override
    protected void initListeners() {

    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    public void sendRequest(int tag) {
        super.sendRequest(tag);
        if (tag == 1) {
            customLoadding.show();
            Map<String, String> parmMap = new HashMap<String, String>();
            parmMap.put("memberId",ConfigManager.instance().getUser().getId());
            HttpManager httpManager = new HttpManager(tag, HttpMothed.GET,
                    IRequestConst.RequestMethod.PostGetnfo, parmMap, this);
            httpManager.request();
        }
    }
    @Override
    public void onSuccess(int tag, ServiceInfo result) {
        super.onSuccess(tag, result);

        JSONObject response = (JSONObject) result.getResponse();
        if (response.optBoolean("success")) {
            try {
                presonInfoModel = JSON.parseObject(response.getString("data"), PresonInfoModel.class);
                ImageLoaderManager.getInstance().displayImage(presonInfoModel.getMemberAvatar(),image);
                name.setText(presonInfoModel.getMemberNickName()+"");
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            tip(response.optString("msg"));
        }
    }

    @Override
    public void OnFailedResponse(int tag, String code, String msg) {
        super.OnFailedResponse(tag, code, msg);
        tip(msg);
    }

    @OnClick({R.id.mine_info_phone, R.id.mine_password, R.id.mine_address, R.id.mine_back, R.id.image, R.id.name})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mine_info_phone://修改手机号
                jumpActivityForResult(1, AlertPhone.class);
                break;
            case R.id.mine_password://修改密码
                jumpActivity(ChangePwdActivity.class);
                break;
            case R.id.mine_address://修改地址
                jumpActivity(UserAdressListActivity.class);
                break;
            case R.id.mine_back:
                finish();
                break;
            case R.id.image:
                setHeadPictrue();
                break;

            case R.id.name:
                showPopupcomment();
                break;
        }
    }

    /**
     * popupwindow
     */
    private void setHeadPictrue() {
        PhotoPopupAdapter photoPopupAdapter = new PhotoPopupAdapter(mContext, data, R.layout.item_photo_button);
        final TakePopupWin takePopupWin = new TakePopupWin(mContext, R.layout.item_photo, R.id.photo_contorl, R.id.photo_listview, photoPopupAdapter);
        backage.setVisibility(View.VISIBLE);
        final WindowManager.LayoutParams attributes = getWindow().getAttributes();
        takePopupWin.showAtLocation(findViewById(R.id.stroe_zuiwai), Gravity.BOTTOM, 0, attributes.height - takePopupWin.getHeight());
        //点击时间
        takePopupWin.setOnItemClickListener(new TakePopupWin.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (PhotoUtils.getTempPath() == null) {
                    tip("没有内存卡");
                    return;
                }
                switch (position) {
                    case 0://拍照
                        PhotoUtils.openCamera(MineInfoActivity.this, CAMERA_CODE, file, PhotoUtils.tempPath);
                        break;
                    case 1://相册
                        PhotoUtils.openAlbum(MineInfoActivity.this, IMAGE_REQUEST_CODE);
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
        mine_phone_text.setText(ConfigManager.instance().getUser().getMobile());
        if (data == null) {
            if (requestCode == CAMERA_CODE) {
                Uri headimgUri = Uri.fromFile(new File(file, PhotoUtils.tempPath));
                PhotoUtils.cropImageUri(this, headimgUri, 1, 1, 600, 600, CROP_CODE, file, PhotoUtils.headImgPath);
            }
        } else if ((requestCode == IMAGE_REQUEST_CODE)) {//相册
            Uri imgUri = data.getData();
            if (imgUri != null) {
                PhotoUtils.cropImageUri(this, imgUri, 1, 1, 600, 600, CROP_CODE, file, PhotoUtils.headImgPath);
            }
        } else if (requestCode == CROP_CODE) {
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath() + PhotoUtils.headImgPath);
            image.setImageBitmap(bitmap);
            //上传头像
            upLoadHead();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void upLoadHead() {
        File headFile = new File(this.file, PhotoUtils.headImgPath);
        if (!headFile.exists()) {
            tip("请从新拍摄");
            return;
        }
        customLoadding.setTip("正在上传头像...");
        customLoadding.show();
        MultipartBody.Builder multiparBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        multiparBody.addFormDataPart("Avatar", headFile.getName(), RequestBody.create(MediaType.parse("image/*"), headFile));
        multiparBody.addFormDataPart("picType", "Avatar");
        UpLoadUtils.instance().uploadPicture(IRequestConst.RequestMethod.PostChangeInfo, multiparBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("onFailure:", e.getMessage());
                tip(e.getMessage());
                customLoadding.dismiss();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                customLoadding.dismiss();
                String string = response.body().string();
                Log.e("response:", string);

                JSONObject jsob = null;
                try {
                    jsob = new JSONObject(string);
                    if (jsob != null && jsob.optBoolean("success")) {
                        if (jsob.optBoolean("data")) {
                            Looper.prepare();
                            tip("头像上传成功");
                            sendRequest(1);
                            Looper.loop();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
    }

    @SuppressLint("WrongConstant")
    private void showPopupcomment() {
        if (popupView == null) {
            //加载评论框的资源文件
            popupView = LayoutInflater.from(mContext).inflate(R.layout.comment_popupwindow, null);
        }
        inputComment = (EditText) popupView.findViewById(R.id.et_discuss);
        btn_submit = (Button) popupView.findViewById(R.id.btn_confirm);
        rl_input_container = (RelativeLayout) popupView.findViewById(R.id.rl_input_container);
        btn_submit.setText("确定");
        //利用Timer这个Api设置延迟显示软键盘，这里时间为200毫秒
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            public void run() {
                mInputManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                mInputManager.showSoftInput(inputComment, 0);
            }

        }, 200);
        if (popupWindow == null) {
            popupWindow = new PopupWindow(popupView, RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT, false);

        }
        //popupWindow的常规设置，设置点击外部事件，背景色
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE)
                    popupWindow.dismiss();
                return false;

            }
        });

        // 设置弹出窗体需要软键盘，放在setSoftInputMode之前
        popupWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        // 再设置模式，和Activity的一样，覆盖，调整大小。
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //设置popupwindow的显示位置，这里应该是显示在底部，即Bottom
        popupWindow.showAtLocation(popupView, Gravity.BOTTOM, 0, 0);

        popupWindow.update();

        //设置监听
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            // 在dismiss中恢复透明度
            @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
            public void onDismiss() {

                mInputManager.hideSoftInputFromWindow(inputComment.getWindowToken(), 0); //强制隐藏键盘


            }
        });
        //外部点击事件
        rl_input_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInputManager.hideSoftInputFromWindow(inputComment.getWindowToken(), 0); //强制隐藏键盘
                popupWindow.dismiss();

            }
        });
        //评论框内的发送按钮设置点击事件
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                nInputContentText = inputComment.getText().toString().trim();

                if (nInputContentText == null || "".equals(nInputContentText)) {
                    tip("请输入昵称");
                    return;
                }
                mInputManager.hideSoftInputFromWindow(inputComment.getWindowToken(), 0);
                popupWindow.dismiss();
                subEvalution(nInputContentText);
                name.setText(nInputContentText);

            }
        });
    }


    private void subEvalution(final String nInputContentText) {
        FormBody requestBody = new FormBody.Builder().add("nickName", nInputContentText).build();
        UpLoadUtils.instance().requesDynamic(IRequestConst.RequestMethod.PostChangeInfo, requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                tip(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                Log.e("onResponse:", string);
                JSONObject jsob;
                if (string != null) {
                    try {
                        jsob = new JSONObject(string);
                        if (jsob != null && jsob.optBoolean("success")) {
                            //提交成功
                            tip("修改成功");

                        } else {
                            //提交失败
                            tip("修改失败");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        tip("json解析异常");
                    }
                }

            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
