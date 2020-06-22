package com.commodity.scw.ui.activity.store;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.commodity.yzrsc.R;
import com.commodity.scw.http.HttpManager;
import com.commodity.scw.http.HttpMothed;
import com.commodity.scw.http.IRequestConst;
import com.commodity.scw.http.ServiceInfo;
import com.commodity.scw.http.UpLoadUtils;
import com.commodity.scw.manager.ConfigManager;
import com.commodity.scw.manager.SPKeyManager;
import com.commodity.scw.model.CommodityBean;
import com.commodity.scw.ui.BaseActivity;
import com.commodity.scw.ui.adapter.AlterOrderAdapter;
import com.commodity.scw.ui.adapter.PhotoPopupAdapter;
import com.commodity.scw.ui.widget.layout.TakePopupWin;
import com.commodity.scw.ui.widget.specialview.MyGridView;
import com.commodity.scw.utils.FileUtil;
import com.commodity.scw.utils.GsonUtils;
import com.commodity.scw.utils.KeyBoardUtils;
import com.commodity.scw.utils.PhotoUtils;

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
 * Created by 328789 on 2017/5/17.
 */

public class AlterOrderActivity extends BaseActivity {
    @Bind(R.id.head_title)
    TextView title;
    @Bind(R.id.head_text_right)
    TextView head_text_right;
    @Bind(R.id.alter_desc)
    EditText alter_desc;
    @Bind(R.id.alter_grid)
    MyGridView alter_grid;
    @Bind(R.id.alter_price)
    EditText alter_price;
    @Bind(R.id.alter_goodid)
    EditText alter_goodid;
    @Bind(R.id.alter_bg)
    View alter_bg;
    private AlterOrderAdapter adpter;
    private String orderId;
    private List<String> pictrueData=new ArrayList<>();
    private List<String> moreData=new ArrayList<>();
    private List<String> taketData=new ArrayList<>();
    private String prctureNmae;

    @Override
    protected int getContentView() {
        return R.layout.activity_alterorder;
    }

    @Override
    protected void initView() {
        title.setText("修改转售商品");
        SPKeyManager.uploadmax=13;
        head_text_right.setText("保存");
        Bundle extras = getIntent().getExtras();
        orderId = extras.getString("orderId");
        if(extras.containsKey("state")){
            String state = extras.getString("state");
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
        }


        sendRequest(0,"");
    }

    @Override
    protected void initListeners() {

    }

