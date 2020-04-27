package com.commodity.yzrsc.ui.activity.store;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.commodity.yzrsc.MainApplication;
import com.commodity.yzrsc.R;
import com.commodity.yzrsc.http.HttpManager;
import com.commodity.yzrsc.http.HttpMothed;
import com.commodity.yzrsc.http.IRequestConst;
import com.commodity.yzrsc.http.ServiceInfo;
import com.commodity.yzrsc.manager.ConfigManager;
import com.commodity.yzrsc.model.store.ShardGridEntity;
import com.commodity.yzrsc.ui.BaseActivity;
import com.commodity.yzrsc.ui.activity.commodity.CommodityDetailActivity;
import com.commodity.yzrsc.ui.adapter.ShardGridAdapter;
import com.commodity.yzrsc.ui.widget.specialview.MyGridView;
import com.commodity.yzrsc.utils.ImageUtil;
import com.commodity.yzrsc.utils.SharetUtil;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cn.yohoutils.StringUtil;

/**
 * 上传完成
 * Created by 328789 on 2017/5/8.
 */

public class ProviewGoodsActivity extends BaseActivity {
    @Bind(R.id.head_title)
    TextView title;
    @Bind(R.id.upload_success_shard)
    MyGridView upload_success_shard;
    private List<ShardGridEntity> data=new ArrayList<>();
    private ShardGridAdapter shardGridAdapter;
    private File file;
    private String goodsSaleUrl;
    private Bitmap bitmap;
    private String desc;
    private String id;

    @Override
    protected int getContentView() {
        return R.layout.activity_proviewgoods;
    }

    @Override
    protected void initView() {
        title.setText("上传成功");
//        data.add(new ShardGridEntity(R.drawable.icon_weixin_p,"朋友圈图文"));
        data.add(new ShardGridEntity(R.drawable.icon_wx_f,"微信好友"));
        data.add(new ShardGridEntity(R.drawable.icon_weixi_f,"朋友圈链接"));
        data.add(new ShardGridEntity(R.drawable.icon_qq_,"QQ"));
        data.add(new ShardGridEntity(R.drawable.icon_qq_work,"QQ空间"));
        data.add(new ShardGridEntity(R.drawable.icon_weibo_,"新浪微博"));
        data.add(new ShardGridEntity(R.drawable.icon_download,"保存图片"));
        data.add(new ShardGridEntity(R.drawable.icon_copy,"复制链接"));

        shardGridAdapter = new ShardGridAdapter(mContext, data, R.layout.item_grid_proview);
        upload_success_shard.setAdapter(shardGridAdapter);
        file = new File(ConfigManager.ROOT + "temp.png");
        goodsSaleUrl = getIntent().getExtras().getString("goodsUrl");
        desc = getIntent().getExtras().getString("desc");
        id = getIntent().getExtras().getString("id");
    }

