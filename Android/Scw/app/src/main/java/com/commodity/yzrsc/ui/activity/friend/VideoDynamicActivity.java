package com.commodity.yzrsc.ui.activity.friend;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.commodity.yzrsc.R;
import com.commodity.yzrsc.http.HttpManager;
import com.commodity.yzrsc.http.HttpMothed;
import com.commodity.yzrsc.http.IRequestConst;
import com.commodity.yzrsc.http.ServiceInfo;
import com.commodity.yzrsc.http.UpLoadUtils;
import com.commodity.yzrsc.manager.SPManager;
import com.commodity.yzrsc.model.TypeModel;
import com.commodity.yzrsc.ottobus.BusProvider;
import com.commodity.yzrsc.ottobus.Event;
import com.commodity.yzrsc.ui.BaseActivity;
import com.commodity.yzrsc.ui.activity.shortvideo.VideoRecorderActivity;
import com.commodity.yzrsc.ui.activity.store.GoodsTypeActivity;
import com.commodity.yzrsc.ui.activity.store.UploadGoodsActivity;
import com.commodity.yzrsc.ui.adapter.PhotoPopupAdapter;
import com.commodity.yzrsc.ui.widget.layout.TakePopupWin;
import com.commodity.yzrsc.utils.KeyBoardUtils;
import com.commodity.yzrsc.utils.PhotoUtils;
import com.commodity.yzrsc.utils.VideoUtils;
import com.commodity.yzrsc.view.SuccessDialog;
import com.sh.shvideolibrary.VideoInputDialog;
import com.yixia.camera.util.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class VideoDynamicActivity extends BaseActivity {


    @Bind(R.id.head_back)
    LinearLayout headBack;
    @Bind(R.id.head_title)
    TextView headTitle;
    @Bind(R.id.head_text_right)
    TextView headTextRight;
    @Bind(R.id.pic_description)
    EditText picDescription;
    @Bind(R.id.upload_add_vdio)
    ImageView uploadAddVdio;
    @Bind(R.id.item_video_delete)
    ImageView itemVideoDelete;
    @Bind(R.id.pic_submit)
    Button picSubmit;
    @Bind(R.id.bg)
    View bg;
    @Bind(R.id.ren_bg)
    FrameLayout renBg;
    @Bind(R.id.tv_type)
    TextView tvType;
    private List<String> moreData = new ArrayList<>();
    private int openAblumVideo = 5;
    //video存储路径
    private String videoPath = "";
    private String upload_desc_value;
    private List<String> dataType = new ArrayList<>();
    private List<String> dataTypeId = new ArrayList<>();
    private String typeId;
    List<TypeModel> typeModel = new ArrayList<>();
    private MediaType MEDIA_TYPE_VIDEO = MediaType.parse("application/octet-stream");

    @Override
    protected int getContentView() {
        return R.layout.activity_video_dynamic;
    }

    @Override
    protected void initView() {
        headTitle.setText("发布视频动态");
        sendRequest(1);
    }

    @Override
    protected void initListeners() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    MultipartBody.Builder multiparBody;

    @OnClick({R.id.head_back, R.id.upload_add_vdio, R.id.item_video_delete, R.id.pic_submit,R.id.tv_type})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.head_back:
                finish();
                break;
            case R.id.upload_add_vdio:
                moreData.clear();
                moreData.add("拍摄视频");
                moreData.add("从文件中选择");
                moreData.add("取消");
                setHeadPictrue(0, bg, R.id.bg, moreData);
                break;
            case R.id.item_video_delete:
                videoPath = "";
                uploadAddVdio.setImageBitmap(BitmapFactory.decodeResource(VideoDynamicActivity.this.getResources(), R.drawable.icon_add_video));
                itemVideoDelete.setVisibility(View.INVISIBLE);
                break;
            case R.id.pic_submit:
                picSubmit.setClickable(false);
                initUpload();
                myHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        picSubmit.setClickable(true);
                    }
                }, 1500);
                break;

            case R.id.tv_type:
                setType();
                break;
        }
    }

    /**
     * popupwindow
     */
    private void setType() {
        PhotoPopupAdapter photoPopupAdapter = new PhotoPopupAdapter(mContext, dataType, R.layout.item_photo_button);
        final TakePopupWin takePopupWin = new TakePopupWin(mContext, R.layout.item_photo, R.id.photo_contorl, R.id.photo_listview, photoPopupAdapter);
        bg.setVisibility(View.VISIBLE);
        final WindowManager.LayoutParams attributes = getWindow().getAttributes();
        takePopupWin.showAtLocation(findViewById(R.id.ren_bg), Gravity.BOTTOM, 0, attributes.height - takePopupWin.getHeight());
        //点击时间
        takePopupWin.setOnItemClickListener(new TakePopupWin.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                typeId = dataTypeId.get(position);
                tvType.setText(dataType.get(position));
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
    public void sendRequest(int tag) {
        super.sendRequest(tag);
        if (tag == 1) {
            customLoadding.show();
            Map<String, String> parmMap = new HashMap<String, String>();
            HttpManager httpManager = new HttpManager(tag, HttpMothed.GET,
                    IRequestConst.RequestMethod.GetDynamicCatalog, parmMap, this);
            httpManager.request();
        }
    }

    @Override
    public void onSuccess(int tag, ServiceInfo result) {
        super.onSuccess(tag, result);

        JSONObject response = (JSONObject) result.getResponse();
        if (response.optBoolean("success")) {
            try {
                typeModel = JSON.parseArray(response.getString("data"), TypeModel.class);
                for (int i = 0; i < typeModel.size(); i++) {
                    dataType.add(typeModel.get(i).getName());
                    dataTypeId.add(typeModel.get(i).getId() + "");
                }

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

    private void initUpload() {

        String upload_desc_value = picDescription.getText().toString().trim();

        if (StringUtils.isEmpty(upload_desc_value)) {
            tip("请输入文字内容");
            return;
        }
        if (TextUtils.isEmpty(typeId)){
            tip("请选择分类");
            return;
        }
        File videoF = new File(videoPath);
        if (!videoF.exists()) {//如果没有视频
            tip("请添加视频");
        } else {//如果有视频
            if (videoF.length() > 1024 * 1024 * 30) {
                tip("视频太大，请重新拍摄");
                Log.e("--------------", videoF.length() + "");
                uploadAddVdio.setImageBitmap(BitmapFactory.decodeResource(VideoDynamicActivity.this.getResources(), R.drawable.icon_add_vdio));
                return;
            }
            if (!customLoadding.isShowing()) {
                customLoadding.show();
            }
            customLoadding.setTip("正在压缩视频...");
            startUpload(videoF);
        }
    }

    /**
     * 开始上传  压缩图片
     */
    private void startUpload(File fileoutput) {
        if (!customLoadding.isShowing()) {
            customLoadding.show();
        }
        customLoadding.setTip("正在上传...");
        upload_desc_value = picDescription.getText().toString().trim();
        multiparBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        multiparBody.addFormDataPart("description", upload_desc_value);
        multiparBody.addFormDataPart("dynamicType", "2");
        multiparBody.addFormDataPart("userDynamicCatalog_Id", typeId);
        //添加视频
        if (fileoutput != null && fileoutput.exists()) {
            if (fileoutput.length() > 1024 * 1024 * 10) {
                tip("视频大小不能超过10M");
                customLoadding.dismiss();
                return;
            }
            multiparBody.addFormDataPart("videos", fileoutput.getName(), RequestBody.create(MEDIA_TYPE_VIDEO, fileoutput));
            Message message = new Message();
            message.what = 1201;
            myHandler.sendMessage(message);
        }
    }

    Handler myHandler = new Handler() {
        //重写消息处理函数
        public void handleMessage(Message msg) {
            switch (msg.what) {
                //判断发送的消息
                case 1201:
                    //更新View
                    UpLoad(multiparBody);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    //走上传接口
    private void UpLoad(MultipartBody.Builder multiparBody) {
        Log.e("UpLoad: ", "开始UpLoad");
        UpLoadUtils.instance().uploadPicture1(IRequestConst.RequestMethod.PostDynamic, multiparBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (customLoadding.isShowing()) {
                    customLoadding.dismiss();
                }
                Looper.prepare();
                tip(e.getMessage());
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseStr = response.body().string();
                Log.e("test", "onResponse: " + responseStr);
                if (customLoadding.isShowing()) {
                    customLoadding.dismiss();
                }
                try {
                    JSONObject jsob = new JSONObject(responseStr);
                    if (jsob != null && jsob.optBoolean("success")) {
                        Looper.prepare();
                        SuccessDialog renzhengSuccessDialog = new SuccessDialog(VideoDynamicActivity.this);
                        renzhengSuccessDialog.show();
                        renzhengSuccessDialog.setOnclickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                                            SPManager.put(RenzhengActivity.this, Constanct.RENZHENG,true);
                                BusProvider.getInstance().post(new Event.NotifyChangedView("MyDynamicActivity"));
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
                    } else {
                        Looper.prepare();
                        tip(jsob.optString("msg"));
                        Looper.loop();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * popupwindow
     */
    private void setHeadPictrue(int position, final View backage, int bg_id, List moreData) {
        KeyBoardUtils.hindKeyBord(picDescription);
        PhotoPopupAdapter photoPopupAdapter = new PhotoPopupAdapter(mContext, moreData, R.layout.item_photo_button);
        final TakePopupWin takePopupWin = new TakePopupWin(mContext, R.layout.item_photo, R.id.photo_contorl, R.id.photo_listview, photoPopupAdapter);
        backage.setVisibility(View.VISIBLE);
        final WindowManager.LayoutParams attributes = getWindow().getAttributes();
        takePopupWin.showAtLocation(findViewById(R.id.ren_bg), Gravity.BOTTOM, 0, attributes.height - takePopupWin.getHeight());
        //点击时间
        takePopupWin.setOnItemClickListener(new TakePopupWin.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0://拍照或摄影

                        if (PhotoUtils.getTempPath() == null) {
                            tip("请插入内存卡");
                            return;
                        }
                        recordVideo();
//                        jumpActivityForResult(1002, VideoRecorderActivity.class);
//                          VideoUtils.openVideo(UploadGoodsActivity.this, openVideo);
                        break;
                    case 1://从文件中选择

                        PhotoUtils.openAlbumVideo(VideoDynamicActivity.this, openAblumVideo);
                        break;
                    case 2://取消

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
    private void recordVideo() {
        //显示视频录制控件
        VideoInputDialog.show(getSupportFragmentManager(), new VideoInputDialog.VideoCall() {
            @Override
            public void videoPathCall(String path) {
                videoPath = path;
                Bitmap thumbnail = VideoUtils.getThumbnail(videoPath);
                uploadAddVdio.setImageBitmap(thumbnail);
                itemVideoDelete.setVisibility(View.VISIBLE);
                saveVideoPath(videoPath);
            }
        }, VideoInputDialog.Q720, VideoDynamicActivity.this);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GoodsTypeActivity.resultCode) {
            if (data != null) {
            }
        }
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == openAblumVideo) {
                if (data == null) {
                    return;
                }
                Uri videoUri = data.getData();
                if (videoUri != null) {
                    videoPath = PhotoUtils.getPath(this, videoUri);
                    Bitmap thumbnail = VideoUtils.getThumbnail(videoPath);
                    uploadAddVdio.setImageBitmap(thumbnail);
                    itemVideoDelete.setVisibility(View.VISIBLE);
                    saveVideoPath(videoPath);
                } else {
                    tip("请重新拍摄");
                }
            }
            if (requestCode == 1002) {//拍摄视频返回
                // 成功
                String path = data.getStringExtra("path");
                videoPath = path;
                Log.e("test", "onActivityResult: " + videoPath);
                if (StringUtils.isEmpty(videoPath)) {
                    tip("请重新拍摄");
                } else {
                    Bitmap thumbnail = VideoUtils.getThumbnail(videoPath);
                    uploadAddVdio.setImageBitmap(thumbnail);
                    itemVideoDelete.setVisibility(View.VISIBLE);
                    saveVideoPath(videoPath);
                }
            }
        }
    }

    public void saveVideoPath(String path) {
        SPManager.put(this, "videoPath", path);
    }

    private void deleteTemp() {
        File tempfile = new File(PhotoUtils.getTempPath(), PhotoUtils.tempPath);
        if (tempfile.exists()) {
            tempfile.delete();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        deleteTemp();
    }
}