    @OnClick({R.id.head_back, R.id.head_text_right})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.head_back:
                finish();
                break;
            case R.id.head_text_right:
                upLoad();
                break;
        }
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
                        taketData.addAll(images);
                        pictrueData.add("");
                        adpter = new AlterOrderAdapter(mContext,pictrueData, R.layout.item_upload);
                        adpter.addPictureListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                moreData.clear();
                                moreData.add("拍照上传");
                                moreData.add("从手机相册选择");
                                moreData.add("取消");
                                setHeadPictrue();
                            }
                        });
                        alter_grid.setAdapter(adpter);
                    }
                    alter_price.setText(String.valueOf(commodityBean.getPrice()));
                    alter_desc.setText(String.valueOf(commodityBean.getDescription()));
                    alter_goodid.setText(commodityBean.getCode());
                }else {
                    tip("没有数据");
                }
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
    /**
     * popupwindow
     */
    private void setHeadPictrue() {
        PhotoPopupAdapter photoPopupAdapter = new PhotoPopupAdapter(mContext, moreData, R.layout.item_photo_button);
        final TakePopupWin takePopupWin = new TakePopupWin(mContext, R.layout.item_photo, R.id.photo_contorl, R.id.photo_listview, photoPopupAdapter);
        alter_bg.setVisibility(View.VISIBLE);
        final WindowManager.LayoutParams attributes = ((Activity)mContext).getWindow().getAttributes();
        takePopupWin.showAtLocation(((Activity)mContext).findViewById(R.id.alter_b), Gravity.BOTTOM,0,attributes.height-takePopupWin.getHeight());
        //点击时间
        takePopupWin.setOnItemClickListener(new TakePopupWin.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0://拍照或摄影
                        PhotoUtils.openCamera(AlterOrderActivity.this,9,PhotoUtils.getTempPath(),PhotoUtils.tempPath);
                        break;
                    case 1://从文件中选择
                        PhotoUtils.openAlbum(AlterOrderActivity.this,10);
                        break;
                    case 2://取消

                        break;

                }

            }
        });
        takePopupWin.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                alter_bg.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 9:
                File file = new File(PhotoUtils.getTempPath(), PhotoUtils.tempPath);
                if(file.exists()){
                    prctureNmae= UUID.randomUUID().toString()+".png";
                    PhotoUtils.cropImageUri(this, Uri.fromFile(file),1,1,300,300,11,PhotoUtils.getTempPath(),prctureNmae);
                }else {
                    tip("请重新拍照");
                }
                break;
            case 10:
                if(data==null){
                    return;
                }
                Uri ablumUri = data.getData();
                if(ablumUri!=null){
                    prctureNmae = UUID.randomUUID().toString()+".png";
                    PhotoUtils.cropImageUri(this, ablumUri,1,1,300,300,11,PhotoUtils.getTempPath(), prctureNmae);
                }else {
                    tip("请重新拍照");
                }
                break;
            case 11:
                pictrueData.remove(pictrueData.size()-1);
                File pictruF = new File(ConfigManager.IMG_PATH + "/" + prctureNmae);
                if(pictruF.exists()){
                    pictrueData.add(ConfigManager.IMG_PATH+"/"+prctureNmae);
                }
                pictrueData.add("add");
                adpter.notifyDataSetChanged();
                deleteTemp();
                break;
            default:
                break;
        }
    }
    private void deleteTemp() {
        File tempfile = new File(PhotoUtils.getTempPath(), PhotoUtils.tempPath);
        if(tempfile.exists()){
            tempfile.delete();
        }
    }
    private void upLoad(){
        String resellGoodsSaleId = alter_goodid.getText().toString().trim();
        String resellPrice = alter_price.getText().toString().trim();
        String description = alter_desc.getText().toString().trim();
        if(StringUtil.isEmpty(description)){
            tip("请输入描述");
            return;
        }else if(StringUtil.isEmpty(resellPrice)){
            tip("请输入价格");
            return;
        }else if(StringUtil.isEmpty(resellGoodsSaleId)){
            tip("请输入商品号");
            return;
        }

        customLoadding.setTip("正在上传...");
        MultipartBody.Builder multiparBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        multiparBody.addFormDataPart("resellGoodsSaleId",orderId);
        multiparBody.addFormDataPart("code",resellGoodsSaleId);
        multiparBody.addFormDataPart("description",description);
        multiparBody.addFormDataPart("resellPrice",resellPrice);

        List<String> deleteList = adpter.getDeleteList();
        String deletedImages="";
        for (String d:deleteList){
            multiparBody.addFormDataPart("deletedImages",d);
        }


        for (int i=0;i<pictrueData.size();i++){
            if(taketData.contains(pictrueData.get(i))){
                continue;
            }
            String ss=pictrueData.get(i);
            File file = new File(ss);
            if(file.exists()){
                if(file.length()>1024*1024*1){
                    tip("图片大小不成超过1M");
                    return;
                }
                multiparBody.addFormDataPart("images",file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
            }
        }
        UpLoadUtils.instance().uploadPicture(IRequestConst.RequestMethod.UpdateResellGoods, multiparBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("failure:****",e.getMessage());
                if(customLoadding.isShowing()){
                    customLoadding.dismiss();
                }
                Looper.prepare();
                tip(e.getMessage());
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseStr=response.body().string();
                Log.e("onResponse:****",responseStr);
                if(customLoadding.isShowing()){
                    customLoadding.dismiss();
                }
                try {
                    JSONObject jsob=new JSONObject(responseStr);
                    if (jsob!=null&&jsob.optBoolean("success")){
                        delete(ConfigManager.IMG_PATH);
//                        File file = new File(ConfigManager.ROOT + "temp.png");
//                        FileUtil.copyFile(new File(pictrueData.get(0)),file);
//                        Bundle bundle = new Bundle();
//                        bundle.putString("goodsUrl",);
//                        jumpActivity(ProviewGoodsActivity.class,bundle);
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
    protected void onDestroy() {
        delete(ConfigManager.IMG_PATH);
        super.onDestroy();
    }

    @Override
    public void finish() {
        super.finish();
        KeyBoardUtils.hindKeyBord(alter_desc);
    }
}