    @Override
    protected void initListeners() {
        upload_success_shard.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            private UMImage umImage;

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String content="文巴掌商城";
                if(file.exists()){
                    bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    umImage = new UMImage(ProviewGoodsActivity.this, bitmap);
                    umImage.setThumb(new UMImage(ProviewGoodsActivity.this, bitmap));
                }else {
                    umImage = new UMImage(ProviewGoodsActivity.this, R.drawable.ic_launcher);
                    umImage.setThumb(new UMImage(ProviewGoodsActivity.this, R.drawable.ic_launcher));
                }
                if(goodsSaleUrl==null){
                    tip("宝贝浏览URL为空");
                    return;
                }

                switch (position){
//                    case 0:
//                        List<String> strings = new ArrayList<>();
//                        strings.add(file.getAbsolutePath());
//                        SharetUtil.shareMoreImage(strings,mContext,desc);
//                        break;
                    case 0:
//                        SharetUtil.share(ProviewGoodsActivity.this, SHARE_MEDIA.WEIXIN,content,umImage,goodsSaleUrl);
                        SharetUtil.shareUrl(ProviewGoodsActivity.this,SHARE_MEDIA.WEIXIN,goodsSaleUrl,desc,umImage);
                        break;
                    case 1:
                        SharetUtil.shareUrl(ProviewGoodsActivity.this,SHARE_MEDIA.WEIXIN_CIRCLE,goodsSaleUrl,desc,umImage);
                        break;
                    case 2:
//                        SharetUtil.share(ProviewGoodsActivity.this, SHARE_MEDIA.QQ,content,umImage,goodsSaleUrl);
                        SharetUtil.shareUrl(ProviewGoodsActivity.this,SHARE_MEDIA.QQ,goodsSaleUrl,desc,umImage);
                        break;
                    case 3:
//                        SharetUtil.share(ProviewGoodsActivity.this, SHARE_MEDIA.QZONE,content,umImage,goodsSaleUrl);
                        SharetUtil.shareUrl(ProviewGoodsActivity.this,SHARE_MEDIA.QZONE,goodsSaleUrl,desc,umImage);
                        break;
                    case 4:
//                        SharetUtil.share(ProviewGoodsActivity.this, SHARE_MEDIA.SINA,content,umImage,goodsSaleUrl);
                        SharetUtil.shareUrl(ProviewGoodsActivity.this,SHARE_MEDIA.SINA,goodsSaleUrl,desc,umImage);
                        break;
                    case 5:
                        if(bitmap!=null)
                        ImageUtil.saveImageToGallery(ProviewGoodsActivity.this,bitmap);
                        break;
                    case 6:
                        ClipboardManager clipboardManager = (ClipboardManager) MainApplication.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clipurl = ClipData.newPlainText("url", goodsSaleUrl);
                        clipboardManager.setPrimaryClip(clipurl);

                        Toast.makeText((Activity) mContext, "复制链接成功", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        });

    }
    @OnClick({R.id.head_back,R.id.success_upload,R.id.success_proview})
    public void click(View v){
        switch (v.getId()){
            case R.id.head_back:
                finish();
                break;
            case R.id.success_upload:
                jumpActivity(UploadGoodsActivity.class);
                finish();
                break;
            case R.id.success_proview://预览宝贝
                if(StringUtil.isEmpty(goodsSaleUrl)){
                    tip("浏览宝贝失败");
                    return;
                }
//                Bundle bundle = new Bundle();
//                bundle.putString("url", goodsSaleUrl);
//                bundle.putString("falg","goods");
//                jumpActivity(StoreProviewActivity.class,bundle);

//                Bundle bundle=new Bundle();
//                bundle.putString("title","预览宝贝");
//                bundle.putString("content_url",goodsSaleUrl);
//                jumpActivity(GeneralWebViewActivity.class,bundle);
                Bundle bundle = new Bundle();
                bundle.putString("goodsSaleId",id);
                bundle.putString("proview","proview");
                jumpActivity(CommodityDetailActivity.class,bundle);
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        if(file.exists()){
            file.delete();
        }
        super.onDestroy();
    }

    @Override
    public void sendRequest(int tag, Object... params) {
        super.sendRequest(tag, params);
        customLoadding.show();
        if(tag==0){
            HashMap<String, String> map = new HashMap<>();
            map.put("goodsSaleId","");
            new HttpManager(tag, HttpMothed.GET, IRequestConst.RequestMethod.GetGoodsDetail).request();
        }
    }

    @Override
    public void OnSuccessResponse(int tag, ServiceInfo resultInfo) {
        super.OnSuccessResponse(tag, resultInfo);
        JSONObject response = (JSONObject) resultInfo.getResponse();
        if(response.optBoolean("success")){
            JSONObject data = response.optJSONObject("data");
            goodsSaleUrl = data.optString("goodsSaleUrl");
        }else {
            tip(response.optString("msg"));
        }
    }
}
