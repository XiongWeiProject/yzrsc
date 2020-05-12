package com.commodity.yzrsc.ui.activity.friend;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
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
import com.commodity.yzrsc.manager.ConfigManager;
import com.commodity.yzrsc.manager.Constanct;
import com.commodity.yzrsc.manager.SPKeyManager;
import com.commodity.yzrsc.manager.SPManager;
import com.commodity.yzrsc.model.TypeModel;
import com.commodity.yzrsc.ottobus.BusProvider;
import com.commodity.yzrsc.ottobus.Event;
import com.commodity.yzrsc.ui.BaseActivity;
import com.commodity.yzrsc.ui.adapter.PhotoPopupAdapter;
import com.commodity.yzrsc.ui.adapter.UpLoadPictureAdapter;
import com.commodity.yzrsc.ui.dialog.CommonDialog;
import com.commodity.yzrsc.ui.widget.layout.TakePopupWin;
import com.commodity.yzrsc.ui.widget.specialview.MyGridView;
import com.commodity.yzrsc.utils.FileUtil;
import com.commodity.yzrsc.utils.PhotoUtils;
import com.commodity.yzrsc.view.SuccessDialog;

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
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PicDynamicActivity extends BaseActivity {

    @Bind(R.id.head_back)
    LinearLayout headBack;
    @Bind(R.id.head_title)
    TextView headTitle;
    @Bind(R.id.head_text_right)
    TextView headTextRight;
    @Bind(R.id.pic_description)
    EditText picDescription;
    @Bind(R.id.pic_grid)
    MyGridView picGrid;
    @Bind(R.id.pic_submit)
    Button picSubmit;
    @Bind(R.id.bg)
    View bg;
    @Bind(R.id.ren_bg)
    FrameLayout renBg;
    public final int openCamera = 1;
    public final int openAblum = 2;
    public final int CROP_CODE = 3;
    String desc;
    public String imgName = "";
    @Bind(R.id.tv_type)
    TextView tvType;
    private List<String> data = new ArrayList<>();
    private List<String> dataType = new ArrayList<>();
    private List<String> dataTypeId = new ArrayList<>();
    private static final String RENZHENG = ConfigManager.ROOT + "dongtai" + File.separator;
    private static final String RENZHENG_DELETE = ConfigManager.ROOT + "dongtai";
    private File savefile;
    private UpLoadPictureAdapter uploadPictureAdapter;
    public MediaType MEDIA_TYPE_PNG = MediaType.parse("image/*");
    private String typeId;
    private List<String> pictrueData = new ArrayList<>();
    List<TypeModel> typeModel = new ArrayList<>();
    String userDynamicCatalog_Id;
    private static final int REQUEST_CODE = 0x00000011;
    private static final int PERMISSION_WRITE_EXTERNAL_REQUEST_CODE = 0x00000012;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_pic_dynamic;
    }

    @Override
    protected void initView() {
        headTitle.setText("上传图文动态");
//        if (getIntent()!=null&&getIntent().getExtras().containsKey("userDynamicCatalog_Id")){
//            userDynamicCatalog_Id=getIntent().getExtras().getString("userDynamicCatalog_Id");
//        }
        SPKeyManager.uploadmax = 10;
        data.add("拍照上传");
        data.add("从手机相册选择");
        data.add("取消");
        FileUtil.deleteFolderRecursively(new File(RENZHENG_DELETE));
        initData();
        uploadPictureAdapter = new UpLoadPictureAdapter(mContext, pictrueData, R.layout.item_pic_upload);
        picGrid.setAdapter(uploadPictureAdapter);
        sendRequest(1);

    }


    @Override
    protected void initListeners() {
        uploadPictureAdapter.addPictureListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHeadPictrue();
            }
        });
        headBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(desc) || pictrueData.size() != 1) {
                    CommonDialog commonDialog = new CommonDialog(PicDynamicActivity.this);
                    commonDialog.show();
                    commonDialog.setContext("放弃此次编辑？");
                    commonDialog.setClickSubmitListener(new CommonDialog.OnClickSubmitListener() {
                        @Override
                        public void clickSubmit() {
                            finish();
                        }
                    });
                }
            }
        });
    }

    private void initData() {
        savefile = new File(RENZHENG);
        if (!savefile.exists()) {
            savefile.mkdirs();
        }
        File[] files = savefile.listFiles();
        for (File f : files) {
            pictrueData.add(f.getAbsolutePath());
        }
        pictrueData.add("add");
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

    private void postPicDynamic(String trim) {
        customLoadding.setTip("上传中...");
        customLoadding.show();
        MultipartBody.Builder multiparBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        multiparBody.addFormDataPart("description", trim);
        multiparBody.addFormDataPart("dynamicType", "1");
        multiparBody.addFormDataPart("userDynamicCatalog_Id", typeId);
        for (int i = 0; i < pictrueData.size() - 1; i++) {
            File file = new File(pictrueData.get(i));
            multiparBody.addFormDataPart("image", file.getName(), RequestBody.create(MEDIA_TYPE_PNG, file));
        }
        UpLoadUtils.instance().uploadPicture1(IRequestConst.RequestMethod.PostDynamic, multiparBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                tip(e.getMessage());
                Log.e("failure:****", e.getMessage());
                if (customLoadding.isShowing()) {
                    customLoadding.dismiss();
                }
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseStr = response.body().string();
                Log.e("onResponse:****", responseStr);
                if (customLoadding.isShowing()) {
                    customLoadding.dismiss();
                }
                JSONObject jsob = null;
                try {
                    jsob = new JSONObject(responseStr);
                    if (jsob != null && jsob.optBoolean("data")) {
                        //提交成功
                        Looper.prepare();
                        SuccessDialog renzhengSuccessDialog = new SuccessDialog(PicDynamicActivity.this);
                        renzhengSuccessDialog.show();
                        renzhengSuccessDialog.setOnclickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                                            SPManager.put(RenzhengActivity.this, Constanct.RENZHENG,true);
                                BusProvider.getInstance().post(new Event.NotifyChangedView("MyDynamicActivity"));
                                BusProvider.getInstance().post(new Event.NotifyChangedView("DynamicFragment"));
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
                    Looper.prepare();
                    e.printStackTrace();
                    tip("json解析异常");
                    Looper.loop();
                }

            }
        });

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

    /**
     * popupwindow
     */
    private void setHeadPictrue() {
        PhotoPopupAdapter photoPopupAdapter = new PhotoPopupAdapter(mContext, data, R.layout.item_photo_button);
        final TakePopupWin takePopupWin = new TakePopupWin(mContext, R.layout.item_photo, R.id.photo_contorl, R.id.photo_listview, photoPopupAdapter);
        bg.setVisibility(View.VISIBLE);
        final WindowManager.LayoutParams attributes = getWindow().getAttributes();
        takePopupWin.showAtLocation(findViewById(R.id.ren_bg), Gravity.BOTTOM, 0, attributes.height - takePopupWin.getHeight());
        //点击时间
        takePopupWin.setOnItemClickListener(new TakePopupWin.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0://拍照
                        toCamera();
                        //PhotoUtils.openCamera(PicDynamicActivity.this, openCamera, savefile, PhotoUtils.tempPath);
                        break;
                    case 1://相册
                        toAlbum();
                       // PhotoUtils.openAlbum(PicDynamicActivity.this, openAblum);
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

    private void toCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, openCamera);
    }

    private void toAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, openAblum);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == openCamera ) {
//            if (data == null) {
                Bitmap photo = data.getParcelableExtra("data");
                imgName = UUID.randomUUID().toString() + ".png";
                File file = FileUtil.bitmapToFile(this, photo, RENZHENG,imgName);
//                File file = new File(savefile, imgName);
                pictrueData.remove(pictrueData.size() - 1);
                pictrueData.add(file.getPath());
                pictrueData.add("add");
                uploadPictureAdapter.notifyDataSetChanged();
                deleteTemp();
               // PhotoUtils.cropImageUri(this, Uri.fromFile(file), 1, 1, 1000, 1000, CROP_CODE, savefile, imgName);

//            } else {
//                tip("请从新拍照");
//            }
        } else if (requestCode == openAblum) {
            if (data != null) {
                Uri uri = data.getData();
                if (uri != null) {
                    Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                        imgName = UUID.randomUUID().toString() + ".png";
                        pictrueData.remove(pictrueData.size() - 1);
                        pictrueData.add(path);
                        pictrueData.add("add");
                        uploadPictureAdapter.notifyDataSetChanged();
                        deleteTemp();
                    }

                    //PhotoUtils.cropImageUri(this, data.getData(), 1, 1, 1000, 1000, CROP_CODE, savefile, imgName);
                }

            } else {
                tip("请重新选择");
            }
        }
        if (requestCode == CROP_CODE && resultCode == Activity.RESULT_OK) {
            pictrueData.remove(pictrueData.size() - 1);
            pictrueData.add(RENZHENG + imgName);
            pictrueData.add("add");
            uploadPictureAdapter.notifyDataSetChanged();
            deleteTemp();
        }

    }

    private void deleteTemp() {
        File tempfile = new File(savefile, PhotoUtils.tempPath);
        if (tempfile.exists()) {
            tempfile.delete();
        }
    }

    @Override
    protected void onDestroy() {
        FileUtil.deleteFolderRecursively(new File(RENZHENG_DELETE));
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 是否触发按键为back键
        if (!TextUtils.isEmpty(desc) || pictrueData.size() != 1) {
            CommonDialog commonDialog = new CommonDialog(PicDynamicActivity.this);
            commonDialog.show();
            commonDialog.setContext("放弃此次编辑？");
            commonDialog.setClickSubmitListener(new CommonDialog.OnClickSubmitListener() {
                @Override
                public void clickSubmit() {
                    finish();
                }
            });
        }
        // 如果不是back键正常响应
        return super.onKeyDown(keyCode, event);
    }

    @OnClick({R.id.head_back, R.id.pic_submit,R.id.tv_type})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.head_back:
                finish();
                break;
            case R.id.pic_submit:
                desc = picDescription.getText().toString().trim();
                if (TextUtils.isEmpty(desc)) {
                    tip("请输入内容");
                    return;
                }
                if (pictrueData.size() == 1) {
                    tip("请上传图片");
                    return;
                }
                if (TextUtils.isEmpty(typeId)){
                    tip("请选择分类");
                    return;
                }
                postPicDynamic(desc);
                break;
            case R.id.tv_type:
                setType();
                break;
        }
    }
}
