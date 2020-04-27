package com.commodity.yzrsc.ui.activity.store;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
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
import com.commodity.yzrsc.manager.SPManager;
import com.commodity.yzrsc.model.CommodityBean;
import com.commodity.yzrsc.ui.BaseActivity;
import com.commodity.yzrsc.ui.activity.general.PhotoSelectActivity;
import com.commodity.yzrsc.ui.activity.shortvideo.VideoRecorderActivity;
import com.commodity.yzrsc.ui.adapter.AlterOrderAdapter;
import com.commodity.yzrsc.ui.adapter.PhotoPopupAdapter;
import com.commodity.yzrsc.ui.widget.layout.TakePopupWin;
import com.commodity.yzrsc.utils.FileUtil;
import com.commodity.yzrsc.utils.GsonUtils;
import com.commodity.yzrsc.utils.KeyBoardUtils;
import com.commodity.yzrsc.utils.PhotoUtils;
import com.commodity.yzrsc.utils.VideoUtils;
import com.ta.utdid2.android.utils.StringUtils;

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
import butterknife.OnClick;
import cn.yohoutils.StringUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by liyushen on 2017/6/3 19:04
 * 上传宝贝商品
 */
public class AlertUploadGoodsActivity extends BaseActivity {
    private static final String TAG = AlertUploadGoodsActivity.class.getSimpleName();
    @Bind(R.id.backage_baby)
    View backage;
    @Bind(R.id.head_title)
    TextView title;
    @Bind(R.id.head_text_right)
    TextView head_text_right;
    @Bind(R.id.upload_add_vdio)
    ImageView upload_add_vdio;
    @Bind(R.id.item_video_delete)
    ImageView item_video_delete;
    @Bind(R.id.upload_grid)
    GridView upload_grid;
    @Bind(R.id.type_value)
    TextView type_value_view;

    @Bind(R.id.upload_desc)
    EditText upload_desc;//宝贝描述
    @Bind(R.id.upload_price)
    EditText upload_price;//宝贝价格
    @Bind(R.id.upload_size)
    EditText upload_size;//宝贝尺寸
    @Bind(R.id.upload_express)
    EditText upload_express;//宝贝邮费
    @Bind(R.id.upload_goods_on)
    EditText upload_goods_on;//货号
    @Bind(R.id.upload_weight)
    EditText upload_weight;//重量

    private List<String> pictrueData = new ArrayList<>();
    private List<String> moreData = new ArrayList<>();
    private List<String> takePictrueData = new ArrayList<>();
    private AlterOrderAdapter uploadPictureAdapter;
    //上传图片 视频的标识
    private boolean isPicture = true;//默认是图片
    //拍摄图片的名称
    private String prctureNmae;
    //video存储路径
    private String videoPath = "";
    //private String videoCompressPath = ConfigManager.VIDEO_PATH_SD+"/compressorvideo/";
    private int openCamera = 1;
    private int openCrop = 4;
    private int openAblumVideo = 5;

    public static MediaType MEDIA_TYPE_PNG = MediaType.parse("image/*");
    private MediaType MEDIA_TYPE_VIDEO = MediaType.parse("application/octet-stream");
    private String typeCode = "1";
    private String upload_desc_value;
    private String orderId;
    private String deleteViewUrl;

    @Override
    protected int getContentView() {
        return R.layout.activity_uploadbaby;
    }

    @Override
    protected void initView() {
        String state = getIntent().getExtras().getString("state");
        switch (state){
            case "zhuanshou":
                break;
            case "upload":
                title.setText("修改上传商品");
                break;
            case "xiajia":
                title.setText("修改下架商品");
                break;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        head_text_right.setText("保存");
        pictrueData.clear();
        uploadPictureAdapter = new AlterOrderAdapter(mContext, pictrueData, R.layout.item_upload);
        upload_grid.setAdapter(uploadPictureAdapter);

        orderId = getIntent().getExtras().getString("orderId");
        sendRequest(0,"");
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

    @Override
    protected void initListeners() {
        uploadPictureAdapter.addPictureListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moreData.clear();
                moreData.add("拍照上传");
                moreData.add("从手机相册选择");
                moreData.add("取消");
                isPicture = true;
                setHeadPictrue(0, backage, R.id.baby_bg, moreData);
            }
        });

