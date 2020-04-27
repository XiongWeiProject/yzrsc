package com.commodity.yzrsc.rongyun.ui.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.commodity.yzrsc.R;
import com.commodity.yzrsc.http.BaseHttpManager;
import com.commodity.yzrsc.http.HttpManager;
import com.commodity.yzrsc.http.HttpMothed;
import com.commodity.yzrsc.http.IRequestConst;
import com.commodity.yzrsc.http.ServiceInfo;
import com.commodity.yzrsc.manager.ConfigManager;
import com.commodity.yzrsc.manager.ImageLoaderManager;
import com.commodity.yzrsc.manager.SPKeyManager;
import com.commodity.yzrsc.manager.SPManager;
import com.commodity.yzrsc.model.CommodityBean;
import com.commodity.yzrsc.ottobus.BusProvider;
import com.commodity.yzrsc.ottobus.Event;
import com.commodity.yzrsc.rongyun.SealAppContext;
import com.commodity.yzrsc.rongyun.SealUserInfoManager;
import com.commodity.yzrsc.rongyun.db.GroupMember;
import com.commodity.yzrsc.rongyun.server.utils.NLog;
import com.commodity.yzrsc.rongyun.server.utils.NToast;
import com.commodity.yzrsc.rongyun.ui.fragment.ConversationFragmentEx;
import com.commodity.yzrsc.rongyun.ui.widget.LoadingDialog;
import com.commodity.yzrsc.ui.activity.general.GeneralWebViewActivity;
import com.commodity.yzrsc.ui.dialog.CustomLoadding;
import com.commodity.yzrsc.ui.widget.imageview.XCRoundRectImageView;
import com.commodity.yzrsc.utils.CustomToast;
import com.commodity.yzrsc.utils.RongIMUtil;
import com.squareup.otto.Subscribe;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

//import io.rong.callkit.RongCallKit;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.UriFragment;
import io.rong.imkit.userInfoCache.RongUserInfoManager;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.MessageTag;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.TypingMessage.TypingStatus;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Discussion;
import io.rong.imlib.model.PublicServiceProfile;
import io.rong.imlib.model.UserInfo;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;

//CallKit start 1
//CallKit end 1

/**
 * 会话页面
 * 1，设置 ActionBar title
 * 2，加载会话页面
 * 3，push 和 通知 判断
 */
public class ConversationActivity extends BaseActivity implements View.OnClickListener {

    private String TAG = ConversationActivity.class.getSimpleName();
    /**
     * 对方id
     */
    private String mTargetId;
    /**
     * 会话类型
     */
    private Conversation.ConversationType mConversationType;
    /**
     * title
     */
    private String title;
    /**
     * 是否在讨论组内，如果不在讨论组内，则进入不到讨论组设置页面
     */
    private boolean isFromPush = false;

    private LoadingDialog mDialog;

    private SharedPreferences sp;

    private final String TextTypingTitle = "对方正在输入...";
    private final String VoiceTypingTitle = "对方正在讲话...";

    private Handler mHandler;

    public static final int SET_TEXT_TYPING_TITLE = 1;
    public static final int SET_VOICE_TYPING_TITLE = 2;
    public static final int SET_TARGET_ID_TITLE = 0;

    private Button mRightButton;
    CommodityBean commodityBean;
    private TextView tv_btn_send;
    private LinearLayout ll_commod;
    private TextView tv_title_;
    private TextView tv_rightbtn;
    private TextView tv_rightbtn2;

    private XCRoundRectImageView iv_image;
    private TextView xioashou_content;
    private TextView tv_yuanjia;

