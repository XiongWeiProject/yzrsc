package com.commodity.scw.ui.activity.user;

import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONException;
import com.commodity.yzrsc.R;
import com.commodity.scw.http.HttpManageNew;
import com.commodity.scw.http.HttpManager;
import com.commodity.scw.http.HttpMothed;
import com.commodity.scw.http.IRequestConst;
import com.commodity.scw.http.ServiceInfo;
import com.commodity.scw.manager.ImageLoaderManager;
import com.commodity.scw.manager.SPKeyManager;
import com.commodity.scw.model.PersonInfo;
import com.commodity.scw.rongyun.SealUserInfoManager;
import com.commodity.scw.rongyun.server.network.http.HttpException;
import com.commodity.scw.ui.BaseActivity;
import com.commodity.scw.ui.widget.imageview.CircleImageView;
import com.commodity.scw.utils.DateUtil;
import com.commodity.scw.utils.RongIMUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Console;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import io.rong.imkit.RongIM;
import io.rong.imkit.userInfoCache.RongUserInfoManager;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

/**
 * Created by liyushen on 2017/4/2 16:10
 * 消息管理界面
 */
public class MessageManagerActivity extends BaseActivity {
    @Bind(R.id.ll_function)
    LinearLayout ll_function;
    List<TextView> tipTextList = new ArrayList<>();
    List<PersonInfo>  conversationList=new ArrayList<>();
    @Override
    protected int getContentView() {
        return R.layout.activity_message_manager;
    }

    @Override
    protected void initView() {
        ll_function.addView(getItemView(R.drawable.icon_xttz2x, "系统通知", 0, true));
        ll_function.addView(getItemView(R.drawable.icon_pl2x, "评论", 0, true));
        ll_function.addView(getItemView(R.drawable.icon_gz2x, "被关注通知", 0, true));
        ll_function.addView(getItemView(R.drawable.icon_lxr2x, "联系人", 0, false));
        View viewItem = View.inflate(mContext, R.layout.item_one_textview, null);
        TextView tv_text1 = (TextView) viewItem.findViewById(R.id.tv_text1);
        tv_text1.setText("最近联系人");
        ll_function.addView(viewItem);
        //获取最近联系人
        getConversationList();
    }

    @Override
    protected void initListeners() {

    }

    @Override
    public void sendRequest(int tag, Object... params) {
        super.sendRequest(tag, params);
        if (tag == 1) {//未读通知数量
            Map<String, String> parmMap = new HashMap<String, String>();
            HttpManager httpManager = new HttpManager(tag, HttpMothed.GET,
                    IRequestConst.RequestMethod.GetUnreadNotificationCounts, parmMap, this);
            httpManager.request();
        }
        if (tag == 2) {  // 通过Id列表获取联系人信息
           // parmMap.put("contactIds", (String) params[0]);
            String pa="";
            if (params[0]!=null){
                List<PersonInfo> list= (List<PersonInfo>) params[0];//request.contactIds=49&request.contactIds=85
                for (int i = 0; i < list.size(); i++) {
                    pa=pa + "contactIds="+list.get(i).getImId()+"&";
                }
                if (pa.endsWith("&")){
                    pa=pa.substring(0,pa.length()-1);
                }
            }
            Map<String, String> parmMap = new HashMap<String, String>();
            HttpManager httpManager = new HttpManager(tag, HttpMothed.GET,
                    IRequestConst.RequestMethod.GetContactInfo+"?"+pa, parmMap, this);
            httpManager.request();
        }
    }

