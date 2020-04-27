package com.commodity.yzrsc.ui.activity.personalcenter;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.commodity.yzrsc.R;
import com.commodity.yzrsc.manager.ConfigManager;
import com.commodity.yzrsc.ui.BaseActivity;
import com.commodity.yzrsc.ui.activity.user.ChangePwdActivity;
import com.commodity.yzrsc.ui.activity.user.UserAdressListActivity;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 个人信息
 * Created by yangxuqiang on 2017/3/26.
 */

public class MineInfoActivity extends BaseActivity {
//    @Bind(R.id.mine_heart)
//    LinearLayout mine_heart;//等级
    @Bind(R.id.mine_phone_text)
    TextView mine_phone_text;
    @Override
    protected int getContentView() {
        return R.layout.activity_mineinfo;
    }

    @Override
    protected void initView() {
        mine_phone_text.setText(ConfigManager.instance().getUser().getMobile());

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
    @OnClick({R.id.mine_info_phone,R.id.mine_password,R.id.mine_address,R.id.mine_back})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.mine_info_phone://修改手机号
                jumpActivityForResult(1,AlertPhone.class);
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
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mine_phone_text.setText(ConfigManager.instance().getUser().getMobile());
    }
}