        item_video_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                videoPath = "";
                upload_add_vdio.setImageBitmap(BitmapFactory.decodeResource(AlertUploadGoodsActivity.this.getResources(), R.drawable.icon_add_vdio));
                item_video_delete.setVisibility(View.GONE);
            }
        });

        head_text_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                head_text_right.setClickable(false);
                initUpload();
                myHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        head_text_right.setClickable(true);
                    }
                },1500);
            }
        });
    }

    @OnClick({R.id.head_back, R.id.head_text_right, R.id.upload_add_vdio, R.id.upload_fenli})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_back:
                finish();
                break;
            case R.id.upload_add_vdio://添加视屏
                moreData.clear();
                moreData.add("拍摄视频");
                moreData.add("从文件中选择");
                moreData.add("取消");
                isPicture = false;
                setHeadPictrue(0, backage, R.id.baby_bg, moreData);

                break;
            case R.id.upload_fenli://分类
                jumpActivityForResult(GoodsTypeActivity.resultCode, GoodsTypeActivity.class);
                break;
        }
    }

    //准备上传
    private void initUpload() {
        String upload_desc_value = upload_desc.getText().toString().trim();
        String upload_price_value = upload_price.getText().toString().trim();
        String upload_express_value = upload_express.getText().toString().trim();

        if (StringUtils.isEmpty(upload_desc_value)) {
            tip("请输入商品描述");
            return;
        } else if (upload_desc_value.length() < 8||upload_desc_value.length()>60) {
            tip("商品描述不能少于8个字,大于60个字");
            return;
        } else if (StringUtils.isEmpty(upload_price_value)) {
            tip("请输入商品价格");
            return;
        } else if (StringUtils.isEmpty(typeCode)) {
            tip("请选择分类");
            return;
        } else if (StringUtils.isEmpty(upload_express_value)) {
            tip("请输入邮费");
            return;
        } else if (pictrueData.size() < 1) {
            tip("请上传图片");
            return;
        }

        if (!StringUtil.isEmpty(upload_express_value) && Double.valueOf(upload_express_value) == 0) {
            tip("运费不能为零");
            return;
        } else if (!StringUtil.isEmpty(upload_price_value) && Double.valueOf(upload_price_value) == 0) {
            tip("价格不能为零");
            return;
        }

        File videoF = new File(videoPath);
        if (!videoF.exists()) {//如果没有视频
            startUpload(null);
        } else {//如果有视频
            if (videoF.length() > 1024 * 1024 * 30) {
                tip("视频太大，请重新拍摄");
                Log.e("--------------", videoF.length() + "");
                upload_add_vdio.setImageBitmap(BitmapFactory.decodeResource(AlertUploadGoodsActivity.this.getResources(), R.drawable.icon_add_vdio));
                return;
            }
            if (!customLoadding.isShowing()) {
                customLoadding.show();
            }
            customLoadding.setTip("正在压缩视频...");
            startUpload(videoF);
        }

    }
    MultipartBody.Builder  multiparBody;
    /**
     * 开始上传  压缩图片
     */
    private void startUpload(File fileoutput) {
        if (!customLoadding.isShowing()) {
            customLoadding.show();
        }
        customLoadding.setTip("正在上传...");
        upload_desc_value = upload_desc.getText().toString().trim();
        String upload_price_value = upload_price.getText().toString().trim();
        String upload_size_value = upload_size.getText().toString().trim();
        String upload_express_value = upload_express.getText().toString().trim();
        String upload_goods_on_value = upload_goods_on.getText().toString().trim();
        String weight = upload_weight.getText().toString().trim();

        multiparBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        multiparBody.addFormDataPart("description", upload_desc_value);
        multiparBody.addFormDataPart("goodsSaleId", orderId);
        multiparBody.addFormDataPart("sellPrice", upload_price_value);
        String size = upload_size_value.replace("毫米", "");
        multiparBody.addFormDataPart("specification", size);
        multiparBody.addFormDataPart("code", upload_goods_on_value);
        multiparBody.addFormDataPart("postage", upload_express_value);
        String w = weight.replace("克", "");
        if(com.yixia.camera.util.StringUtils.isEmpty(w)){
            w="0";
        }
        multiparBody.addFormDataPart("weight", w);
        multiparBody.addFormDataPart("kind", typeCode);

        List<String> deleteList = uploadPictureAdapter.getDeleteList();
        if(deleteList!=null&&deleteList.size()>0){
            for (String s:deleteList){
                multiparBody.addFormDataPart("deletedImages",s);
            }
        }


        //添加视频
        if (fileoutput!=null&&fileoutput.exists()) {
            if (fileoutput.length() > 1024 * 1024 * 10) {
                tip("视频大小不能超过10M");
                customLoadding.dismiss();
                return;
            }
            multiparBody.addFormDataPart("videos", fileoutput.getName(), RequestBody.create(MEDIA_TYPE_VIDEO, fileoutput));
            multiparBody.addFormDataPart("deletedVideos",deleteViewUrl);
        }

        //添加图片
        final PhotoUtils photoUtils=new PhotoUtils();
        final long time = System.currentTimeMillis();
        final List<String>  uploadList=new ArrayList<>();
        final FileUtil fileUtil=new FileUtil();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < pictrueData.size(); i++) {
                    if(takePictrueData.contains(pictrueData.get(i))|| com.yixia.camera.util.StringUtils.isEmpty(pictrueData.get(i))){
                        continue;
                    }
                    if (fileUtil.copyFile(pictrueData.get(i),ConfigManager.IMG_PATH+time+i+".jpg")){
                        uploadList.add(ConfigManager.IMG_PATH+time+i+".jpg");
                    }
                }
                for (int i = 0; i < uploadList.size(); i++) {
                    Bitmap bitmap=photoUtils.compressPixelPhotos(uploadList.get(i));
                    File picFile=photoUtils.saveBitmapFile(bitmap,ConfigManager.IMG_PATH+time+i+".jpg");
                    Log.e("startUpload: ", picFile.getPath());
                    multiparBody.addFormDataPart("images", picFile.getName(), RequestBody.create(MEDIA_TYPE_PNG, picFile));
                }
                Message message = new Message();
                message.what=1201;
                myHandler.sendMessage(message);
            }
        }).start();
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
    private void UpLoad(MultipartBody.Builder multiparBody){
        Log.e("UpLoad: ", "开始UpLoad");
        UpLoadUtils.instance().uploadPicture(IRequestConst.RequestMethod.UpdateMyGoods, multiparBody, new Callback() {
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
                Log.e(TAG, "onResponse: "+responseStr );
                if (customLoadding.isShowing()) {
                    customLoadding.dismiss();
                }
                try {
                    JSONObject jsob = new JSONObject(responseStr);
                    if (jsob != null && jsob.optBoolean("success")) {
                        setResult(RESULT_OK);
                        finish();
                        Looper.prepare();
                        tip("修改成功");
                        Looper.loop();
                    }else {
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




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GoodsTypeActivity.resultCode) {
            if (data!=null){
                typeCode = data.getStringExtra("typeCode");
                String typeCode_value = data.getStringExtra("typeCode_value");
                type_value_view.setText(typeCode_value);
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
                    upload_add_vdio.setImageBitmap(thumbnail);
                    item_video_delete.setVisibility(View.VISIBLE);
                    saveVideoPath(videoPath);
                } else {
                    tip("请重新拍摄");
                }
            } else if (requestCode == openCamera) {//打开相机
                File file = new File(PhotoUtils.getTempPath(), PhotoUtils.tempPath);
                if (file.exists()) {
                    prctureNmae = UUID.randomUUID().toString() + ".png";
                    PhotoUtils.cropImageUri(this, Uri.fromFile(file), 1, 1, 300, 300, openCrop, PhotoUtils.getTempPath(), prctureNmae);
                } else {
                    tip("请重新拍照");
                }
            } else if (requestCode == openCrop) {//裁剪后
                File file = new File(ConfigManager.IMG_PATH + "/" + prctureNmae);
                if (file.exists()) {
                    pictrueData.remove("");
                    pictrueData.add(ConfigManager.IMG_PATH + "/" + prctureNmae);
                    pictrueData.add("");
                }
                uploadPictureAdapter.notifyDataSetChanged();
                deleteTemp();
            } else if (requestCode == 1001) {//打开选择界面
                 pictrueData.remove("");
                if (MainApplication.mHashMap.containsKey("SelectPhotos")) {
                    List<String> resultPhots = (List<String>) MainApplication.mHashMap.get("SelectPhotos");
                    for (int i = 0; i < resultPhots.size(); i++) {
                        if (!pictrueData.contains(resultPhots.get(i))) {
                            pictrueData.add(resultPhots.get(i));
                        }
                    }
                    pictrueData.add("");
                }
                if (pictrueData == null || pictrueData.size() == 0) {
                    uploadPictureAdapter.notifyDataSetChanged();
                } else {
                    uploadPictureAdapter.notifyDataSetChanged();
                }
            }else if (requestCode == 1002) {//拍摄视频返回
                // 成功
                 String  path = data.getStringExtra("path");
                 videoPath = path ;
                 Log.e(TAG, "onActivityResult: "+videoPath );
                 if (StringUtils.isEmpty(videoPath)) {
                    tip("请重新拍摄");
                 }else {
                    Bitmap thumbnail = VideoUtils.getThumbnail(videoPath);
                    upload_add_vdio.setImageBitmap(thumbnail);
                    item_video_delete.setVisibility(View.VISIBLE);
                    saveVideoPath(videoPath);
                 }
            }
        }
    }

    private void deleteTemp() {
        File tempfile = new File(PhotoUtils.getTempPath(), PhotoUtils.tempPath);
        if (tempfile.exists()) {
            tempfile.delete();
        }
    }

    /**
     * popupwindow
     */
    private void setHeadPictrue(int position, final View backage, int bg_id, List moreData) {
        KeyBoardUtils.hindKeyBord(upload_desc);
        PhotoPopupAdapter photoPopupAdapter = new PhotoPopupAdapter(mContext, moreData, R.layout.item_photo_button);
        final TakePopupWin takePopupWin = new TakePopupWin(mContext, R.layout.item_photo, R.id.photo_contorl, R.id.photo_listview, photoPopupAdapter);
        backage.setVisibility(View.VISIBLE);
        final WindowManager.LayoutParams attributes = ((Activity) mContext).getWindow().getAttributes();
        takePopupWin.showAtLocation(((Activity) mContext).findViewById(bg_id), Gravity.BOTTOM, 0, attributes.height - takePopupWin.getHeight());
        //点击时间
        takePopupWin.setOnItemClickListener(new TakePopupWin.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0://拍照或摄影
                        if (isPicture) {//拍照
                            PhotoUtils.openCamera(AlertUploadGoodsActivity.this, openCamera, PhotoUtils.getTempPath(), PhotoUtils.tempPath);
                        } else {
                            if (PhotoUtils.getTempPath() == null) {
                                tip("请插入内存卡");
                                return;
                            }
                            jumpActivityForResult(1002,VideoRecorderActivity.class);
                          //  VideoUtils.openVideo(UploadGoodsActivity.this, openVideo);
                        }
                        break;
                    case 1://从文件中选择
                        if (isPicture) {//相册
                            Intent intent = new Intent(getApplicationContext(), PhotoSelectActivity.class);
                            MainApplication.mHashMap.put("SelectPhotos", pictrueData);
                            startActivityForResult(intent, 1001);
                        } else {
                            PhotoUtils.openAlbumVideo(AlertUploadGoodsActivity.this, openAblumVideo);
                        }
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

    @Override
    protected void onDestroy() {
        delete(ConfigManager.IMG_PATH);
        super.onDestroy();
    }

    public void saveVideoPath(String path) {
        SPManager.put(this, "videoPath", path);
    }

    @Override
    public void sendRequest(int tag, Object... params) {
        super.sendRequest(tag, params);
        customLoadding.show();
        if (tag == 0) {
            Map<String, String> parmMap = new HashMap<String, String>();
            parmMap.put("goodsSaleId", orderId);
            HttpManager httpManager = new HttpManager(tag, HttpMothed.GET,
                    IRequestConst.RequestMethod.GetGoodsDetail, parmMap, this);
            httpManager.request();
        }
    }
    @Override
    public void OnSuccessResponse(int tag, ServiceInfo resultInfo) {
        super.OnSuccessResponse(tag, resultInfo);
        JSONObject response = (JSONObject) resultInfo.getResponse();
        if(response != null&&response.optBoolean("success")){
            if(tag==0){
                JSONObject dataGson = response.optJSONObject("data");
                CommodityBean commodityBean = GsonUtils.jsonToObject(dataGson.toString(), CommodityBean.class);
                if(commodityBean!=null){
                    List<String> images = commodityBean.getImages();
                    if(images!=null){
                        pictrueData.addAll(images);
                        takePictrueData.addAll(images);
                        pictrueData.add("");

                    }
                    uploadPictureAdapter.notifyDataSetChanged();

                    upload_price.setText(String.valueOf(commodityBean.getPrice()));
                    upload_desc.setText(String.valueOf(commodityBean.getDescription()));
                    upload_goods_on.setText(commodityBean.getCode());
                    upload_size.setText(commodityBean.getSpecification().replace("毫米",""));
                    upload_express.setText(commodityBean.getPostage()+"");
                    upload_weight.setText(commodityBean.getWeight().replace("克",""));
                    ImageLoaderManager.getInstance().displayImage(commodityBean.getVideoSnap(),upload_add_vdio,R.drawable.icon_add_vdio);
                    type_value_view.setText(commodityBean.getKind());
                    typeCode=commodityBean.getKindId();
                    deleteViewUrl = commodityBean.getVideo();
                }else {
                    tip("没有数据");
                }
            }
        }else {
            tip(response.optString("msg"));
        }
    }

    @Override
    public void finish() {
        super.finish();
        KeyBoardUtils.hindKeyBord(upload_desc);
    }
}
