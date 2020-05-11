package com.commodity.yzrsc.ui.activity.friend;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.AppCompatImageView;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.commodity.yzrsc.manager.SPKeyManager;
import com.commodity.yzrsc.model.DynamicAllListModel;
import com.commodity.yzrsc.model.PresonInfoModel;
import com.commodity.yzrsc.ottobus.BusProvider;
import com.commodity.yzrsc.ottobus.Event;
import com.commodity.yzrsc.ui.BaseActivity;
import com.commodity.yzrsc.ui.activity.personalcenter.MineInfoActivity;
import com.commodity.yzrsc.ui.adapter.MyDynamicListAdapter;
import com.commodity.yzrsc.ui.adapter.PhotoPopupAdapter;
import com.commodity.yzrsc.ui.widget.layout.TakePopupWin;
import com.commodity.yzrsc.ui.widget.textview.CenterDrawableTextView;
import com.commodity.yzrsc.ui.widget.xlistView.XListView;
import com.commodity.yzrsc.utils.GsonUtils;
import com.commodity.yzrsc.utils.PhotoUtils;
import com.commodity.yzrsc.view.PopWinShare;
import com.squareup.otto.Subscribe;
import com.yixia.camera.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.utilities.RongUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MyDynamicActivity extends BaseActivity {
    List<DynamicAllListModel> listModels = new ArrayList<>();
    DynamicAllListModel data;
    MyDynamicListAdapter dynamicListAdapter;
    @Bind(R.id.head_back)
    ImageView headBack;
    @Bind(R.id.iv_release_dynamic)
    AppCompatImageView ivReleaseDynamic;
    @Bind(R.id.tv_nodata)
    CenterDrawableTextView tvNodata;
    @Bind(R.id.xlist_dynamic)
    XListView xlistDynamic;
    @Bind(R.id.iv_change_bg)
    ImageView ivChangeBg;
    @Bind(R.id.backage)
    View backage;

    private int pageIndex = 1;
    private int totalPage = 1;
    private String memberId = "0";
    private String minId = "0";//页码的最小id
    PopWinShare popWinShare;
    private String catalogId;
    private InputMethodManager mInputManager;
    private PopupWindow popupWindow;
    private View popupView = null;
    private EditText inputComment;
    private String nInputContentText;
    private TextView btn_submit;
    private RelativeLayout rl_input_container;
    private List<String> datas = new ArrayList<>();
    private File file;
    private PresonInfoModel presonInfoModel;
    public static final String HEADIMG = ConfigManager.ROOT + "headimg" + File.separator;
    @Override
    protected int getContentView() {
        return R.layout.activity_my_dynamic2;
    }

    @Override
    protected void initView() {
        Bundle extras = getIntent().getExtras();
        memberId = extras.getString("dynamicId");
        catalogId = extras.getString("TypeId");
        xlistDynamic.setPullLoadEnable(true);
        dynamicListAdapter = new MyDynamicListAdapter(this, listModels);
        xlistDynamic.setAdapter(dynamicListAdapter);
        sendRequest(1);
        sendRequest(2);
        datas.add("拍照上传");
        datas.add("从手机相册选择");
        datas.add("取消");
        file = new File(HEADIMG);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    @Override
    protected void initListeners() {
        xlistDynamic.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pageIndex = 1;
                        minId = "0";
                        sendRequest(1);
                        xlistDynamic.setRefreshTime(SPKeyManager.dateFormat.format(new Date()));
                    }
                }, SPKeyManager.delay_time);
            }

            @Override
            public void onLoadMore() {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pageIndex++;
                        minId = listModels.get(listModels.size() - 1).getId() + "";
                        sendRequest(1);

                    }
                }, SPKeyManager.delay_time);
            }
        });
    }

    @Override
    public void sendRequest(int tag) {
        super.sendRequest(tag);
        if (tag == 1) {
            customLoadding.show();
            if (pageIndex == 1) {
                minId = "0";
            }
            Map<String, String> parmMap = new HashMap<String, String>();
            parmMap.put("likeCount", 6+"");
            parmMap.put("commentCount", 6+"");
            parmMap.put("memberId", memberId);
            parmMap.put("catalogId", catalogId);
            parmMap.put("minId", minId);
            parmMap.put("pageSize", "" + SPKeyManager.pageSize);
            HttpManager httpManager = new HttpManager(tag, HttpMothed.GET,
                    IRequestConst.RequestMethod.GetDynamicList, parmMap, this);
            httpManager.request();
        }else if (tag ==2){
            customLoadding.show();
            Map<String, String> parmMap = new HashMap<String, String>();
            parmMap.put("memberId",ConfigManager.instance().getUser().getId());
            HttpManager httpManager = new HttpManager(tag, HttpMothed.GET,
                    IRequestConst.RequestMethod.PostGetnfo, parmMap, this);
            httpManager.request();
        }
    }

    @Override
    public void OnSuccessResponse(int tag, ServiceInfo resultInfo) {
        super.OnSuccessResponse(tag, resultInfo);
        if (tag == 1) {
            JSONObject resultJson = (JSONObject) resultInfo.getResponse();
            if (resultJson != null && resultJson.optBoolean("success")) {
                if (pageIndex == 1) {
                    listModels.clear();
                }
//                totalPage = resultJson.optJSONObject("pageInfo").optInt("totalPage");
                JSONArray dataArray = resultJson.optJSONArray("data");
                if (dataArray != null) {
                    for (int i = 0; i < dataArray.length(); i++) {
                        try {
                            data = GsonUtils.jsonToObject(dataArray.getJSONObject(i).toString(), DynamicAllListModel.class);
                            if (data != null) {
                                listModels.add(data);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    if (listModels.size() < SPKeyManager.pageSize) {
                        xlistDynamic.setPullLoadEnable(false);
                    } else {
                        xlistDynamic.setPullLoadEnable(true);
                    }
                }
                dynamicListAdapter.notifyDataSetChanged();

                if (listModels.size() > 0) {
                    tvNodata.setVisibility(View.GONE);
                } else {
                    tvNodata.setVisibility(View.VISIBLE);
                }

            } else {
                if (resultJson != null && !resultJson.optBoolean("success")) {
                    tip(resultJson.optString("msg"));
                }
            }
            xlistDynamic.stopLoadMore();
            xlistDynamic.stopRefresh();
        }else if (tag==2){
            JSONObject response = (JSONObject) resultInfo.getResponse();
            if (response.optBoolean("success")) {
                try {
                    presonInfoModel = JSON.parseObject(response.getString("data"), PresonInfoModel.class);
                    ImageLoaderManager.getInstance().displayImage(presonInfoModel.getDynamicBackGroundPic(),ivChangeBg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                tip(response.optString("msg"));
            }
        }

    }

    @Override
    public void OnFailedResponse(int tag, String code, String msg) {
        super.OnFailedResponse(tag, code, msg);
        tip(msg);
    }

    //获取传递过来信息进行相关操作
    @Subscribe
    public void NotifyChangedView(Event.NotifyChangedView event) {
        if (event.getDataObject().equals("MyDynamicActivity")) {
            Log.d("MyDynamicActivity", "传递过来的数据" + event.getDataObject());
            sendRequest(1);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        BusProvider.getInstance().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BusProvider.getInstance().unregister(this);
        BusProvider.disposeInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }


    @OnClick({R.id.head_back, R.id.iv_release_dynamic, R.id.iv_change_bg})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.head_back:
                finish();
                break;
            case R.id.iv_release_dynamic:
                if (popWinShare == null) {
                    //自定义的单击事件
                    OnClickLintener paramOnClickListener = new OnClickLintener();
                    popWinShare = new PopWinShare(MyDynamicActivity.this, paramOnClickListener, RongUtils.dip2px(110), RongUtils.dip2px(84), 1);
                    //监听窗口的焦点事件，点击窗口外面则取消显示
                    popWinShare.getContentView().setOnFocusChangeListener(new View.OnFocusChangeListener() {

                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (!hasFocus) {
                                popWinShare.dismiss();
                            }
                        }
                    });
                }
//设置默认获取焦点
                popWinShare.setFocusable(true);
//以某个控件的x和y的偏移量位置开始显示窗口
                popWinShare.showAsDropDown(ivReleaseDynamic, -175, 0);
//如果窗口存在，则更新
                popWinShare.update();
                break;
            case R.id.iv_change_bg:
                setHeadPictrue();
                break;
        }
    }

    class OnClickLintener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            popWinShare.dismiss();

            switch (v.getId()) {

                case R.id.pic:
//                    Bundle bundle=new Bundle();
//                    bundle.putString("userDynamicCatalog_Id",typeModel.get(i).getId());

                    jumpActivity(PicDynamicActivity.class);
                    break;
                case R.id.video:
                    jumpActivity(VideoDynamicActivity.class);
                    break;

                default:
                    break;
            }

        }
    }

    /**
     * popupwindow
     */
    private void setHeadPictrue() {
        PhotoPopupAdapter photoPopupAdapter = new PhotoPopupAdapter(mContext, datas, R.layout.item_photo_button);
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
                        PhotoUtils.openCamera(MyDynamicActivity.this, CAMERA_CODE, file, PhotoUtils.tempPath);
                        break;
                    case 1://相册
                        PhotoUtils.openAlbum(MyDynamicActivity.this, IMAGE_REQUEST_CODE);
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
            ivChangeBg.setImageBitmap(bitmap);
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
        customLoadding.setTip("正在上传...");
        customLoadding.show();
        MultipartBody.Builder multiparBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        multiparBody.addFormDataPart("DynamicBackGroundPic", headFile.getName(), RequestBody.create(MediaType.parse("image/*"), headFile));
        multiparBody.addFormDataPart("picType", "DynamicBackGroundPic");
        UpLoadUtils.instance().uploadPicture(IRequestConst.RequestMethod.PostChangeInfo, multiparBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                android.util.Log.e("onFailure:", e.getMessage());
                tip(e.getMessage());
                customLoadding.dismiss();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                customLoadding.dismiss();
                String string = response.body().string();
                android.util.Log.e("response:", string);

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
}
