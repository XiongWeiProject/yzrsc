package com.commodity.scw.ui.activity.store;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.commodity.scw.R;
import com.commodity.scw.manager.ImageLoaderManager;
import com.commodity.scw.ui.BaseActivity;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 已提交材料
 * Created by yangxuqiang on 2017/4/4.
 */

public class YiTijiaoActivity extends BaseActivity {

    @Bind(R.id.head_title)
    TextView title;
    @Bind(R.id.shenhe_image)
    ImageView shenhe_image;
    @Bind(R.id.shenhe_name)
    TextView shenhe_name;
    @Bind(R.id.tijiao_state)
    TextView tijiao_state;
    @Bind(R.id.text_ren)
    TextView text_ren;
    @Override
    protected int getContentView() {
        return R.layout.activity_yitiao;
    }

    @Override
    protected void initView() {
        Bundle extras = getIntent().getExtras();
        String avatar = extras.getString("avatar");
        String shopName = extras.getString("shopName");
        int sellAuthenticatedStatus = extras.getInt("sellAuthenticatedStatus");
        switch (sellAuthenticatedStatus){
            case 0:
                tijiao_state.setText("待审核");
                break;
            case 1:
                tijiao_state.setText("审核通过");
                text_ren.setText("审核通过");
                break;
            case 2:
                tijiao_state.setText("审核未通过");
                text_ren.setText("审核未通过");
                break;
            case 3:
                tijiao_state.setText("未提交审核");
                text_ren.setText("未提交审核");
                break;
        }
        ImageLoaderManager.getInstance().displayImage(avatar,shenhe_image);
        shenhe_name.setText("店铺名称:"+shopName);
    }

    @Override
    protected void initListeners() {

    }
    @OnClick({R.id.head_back,R.id.shenhe_phone})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.head_back:
                finish();
                break;
            case R.id.shenhe_phone:
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+"0755-86570026"));
                startActivity(intent);
                break;
        }
    }
}