    @Override
    @TargetApi(23)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversation);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        mDialog = new LoadingDialog(this);

        mRightButton = getHeadRightButton();
        tv_rightbtn=(TextView) findViewById(R.id.tv_rightbtn);
        tv_rightbtn2=(TextView) findViewById(R.id.tv_rightbtn2);
        tv_title_=(TextView) findViewById(R.id.tv_title_);
        tv_rightbtn.setVisibility(View.GONE);
        tv_rightbtn2.setVisibility(View.GONE);
        tv_title_.setVisibility(View.VISIBLE);

        tv_btn_send= (TextView) findViewById(R.id.tv_btn_send);
        ll_commod= (LinearLayout) findViewById(R.id.ll_commod);
        iv_image= (XCRoundRectImageView) findViewById(R.id.iv_image);
        xioashou_content= (TextView) findViewById(R.id.xioashou_content);
        tv_yuanjia= (TextView) findViewById(R.id.tv_yuanjia);
        Intent intent = getIntent();

        if (intent == null || intent.getData() == null)
            return;

        mTargetId = intent.getData().getQueryParameter("targetId");
        //10000 为 Demo Server 加好友的 id，若 targetId 为 10000，则为加好友消息，默认跳转到 NewFriendListActivity
        // Demo 逻辑
        if (mTargetId != null && mTargetId.equals("10000")) {
           // startActivity(new Intent(ConversationActivity.this, NewFriendListActivity.class));
            return;
        }
        mConversationType = Conversation.ConversationType.valueOf(intent.getData()
                .getLastPathSegment().toUpperCase(Locale.US));

        title = intent.getData().getQueryParameter("title");

        setActionBarTitle(mConversationType, mTargetId);


        if (mConversationType.equals(Conversation.ConversationType.GROUP)) {
            mRightButton.setBackground(getResources().getDrawable(R.drawable.icon2_menu));
        } else if (mConversationType.equals(Conversation.ConversationType.PRIVATE) | mConversationType.equals(Conversation.ConversationType.PUBLIC_SERVICE) | mConversationType.equals(Conversation.ConversationType.DISCUSSION)) {
            mRightButton.setBackground(getResources().getDrawable(R.drawable.icon1_menu));
        } else {
            mRightButton.setVisibility(View.GONE);
            mRightButton.setClickable(false);
        }
        mRightButton.setOnClickListener(this);

        isPushMessage(intent);

        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case SET_TEXT_TYPING_TITLE:
                        setTitle(TextTypingTitle);
                        break;
                    case SET_VOICE_TYPING_TITLE:
                        setTitle(VoiceTypingTitle);
                        break;
                    case SET_TARGET_ID_TITLE:
                        setActionBarTitle(mConversationType, mTargetId);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        RongIMClient.setTypingStatusListener(new RongIMClient.TypingStatusListener() {
            @Override
            public void onTypingStatusChanged(Conversation.ConversationType type, String targetId, Collection<TypingStatus> typingStatusSet) {
                //当输入状态的会话类型和targetID与当前会话一致时，才需要显示
                if (type.equals(mConversationType) && targetId.equals(mTargetId)) {
                    int count = typingStatusSet.size();
                    //count表示当前会话中正在输入的用户数量，目前只支持单聊，所以判断大于0就可以给予显示了
                    if (count > 0) {
                        Iterator iterator = typingStatusSet.iterator();
                        TypingStatus status = (TypingStatus) iterator.next();
                        String objectName = status.getTypingContentType();

                        MessageTag textTag = TextMessage.class.getAnnotation(MessageTag.class);
                        MessageTag voiceTag = VoiceMessage.class.getAnnotation(MessageTag.class);
                        //匹配对方正在输入的是文本消息还是语音消息
                        if (objectName.equals(textTag.value())) {
                            mHandler.sendEmptyMessage(SET_TEXT_TYPING_TITLE);
                        } else if (objectName.equals(voiceTag.value())) {
                            mHandler.sendEmptyMessage(SET_VOICE_TYPING_TITLE);
                        }
                    } else {//当前会话没有用户正在输入，标题栏仍显示原来标题
                        mHandler.sendEmptyMessage(SET_TARGET_ID_TITLE);
                    }
                }
            }
        });

        SealAppContext.getInstance().pushActivity(this);

        //CallKit start 2
