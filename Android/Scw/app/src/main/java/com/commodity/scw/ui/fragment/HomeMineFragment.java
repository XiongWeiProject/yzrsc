package com.commodity.scw.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.commodity.yzrsc.R;
import com.commodity.scw.http.HttpManager;
import com.commodity.scw.http.HttpMothed;
import com.commodity.scw.http.IRequestConst;
import com.commodity.scw.http.ServiceInfo;
import com.commodity.scw.manager.SPKeyManager;
import com.commodity.scw.manager.SPManager;
import com.commodity.scw.ui.BaseFragment;
import com.commodity.scw.ui.activity.personalcenter.orde.MyOrdeActivity;
import com.commodity.scw.ui.activity.user.MessageManagerActivity;
import com.commodity.scw.ui.activity.user.MyFavoriteCommodActivity;
import com.commodity.scw.ui.activity.personalcenter.MineInfoActivity;
import com.commodity.scw.ui.activity.personalcenter.XiaoShouActivity;
import com.commodity.scw.ui.activity.personalcenter.ZhuanShouActivity;
import com.commodity.scw.ui.activity.personalcenter.money.MyMoneyActivity;
import com.commodity.scw.ui.activity.user.SettingActivity;
import com.commodity.scw.ui.activity.user.UseHelpActivity;
import com.commodity.scw.ui.dialog.CommonDialog;
import com.commodity.scw.utils.RongIMUtil;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;

/**
 * Created by liyushen on 2017/3/18 21:14  17183457139
 * 个人中心
 */
public class HomeMineFragment  extends BaseFragment {
    @Bind(R.id.mine_info)
    TextView mine_info;
    @Bind(R.id.img_info)
    ImageView img_info;

    @Override
    protected int getContentView() {
        return R.layout.fragment_home_mine;
    }

    @Override
    protected void initView() {
        sendRequest(0,"");
        SPKeyManager.messageTipTextViewList.add(mine_info);
    }

    @Override
    protected void initListeners() {
        mine_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpActivity(MessageManagerActivity.class);
            }
        });
        img_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpActivity(MessageManagerActivity.class);
            }
        });
    }
    @OnClick({R.id.user_phone,R.id.user_service,R.id.user_info1,R.id.user_info2,R.id.user_info3,R.id.user_info4,R.id.user_info5,R.id.user_info6,R.id.user_info7,R.id.user_info8,R.id.user_info9})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.user_info1://个人中心
                jumpActivity(MineInfoActivity.class);
                break;
            case R.id.user_info2://销售管理
                jumpActivity(XiaoShouActivity.class);
                break;
            case R.id.user_info3://转售订单管理
                jumpActivity(ZhuanShouActivity.class);
                break;
            case R.id.user_info4://我的订单
                jumpActivity(MyOrdeActivity.class);
                break;
            case R.id.user_info5://我的收藏
                jumpActivity(MyFavoriteCommodActivity.class);
                break;
            case R.id.user_info6://我的钱包
                jumpActivity(MyMoneyActivity.class);
                break;
            case R.id.user_info7://消息管理
                jumpActivity(MessageManagerActivity.class);
                break;
            case R.id.user_info8://使用帮助
                jumpActivity(UseHelpActivity.class);
                break;
            case R.id.user_info9://系统设置
                jumpActivity(SettingActivity.class);
                break;
            case R.id.user_phone://电话
                final CommonDialog commonDialog = new CommonDialog(mContext);
                commonDialog.show();
                commonDialog.setContext("是否呼叫：0755-86570026");
                commonDialog.setClickCancelListener(new CommonDialog.OnClickCancelListener() {
                    @Override
                    public void clickCance() {
                        commonDialog.dismiss();
                    }
                });
                commonDialog.setClickSubmitListener(new CommonDialog.OnClickSubmitListener() {
                    @Override
                    public void clickSubmit() {
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+"0755-86570026"));
                        startActivity(intent);
                    }
                });

                break;
            case R.id.user_service://人工服务
                //ZYRUFd31ykIRRhNO+paRu6+YsUIoF3ojin3K277sfOlhJ/rg+zl+UDnfsZtU5rZXn+kMyUHT/xLAVh8efnqMZqtdpZUyLdaH   18516393835  tvY4QloSv
                RongIMUtil.updateUserInfo("24","客服", SPManager.instance().getString(SPKeyManager.USERINFO_avatar));
                RongIM.getInstance().startConversation(getActivity(), Conversation.ConversationType.PRIVATE,"24","客服");
                break;
        }
    }

    @Override
    public void sendRequest(int tag, Object... params) {
        super.sendRequest(tag, params);
        if(tag==0){
            Map<String, String> parmMap = new HashMap<String, String>();
            HttpManager httpManager = new HttpManager(tag, HttpMothed.GET,
                    IRequestConst.RequestMethod.GetUnreadNotificationCounts, parmMap, this);
            httpManager.request();
        }
    }

    @Override
    public void OnSuccessResponse(int tag, ServiceInfo resultInfo) {
        super.OnSuccessResponse(tag, resultInfo);
        JSONObject resultJson= (JSONObject) resultInfo.getResponse();
        if (resultJson!=null&&resultJson.optBoolean("success")){
            JSONObject dataJson=resultJson.optJSONObject("data");
            int count = dataJson.optInt("systemCount")+dataJson.optInt("evaluationCount")+dataJson.optInt("followCount");
            if (count>0){
                mine_info.setText(count+"");
                mine_info.setVisibility(View.VISIBLE);
            }else {
                mine_info.setVisibility(View.GONE);
            }

        }else {
            if (resultJson!=null&&!resultJson.optBoolean("success")){
                tip(resultJson.optString("msg"));
                mine_info.setVisibility(View.GONE);
            }
        }
    }
    @Override
    public void OnFailedResponse(int tag, String code, String msg) {
        super.OnFailedResponse(tag, code, msg);
        tip(msg);
    }
}