    @Override
    public void OnSuccessResponse(int tag, ServiceInfo resultInfo) {
        super.OnSuccessResponse(tag, resultInfo);
        if (tag == 1) {
            JSONObject resultJson = (JSONObject) resultInfo.getResponse();
            if (resultJson != null && resultJson.optBoolean("success")) {
                JSONObject dataJson = resultJson.optJSONObject("data");
                for (int i = 0; i < tipTextList.size(); i++) {
                    if (tipTextList.get(i).getTag().equals("系统通知")) {
                        setTipText(dataJson.optInt("systemCount"), tipTextList.get(i));
                    } else if (tipTextList.get(i).getTag().equals("评论")) {
                        setTipText(dataJson.optInt("evaluationCount"), tipTextList.get(i));
                    }
                    if (tipTextList.get(i).getTag().equals("被关注通知")) {
                        setTipText(dataJson.optInt("followCount"), tipTextList.get(i));
                    }
                }
                int count = dataJson.optInt("systemCount")+dataJson.optInt("evaluationCount")+dataJson.optInt("followCount");
                if (count>0){
                    for (int i = 0; i < SPKeyManager.messageTipTextViewList.size(); i++) {
                        SPKeyManager.messageTipTextViewList.get(i).setText(count+"");
                        SPKeyManager.messageTipTextViewList.get(i).setVisibility(View.VISIBLE);
                    }
                }else {
                    for (int i = 0; i < SPKeyManager.messageTipTextViewList.size(); i++) {
                        SPKeyManager.messageTipTextViewList.get(i).setVisibility(View.GONE);
                    }
                }
            } else {
                if (resultJson != null && !resultJson.optBoolean("success")) {
                    tip(resultJson.optString("msg"));
                    for (int i = 0; i < SPKeyManager.messageTipTextViewList.size(); i++) {
                        SPKeyManager.messageTipTextViewList.get(i).setVisibility(View.GONE);
                    }
                    for (int i = 0; i < tipTextList.size(); i++) {
                        if (tipTextList.get(i).getTag().equals("系统通知")) {
                            setTipText(0, tipTextList.get(i));
                        } else if (tipTextList.get(i).getTag().equals("评论")) {
                            setTipText(0, tipTextList.get(i));
                        }
                        if (tipTextList.get(i).getTag().equals("被关注通知")) {
                            setTipText(0, tipTextList.get(i));
                        }
                    }
                }
            }
        } else if (tag == 2) {
            JSONObject resultJson = (JSONObject) resultInfo.getResponse();
            if (resultJson != null && resultJson.optBoolean("success")) {
                JSONArray dataJson = resultJson.optJSONArray("data");
                if (dataJson != null && dataJson.length() > 0) {
                    for (int i = 0; i < dataJson.length(); i++) {
                        for (int j = 0; j < conversationList.size(); j++) {
                            try {
                                if(dataJson.getJSONObject(i).optString("imId").equals(conversationList.get(j).getImId())){
                                    conversationList.get(j).setAvatar(dataJson.getJSONObject(i).optString("avatar"));
                                    conversationList.get(j).setImToken(dataJson.getJSONObject(i).optString("imToken"));
                                    conversationList.get(j).setImId(dataJson.getJSONObject(i).optString("imId"));
                                    conversationList.get(j).setName(dataJson.getJSONObject(i).optString("name"));
                                }
                            } catch (org.json.JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    for (int i = 0; i <conversationList.size() ; i++) {
                        ll_function.addView(getItemView2(conversationList.get(i),true));
                    }
                }
            }
        }
    }

    @Override
    public void OnFailedResponse(int tag, String code, String msg) {
        super.OnFailedResponse(tag, code, msg);
        tip(msg);
    }

    //每一个子View工具方法
    private View getItemView(int icoId, final String str1, int tipNum, final boolean showLine) {
        View view = View.inflate(mContext, R.layout.item_message_manager1, null);
        TextView tv_text1 = (TextView) view.findViewById(R.id.tv_text1);
        View view_ico1 = view.findViewById(R.id.view_ico1);
        TextView tv_text2 = (TextView) view.findViewById(R.id.tv_text2);
        tv_text2.setTag(str1);
        tv_text1.setText(str1);
        tipTextList.add(tv_text2);
        view_ico1.setBackgroundResource(icoId);
        if (!showLine) {
            view.findViewById(R.id.view_line).setVisibility(View.GONE);
        }
        setTipText(tipNum, tv_text2);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (str1.equals("系统通知")) {
                    jumpActivity(SystemNoticeActivity.class);
                } else if (str1.equals("评论")) {
                    jumpActivity(CommentsActivity.class);
                } else if (str1.equals("被关注通知")) {
                    jumpActivity(FocusOnActivity.class);
                } else if (str1.equals("联系人")) {
                    jumpActivity(ContactPersonActivity.class);
                }
            }
        });
        return view;
    }

    //每一个子View工具方法
    private View getItemView2(final PersonInfo personInfo, final boolean showLine ) {
        View view = View.inflate(mContext, R.layout.item_person_info, null);
        CircleImageView view_ico1= (CircleImageView) view.findViewById(R.id.view_ico1);
        TextView tv_text1 = (TextView) view.findViewById(R.id.tv_text1);
        TextView tv_text2 = (TextView) view.findViewById(R.id.tv_text2);
        tv_text1.setText(personInfo.getName());
        tv_text2.setText(personInfo.getReceivedTime());
        ImageLoaderManager.getInstance().displayImage(personInfo.getAvatar(),view_ico1,R.drawable.ico_defalut_header);
        if (!showLine) {
            view.findViewById(R.id.view_line).setVisibility(View.GONE);
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RongIMUtil.startConversation(MessageManagerActivity.this, personInfo.getImId(), personInfo.getName());
            }
        });
        return view;
    }

    //设置消息数字
    private void setTipText(int tipNum, TextView tv_text2) {
        if (tipNum > 0) {
            tv_text2.setText(tipNum + "");
            tv_text2.setVisibility(View.VISIBLE);
        } else {
            tv_text2.setText(tipNum + "");
            tv_text2.setVisibility(View.GONE);
        }
    }

    private void getConversationList() {
        RongIM.getInstance().getConversationList(new RongIMClient.ResultCallback<List<Conversation>>() {
            @Override
            public void onSuccess(List<Conversation> conversations) {
                if (conversations != null) {
                    conversationList.clear();
                    for (int i = 0; i < conversations.size(); i++) {
                        PersonInfo personInfo = new PersonInfo();
                        personInfo.setName(conversations.get(i).getTargetId());
                        personInfo.setImToken(conversations.get(i).getTargetId());
                        personInfo.setImId(conversations.get(i).getTargetId());
                        personInfo.setReceivedTime(DateUtil.longToDate(conversations.get(i).getReceivedTime()));
                        personInfo.setAvatar(conversations.get(i).getTargetId());
                        conversationList.add(personInfo);
                    }
                    if (conversationList != null && conversationList.size() > 0) {
                        sendRequest(2,conversationList);
                    }
                }
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        sendRequest(1, "");
    }
}