//        RongCallKit.setGroupMemberProvider(new RongCallKit.GroupMembersProvider() {
//            @Override
//            public ArrayList<String> getMemberList(String groupId, final RongCallKit.OnGroupMembersResult result) {
//                getGroupMembersForCall();
//                mCallMemberResult = result;
//                return null;
//            }
//        });
        //CallKit end 2
        initListen();
        SPManager.instance().setString(SPKeyManager.CURACTIVITYNAME,"ConversationActivity");
        RongIMUtil.connectRongIM(ConfigManager.instance().getUser().getImToken());
    }

    /**
     * 判断是否是 Push 消息，判断是否需要做 connect 操作
     */
    private void isPushMessage(Intent intent) {

        if (intent == null || intent.getData() == null)
            return;
        //push
        if (intent.getData().getScheme().equals("rong") && intent.getData().getQueryParameter("isFromPush") != null) {

            //通过intent.getData().getQueryParameter("push") 为true，判断是否是push消息
            if (intent.getData().getQueryParameter("isFromPush").equals("true")) {
                //只有收到系统消息和不落地 push 消息的时候，pushId 不为 null。而且这两种消息只能通过 server 来发送，客户端发送不了。
                //RongIM.getInstance().getRongIMClient().recordNotificationEvent(id);
                if (mDialog != null && !mDialog.isShowing()) {
                    mDialog.show();
                }
                isFromPush = true;
                enterActivity();
            } else if (RongIM.getInstance().getCurrentConnectionStatus().equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.DISCONNECTED)) {
                if (mDialog != null && !mDialog.isShowing()) {
                    mDialog.show();
                }
                if (intent.getData().getPath().contains("conversation/system")) {
                   // Intent intent1 = new Intent(mContext, MainActivity.class);
                   // intent1.putExtra("systemconversation", true);
                   // startActivity(intent1);
                   // SealAppContext.getInstance().popAllActivity();
                    return;
                }
                enterActivity();
            } else {
                if (intent.getData().getPath().contains("conversation/system")) {
//                    Intent intent1 = new Intent(mContext, MainActivity.class);
//                    intent1.putExtra("systemconversation", true);
//                    startActivity(intent1);
//                    SealAppContext.getInstance().popAllActivity();
                    return;
                }
                enterFragment(mConversationType, mTargetId);
            }

        } else {
            if (RongIM.getInstance().getCurrentConnectionStatus().equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.DISCONNECTED)) {
                if (mDialog != null && !mDialog.isShowing()) {
                    mDialog.show();
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        enterActivity();
                    }
                }, 300);
            } else {
                enterFragment(mConversationType, mTargetId);
            }
        }
    }


    /**
     * 收到 push 消息后，选择进入哪个 Activity
     * 如果程序缓存未被清理，进入 MainActivity
     * 程序缓存被清理，进入 LoginActivity，重新获取token
     * <p>
     * 作用：由于在 manifest 中 intent-filter 是配置在 ConversationActivity 下面，所以收到消息后点击notifacition 会跳转到 DemoActivity。
     * 以跳到 MainActivity 为例：
     * 在 ConversationActivity 收到消息后，选择进入 MainActivity，这样就把 MainActivity 激活了，当你读完收到的消息点击 返回键 时，程序会退到
     * MainActivity 页面，而不是直接退回到 桌面。
     */
    private void enterActivity() {

        String token = sp.getString("loginToken", "");

        if (token.equals("default")) {
            NLog.e("ConversationActivity push", "push2");
            //startActivity(new Intent(ConversationActivity.this, LoginActivity.class));
            //SealAppContext.getInstance().popAllActivity();
        } else {
            NLog.e("ConversationActivity push", "push3");
            reconnect(token);
        }
    }

    private void reconnect(String token) {
        RongIM.connect(token, new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {

                Log.e(TAG, "---onTokenIncorrect--");
            }

            @Override
            public void onSuccess(String s) {
                Log.i(TAG, "---onSuccess--" + s);
                NLog.e("ConversationActivity push", "push4");

                if (mDialog != null)
                    mDialog.dismiss();

                enterFragment(mConversationType, mTargetId);

            }

            @Override
            public void onError(RongIMClient.ErrorCode e) {
                Log.e(TAG, "---onError--" + e);
                if (mDialog != null)
                    mDialog.dismiss();

                enterFragment(mConversationType, mTargetId);
            }
        });

    }

    private ConversationFragmentEx fragment;

    /**
     * 加载会话页面 ConversationFragmentEx 继承自 ConversationFragment
     *
     * @param mConversationType 会话类型
     * @param mTargetId         会话 Id
     */
    private void enterFragment(Conversation.ConversationType mConversationType, String mTargetId) {

        fragment = new ConversationFragmentEx();

        Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                .appendPath("conversation").appendPath(mConversationType.getName().toLowerCase())
                .appendQueryParameter("targetId", mTargetId).build();

        fragment.setUri(uri);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //xxx 为你要加载的 id
        transaction.add(R.id.rong_content, fragment);
        transaction.commitAllowingStateLoss();
    }


    /**
     * 设置会话页面 Title
     *
     * @param conversationType 会话类型
     * @param targetId         目标 Id
     */
    private void setActionBarTitle(Conversation.ConversationType conversationType, String targetId) {

        if (conversationType == null)
            return;

        if (conversationType.equals(Conversation.ConversationType.PRIVATE)) {
            setPrivateActionBar(targetId);
        } else if (conversationType.equals(Conversation.ConversationType.GROUP)) {
            setGroupActionBar(targetId);
        } else if (conversationType.equals(Conversation.ConversationType.DISCUSSION)) {
            setDiscussionActionBar(targetId);
        } else if (conversationType.equals(Conversation.ConversationType.CHATROOM)) {
            setTitle(title);
        } else if (conversationType.equals(Conversation.ConversationType.SYSTEM)) {
            setTitle(R.string.de_actionbar_system);
        } else if (conversationType.equals(Conversation.ConversationType.APP_PUBLIC_SERVICE)) {
            setAppPublicServiceActionBar(targetId);
        } else if (conversationType.equals(Conversation.ConversationType.PUBLIC_SERVICE)) {
            setPublicServiceActionBar(targetId);
        } else if (conversationType.equals(Conversation.ConversationType.CUSTOMER_SERVICE)) {
            setTitle(R.string.main_customer);
        } else {
            setTitle(R.string.de_actionbar_sub_defult);
        }

    }

    /**
     * 设置群聊界面 ActionBar
     *
     * @param targetId 会话 Id
     */
    private void setGroupActionBar(String targetId) {
        if (!TextUtils.isEmpty(title)) {
            setTitle(title);
        } else {
            setTitle(targetId);
        }
    }

    /**
     * 设置应用公众服务界面 ActionBar
     */
    private void setAppPublicServiceActionBar(String targetId) {
        if (targetId == null)
            return;

        RongIM.getInstance().getPublicServiceProfile(Conversation.PublicServiceType.APP_PUBLIC_SERVICE
                , targetId, new RongIMClient.ResultCallback<PublicServiceProfile>() {
                    @Override
                    public void onSuccess(PublicServiceProfile publicServiceProfile) {
                        setTitle(publicServiceProfile.getName());
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {

                    }
                });
    }

    /**
     * 设置公共服务号 ActionBar
     */
    private void setPublicServiceActionBar(String targetId) {

        if (targetId == null)
            return;


        RongIM.getInstance().getPublicServiceProfile(Conversation.PublicServiceType.PUBLIC_SERVICE
                , targetId, new RongIMClient.ResultCallback<PublicServiceProfile>() {
                    @Override
                    public void onSuccess(PublicServiceProfile publicServiceProfile) {
                        setTitle(publicServiceProfile.getName());
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {

                    }
                });
    }

    /**
     * 设置讨论组界面 ActionBar
     */
    private void setDiscussionActionBar(String targetId) {

        if (targetId != null) {

            RongIM.getInstance().getDiscussion(targetId
                    , new RongIMClient.ResultCallback<Discussion>() {
                        @Override
                        public void onSuccess(Discussion discussion) {
                            setTitle(discussion.getName());
                        }

                        @Override
                        public void onError(RongIMClient.ErrorCode e) {
                            if (e.equals(RongIMClient.ErrorCode.NOT_IN_DISCUSSION)) {
                                setTitle("不在讨论组中");
                                supportInvalidateOptionsMenu();
                            }
                        }
                    });
        } else {
            setTitle("讨论组");
        }
    }


    /**
     * 设置私聊界面 ActionBar
     */
    private void setPrivateActionBar(String targetId) {
        if (!TextUtils.isEmpty(title)) {
            if (title.equals("null")) {
                if (!TextUtils.isEmpty(targetId)) {
                    UserInfo userInfo = RongUserInfoManager.getInstance().getUserInfo(targetId);
                    if (userInfo != null) {
                        setTitle(userInfo.getName());
                    }
                }
            } else {
                setTitle(title);
            }

        } else {
            setTitle(targetId);
        }
    }

    /**
     * 根据 targetid 和 ConversationType 进入到设置页面
     */
    private void enterSettingActivity() {

        if (mConversationType == Conversation.ConversationType.PUBLIC_SERVICE
                || mConversationType == Conversation.ConversationType.APP_PUBLIC_SERVICE) {

            RongIM.getInstance().startPublicServiceProfile(this, mConversationType, mTargetId);
        } else {
            UriFragment fragment = (UriFragment) getSupportFragmentManager().getFragments().get(0);
            //得到讨论组的 targetId
            mTargetId = fragment.getUri().getQueryParameter("targetId");

            if (TextUtils.isEmpty(mTargetId)) {
                NToast.shortToast(mContext, "讨论组尚未创建成功");
            }


            Intent intent = null;
//            if (mConversationType == Conversation.ConversationType.GROUP) {
//                intent = new Intent(this, GroupDetailActivity.class);
//                intent.putExtra("conversationType", Conversation.ConversationType.GROUP);
//            } else if (mConversationType == Conversation.ConversationType.PRIVATE) {
//                intent = new Intent(this, PrivateChatDetailActivity.class);
//                intent.putExtra("conversationType", Conversation.ConversationType.PRIVATE);
//            } else if (mConversationType == Conversation.ConversationType.DISCUSSION) {
//                intent = new Intent(this, DiscussionDetailActivity.class);
//                intent.putExtra("TargetId", mTargetId);
//                startActivityForResult(intent, 166);
//                return;
//            }
            intent.putExtra("TargetId", mTargetId);
            if (intent != null) {
                startActivityForResult(intent, 500);
            }

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 501) {
            SealAppContext.getInstance().popAllActivity();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        BusProvider.getInstance().register(this);
    }

    @Override
    protected void onDestroy() {
        //CallKit start 3
        //RongCallKit.setGroupMemberProvider(null);
        //CallKit end 3

        RongIMClient.setTypingStatusListener(null);
        SealAppContext.getInstance().popActivity(this);
        super.onDestroy();
        BusProvider.getInstance().unregister(this);
        BusProvider.disposeInstance();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == event.getKeyCode()) {
            if (fragment != null && !fragment.onBackPressed()) {
                if (isFromPush) {
                    isFromPush = false;
                  //  startActivity(new Intent(this, MainActivity.class));
                    SealAppContext.getInstance().popAllActivity();
                } else {
                    if (fragment.isLocationSharing()) {
                        fragment.showQuitLocationSharingDialog(this);
                        return true;
                    }
                    if (mConversationType.equals(Conversation.ConversationType.CHATROOM)
                            || mConversationType.equals(Conversation.ConversationType.CUSTOMER_SERVICE)) {
                        SealAppContext.getInstance().popActivity(this);
                    } else {
                        SealAppContext.getInstance().popActivity(this);
                    }
                }
            }
        }
        return false;
    }

    private void hintKbTwo() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive() && getCurrentFocus() != null) {
            if (getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    //CallKit start 4
   // private RongCallKit.OnGroupMembersResult mCallMemberResult;

    private void getGroupMembersForCall() {
        SealUserInfoManager.getInstance().getGroupMembers(mTargetId, new SealUserInfoManager.ResultCallback<List<GroupMember>>() {
            @Override
            public void onSuccess(List<GroupMember> groupMembers) {
                ArrayList<String> userIds = new ArrayList<>();
                if (groupMembers != null) {
                    for (GroupMember groupMember : groupMembers) {
                        if (groupMember != null) {
                            userIds.add(groupMember.getUserId());
                        }
                    }
                }
               // mCallMemberResult.onGotMemberList(userIds);
            }

            @Override
            public void onError(String errString) {
                //mCallMemberResult.onGotMemberList(null);
            }
        });
    }
    //CallKit end 4

    @Override
    public void onClick(View v) {
        enterSettingActivity();
    }

    @Override
    public void onHeadLeftButtonClick(View v) {
        if (fragment != null && !fragment.onBackPressed()) {
            if (fragment.isLocationSharing()) {
                fragment.showQuitLocationSharingDialog(this);
                return;
            }
            hintKbTwo();
            if (isFromPush) {
                isFromPush = false;
                //startActivity(new Intent(this, MainActivity.class));
            }
            if (mConversationType.equals(Conversation.ConversationType.CHATROOM)
                    || mConversationType.equals(Conversation.ConversationType.CUSTOMER_SERVICE)) {
                SealAppContext.getInstance().popActivity(this);
            } else {
                SealAppContext.getInstance().popAllActivity();
            }
        }
    }


    //获取传递过来信息
    @Subscribe
    public void SendCommodInfo(Event.SendCommodInfo event) {
        commodityBean=event.getCommodityBean();
        if (commodityBean!=null){
            tv_title_.setText("联系货主");
            ll_commod.setVisibility(View.VISIBLE);
            tv_title_.setVisibility(View.VISIBLE);
            tv_rightbtn.setVisibility(View.VISIBLE);
            tv_rightbtn2.setVisibility(View.VISIBLE);
            if (commodityBean.isIsFollowed()){
                tv_rightbtn2.setText("取消关注");
            }
            if (commodityBean.getImages()!=null&&commodityBean.getImages().size()>0){
                ImageLoaderManager.getInstance().displayImage(commodityBean.getImages().get(0),iv_image,R.drawable.ico_pic_fail_defalt);
            }else {
                iv_image.setBackgroundResource(R.drawable.ico_pic_fail_defalt);
            }

            xioashou_content.setText(commodityBean.getDescription());
            tv_yuanjia.setText("￥"+commodityBean.getSuggestedPrice());
        }
    }
    public void back(View v) {
        finish();
    }
    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.slide_out_right);
        SPManager.instance().setString(SPKeyManager.CURACTIVITYNAME,"");
    }

    //监听事件

    private void initListen(){
        tv_rightbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ConversationActivity.this,GeneralWebViewActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("title",commodityBean.getShopName());
                bundle.putString("content_url",commodityBean.getPropagationUrl());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        tv_rightbtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tv_rightbtn2.getText().toString().equals("取消关注")){
                    fouce(commodityBean.getSellerImId()+"",false);
                }else {
                    fouce(commodityBean.getSellerImId()+"",true);
                }

            }
        });
        tv_btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage(commodityBean.getGoodsSaleUrl());
                ll_commod.setVisibility(View.GONE);
            }
        });
    }

    //发送消息
    private void sendMessage(String message){
        // 构造 TextMessage 实例
        TextMessage myTextMessage = TextMessage.obtain(message);
        io.rong.imlib.model.Message myMessage = io.rong.imlib.model.Message.obtain(commodityBean.getSellerImId(), Conversation.ConversationType.PRIVATE, myTextMessage);
        RongIM.getInstance().sendMessage(myMessage, null, null, new IRongCallback.ISendMessageCallback() {
            @Override
            public void onAttached(io.rong.imlib.model.Message message) {
                //消息本地数据库存储成功的回调
            }

            @Override
            public void onSuccess(io.rong.imlib.model.Message message) {
                //消息通过网络发送成功的回调
            }

            @Override
            public void onError(io.rong.imlib.model.Message message, RongIMClient.ErrorCode errorCode) {
                //消息发送失败的回调
            }
        });
    }

    private void fouce(String id, final boolean isFouce){
        final CustomLoadding customLoadding=new CustomLoadding(this);
        customLoadding.show();
        String url="";
        if (isFouce){//关注
            url=IRequestConst.RequestMethod.AddContact;
        }else {//取消关注
            url= IRequestConst.RequestMethod.DeleteContact;
        }
        Map<String, String> parmMap = new HashMap<String, String>();
        parmMap.put("contactId",id);
        HttpManager httpManager = new HttpManager(0, HttpMothed.POST, url, parmMap, new BaseHttpManager.IRequestListener() {
            @Override
            public void onPreExecute(int Tag) {

            }

            @Override
            public void onSuccess(int Tag, ServiceInfo result) {
                customLoadding.dismiss();
                JSONObject resultJson= (JSONObject) result.getResponse();
                if (resultJson!=null&&resultJson.optBoolean("success")){
                    if (isFouce){//关注
                        CustomToast.showToast("关注成功");
                        tv_rightbtn2.setText("取消关注");
                        commodityBean.setIsFavored(true);
                    }else {//取消关注
                        CustomToast.showToast("取消关注成功");
                        tv_rightbtn2.setText("关注");
                        commodityBean.setIsFavored(false);
                    }
                }else {
                    if (resultJson!=null&&!resultJson.optBoolean("success")){
                        CustomToast.showToast(resultJson.optString("msg"));
                    }
                }
            }

            @Override
            public void onError(int Tag, String code, String msg) {
                customLoadding.dismiss();
            }

            @Override
            public void OnTimeOut(int Tag, boolean isShowTip) {
                customLoadding.dismiss();
            }

            @Override
            public void OnNetError(int Tag, boolean isShowTip) {
                customLoadding.dismiss();
            }
        });
        httpManager.request();
    }
}
